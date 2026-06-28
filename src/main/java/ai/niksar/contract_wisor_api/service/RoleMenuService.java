package ai.niksar.contract_wisor_api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ai.niksar.contract_wisor_api.dto.ResponseDTO;
import ai.niksar.contract_wisor_api.exception.ContractWisorException;
import ai.niksar.contract_wisor_api.helpers.MenuHelper;
import ai.niksar.contract_wisor_api.model.Menu;
import ai.niksar.contract_wisor_api.model.Role;
import ai.niksar.contract_wisor_api.model.RoleMenu;
import ai.niksar.contract_wisor_api.repository.MenuRepository;
import ai.niksar.contract_wisor_api.repository.RoleMenuRepository;
import ai.niksar.contract_wisor_api.repository.RoleRepository;

import java.util.*;

@Service
public class RoleMenuService {
    Logger logger = LoggerFactory.getLogger(RoleMenuService.class);

    @Autowired
    private RoleMenuRepository roleMenuRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRoleService userRoleService;

    public RoleMenu assignMenuToRole(RoleMenu roleMenu) {
        Menu menu = menuRepository.findById(roleMenu.getMenu().getId())
                .orElseThrow(() -> new IllegalArgumentException("Menu not found"));

        if(roleMenuRepository.existsByRoleIdAndMenuId(roleMenu.getRole().getId(), menu.getId())){
            throw new RuntimeException("The menu is already assigned to the role.");
        }

        if (!roleMenuRepository.existsByRoleIdAndMenuId(roleMenu.getRole().getId(), menu.getId())) {
            roleMenuRepository.save(roleMenu);
        }

        return roleMenu;
    }

    public void assignAllMenusToAdmin() {
        Role adminRoleOpt = roleRepository.findByIsAdminTrue();
        if (adminRoleOpt == null) {
            logger.info("admin role not found, operation aborted.");
            return;
        }
        List<Menu> allMenus = menuRepository.findAll();
        int assignedCount = 0;

        for (Menu menu : allMenus) {
            if (!roleMenuRepository.existsByRoleIdAndMenuId(adminRoleOpt.getId(), menu.getId())) {
                RoleMenu roleMenu = new RoleMenu();
                roleMenu.setRole(adminRoleOpt);
                roleMenu.setMenu(menu);
                roleMenuRepository.save(roleMenu);
                assignedCount++;
            }
        }

        if (assignedCount > 0) {
            logger.info("Successfully assigned " + assignedCount + " menus to the Admin role.");
        } else {
            logger.info("No new menus were found to assign to the Admin role.");
        }
    }


    public List<Object> getMenuTreeByRoleId(UUID roleId) {
        List<RoleMenu> roleMenus = roleMenuRepository.findByRoleId(roleId);
        List<Menu> allMenus = menuRepository.findAll();
        Map<UUID, Menu> menuMap = new HashMap<>();
        for (Menu menu : allMenus) {
            menuMap.put(menu.getId(), menu);
        }
        return MenuHelper.buildMenuHierarchyWithRoleId(menuMap,roleMenus);
    }
    public List<Menu> getMenusByRoleId(UUID roleId) {
        List<RoleMenu> roleMenus = roleMenuRepository.findByRoleId(roleId);
        List<Menu> menusWithParents = new ArrayList<>();

        for (RoleMenu roleMenu : roleMenus) {
            Menu menu = roleMenu.getMenu();

            // Adding the authorized menu
            if (!menusWithParents.contains(menu)) {
                menusWithParents.add(menu);
            }

            // Adding the parent menus
            while (menu.getParentMenu() != null) {
                menu = menu.getParentMenu();
                if (!menusWithParents.contains(menu)) {
                    menusWithParents.add(menu);
                }
            }
        }

        return menusWithParents;
    }
    public List<RoleMenu> getRolesByMenuId(UUID menuId) {
        return roleMenuRepository.findByMenuId(menuId);
    }

    public List<RoleMenu> getByMenuId(UUID menuId) {
        return roleMenuRepository.findByMenuId(menuId);
    }

    public boolean existsByRoleIdAndMenuId(UUID roleId, UUID menuId) {
        return roleMenuRepository.existsByRoleIdAndMenuId(roleId, menuId);
    }

    @Transactional
    public RoleMenu save(RoleMenu roleMenu) {
        return roleMenuRepository.save(roleMenu);
    }

    public RoleMenu updateRoleMenu(UUID id, RoleMenu roleMenuDetails) {
        RoleMenu existingRoleMenu = roleMenuRepository.findById(id)
                .orElseThrow(ContractWisorException.E022::new);

        Menu menu = menuRepository.findById(roleMenuDetails.getMenu().getId())
                .orElseThrow(ContractWisorException.E009::new);

        // if the added menu is a submenu, its relation with the parent is established
        if (menu.getParentMenu() != null) {
            existingRoleMenu.getMenu().setParentMenu(menu.getParentMenu());
        }

        existingRoleMenu.setRole(roleMenuDetails.getRole());
        existingRoleMenu.setMenu(roleMenuDetails.getMenu());

        return roleMenuRepository.save(existingRoleMenu);
    }
    @Transactional
    public ResponseDTO updateRoleMenuWithMenuIdList(UUID id, Map<String, List<String>> input){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Role role=roleRepository.findById(id).orElseThrow(ContractWisorException.E008::new);
        if(role.isAdmin() && !userRoleService.isAdminUser(username)){
            throw new ContractWisorException.E028();
        }
        ResponseDTO response= new ResponseDTO();
        List<RoleMenu> roleMenus=roleMenuRepository.findByRoleId(id);
        roleMenus.forEach(item-> roleMenuRepository.deleteById(item.getId()));
        roleMenuRepository.flush();
        List<String> menuIdList = input.get("menuList");
        menuIdList.forEach(item->{
            Menu menu=menuRepository.findById(UUID.fromString(item)).orElseThrow(ContractWisorException.E009::new);
            List<Menu> childMenus=menuRepository.findAllByParentId(menu.getId());
            if(childMenus.size() == 0){
                RoleMenu roleMenu=new RoleMenu();
                roleMenu.setRole(role);
                roleMenu.setMenu(menu);
                roleMenuRepository.save(roleMenu);
            }
        });
        response.setMessage("Role menu association operations completed successfully.");
        return response;
    }
    public void deleteRoleMenu(UUID id) {
        roleMenuRepository.deleteById(id);
    }

    public void deleteRoleMenuRelationsByMenuId(UUID menuId) {
        List<RoleMenu> roleMenus = roleMenuRepository.findByMenuId(menuId);
        if (!roleMenus.isEmpty()) {
            roleMenuRepository.deleteAll(roleMenus);
        }
    }
}