package ai.niksar.contract_wisor_api.service;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ai.niksar.contract_wisor_api.dto.MenuDTO;
import ai.niksar.contract_wisor_api.dto.MenuUpdateDTO;
import ai.niksar.contract_wisor_api.dto.ResponseDTO;
import ai.niksar.contract_wisor_api.exception.ContractWisorException;
import ai.niksar.contract_wisor_api.helpers.MenuHelper;
import ai.niksar.contract_wisor_api.helpers.RoleMenuHelper;
import ai.niksar.contract_wisor_api.model.Menu;
import ai.niksar.contract_wisor_api.model.User;
import ai.niksar.contract_wisor_api.model.UserRole;
import ai.niksar.contract_wisor_api.repository.MenuRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class MenuService {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private RoleMenuHelper roleMenuHelper;

    @Autowired
    private RoleMenuService roleMenuService;

    @Autowired
    private UserRoleService userRoleService;


    @Transactional
    public Menu createMenu(Menu menu) {
        if (menu.getParentId() != null) {
            Menu parentMenu = menuRepository.findById(menu.getParentId()).orElse(null);
            menu.setParentMenu(parentMenu);
            List<Menu> menus = menuRepository.findMenusByParentIdNullOrderByMenuOrder();
            if(menu.getMenuOrder() == null || menu.getMenuOrder() <= 0 || menu.getMenuOrder() > menus.size()){
                menus.add(menu);
                menu.setMenuOrder(menus.size());
            }
            else{
                menus.add(menu.getMenuOrder() - 1, menu);
                menu.setMenuOrder(menu.getMenuOrder());
            }
            for (int i = 0; i < menus.size(); i++) {
                menus.get(i).setMenuOrder(i + 1);
            }
            menus.remove(menu);
            menuRepository.saveAll(menus);
        } else {
            List<Menu> menus = menuRepository.findMenusByParentIdNullOrderByMenuOrder();
            if(menu.getMenuOrder() == null || menu.getMenuOrder() <= 0 || menu.getMenuOrder() > menus.size()){
                menus.add(menu);
                menu.setMenuOrder(menus.size());
            }
            else{
                menus.add(menu.getMenuOrder() - 1, menu);
                menu.setMenuOrder(menu.getMenuOrder());
            }
            for (int i = 0; i < menus.size(); i++) {
                menus.get(i).setMenuOrder(i + 1);
            }
            menus.remove(menu);
            menuRepository.saveAll(menus);
        }
        menu.setCreatedAt(LocalDateTime.now());
        menu.setUpdatedAt(LocalDateTime.now());

        return menuRepository.save(menu);
    }

    public List<Object> getAllMenus(String type) {
        List<Menu> allMenus = menuRepository.findAll();

        Map<UUID, Menu> menuMap = new HashMap<>();
        for (Menu menu : allMenus) {
            menuMap.put(menu.getId(), menu);
        }

        // If the "tree" type is requested, return the menu tree
        if ("tree".equalsIgnoreCase(type)) {
            return MenuHelper.buildMenuHierarchy(menuMap);
        }

        // If the "list" type is requested, return all menus as a list
        else if ("list".equalsIgnoreCase(type)) {
            return new ArrayList<>(menuMap.values());
        }

        // Invalid
        return new ArrayList<>();
    }


    public Menu getMenuById(UUID id) {
        return menuRepository.findById(id).orElseThrow(ContractWisorException.E004::new);
    }

    @Transactional
    public Menu updateMenu(UUID id, MenuUpdateDTO menuUpdateDto) {
        Menu existingMenu = menuRepository.findById(id)
                .orElseThrow(ContractWisorException.E009::new);

        UUID oldParentId = existingMenu.getParentMenu() != null ? existingMenu.getParentMenu().getId() : null;
        Integer oldMenuOrder = existingMenu.getMenuOrder();
        UUID newParentId = menuUpdateDto.getParentId();

        boolean isParentIdUpdated = (newParentId == null && oldParentId != null) || (newParentId != null && !newParentId.equals(oldParentId));
        boolean isMenuOrderUpdated = menuUpdateDto.getMenuOrder() != null && !menuUpdateDto.getMenuOrder().equals(oldMenuOrder);

        //basically 3 cases
        // if the parent menu and order have changed
        if(oldParentId == null  && newParentId == null && isMenuOrderUpdated){
            sortingNewMenus(existingMenu,menuUpdateDto.getMenuOrder(),null);
        }
        // if it is a sub menu and its parent has changed
        else if(oldParentId != null && newParentId != null && !oldParentId.equals(newParentId)){
            sortingDeletedMenu(existingMenu,oldParentId);
            sortingNewMenus(existingMenu,menuUpdateDto.getMenuOrder(),newParentId);
            Menu newParentMenu = menuRepository.findById(newParentId)
                    .orElseThrow(ContractWisorException.E009::new);
            existingMenu.setParentMenu(newParentMenu);
            existingMenu.setParentId(newParentMenu.getId());
        }
        // if it is a sub menu, the parent is the same and the order has changed
        else if(oldParentId !=null && oldParentId.equals(newParentId) && isMenuOrderUpdated){
            sortingNewMenus(existingMenu,menuUpdateDto.getMenuOrder(),newParentId);
        }

        if (isParentIdUpdated) {
            roleMenuHelper.establishRoleMenuRelations(existingMenu, newParentId);
        }
        MenuHelper.updateIfPresent(menuUpdateDto.getMenuName(), existingMenu::setMenuName);
        MenuHelper.updateIfPresent(menuUpdateDto.getUrl(), existingMenu::setUrl);
        MenuHelper.updateIfPresent(menuUpdateDto.getIsShowMenu(), existingMenu::setIsShowMenu);
        MenuHelper.updateIfPresent(menuUpdateDto.getIcon(), existingMenu::setIcon);
        MenuHelper.updateIfPresent(menuUpdateDto.getCategory(), existingMenu::setCategory);
        MenuHelper.updateIfPresent(menuUpdateDto.getDescription(), existingMenu::setDescription);
        existingMenu.setUpdatedAt(LocalDateTime.now());
        return menuRepository.save(existingMenu);
    }

    @Transactional
    public ResponseDTO deleteMenu(UUID id) {
        ResponseDTO responseDTO=new ResponseDTO();
        Menu existingMenu = menuRepository.findById(id).orElseThrow(ContractWisorException.E009::new);
        List<Menu> relationMenus = menuRepository.findAllByParentId(existingMenu.getId());
        if (!relationMenus.isEmpty()) {
            for (Menu relationMenu : relationMenus) {
                deleteMenu(relationMenu.getId());
            }
        }
        if(existingMenu.getParentMenu() == null ){
            sortingDeletedMenu(existingMenu,null);
        }
        else {
            sortingDeletedMenu(existingMenu,existingMenu.getParentMenu().getId());
        }
        roleMenuService.deleteRoleMenuRelationsByMenuId(id);

        menuRepository.deleteById(id);
        responseDTO.setMessage("The menu has been deleted successfully.");
        return responseDTO;
    }
    private void sortingNewMenus(Menu menu, int newOrder,UUID newParentId){
        List<Menu> menus;
        if(newParentId == null){
            menus = menuRepository.findMenusByParentIdNullOrderByMenuOrder();
        }
        else{
            menus = menuRepository.findMenusByParentIdOrderByMenuOrder(newParentId);
        }
        menus.remove(menu);
        if(newOrder > menus.size() || newOrder <= 0) {
            menus.add(menu);
            menu.setMenuOrder(menus.size());
        }
        else{
            menus.add(newOrder - 1, menu);
            menu.setMenuOrder(newOrder);
        }
        for (int i = 0; i < menus.size(); i++) {
            menus.get(i).setMenuOrder(i + 1);
        }
        menuRepository.saveAll(menus);
    }
    private void sortingDeletedMenu(Menu menu,UUID oldParentId){
        List<Menu> menus;
        if(oldParentId == null){
            menus = menuRepository.findMenusByParentIdNullOrderByMenuOrder();
        }
        else{
            menus = menuRepository.findMenusByParentIdOrderByMenuOrder(oldParentId);
        }
        menus.remove(menu);
        for (int i = 0; i < menus.size(); i++) {
            menus.get(i).setMenuOrder(i + 1);
        }
        menuRepository.saveAll(menus);
    }

    public List<Object> getHierarchicalMenusForUser(User user) {
        Map<UUID, Menu> menuMap = new HashMap<>();
        List<UserRole> userRoleRelations = userRoleService.getRolesByUserId(user.getId());
        for (UserRole userRole : userRoleRelations) {
            List<Menu> menusForRole = roleMenuService.getMenusByRoleId(userRole.getRole().getId());

            for (Menu menu : menusForRole) {
                while (menu != null) {
                    menuMap.put(menu.getId(), menu);
                    menu = menu.getParentMenu();
                }
            }
        }
        List<Object> menuTree = MenuHelper.buildMenuHierarchy(menuMap);
        MenuHelper.sortMenuTree((List<Map<String, Object>>) (Object) menuTree);
        return menuTree;
    }
    @Transactional
    public void updateTemplateMenu(List<MenuDTO> menus){
        Set<String> sentUrls = menus.stream()
                .flatMap(menu -> Stream.concat(
                        Stream.of(menu.getUrl()),
                        menu.getChildren().stream().map(MenuDTO::getUrl)
                ))
                .collect(Collectors.toSet());

        menus.forEach(item->{
            Menu parentMenu;
            if(menuRepository.findByUrl(item.getUrl()).isEmpty()){
                Menu menu=new Menu();
                menu.setMenuOrder(item.getMenuOrder());
                menu.setMenuName(item.getMenuName());
                menu.setIsShowMenu(item.isShowMenu());
                menu.setCategory(item.getCategory());
                menu.setCreatedAt(LocalDateTime.now());
                menu.setDescription(item.getDescription());
                menu.setIcon(item.getIcon());
                menu.setUpdatedAt(LocalDateTime.now());
                menu.setUrl(item.getUrl());
                parentMenu=menuRepository.save(menu);
            }
            else{
                parentMenu=menuRepository.findByUrl(item.getUrl()).get();
            }
            item.getChildren().forEach(child->{
                if(menuRepository.findByUrl(child.getUrl()).isEmpty()){
                    Menu childMenu=new Menu();
                    childMenu.setMenuOrder(child.getMenuOrder());
                    childMenu.setMenuName(child.getMenuName());
                    childMenu.setIsShowMenu(child.isShowMenu());
                    childMenu.setCategory(child.getCategory());
                    childMenu.setCreatedAt(LocalDateTime.now());
                    childMenu.setDescription(child.getDescription());
                    childMenu.setIcon(child.getIcon());
                    childMenu.setUpdatedAt(LocalDateTime.now());
                    childMenu.setUrl(child.getUrl());
                    childMenu.setParentMenu(parentMenu);
                    menuRepository.save(childMenu);
                }
            });
        });
        List<Menu> dropMenus=menuRepository.findAll().stream()
                .filter(menu -> !sentUrls.contains(menu.getUrl())).collect(Collectors.toList());
        deleteMenus(dropMenus);
    }
    private void deleteMenus(List<Menu> dropMenus){
        dropMenus.forEach(item->{
            List<Menu> relationMenus = menuRepository.findAllByParentId(item.getId());
            if (!relationMenus.isEmpty()) {
                for (Menu relationMenu : relationMenus) {
                    deleteMenu(relationMenu.getId());
                }
            }
            if(item.getParentMenu() == null ){
                sortingDeletedMenu(item,null);
            }
            else {
                sortingDeletedMenu(item, item.getParentMenu().getId());
            }
            roleMenuService.deleteRoleMenuRelationsByMenuId(item.getId());
            menuRepository.deleteById(item.getId());
        });
    }
}