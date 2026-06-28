package ai.niksar.contract_wisor_api.dto;

import ai.niksar.contract_wisor_api.model.User;

import java.util.List;

public class ApplicationSetupDTO {
    private User user;
    private List<MenuDTO> menus;



    public List<MenuDTO> getMenus() {
        return menus;
    }

    public void setMenus(List<MenuDTO> menus) {
        this.menus = menus;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
