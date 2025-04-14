package tomaat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tomaat.model.ApiResponse;
import tomaat.model.Role;
import tomaat.model.User;
import tomaat.service.RoleService;
import tomaat.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(value = "/role")
public class RoleController {
    private static final List<String> VALID_ROLES = List.of("ADMIN", "USER");

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @GetMapping("/{userId}")
    public ApiResponse<String> getUserRole(@PathVariable UUID userId) {
        String role = this.roleService.getRoleNameByUserId(userId);
        return new ApiResponse<>(HttpStatus.OK, role, "");
    }

    @PostMapping("/setRole")
    public ApiResponse<User> setUserRole(@RequestBody User user) {
        Optional<User> userInDB = userService.getById(UUID.fromString(user.getId()));
        if (userInDB.isEmpty()) {
            return new ApiResponse<>(HttpStatus.NOT_FOUND, "No user with that id");
        }

        String roleName = user.getRole().getName();
        if (!VALID_ROLES.contains(roleName)) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST, "Invalid role name");
        }

        Role roleToSet = roleService.getByName(roleName);
        User updatedUser = userInDB.get();
        updatedUser.setRole(roleToSet);

        userService.createUser(updatedUser);
        return new ApiResponse<>(HttpStatus.OK, updatedUser, "Role updated successfully");
    }
}