package ai.niksar.contract_wisor_api.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ai.niksar.contract_wisor_api.exception.ContractWisorException;
import ai.niksar.contract_wisor_api.model.Session;
import ai.niksar.contract_wisor_api.model.SessionHistory;
import ai.niksar.contract_wisor_api.model.User;
import ai.niksar.contract_wisor_api.repository.SessionRepository;
import ai.niksar.contract_wisor_api.repository.UserRepository;
import ai.niksar.contract_wisor_api.util.Util;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
public class SessionService {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionHistoryService sessionHistoryService;

    @Transactional
    public Session createSession(Map<String, Object> loginHistoryMap) {

        UUID userId = (UUID) loginHistoryMap.get("userId");

        User user = userRepository.findById(userId)
                .orElseThrow(ContractWisorException.E016::new);

        Session session = new Session();
        session.setUser(user);
        session.setLoginTime((String) loginHistoryMap.get("historyTime"));
        session.setLoginDate((String) loginHistoryMap.get("historyDate"));
        session.setIpAddress((String) loginHistoryMap.get("ipAddress"));
        session.setDeviceInfo((String) loginHistoryMap.get("deviceInfo"));
        return sessionRepository.save(session);
    }

    public Session recordSession(Map<String, Object> loginHistoryMap, UUID userId, HttpServletRequest request) {
        loginHistoryMap.put("userId", userId);
        loginHistoryMap.put("historyTime", Util.getRealTime());
        loginHistoryMap.put("historyDate", Util.getRealDate());
        loginHistoryMap.put("ipAddress", getClientIp(request));
        loginHistoryMap.put("deviceInfo", request.getHeader("User-Agent"));
        return this.createSession(loginHistoryMap);
    }

    private String getClientIp(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("X-Real-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        if (ipAddress != null && ipAddress.contains(",")) {
            ipAddress = ipAddress.split(",")[0];
        }
        return ipAddress;
    }
    public Session getSessionByUser(User user){
        return sessionRepository.findByUser(user);
    }
    public Session getSessionById(UUID id){
        return sessionRepository.findById(id).orElse(new Session());
    }

    public boolean isSessionActive(UUID id) {
        return !sessionRepository.findById(id).isEmpty();
    }
    public void deleteSession(UUID id){
        sessionRepository.deleteById(id);
    }
    public void save(Session session){
        sessionRepository.save(session);
    }
    public void updateSession(Session session, LocalDateTime expireDate){
        session.setExpireDate(expireDate);
        SessionHistory sessionHistory=sessionHistoryService.getSessionHistoryBySession(session.getId());
        sessionHistory.setExpireDate(expireDate);
        sessionHistoryService.saveSessionHistory(sessionHistory);
        save(session);
    }
    public void logoutSession(String sessionId){
        SessionHistory sessionHistory=sessionHistoryService.getSessionHistoryBySession(UUID.fromString(sessionId));
        sessionHistory.setLoginStatus("2");
        sessionHistory.setLogoutDate(Util.getRealDate());
        sessionHistory.setLogoutTime(Util.getRealTime());
        sessionHistory.setUpdatedAt(LocalDateTime.now());
        sessionHistoryService.saveSessionHistory(sessionHistory);
        deleteSession(UUID.fromString(sessionId));
    }
}
