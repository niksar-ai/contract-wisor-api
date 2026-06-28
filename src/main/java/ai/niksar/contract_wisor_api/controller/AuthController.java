package ai.niksar.contract_wisor_api.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ai.niksar.contract_wisor_api.dto.PasswordDTO;
import ai.niksar.contract_wisor_api.dto.ResponseDTO;
import ai.niksar.contract_wisor_api.exception.ContractWisorException;
import ai.niksar.contract_wisor_api.model.ResetPassword;
import ai.niksar.contract_wisor_api.model.Session;
import ai.niksar.contract_wisor_api.model.User;
import ai.niksar.contract_wisor_api.service.*;
import ai.niksar.contract_wisor_api.util.Constants.*;
import ai.niksar.contract_wisor_api.util.JwtTokenProvider;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    SessionHistoryService sessionHistoryService;

    @Autowired
    SessionService sessionService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private ResetPasswordService resetPasswordService;

    @Autowired
    private EmailService emailService;

    @Value("${auth.password.expiry.enabled}")
    private boolean passwordExpiryEnable;

    @Value("${web.domain}")
    private String webDomain;

    @Value("${company.mail.info}")
    private String companyMailInfo;

    public AuthController() {
    }

    // Register
    @Transactional
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user){
        User savedUser = userService.registerUser(user);
        emailService.sendMailAsync(savedUser.getEmail(),"New User Registration",savedUser.getUsername() +" user registration has been completed successfully."
                +"\n You can log in by clicking the link below."
                +"\n"+webDomain+"/login"
                +"\n For support : "+companyMailInfo);
        ResponseDTO responseDTO= new ResponseDTO();
        responseDTO.setMessage(savedUser.getUsername() +" has registered successfully.");
        return ResponseEntity.ok(responseDTO);
    }

    // Login
    @Transactional(noRollbackFor = BadCredentialsException.class)
    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody User user, HttpServletRequest request){

        Map<String, Object> loginHistoryMap = new HashMap<>();
        UUID userId = null;

        try{
            if(user.getUsername().contains("@")){
                user.setUsername(user.getUsername().split("@")[0]);
            }
            User foundUser = userService.findUserByUsername(user.getUsername());
            if(foundUser.getUsername().equals("anonymousUser")){
                throw new ContractWisorException.E028();
            }
            if(userService.isUserBlocked(foundUser.getUsername())){
                throw new ContractWisorException.E018();
            }
            userId = foundUser.getId();
            loginHistoryMap.put("userId", userId);

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

            Session session=sessionService.recordSession(loginHistoryMap,userId,request);
            loginHistoryMap.put("sessionId",session.getId());
            sessionHistoryService.recordLoginHistory(loginHistoryMap, userId, request, LoginStatus.SUCCESS, "");


            final String accessToken = jwtTokenProvider.generateAccessToken(foundUser,session.getId());
            final String refreshToken = jwtTokenProvider.generateRefreshToken(foundUser,session.getId());

            LocalDateTime expireDate=jwtTokenProvider.extractExpirationFromRefreshToken(refreshToken).toInstant().atZone(ZoneId.systemDefault()) .toLocalDateTime();
            sessionService.updateSession(session,expireDate);

            Map<String, Object> fieldsToUpdate = new HashMap<>();
            fieldsToUpdate.put(GeneralKeys.LAST_LOGIN_TIME, LocalDateTime.now());
            if(!foundUser.isPasswordChange() && passwordExpiryEnable) fieldsToUpdate.put("isPasswordChange",userService.checkPassExpiryDate(foundUser));
            userService.updateUserFields(userId, fieldsToUpdate);
            emailService.sendMailAsync(foundUser.getEmail(),"User Login","A login to the application was made with the username "+foundUser.getUsername() +"."
            +"\n If you did not initiate this action, please disregard this email or contact us."
            +"\n For support : "+companyMailInfo);
            List<Object> hierarchicalMenus = menuService.getHierarchicalMenusForUser(foundUser);
            Map<String, Object> tokens = new HashMap<>();
            tokens.put(GeneralKeys.ACCESS_TOKEN, accessToken);
            tokens.put(GeneralKeys.REFRESH_TOKEN, refreshToken);
            tokens.put("menus",hierarchicalMenus);
            return ResponseEntity.ok(tokens);


        }catch (BadCredentialsException e){
            sessionHistoryService.recordLoginHistory(loginHistoryMap, userId, request, LoginStatus.FAILURE, "Invalid credentials");
            if(userId != null){
                Map<String, Object> fieldsToUpdate = new HashMap<>();
                fieldsToUpdate.put("lastFailureLoginTime", LocalDateTime.now());
                userService.updateUserFields(userId, fieldsToUpdate);
            }
            return buildErrorResponse("Invalid credentials");
        }
    }


    @Transactional
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> refreshToken) {

        Duration REFRESH_TOKEN_RENEW_THRESHOLD = Duration.ofHours(24);
        String username = jwtTokenProvider.extractUsernameFromRefreshToken(refreshToken.get(GeneralKeys.REFRESH_TOKEN));
        String sessionId=jwtTokenProvider.getSessionIdFromRefreshToken(refreshToken.get(GeneralKeys.REFRESH_TOKEN));
        Session session=sessionService.getSessionById(UUID.fromString(sessionId));
        if (jwtTokenProvider.validateRefreshToken(refreshToken.get(GeneralKeys.REFRESH_TOKEN), username) && session.getId() != null) {
            User userInfo=userService.findUserByUsername(username);
            List<Object> hierarchicalMenus = menuService.getHierarchicalMenusForUser(userInfo);
            if(userService.isUserBlocked(userInfo.getUsername())){
                throw new ContractWisorException.E018();
            }
            String newAccessToken = jwtTokenProvider.generateAccessToken(userInfo,session.getId());
            if(Duration.between(LocalDateTime.now(),jwtTokenProvider.extractExpirationFromRefreshToken(refreshToken.get(GeneralKeys.REFRESH_TOKEN)).toInstant().atZone(ZoneId.systemDefault()) .toLocalDateTime()).compareTo(REFRESH_TOKEN_RENEW_THRESHOLD) <= 0){
                String newRefreshToken=jwtTokenProvider.generateRefreshToken(userInfo,session.getId());
                LocalDateTime expireDate=jwtTokenProvider.extractExpirationFromRefreshToken(newRefreshToken).toInstant().atZone(ZoneId.systemDefault()) .toLocalDateTime();
                sessionService.updateSession(session,expireDate);
                Map<String, Object> tokens = new HashMap<>();
                tokens.put(GeneralKeys.ACCESS_TOKEN, newAccessToken);
                tokens.put(GeneralKeys.REFRESH_TOKEN, newRefreshToken);
                tokens.put("menus",hierarchicalMenus);
                return ResponseEntity.ok(tokens);
            }
            else{
                Map<String, Object> tokens = new HashMap<>();
                tokens.put(GeneralKeys.ACCESS_TOKEN, newAccessToken);
                tokens.put(GeneralKeys.REFRESH_TOKEN, refreshToken.get(GeneralKeys.REFRESH_TOKEN));
                tokens.put("menus",hierarchicalMenus);
                return ResponseEntity.ok(tokens);
            }

        } else {
            return buildErrorResponse("Invalid refresh token.");
        }
    }

    @Transactional
    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new ContractWisorException.CustomException("An invalid or missing token was provided.");
        }
        String token = authorizationHeader.substring(7);
        String username = jwtTokenProvider.extractUsername(token);
        String sessionId = jwtTokenProvider.getSessionIdFromAccessToken(token);
        sessionService.logoutSession(sessionId);

        return ResponseEntity.ok(username + " has logged out.");
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getUserDetails() {
        return ResponseEntity.ok(userService.getProfile(null));
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateUserProfile(@RequestBody User userUpdateRequest){
        return ResponseEntity.ok(userService.updateUserProfile(userUpdateRequest));
    }

    @PostMapping("/password-update")
    public ResponseEntity<?> updateUserPass(@RequestBody PasswordDTO passwordDTO){
        return ResponseEntity.ok(userService.updateUserPassword(passwordDTO));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String,String> input){
        return ResponseEntity.ok(resetPasswordService.forgotPassword(input));
    }

    @GetMapping("/reset-password/{id}")
    public ResponseEntity<?> getResetPassword(@PathVariable UUID id){
        return ResponseEntity.ok(resetPasswordService.getActiveResetPassword(id));
    }

    @PostMapping("/reset-password/{id}")
    public ResponseEntity<?> getResetPassword(@PathVariable UUID id, @RequestBody PasswordDTO passwordDTO){
        return ResponseEntity.ok(resetPasswordService.resetUserPassword(id,passwordDTO));
    }

    private ResponseEntity<Map<String, String>> buildErrorResponse(String message) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", message);
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }
}
