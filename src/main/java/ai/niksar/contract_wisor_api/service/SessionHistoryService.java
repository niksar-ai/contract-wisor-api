package ai.niksar.contract_wisor_api.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ai.niksar.contract_wisor_api.exception.ContractWisorException;
import ai.niksar.contract_wisor_api.model.SessionHistory;
import ai.niksar.contract_wisor_api.model.User;
import ai.niksar.contract_wisor_api.repository.SessionHistoryRepository;
import ai.niksar.contract_wisor_api.repository.UserRepository;
import ai.niksar.contract_wisor_api.util.Util;

import java.util.Map;
import java.util.UUID;

@Service
public class SessionHistoryService {

    @Autowired
    private SessionHistoryRepository sessionHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void createLoginHistory(Map<String, Object> loginHistoryMap) {

        UUID userId = (UUID) loginHistoryMap.get("userId");

        User user = userRepository.findById(userId)
                .orElseThrow(ContractWisorException.E016::new);

        SessionHistory sessionHistory = new SessionHistory();
        sessionHistory.setUser(user);
        sessionHistory.setHistoryTime((String) loginHistoryMap.get("historyTime"));
        sessionHistory.setHistoryDate((String) loginHistoryMap.get("historyDate"));
        sessionHistory.setIpAddress((String) loginHistoryMap.get("ipAddress"));
        sessionHistory.setDeviceInfo((String) loginHistoryMap.get("deviceInfo"));
        sessionHistory.setLoginStatus((String) loginHistoryMap.get("loginStatus"));
        sessionHistory.setFailureReason((String) loginHistoryMap.get("failureReason"));
        sessionHistory.setSessionId((UUID) loginHistoryMap.get("sessionId"));
         sessionHistoryRepository.save(sessionHistory);
    }

    public void recordLoginHistory(Map<String, Object> loginHistoryMap, UUID userId, HttpServletRequest request, String status, String failureReason) {
        loginHistoryMap.put("userId", userId);
        loginHistoryMap.put("historyTime", Util.getRealTime());
        loginHistoryMap.put("historyDate", Util.getRealDate());
        loginHistoryMap.put("ipAddress", getClientIp(request));
        loginHistoryMap.put("deviceInfo", request.getHeader("User-Agent"));
        loginHistoryMap.put("loginStatus", status);
        loginHistoryMap.put("failureReason", failureReason);

         this.createLoginHistory(loginHistoryMap);
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
    public SessionHistory getSessionHistoryBySession(UUID sessionId){
        return sessionHistoryRepository.findBySessionId(sessionId);
    }
    public void saveSessionHistory(SessionHistory sessionHistory){
        sessionHistoryRepository.save(sessionHistory);
    }
}
