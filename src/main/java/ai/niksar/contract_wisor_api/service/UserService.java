package ai.niksar.contract_wisor_api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ai.niksar.contract_wisor_api.dto.*;
import ai.niksar.contract_wisor_api.exception.ContractWisorException;
import ai.niksar.contract_wisor_api.helpers.UserHelper;
import ai.niksar.contract_wisor_api.model.User;
import ai.niksar.contract_wisor_api.model.UserProfile;
import ai.niksar.contract_wisor_api.repository.UserRepository;
import ai.niksar.contract_wisor_api.specification.UserSpecification;
import ai.niksar.contract_wisor_api.util.Constants.*;
import ai.niksar.contract_wisor_api.util.Util;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import static ai.niksar.contract_wisor_api.util.Util.dateFormatter;
import static ai.niksar.contract_wisor_api.util.Util.getRealDate;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final UserRoleService userRoleService;

    @Autowired
    private final BlockedUserService blockedUserService;

    @Autowired
    private EmailService emailService;

    @Value("${auth.password.expiry.month}")
    private Integer monthExpiry;

    @Value("${web.domain}")
    private String webDomain;

    @Value("${company.mail.info}")
    private String companyMailInfo;

    public UserService(UserRepository userRepository, UserRoleService userRoleService, BlockedUserService blockedUserService) {
        this.userRepository = userRepository;
        this.userRoleService = userRoleService;
        this.blockedUserService = blockedUserService;
    }

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    @Transactional
    public User registerUser(User user) {
        String userName= UserHelper.extractUserName(user.getEmail());
        if (userRepository.findByUsername(userName).isPresent()) {
            throw new ContractWisorException.E014();
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new ContractWisorException.E035();
        }
        if(!Util.isValidEmail(user.getEmail())){
            throw new ContractWisorException.E026();
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUsername(userName);
        if(user.getNameTitle() == null || user.getNameTitle().equals("")) user.setNameTitle(UserHelper.extractNameTitle(user.getName(),user.getSurname()));
        user.setCreateTime(Util.getRealTime());
        user.setCreateDate(Util.getRealDate());
        user.setStatus(Status.ACTIVE);
        user.setPasswordExpiryMonth(LocalDateTime.now().plusMonths(monthExpiry));
        return userRepository.save(user);
    }

    @Transactional
    public void updateUserFields(UUID userId, Map<String, Object> updateData) {
        User user = userRepository.findById(userId)
                .orElseThrow(ContractWisorException.E016::new);
        updateData.forEach((key, value) -> {
            try {
                Field field = User.class.getDeclaredField(key);
                field.setAccessible(true);
                field.set(user, value);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new ContractWisorException.InvalidRequestField(key);
            }
        });
        userRepository.save(user);
    }

    @Transactional
    public void updateUserStatus(UUID userId, String status) {
        User user = userRepository.findById(userId)
                .orElseThrow(ContractWisorException.E016::new);
        user.setStatus(status);
        userRepository.save(user);
    }

    public List<User> getAllUsers() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<User> allUsers=userRepository.findAllUsers();
        if(!userRoleService.isAdminUser(username)){
            return allUsers.stream().filter(item->!userRoleService.isAdminUser(item.getUsername())).collect(Collectors.toList());
        }
        else{
            return allUsers;
        }
    }

    public UserListDTO getUsersFilter(Integer pageNumber, Integer pageSize, String status, String nameTitle, LocalDateTime firstCreateDate, LocalDateTime lastCreateDate){
        UserFilterDTO filterDTO  = userFilterDTO(status,nameTitle,firstCreateDate, lastCreateDate);
        Pageable pageable        = PageRequest.of(pageNumber - 1, pageSize > 100 ? 100 : pageSize);
        Specification<User> spec = new UserSpecification(filterDTO);
        Page<User> documentPage                = userRepository.findAll(spec,pageable);
        UserListDTO userListDTO = new UserListDTO();
        userListDTO.setRecords(documentPage.getContent());
        userListDTO.setTotalRecords((int) documentPage.getTotalElements());
        userListDTO.setCurrent(pageNumber);
        userListDTO.setNumPages(documentPage.getTotalPages());
        userListDTO.setPerPage(documentPage.getContent().size());
        return userListDTO;
    }

    private UserFilterDTO userFilterDTO(String status, String name, LocalDateTime firstCreateDate, LocalDateTime lastCreateDate) {
        UserFilterDTO filterDTO = new UserFilterDTO();

        filterDTO.setStatus(status == null ? "1" : status);
        filterDTO.setCreatedAtEnd(firstCreateDate);
        filterDTO.setCreatedAtEnd(lastCreateDate);
        filterDTO.setNameTitle(name);
        return filterDTO;
    }
    public List<User> findByStatus(String status) {
        return userRepository.findByStatus(status);
    }

    public User findById(UUID id) {
        return userRepository.findById(id).orElseThrow(ContractWisorException.E016::new);
    }
    public UserProfile getProfile(UUID id) {
        UserProfile profile;
        if(id == null ){
             profile= userRepository.userProfileByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        }
        else {
             profile= userRepository.userProfileById(id);
        }
        if(profile == null || profile.getId() == null){
            throw new ContractWisorException.E007();
        }
        profile.setRoles(userRoleService.roleNameListByUserId(profile.getId()));
        return profile;
    }

    @Transactional
    public void blockUser(UUID id, String reason, LocalDateTime unblockedAt) {
        User user = findById(id);
        user.setStatus(Status.PASSIVE);
        blockedUserService.blockUserSave(user.getId(),reason, unblockedAt);
        userRepository.save(user);
    }

    @Transactional
    public void unblockUser(UUID id) {
        User user = findById(id);
        user.setStatus(Status.ACTIVE);
        blockedUserService.unblockUserUpdate(user.getId());
        userRepository.save(user);
    }

    public ResponseDTO deleteUser(UUID id) {
        User user = findById(id);
        if(user.getStatus().equals(Status.PASSIVE)){
            throw new ContractWisorException.E011();
        }
        user.setStatus(Status.PASSIVE);
        userRepository.save(user);
        ResponseDTO responseDTO= new ResponseDTO();
        responseDTO.setMessage("User deleted successfully.");
        return responseDTO;
    }
    public ResponseDTO activeUser(UUID id) {
        User user = findById(id);
        user.setStatus(Status.ACTIVE);
        userRepository.save(user);
        ResponseDTO responseDTO= new ResponseDTO();
        responseDTO.setMessage("User activated successfully.");
        return responseDTO;
    }
    public ResponseDTO updateUserProfile(User userUpdateRequest){
        String username=SecurityContextHolder.getContext().getAuthentication().getName();
        if(userUpdateRequest.getName() == null || userUpdateRequest.getSurname() == null || userUpdateRequest.getEmail() == null || userUpdateRequest.getAvatar() == null || userUpdateRequest.getName().equals("")){
            throw  new ContractWisorException.CustomException("A required field is missing.");
        }
        if(!Util.isValidEmail(userUpdateRequest.getEmail())){
            throw new ContractWisorException.E026();
        }
        updateUser(null,userUpdateRequest,username);
        ResponseDTO responseDTO= new ResponseDTO();
        responseDTO.setMessage("Profile update completed successfully.");
        return responseDTO;
    }
    public ResponseDTO updateUser(UUID id, User userUpdateRequest,String username) {
        User user;
        if(username != null && !username.equals("")){
             user =findUserByUsername(username);
        }
        else {
             user = findById(id);
        }
        if(user.getStatus().equals(Status.PASSIVE)){
            throw new ContractWisorException.E011();
        }
        user.setName(userUpdateRequest.getName());
        user.setSurname(userUpdateRequest.getSurname());
        user.setEmail(userUpdateRequest.getEmail());
        user.setAvatar(userUpdateRequest.getAvatar());
        user.setNameTitle(userUpdateRequest.getNameTitle() == null || userUpdateRequest.getNameTitle().equals("") ? UserHelper.extractNameTitle(user.getName(),user.getSurname()) : userUpdateRequest.getNameTitle());
        userRepository.save(user);
        ResponseDTO responseDTO= new ResponseDTO();
        responseDTO.setMessage("User information updated successfully.");
        return responseDTO;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws ContractWisorException.E016 {
        return loadUserByUsernameWithId(username).getKey();
    }

    @Transactional
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(ContractWisorException.E016::new);
    }
    @Transactional
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(ContractWisorException.E016::new);
    }
    public Entry<UserDetails, UUID> loadUserByUsernameWithId(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(ContractWisorException.E016::new);
        UUID userId = user.getId();
        org.springframework.security.core.userdetails.User.UserBuilder builder = org.springframework.security.core.userdetails.User.withUsername(user.getUsername());
        builder.password(user.getPassword());
        builder.authorities("USER");
        UserDetails userDetails = builder.build();
        return new AbstractMap.SimpleEntry<>(userDetails, userId);
    }

    public boolean isUserBlocked(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(ContractWisorException.E016::new);
        return Status.PASSIVE.equals(user.getStatus());
    }

    public boolean isUserBlocked(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(ContractWisorException.E016::new);
        return Status.PASSIVE.equals(user.getStatus());
    }
    public ResponseDTO updateUserPassword(PasswordDTO passwordDTO){
        User user=findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if(user.getStatus().equals(Status.PASSIVE)){
            throw new ContractWisorException.E011();
        }
        if(passwordDTO.getCurrentPassword() == null || !passwordEncoder.matches(passwordDTO.getCurrentPassword(),user.getPassword())){
            throw new ContractWisorException.E027();
        }
        if(passwordDTO.getNewPassword() == null || passwordDTO.getNewPassword().equals("")){
            throw new ContractWisorException.CustomException("Password cannot be empty");
        }
        user.setPassword(passwordEncoder.encode(passwordDTO.getNewPassword()));
        user.setPasswordChange(false);
        user.setPasswordExpiryMonth(LocalDateTime.now().plusMonths(monthExpiry));
        userRepository.save(user);
        ResponseDTO responseDTO= new ResponseDTO();
        responseDTO.setMessage("Password update completed successfully.");
        return responseDTO;
    }
    public ResponseDTO resetUserPassword(UUID id,PasswordDTO passwordDTO){
        User user=findById(id);
        if(user.getStatus().equals(Status.PASSIVE)){
            throw new ContractWisorException.E011();
        }
        if(passwordDTO.getNewPassword() == null || passwordDTO.getNewPassword().equals("")){
            throw new ContractWisorException.CustomException("Password cannot be empty");
        }
        user.setPassword(passwordEncoder.encode(passwordDTO.getNewPassword()));
        user.setPasswordChange(true);
        user.setPasswordExpiryMonth(LocalDateTime.now().plusMonths(monthExpiry));
        userRepository.save(user);
        ResponseDTO responseDTO= new ResponseDTO();
        responseDTO.setMessage("Password reset completed successfully.");
        return responseDTO;
    }
    public ResponseDTO createUser(User user){
        ResponseDTO responseDTO= new ResponseDTO();
        user.setPasswordChange(true);
        String password=user.getPassword();
        User savedUser=registerUser(user);
        String body=savedUser.getUsername() +" - your account has been created with this username."
                +"\n username: " +savedUser.getUsername()
                +"\n password: " +password
                +"\n You can log in using the link below with the credentials provided."
                +"\n "+webDomain+"/login"
                +"\n For support: "+companyMailInfo;
        logger.info(body);
        emailService.sendMailAsync(savedUser.getEmail(),"New User Registration",body);
        responseDTO.setMessage("The user " + savedUser.getUsername() + " has been created successfully.");
        return responseDTO;
    }
    public boolean checkPassExpiryDate(User user){
        return user.getPasswordExpiryMonth().isBefore(dateFormatter(getRealDate()));
    }
    public ResponseDTO resetUserPasswordWithUsername(String username,PasswordDTO passwordDTO){
        User user=findUserByUsername(username);
        if(user.getStatus().equals(Status.PASSIVE)){
            throw new ContractWisorException.E011();
        }
        if(passwordDTO.getNewPassword() == null || passwordDTO.getNewPassword().equals("")){
            throw new ContractWisorException.CustomException("Password cannot be empty");
        }
        user.setPassword(passwordEncoder.encode(passwordDTO.getNewPassword()));
        user.setPasswordExpiryMonth(LocalDateTime.now().plusMonths(monthExpiry));
        userRepository.save(user);
        ResponseDTO responseDTO= new ResponseDTO();
        responseDTO.setMessage("Password reset completed successfully.");
        return responseDTO;
    }
    public void createAdminUser(User user,boolean isFirstSetup) {
        if (isFirstSetup) {
            String userName= UserHelper.extractUserName(user.getEmail());
            if (userRepository.findByUsername(userName).isPresent()) {
                throw new ContractWisorException.E014();
            }
            if (userRepository.findByEmail(user.getEmail()).isPresent()) {
                throw new ContractWisorException.E035();
            }
            if(!Util.isValidEmail(user.getEmail())){
                throw new ContractWisorException.E026();
            }
            user.setUsername(userName);
            user.setStatus("1");
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setCreateTime(Util.getRealTime());
            user.setCreateDate(Util.getRealDate());
            user.setNameTitle(UserHelper.extractNameTitle(user.getName(),user.getSurname()));
            user.setPasswordExpiryMonth(LocalDateTime.now().plusMonths(monthExpiry));
            userRepository.save(user);
        }
        else{
            checkUser(user.getUsername(),user.getPassword());
        }
    }
    public void checkUser(String username,String password){
        User adminUser=findUserByUsername(username);
        if(!passwordEncoder.matches(password,adminUser.getPassword())){
            throw new ContractWisorException.E027();
        }
        if(!userRoleService.isAdminUser(adminUser.getUsername())){
            throw new ContractWisorException.E028();
        }
    }
    public void createAnonymousUser(User user) {
        if (userRepository.findByUsername("anonymousUser").isEmpty()) {
            user.setStatus("-1");
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setUsername("anonymousUser");
            user.setNameTitle("Unknown User");
            user.setName("");
            user.setSurname("");
            user.setCreateTime(Util.getRealTime());
            user.setCreateDate(Util.getRealDate());
            user.setPasswordExpiryMonth(LocalDateTime.now().plusMonths(monthExpiry));
            userRepository.save(user);
        }
    }
}