package ai.niksar.contract_wisor_api.helpers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ai.niksar.contract_wisor_api.exception.ContractWisorException;
import ai.niksar.contract_wisor_api.model.Menu;
import ai.niksar.contract_wisor_api.model.RoleMenu;
import ai.niksar.contract_wisor_api.repository.MenuRepository;
import ai.niksar.contract_wisor_api.service.RoleMenuService;

import java.util.List;
import java.util.UUID;

@Component
public class RoleMenuHelper {

    @Autowired
    private RoleMenuService roleMenuService;

    @Autowired
    private MenuRepository menuRepository;

    public void establishRoleMenuRelations(Menu menu, UUID parentId) {
        if (parentId != null) {
            Menu parentMenu = menuRepository.findById(parentId)
                    .orElseThrow(ContractWisorException.E009::new);

            List<RoleMenu> parentRoleMenus = roleMenuService.getByMenuId(parentId);
            for (RoleMenu roleMenu : parentRoleMenus) {
                if (!roleMenuService.existsByRoleIdAndMenuId(roleMenu.getRole().getId(), menu.getId())) {
                    RoleMenu newRoleMenu = new RoleMenu();
                    newRoleMenu.setRole(roleMenu.getRole());
                    newRoleMenu.setMenu(menu);
                    roleMenuService.save(newRoleMenu);
                }
            }
            establishRoleMenuRelations(parentMenu, parentMenu.getParentMenu() != null ? parentMenu.getParentMenu().getId() : null);
        }
    }
}
