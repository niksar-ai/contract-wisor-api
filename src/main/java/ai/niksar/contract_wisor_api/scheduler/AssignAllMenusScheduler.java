package ai.niksar.contract_wisor_api.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ai.niksar.contract_wisor_api.service.RoleMenuService;

@Component
public class AssignAllMenusScheduler {
    @Autowired
    private RoleMenuService roleMenuService;
    @EventListener(ApplicationReadyEvent.class)
    public void assignAllMenus() {
        roleMenuService.assignAllMenusToAdmin();
    }
}
