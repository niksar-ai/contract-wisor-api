package ai.niksar.contract_wisor_api.service;

import com.jcraft.jsch.*;
import com.jcraft.jsch.Session;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ai.niksar.contract_wisor_api.dto.FTPServerDTO;
import ai.niksar.contract_wisor_api.exception.ContractWisorException;
import ai.niksar.contract_wisor_api.model.*;
import ai.niksar.contract_wisor_api.repository.FTPPathRepository;
import ai.niksar.contract_wisor_api.repository.FTPServerRepository;
import ai.niksar.contract_wisor_api.util.Constants;
import ai.niksar.contract_wisor_api.util.Util;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class FTPService {
    @Autowired
    private FTPPathRepository       ftpPathRepository;
    @Autowired
    private FTPServerRepository     ftpServerRepository;

    @Value("${sftp.secret}")
    private   String key;

    private   Session          session           = null;
    private   ChannelSftp      channelSftp       = null;

    public void connect(String server, int port, String user, String password) throws JSchException {
        try {
            setSession(server,port,user,password);

            channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();

            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            ftpServerRepository.findAndUpdateForLastConnectAt(LocalDateTime.now(),server,String.valueOf(port),username);

        } catch (JSchException e){
            throw new JSchException("An error occurred while connecting to the FTP Server. \nError: " + e.getMessage());
        }
    }

    private void setSession(String server, int port, String user, String password)throws JSchException {
        JSch jsch   = new JSch();

        session     = jsch.getSession(user, server, port);
        session.setPassword(password);

        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);

        session.connect();
    }

    public String connectTest(String server, int port, String user, String password) throws JSchException {
        try {
            String result = "";

            setSession(server,port,user,password);

            if(session.isConnected()) result = "Connection successful.";

            return result;
        } catch (JSchException e){
            throw new JSchException("Connection failed. \nError: " + e.getMessage());
        } finally {
            sessionDisconnect();
        }
    }

    public void disconnect() {
        channelSftpDisconnect();
        sessionDisconnect();
    }

    private void channelSftpDisconnect() {
        if (channelSftp != null) {
            channelSftp.disconnect();
        }
    }

    private void sessionDisconnect() {
        if (session != null) {
            session.disconnect();
        }
    }

    public ChannelSftp getChannelSftp() {
        return channelSftp;
    }

    public void save(String longName){
        FTPPath ftpPath;
        ftpPath=ftpPathRepository.getFTPPathByLongName(longName);
        if(ftpPath == null){
            FTPPath newFtpPath = new FTPPath();
            newFtpPath.setLongName(longName);
            ftpPathRepository.save(newFtpPath);
        }
        else{
            ftpPath.setUpdatedAt(LocalDateTime.now());
            ftpPathRepository.save(ftpPath);
        }
    }

    public List<Map<String,Object>> listFTPPath(List<ChannelSftp.LsEntry> list){
        List<Map<String, Object>> updatedFileList = new ArrayList<>();
        for(ChannelSftp.LsEntry entry : list){
            String                  longName    = entry.getLongname();
            FTPPath                 ftpPath     = ftpPathRepository.getFTPPathByLongName(longName);
            Map<String, Object>     fileInfo    = new HashMap<>();

            fileInfo.put("filename", entry.getFilename());
            fileInfo.put("longname", longName);
            fileInfo.put("attrs", entry.getAttrs());

            if(ftpPath !=null){
                fileInfo.put("isExists",true);
                fileInfo.put("updatedAt",ftpPath.getUpdatedAt().toString());
            }
            else{
                fileInfo.put("isExists",false);
                fileInfo.put("updatedAt",null);
            }
            updatedFileList.add(fileInfo);
        }

        return updatedFileList;
    }

    public FTPServerDTO saveFTPServer(String server, String port, String ftpUser,String password, String serverName) throws Exception{
        FTPServer   ftpServer       = new FTPServer();
        String      username        = SecurityContextHolder.getContext().getAuthentication().getName();

        ftpServer.setServer(server);
        ftpServer.setServerName(serverName);
        ftpServer.setPort(port);
        ftpServer.setFtpUser(ftpUser);
        ftpServer.setPassword(Util.encrypt(password,key));
        ftpServer.setCreateUser(username);
        ftpServer.setUpdateUser(username);
        ftpServer.setUserName(username);
        ftpServer.setCreatedAt(LocalDateTime.now());
        ftpServer.setUpdatedAt(LocalDateTime.now());
        ftpServer.setLastConnectAt(LocalDateTime.now());
        ftpServer.setStatus(Constants.Status.ACTIVE);

        FTPServer   savedFTPServer  = ftpServerRepository.save(ftpServer);
        return  new FTPServerDTO(savedFTPServer.getId(),savedFTPServer.getServer(), savedFTPServer.getServerName(),savedFTPServer.getPort(), savedFTPServer.getFtpUser(), password, savedFTPServer.getLastConnectAt());
    }

    public List<FTPServerDTO> listFTPServer() {
        String                  username    = SecurityContextHolder.getContext().getAuthentication().getName();
        List<FTPServerDTO>      list        = ftpServerRepository.findAllFTPServerByUsername(username);

        list.forEach(item -> {
            try {
                item.setPassword(Util.decrypt(item.getPassword(),key));
            }  catch (Exception e){
                e.printStackTrace();
            }
        });

        return list;
    }

    public FTPServerDTO listFTPServerById(UUID ftpId) throws Exception{
        FTPServer       ftpServer       = ftpServerRepository.findById(ftpId).orElseThrow(ContractWisorException.E024::new);
        String          password        = Util.decrypt(ftpServer.getPassword(),key);

        return new FTPServerDTO(ftpServer.getId(), ftpServer.getServer(), ftpServer.getServerName(), ftpServer.getPort(), ftpServer.getFtpUser(), password, ftpServer.getLastConnectAt());
    }

    @Transactional
    public void deleteFTPServer(UUID ftpId){
        if(ftpId != null)   ftpServerRepository.deleteById(ftpId);
    }

    @Transactional
    public void updateFTPServer(UUID ftpId,FTPServer jsonData) throws Exception{
        FTPServer ftpServer   = ftpServerRepository.findById(ftpId).orElse(new FTPServer());
        String    username    = SecurityContextHolder.getContext().getAuthentication().getName();

        ftpServer.setServer((jsonData.getServer()           != null) ? jsonData.getServer()                                                     : ftpServer.getServer());
        ftpServer.setServerName((jsonData.getServerName()   != null) ? jsonData.getServerName()                                                 : ftpServer.getServerName());
        ftpServer.setPort((jsonData.getPort()               != null) ? jsonData.getPort()                                                       : ftpServer.getPort());
        ftpServer.setFtpUser((jsonData.getFtpUser()         != null) ? jsonData.getFtpUser()                                                    : ftpServer.getFtpUser());
        ftpServer.setPassword((jsonData.getPassword()       != null) ? Util.encrypt(jsonData.getPassword(),key)                                 : ftpServer.getPassword());
        ftpServer.setUpdateUser(username);
        ftpServer.setUpdatedAt(LocalDateTime.now());
        ftpServer.setLastConnectAt(LocalDateTime.now());

        ftpServerRepository.save(ftpServer);
    }
}