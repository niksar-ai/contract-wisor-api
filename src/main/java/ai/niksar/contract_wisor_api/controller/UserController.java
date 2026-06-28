package ai.niksar.contract_wisor_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ai.niksar.contract_wisor_api.dto.BlockedUserDTO;
import ai.niksar.contract_wisor_api.dto.PasswordDTO;
import ai.niksar.contract_wisor_api.model.User;
import ai.niksar.contract_wisor_api.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/administration/user")
public class UserController {

    @Autowired
    private UserService userService;

    // List users (Active/Inactive) by the (status) key
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(@RequestParam(required = false) String status) {
        List<User> users;
        if (status != null) {
            users = userService.findByStatus(status);
        } else {
            users = userService.getAllUsers();
        }
        return ResponseEntity.ok(users);
    }
    @GetMapping("/list")
    public ResponseEntity<Object> getUserList(@RequestParam(defaultValue = "1") int pageNumber,@RequestParam(defaultValue = "10") int pageSize,@RequestParam(required = false) String status ,@RequestParam(required = false) String nameTitle ,@RequestParam(required = false) LocalDateTime firstCreateDate ,@RequestParam(required = false) LocalDateTime lastCreateDate)  {
        return ResponseEntity.ok(userService.getUsersFilter(pageNumber, pageSize, status, nameTitle,firstCreateDate,lastCreateDate));
    }

    // View user details
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserDetails(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getProfile(id));
    }

    // Block user
    @PatchMapping("/{id}/block")
    public ResponseEntity<String> blockUser(@PathVariable UUID id, @RequestBody BlockedUserDTO blockUserRequest) {
        String reason = blockUserRequest.getReason();
        LocalDateTime unblockedAt = blockUserRequest.getUnblockedAt();
        userService.blockUser(id, reason, unblockedAt);

        return ResponseEntity.ok("User blocked successfully");
    }

    // Unblock user
    @PatchMapping("/{id}/unblock")
    public ResponseEntity<String> unblockUser(@PathVariable UUID id) {
        userService.unblockUser(id);
        return ResponseEntity.ok("User unblocked successfully");
    }

    // Delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.deleteUser(id));
    }

    // Update user information
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable UUID id, @RequestBody User userUpdateRequest) {
        return ResponseEntity.ok(userService.updateUser(id, userUpdateRequest,null));
    }

    @PostMapping("/{id}/reset-password")
    public ResponseEntity<?> resetUserPass(@PathVariable UUID id, @RequestBody PasswordDTO passwordDTO){
        return ResponseEntity.ok(userService.resetUserPassword(id,passwordDTO));
    }

    @PostMapping("/{id}/active")
    public ResponseEntity<?> activeUser(@PathVariable UUID id){
        return ResponseEntity.ok(userService.activeUser(id));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody User user){
        return ResponseEntity.ok(userService.createUser(user));
    }

}
