package ai.niksar.contract_wisor_api.controller;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;
import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ai.niksar.contract_wisor_api.dto.FTPServerDTO;
import ai.niksar.contract_wisor_api.dto.FtpControllerDTO;
import ai.niksar.contract_wisor_api.dto.ResponseDTO;
import ai.niksar.contract_wisor_api.model.DocumentContent;
import ai.niksar.contract_wisor_api.model.FTPServer;
import ai.niksar.contract_wisor_api.service.DocumentParserService;
import ai.niksar.contract_wisor_api.service.DocumentService;
import ai.niksar.contract_wisor_api.service.FTPService;
import ai.niksar.contract_wisor_api.util.Constants;
import ai.niksar.contract_wisor_api.util.Util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api/ftp")
public class FTPController {

    private static final Logger logger = LoggerFactory.getLogger(FTPController.class);

    @Autowired
    private FTPService ftpService;
    @Autowired
    private DocumentService documentService;
    @Autowired
    private DocumentParserService documentParserService;

    private final Tika tika = new Tika();

    @PostMapping("/connect")
    public ResponseEntity<?> connectServer(@RequestBody FtpControllerDTO ftpControllerDTO) {
        try {
            ftpService.connect(ftpControllerDTO.getServer(), ftpControllerDTO.getPort(), ftpControllerDTO.getFtpUser(), ftpControllerDTO.getPassword());
            ResponseDTO response= new ResponseDTO();
            response.setMessage("Connection successful.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("FTP operation failed", e);
            return buildErrorResponse(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/connect/test")
    public ResponseEntity<?> connectTestServer(@RequestBody FtpControllerDTO ftpControllerDTO) {
        try {
            return ResponseEntity.ok(ftpService.connectTest(ftpControllerDTO.getServer(), ftpControllerDTO.getPort(), ftpControllerDTO.getFtpUser(), ftpControllerDTO.getPassword()));
        } catch (Exception e) {
            logger.error("FTP operation failed", e);
            return buildErrorResponse(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/disconnect")
    public ResponseEntity<?> disconnectServer() {
        try {
            ftpService.disconnect();
            ResponseDTO response= new ResponseDTO();
            response.setMessage("Connection terminated successfully.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("FTP operation failed", e);
            return buildErrorResponse("An error occurred while terminating the connection. \nError:" + e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/file/list")
    public ResponseEntity<?> listFiles(@RequestParam String remotePath){
        try {
            ChannelSftp channelSftp = ftpService.getChannelSftp();
            return ResponseEntity.ok(ftpService.listFTPPath(channelSftp.ls(remotePath)));
        } catch (Exception e) {
            logger.error("FTP operation failed", e);
            return buildErrorResponse("An error occurred during the listing operation on the FTP Server. \nError: " + e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/file/upload")
    public ResponseEntity<?> uploadFiles(@RequestBody List<Map<String,Object>> request) {
        try {
            ChannelSftp     channelSftp         = ftpService.getChannelSftp();
            StringBuilder   errorMessages       = new StringBuilder();
            ResponseDTO response= new ResponseDTO();
            if (channelSftp == null) return buildErrorResponse("Connection could not be established. Please reconnect to the server.",HttpStatus.BAD_REQUEST);

            request.forEach(item -> {
                try {
                    DocumentContent             document             = new DocumentContent();
                    ByteArrayOutputStream       outputStream         = new ByteArrayOutputStream();
                    ChannelSftp.LsEntry         documentSftp         = (ChannelSftp.LsEntry) channelSftp.ls(item.get("path").toString()).firstElement();
                    byte[]                      fileContent;

                    if (documentSftp != null) {
                        ftpService.save(documentSftp.getLongname());
                        channelSftp.get(item.get("path").toString(), outputStream);
                        fileContent = outputStream.toByteArray();

                        document.setName(documentSftp.getFilename());
                        document.setSize(documentSftp.getAttrs().getSize());
                        String fileType = documentParserService.determineFileType(fileContent);
                        document.setPageCount(documentParserService.determinePageCount(fileContent,fileType));
                        document.setContent(fileContent);
                        document.setStatus(Constants.Status.ACTIVE);
                        document.setState(Constants.DocumentState.LOADED);
                        document.setParseState(Constants.ParseState.WAITING);
                        document.setAnalyzeState(Constants.AnalyzeState.WAITING);
                        document.setCreateDate(Util.getRealDate());
                        document.setCreateTime(Util.getRealTime());
                        document.setUpdateDate(Util.getRealDate());
                        document.setUpdateTime(Util.getRealTime());
                        document.setDocumentTypeId(UUID.fromString(item.get("documentTypeId").toString()));
                        document.setDocumentFileType(fileType);
                        String username = SecurityContextHolder.getContext().getAuthentication().getName();
                        document.setCreateUser(username);
                        document.setUpdateUser(username);
                        documentService.uploadDocument(document);
                    } else {
                        errorMessages.append("File does not exist on the server: ").append(item.get("path").toString());
                    }
                } catch (SftpException | IOException e) {
                    logger.error("FTP upload failed for item {}", item.get("path"), e);
                     errorMessages.append("SFTP error: ").append(e.getMessage());
                } catch (Exception e) {
                    logger.error("FTP upload failed for item {}", item.get("path"), e);
                     errorMessages.append("An error occurred while uploading the file to the FTP Server. \nError: ").append(e.getMessage());
                }
            });
            if(!errorMessages.isEmpty()) return buildErrorResponse(errorMessages.toString(),HttpStatus.BAD_REQUEST);
            response.setMessage("Document saved successfully.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("FTP operation failed", e);
            return buildErrorResponse("An error occurred while uploading the file to the FTP Server. \nError: " + e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveServer(@RequestBody FTPServerDTO ftpServerDTO) {
        try{
            return ResponseEntity.ok(ftpService.saveFTPServer(ftpServerDTO.getServer(), ftpServerDTO.getPort(), ftpServerDTO.getFtpUser(), ftpServerDTO.getPassword(), ftpServerDTO.getServerName()));
        } catch (Exception e){
            logger.error("FTP operation failed", e);
            return buildErrorResponse("An error occurred while saving the FTP Server. \nError:\t" + e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<?> listFTPServer() {
        try {
            return ResponseEntity.ok(ftpService.listFTPServer());
        } catch (Exception e) {
            logger.error("FTP operation failed", e);
            return buildErrorResponse("An error occurred while listing the FTP Servers. \nError:\t" + e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{ftpId}")
    public ResponseEntity<?> listServerById(@PathVariable UUID ftpId) {
        try {
            return ResponseEntity.ok(ftpService.listFTPServerById(ftpId));
        } catch (Exception e) {
            logger.error("FTP operation failed", e);
            return buildErrorResponse("An error occurred while listing the FTP Server. \nError:\t" + e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{ftpId}")
    public ResponseEntity<?> deleteFTPServer(@PathVariable UUID ftpId){
        try {
            ftpService.deleteFTPServer(ftpId);
            ResponseDTO response= new ResponseDTO();
            response.setMessage("Process Succeed");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("FTP operation failed", e);
            return buildErrorResponse("An error occurred during the FTP Server delete operation. \nError:\t" + e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{ftpId}")
    public ResponseEntity<?> updateFTPServer(@PathVariable UUID ftpId, @RequestBody FTPServer jsonData) {
        try {
            ftpService.updateFTPServer(ftpId, jsonData);
            ResponseDTO response= new ResponseDTO();
            response.setMessage("Process Succeed");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("FTP operation failed", e);
            return buildErrorResponse("An error occurred during the FTP Server update operation. \nError:\t" + e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    private ResponseEntity<Map<String, String>> buildErrorResponse(String message, HttpStatus status) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", message);
        return new ResponseEntity<>(errorResponse, status);
    }
}