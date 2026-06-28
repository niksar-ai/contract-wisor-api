package ai.niksar.contract_wisor_api.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ai.niksar.contract_wisor_api.dto.ApplicationSetupDTO;

@Service
public class ApplicationSetupService {
    @Autowired
    private MenuService menuService;
    @Autowired
    private RoleApiUrlService roleApiUrlService;
    @Autowired
    private RoleMenuService roleMenuService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private UserService userService;

    @Transactional
    public void setupApplication(ApplicationSetupDTO applicationSetupDTO){
        if(!isFirstSetup()){
            userService.checkUser(applicationSetupDTO.getUser().getUsername(),applicationSetupDTO.getUser().getPassword());
        }
        menuService.updateTemplateMenu(applicationSetupDTO.getMenus());
        if(!roleService.isAdminRoleExists()){
            roleService.createAdminRole();
        }
        userService.createAnonymousUser(applicationSetupDTO.getUser());
        userService.createAdminUser(applicationSetupDTO.getUser(),isFirstSetup());
        roleApiUrlService.assignAllActionsToAdmin();
        userRoleService.setUserRoleToAdmin(applicationSetupDTO.getUser().getUsername());
        roleMenuService.assignAllMenusToAdmin();
    }
    public boolean isFirstSetup(){
        if(!roleService.isAdminRoleExists()){
            return true;
        }
        return userRoleService.getAdminUserList().isEmpty();
    }
}
