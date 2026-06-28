package ai.niksar.contract_wisor_api.service;

import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ai.niksar.contract_wisor_api.exception.ContractWisorException;
import ai.niksar.contract_wisor_api.model.Role;
import ai.niksar.contract_wisor_api.model.RoleMenu;
import ai.niksar.contract_wisor_api.model.User;
import ai.niksar.contract_wisor_api.model.UserRole;
import ai.niksar.contract_wisor_api.repository.RoleMenuRepository;
import ai.niksar.contract_wisor_api.repository.RoleRepository;
import ai.niksar.contract_wisor_api.repository.UserRoleRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static ai.niksar.contract_wisor_api.util.Util.dateFormatter;
import static ai.niksar.contract_wisor_api.util.Util.getRealDate;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private RoleMenuRepository roleMenuRepository;

    @Autowired
    private UserRoleService userRoleService;

    public Role createRole(Role role) {

        if (roleRepository.findByRoleName(role.getRoleName()).isPresent()) {
            throw new ContractWisorException.E013();
        }

        return roleRepository.save(role);
    }

    public List<Role> getAllRoles() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Role> allRoles=roleRepository.findAll();
        if(!userRoleService.isAdminUser(username)){
            return allRoles.stream().filter(item->!item.isAdmin()).collect(Collectors.toList());
        }
        else{
            return allRoles;
        }
    }

    public Role getRoleById(UUID id) {
        return roleRepository.findById(id).orElse(null);
    }

    public Role updateRole(UUID id, Role roleDetails) {
        Role role = roleRepository.findById(id).orElseThrow(ContractWisorException.E008::new);
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if(role.isAdmin() && !userRoleService.isAdminUser(username)){
            throw new ContractWisorException.E028();
        }
        role.setRoleName(roleDetails.getRoleName());
        return roleRepository.save(role);
    }

    @Transactional
    public void deleteRole(UUID id) {
        Role existingRole = roleRepository.findById(id)
                .orElseThrow(ContractWisorException.E008::new);
        if(existingRole.isAdmin()){
            throw new ContractWisorException.E030();
        }
        List<UserRole> userRoles = userRoleService.getUsersByRoleId(id);
        for (UserRole userRole : userRoles) {
            userRoleRepository.deleteById(userRole.getId());
        }

        List<RoleMenu> roleMenus = roleMenuRepository.findByRoleId(id);
        for (RoleMenu roleMenu : roleMenus) {
            roleMenuRepository.deleteById(roleMenu.getId());
        }

        roleRepository.deleteById(id);
    }

    public List<String> getRolesForUser(User user) {
        return userRoleRepository.roleNameListByUserIdAndTime(user.getId(),dateFormatter(getRealDate()));
    }
    public List<UUID> getRoleIdListForUser(User user) {
        return userRoleRepository.roleIdListByUserIdAndTime(user.getId(),dateFormatter(getRealDate()));
    }
    public boolean isAdminRoleExists(){
        return roleRepository.findByIsAdminTrue() != null;
    }
    public void createAdminRole(){
        if(!isAdminRoleExists()){
            Role role=new Role();
            role.setRoleName("admin");
            role.setAdmin(true);
            roleRepository.save(role);
        }
    }
    public Role getAdminRole(){
        return roleRepository.findByIsAdminTrue();
    }


}
