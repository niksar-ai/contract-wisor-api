package ai.niksar.contract_wisor_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ai.niksar.contract_wisor_api.dto.ResponseDTO;
import ai.niksar.contract_wisor_api.dto.UserRoleAssignDTO;
import ai.niksar.contract_wisor_api.dto.UserRoleDTO;
import ai.niksar.contract_wisor_api.exception.ContractWisorException;
import ai.niksar.contract_wisor_api.model.*;
import ai.niksar.contract_wisor_api.repository.RoleRepository;
import ai.niksar.contract_wisor_api.repository.UserRepository;
import ai.niksar.contract_wisor_api.repository.UserRoleRepository;
import ai.niksar.contract_wisor_api.util.Constants.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static ai.niksar.contract_wisor_api.util.Util.dateFormatter;
import static ai.niksar.contract_wisor_api.util.Util.getRealDate;

@Service
public class UserRoleService {

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public List<UserRole> assignRolesToUser(UserRoleAssignDTO userRoleAssignDto) {
        User user = userRepository.findById(userRoleAssignDto.getUserId())
                .orElseThrow(ContractWisorException.E007::new);

        if (Status.PASSIVE.equals(user.getStatus())) {
            throw new ContractWisorException.E011();
        }

        List<Role> roles = roleRepository.findAllById(userRoleAssignDto.getRoleIds());

        List<UserRole> userRoles = roles.stream()
                .map(role -> {
                    UserRole userRole = new UserRole();
                    userRole.setUser(user);
                    userRole.setRole(role);
                    return userRole;
                })
                .collect(Collectors.toList());

        return userRoleRepository.saveAll(userRoles);
    }

    public List<UserRole> getRolesByUserId(UUID userId) {
        return userRoleRepository.findByUserId(userId,dateFormatter(getRealDate()));
    }

    public List<UserRole> getUsersByRoleId(UUID roleId) {
        return userRoleRepository.findByRoleId(roleId);
    }
    public List<UserRoleModel> getUsersDTOByRoleId(UUID roleId) {
        List<UserRole> userRoles=userRoleRepository.findByRoleIdWithDate(roleId,dateFormatter(getRealDate()));
        List<UserRoleModel> users=userRepository.listUsers();
        userRoles.forEach(item-> users.forEach(it->{
            if(item.getUser().getId().equals(it.getId())){
                it.setExpiryDate(item.getExpiryDate());
                it.setRoleAssign(true);
            }
        }));
        users.forEach(it->it.setRoles(userRoleRepository.roleNameListByUserIdAndTime(it.getId(),dateFormatter(getRealDate()))));
        return users;
    }
    @Transactional
    public ResponseDTO updateUserRoles(UUID roleId, Map<String,List<UserRoleDTO>> input) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Role role=roleRepository.findById(roleId).orElseThrow(ContractWisorException.E008::new);
        if(role.isAdmin() && !isAdminUser(username)){
            throw new ContractWisorException.E028();
        }
        List<UserRoleDTO> list=input.get("userList");
        List<UserRole> userRoles=userRoleRepository.findByRoleId(roleId);
        userRoles.forEach(item->userRoleRepository.deleteById(item.getId()));
        userRoleRepository.flush();
        list.forEach(item->{
            UserRole userRole= new UserRole();
            User user=userRepository.findById(item.getId()).orElseThrow(ContractWisorException.E007::new);
            userRole.setRole(role);
            if(item.getExpiryDate() != null && item.getExpiryDate().contains("-")){
                item.setExpiryDate(item.getExpiryDate().replace("-",""));
            }
            if(item.getExpiryDate() != null && !item.getExpiryDate().equals("") && dateFormatter(item.getExpiryDate()).isBefore(dateFormatter(getRealDate()))){
                throw new ContractWisorException.CustomException("The validity date cannot be earlier than the system date");
            }
            userRole.setUser(user);
            userRole.setExpiryDate(item.getExpiryDate() == null || item.getExpiryDate().equals("") ? null : dateFormatter(item.getExpiryDate()));
            userRoleRepository.save(userRole);
            userRoleRepository.flush();
        });
        ResponseDTO responseDTO=new ResponseDTO();
        responseDTO.setMessage("Role user association operations completed successfully.");
        return responseDTO;
    }
    public void deleteUsersByRoleId(UUID roleId) {
        List<UserRole> userRoles = userRoleRepository.findByRoleId(roleId);
        if (userRoles.isEmpty()) {
            throw new ContractWisorException.E012();
        }
        userRoleRepository.deleteAll(userRoles);
    }
    public List<String> roleNameListByUserId(UUID userId){
        return userRoleRepository.roleNameListByUserIdAndTime(userId,dateFormatter(getRealDate()));
    }

    public List<UUID> roleIdListWithUser(UUID userId){
        return userRoleRepository.roleIdListByUserIdAndTime(userId,dateFormatter(getRealDate()));
    }
    public void setUserRoleToAdmin(String username){
        UserRole userRole= new UserRole();
        User user=userRepository.findByUsername(username).get();
        Role adminRole=roleRepository.findByIsAdminTrue();
        if(userRoleRepository.findByRoleIdAndUserId(adminRole.getId(),user.getId()) == null){
            userRole.setRole(adminRole);
            userRole.setUser(user);
            userRoleRepository.save(userRole);
            userRoleRepository.flush();
        }
    }

    public List<UserRole> getAdminUserList(){
        Role adminRole=roleRepository.findByIsAdminTrue();
        return userRoleRepository.findByRoleIdWithDate(adminRole.getId(),dateFormatter(getRealDate()));
    }
    public boolean isAdminUser(String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(ContractWisorException.E016::new);
        Boolean isAdmin=userRoleRepository.isAdminUser(user.getId(),dateFormatter(getRealDate()));
        return isAdmin != null && isAdmin;
    }
}
