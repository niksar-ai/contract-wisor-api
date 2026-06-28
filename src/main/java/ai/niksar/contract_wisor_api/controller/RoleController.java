package ai.niksar.contract_wisor_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ai.niksar.contract_wisor_api.dto.UserRoleDTO;
import ai.niksar.contract_wisor_api.model.Role;
import ai.niksar.contract_wisor_api.service.RoleApiUrlService;
import ai.niksar.contract_wisor_api.service.RoleMenuService;
import ai.niksar.contract_wisor_api.service.RoleService;
import ai.niksar.contract_wisor_api.service.UserRoleService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/administration/role")
public class RoleController {

    @Autowired
    private RoleService roleService;
    @Autowired
    private RoleMenuService roleMenuService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RoleApiUrlService roleApiUrlService;
    @PostMapping
    public ResponseEntity<Role> createRole(@RequestBody Role role) {
        return ResponseEntity.ok(roleService.createRole(role));
    }

    @GetMapping
    public ResponseEntity<List<Role>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable UUID id) {
        return ResponseEntity.ok(roleService.getRoleById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Role> updateRole(@PathVariable UUID id, @RequestBody Role roleDetails) {
        Role updatedRole = roleService.updateRole(id, roleDetails);
        return ResponseEntity.ok(updatedRole);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable UUID id) {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{roleId}/menu/tree")
    public ResponseEntity<?> getMenusByRoleId(@PathVariable UUID roleId) {
        return ResponseEntity.ok(roleMenuService.getMenuTreeByRoleId(roleId));
    }
    @PostMapping("/{id}/menu/save")
    public ResponseEntity<?> updateRoleMenu(@PathVariable UUID id, @RequestBody Map<String, List<String>> input) {
        return ResponseEntity.ok(roleMenuService.updateRoleMenuWithMenuIdList(id,input));
    }
    @PostMapping("/{id}/user/save")
    public ResponseEntity<?> updateRoleUser(@PathVariable UUID id, @RequestBody Map<String,List<UserRoleDTO>> list) {
        return ResponseEntity.ok(userRoleService.updateUserRoles(id,list));
    }
    @GetMapping("/{roleId}/user/list")
    public ResponseEntity<?> getUsersByRoleId(@PathVariable UUID roleId) {
        return ResponseEntity.ok(userRoleService.getUsersDTOByRoleId(roleId));
    }
    @PostMapping("/{roleId}/action/save")
    public ResponseEntity<?> updateRoleApiUrl(@PathVariable UUID roleId, @RequestBody Map<String, List<String>> input) {
        return ResponseEntity.ok(roleApiUrlService.saveRoleApiUrl(roleId,input));
    }
    @GetMapping("/{roleId}/action/list")
    public ResponseEntity<?> getApiUrlListWithRoleId(@PathVariable UUID roleId) {
        return ResponseEntity.ok(roleApiUrlService.getApiUrlListWithRoleId(roleId));
    }
}
