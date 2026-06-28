package ai.niksar.contract_wisor_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ai.niksar.contract_wisor_api.dto.MenuDTO;
import ai.niksar.contract_wisor_api.dto.MenuUpdateDTO;
import ai.niksar.contract_wisor_api.model.Menu;
import ai.niksar.contract_wisor_api.service.MenuService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/administration/menus")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @PostMapping
    public ResponseEntity<Menu> createMenu(@RequestBody Menu menu) {
        return ResponseEntity.ok(menuService.createMenu(menu));
    }

    @GetMapping("/list")
    public ResponseEntity<List<Object>> getAllListMenu() {
        return ResponseEntity.ok(menuService.getAllMenus("list"));
    }

    @GetMapping("/tree")
    public ResponseEntity<List<Object>> getAllTreeMenu() {
        return ResponseEntity.ok(menuService.getAllMenus("tree"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Menu> getMenuById(@PathVariable UUID id) {
        return ResponseEntity.ok(menuService.getMenuById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Menu> updateMenu(@PathVariable UUID id, @RequestBody MenuUpdateDTO menuUpdateDto){
        Menu updatedMenu = menuService.updateMenu(id, menuUpdateDto);
        return ResponseEntity.ok(updatedMenu);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMenu(@PathVariable UUID id) {
        return ResponseEntity.ok(menuService.deleteMenu(id));
    }

}
