package ai.niksar.contract_wisor_api.helpers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ai.niksar.contract_wisor_api.model.Menu;
import ai.niksar.contract_wisor_api.model.RoleMenu;
import ai.niksar.contract_wisor_api.repository.MenuRepository;

import java.util.*;
import java.util.function.Consumer;

@Component
public class MenuHelper {

    @Autowired
    private MenuRepository menuRepository;

    public static void shiftSubMenuOrders(List<Menu> menusToShift, int shiftBy) {
        for (Menu menu : menusToShift) {
            menu.setMenuOrder(menu.getMenuOrder() + shiftBy);
        }
    }

    public static void shiftMenuOrders(List<Menu> menusToShift, int shiftBy) {
        for (Menu menu : menusToShift) {
            menu.setMenuOrder(menu.getMenuOrder() + shiftBy);
        }
    }

    public static <T> void updateIfPresent(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }

    public static List<Object> buildMenuHierarchy(Map<UUID, Menu> menuMap) {
        Map<UUID, List<Object>> parentToChildrenMap = new HashMap<>();
        List<Object> rootMenus = new ArrayList<>();

        // Build the hierarchy between menus
        for (Menu menu : menuMap.values()) {

            Map<String, Object> menuData = convertMenuToMap(menu);
            if (menu.getParentMenu() != null && menuMap.containsKey(menu.getParentMenu().getId())) {
                UUID parentId = menu.getParentMenu().getId();
                parentToChildrenMap.computeIfAbsent(parentId, k -> new ArrayList<>()).add(menuData);
            } else {
                rootMenus.add(menuData);
            }
        }

        // Order the menus and submenus
        rootMenus.sort(Comparator.comparingInt(m -> (int) ((Map<String, Object>) m).get("menuOrder")));
        for (Object rootMenu : rootMenus) {
            addChildrenToMenu((Map<String, Object>) rootMenu, parentToChildrenMap);
        }

        return rootMenus;
    }
    public static List<Object> buildMenuHierarchyWithRoleId(Map<UUID, Menu> menuMap, List<RoleMenu> roleMenus) {
        Map<UUID, List<Object>> parentToChildrenMap = new HashMap<>();
        List<Object> rootMenus = new ArrayList<>();

        // Build the hierarchy between menus
        for (Menu menu : menuMap.values()) {
            Map<String, Object> menuData = convertMenuToMapWithRoleId(menu, roleMenus.stream().anyMatch(item -> item.getMenu().getId().equals(menu.getId())));
            if (menu.getParentMenu() != null && menuMap.containsKey(menu.getParentMenu().getId())) {
                UUID parentId = menu.getParentMenu().getId();
                parentToChildrenMap.computeIfAbsent(parentId, k -> new ArrayList<>()).add(menuData);
            } else {
                rootMenus.add(menuData);
            }
        }

        // Order the menus and submenus
        rootMenus.sort(Comparator.comparingInt(m -> (int) ((Map<String, Object>) m).get("menuOrder")));
        for (Object rootMenu : rootMenus) {
            addChildrenToMenu((Map<String, Object>) rootMenu, parentToChildrenMap);
        }

        return rootMenus;
    }

    private static Map<String, Object> convertMenuToMap(Menu menu) {
        Map<String, Object> menuData = new HashMap<>();
        menuData.put("id", menu.getId().toString());
        menuData.put("menuName", menu.getMenuName());
        menuData.put("url", menu.getUrl());
        menuData.put("menuOrder", menu.getMenuOrder());
        menuData.put("icon", menu.getIcon());
        menuData.put("category", menu.getCategory());
        menuData.put("children", new ArrayList<>());
        menuData.put("isShowMenu", menu.getIsShowMenu());
        menuData.put("parentId", menu.getParentMenu() != null ? menu.getParentMenu().getId() : null);
        menuData.put("description",menu.getDescription());
        return menuData;
    }
    private static Map<String, Object> convertMenuToMapWithRoleId(Menu menu,boolean isRoleAssign) {
        Map<String, Object> menuData = new HashMap<>();
        menuData.put("id", menu.getId().toString());
        menuData.put("menuName", menu.getMenuName());
        menuData.put("url", menu.getUrl());
        menuData.put("menuOrder", menu.getMenuOrder());
        menuData.put("icon", menu.getIcon());
        menuData.put("category", menu.getCategory());
        menuData.put("children", new ArrayList<>());
        menuData.put("isShowMenu", menu.getIsShowMenu());
        menuData.put("parentId", menu.getParentMenu() != null ? menu.getParentMenu().getId() : null);
        menuData.put("isRoleAssign",isRoleAssign);
        menuData.put("description",menu.getDescription());
        return menuData;
    }

    private static void addChildrenToMenu(Map<String, Object> menuData, Map<UUID, List<Object>> parentToChildrenMap) {
        UUID menuId = UUID.fromString((String) menuData.get("id"));
        List<Object> children = parentToChildrenMap.get(menuId);
        if (children != null && !children.isEmpty()) {
            // Sort the submenu list by menuOrder
            children.sort(Comparator.comparingInt(c -> (int) ((Map<String, Object>) c).get("menuOrder")));
            menuData.put("children", children);
            for (Object activeChildMenu : children) {
                addChildrenToMenu((Map<String, Object>) activeChildMenu, parentToChildrenMap);
            }
        }
    }

    public static void sortMenuTree(List<Map<String, Object>> menuTree) {
        int currentOrder = 1;

        // Sort the main menu
        for (Map<String, Object> menu : menuTree) {
            menu.put("menuOrder", currentOrder);

            // Sort the submenu
            List<Map<String, Object>> children = (List<Map<String, Object>>) menu.get("children");
            if (children != null && !children.isEmpty()) {
                sortSubMenuTree(children, 1); // Submenus start from 1
            }

            currentOrder++;
        }
    }

    private static int sortSubMenuTree(List<Map<String, Object>> menuTree, int startOrder) {
        int currentOrder = startOrder;

        for (Map<String, Object> menu : menuTree) {
            menu.put("menuOrder", currentOrder);

            List<Map<String, Object>> children = (List<Map<String, Object>>) menu.get("children");
            if (children != null && !children.isEmpty()) {
                sortSubMenuTree(children, 1); // Submenus are also ordered within themselves starting from 1
            }

            currentOrder++;
        }

        return currentOrder - 1;
    }

}
