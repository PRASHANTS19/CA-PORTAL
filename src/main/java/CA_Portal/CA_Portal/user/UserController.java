package CA_Portal.CA_Portal.user;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        List<UserEntity> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserEntity> getUserById(@PathVariable Long id) {
        return userService.findUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/organization/{orgId}")
    public ResponseEntity<List<UserEntity>> getUsersByOrganization(@PathVariable Long orgId) {
        List<UserEntity> users = userService.findUserByOrgId(orgId);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/type/{userType}")
    public ResponseEntity<List<UserEntity>> getUsersByType(@PathVariable UserType userType) {
        List<UserEntity> users = userService.findUserByUserType(userType);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserEntity> getUserByEmail(@PathVariable String email) {
        return userService.findUserByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/active")
    public ResponseEntity<List<UserEntity>> getActiveUsers() {
        List<UserEntity> users = userService.findActiveUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/organization/{orgId}/internal")
    public ResponseEntity<List<UserEntity>> getInternalUsersByOrg(@PathVariable Long orgId) {
        List<UserEntity> users = userService.findInternalUsersByOrg(orgId);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/organization/{orgId}/customers")
    public ResponseEntity<List<UserEntity>> getCustomersByOrg(@PathVariable Long orgId) {
        List<UserEntity> users = userService.findCustomersByOrg(orgId);
        return ResponseEntity.ok(users);
    }

    @PostMapping
    public ResponseEntity<UserEntity> createUser(@Valid @RequestBody UserEntity user) {
        try {
            UserEntity createdUser = userService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserEntity> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserEntity user) {
        try {
            UserEntity updatedUser = userService.updateUser(id, user);
            return ResponseEntity.ok(updatedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserEntity> partialUpdateUser(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {
        try {
            UserEntity updatedUser = userService.partialUpdateUser(id, updates);
            return ResponseEntity.ok(updatedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            Map<String, Boolean> response = new HashMap<>();
            response.put("deleted", Boolean.TRUE);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/deactivate")
    public ResponseEntity<UserEntity> deactivateUser(@PathVariable Long id) {
        try {
            Map<String, Object> updates = new HashMap<>();
            updates.put("isActive", false);
            UserEntity updatedUser = userService.partialUpdateUser(id, updates);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/activate")
    public ResponseEntity<UserEntity> activateUser(@PathVariable Long id) {
        try {
            Map<String, Object> updates = new HashMap<>();
            updates.put("isActive", true);
            UserEntity updatedUser = userService.partialUpdateUser(id, updates);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
