package ai.niksar.contract_wisor_api.dto;

import java.util.UUID;

public class MenuUpdateDTO {
    private String menuName;
    private String url;
    private Boolean isShowMenu;
    private Integer menuOrder;
    private String icon;
    private String category;
    private String description;
    private UUID parentId;

    // Getters ve Setters
    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getIsShowMenu() {
        return isShowMenu;
    }

    public void setIsShowMenu(Boolean isShowMenu) {
        this.isShowMenu = isShowMenu;
    }

    public Integer getMenuOrder() {
        return menuOrder;
    }

    public void setMenuOrder(int menuOrder) {
        this.menuOrder = menuOrder;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public UUID getParentId() {
        return parentId;
    }

    public void setParentId(UUID parentId) {
        this.parentId = parentId;
    }
}
