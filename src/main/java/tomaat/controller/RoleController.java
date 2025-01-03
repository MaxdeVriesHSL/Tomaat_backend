//package tomaat.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//import tomaat.model.ApiResponse;
//import tomaat.model.Role;
//import tomaat.model.User;
//import tomaat.service.RoleService;
//import tomaat.service.UserService;
//
//import java.util.Objects;
//import java.util.Optional;
//import java.util.UUID;
//
//@Controller
//@RequestMapping(value = "/role")
//public class RoleController {
//
//    @Autowired
//    private RoleService roleService;
//    @Autowired
//    private UserService userService;
//
//    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
//    @ResponseBody
//    public ApiResponse<String> getUserRole(@PathVariable UUID userId) {
//        String role = this.roleService.getRoleNameByUserId(userId);
//        return new ApiResponse<>(HttpStatus.ACCEPTED, role, "");
//    }
//
//    @RequestMapping(value = "/setRole", method = RequestMethod.POST)
//    @ResponseBody
//    public ApiResponse<User> setUserRole(@RequestBody User user) {
//        Optional<User> userInDB = userService.getById(user.getId());
//        if (userInDB.isEmpty()) {
//            return new ApiResponse<>(HttpStatus.NOT_FOUND, "No user with that id");
//        }
//        Role userRole;
//        if (!user.getRole().getName().equals("ADMIN") && !user.getRole().getName().equals("SECRETARY") && !user.getRole().getName().equals("USER")) {
//            return new ApiResponse<>(HttpStatus.BAD_REQUEST, "No existing role");
//        }
//        if (Objects.equals(user.getRole().getName(), "ADMIN")) {
//            userRole = roleService.getByName("ADMIN");
//            userInDB.get().setRole(userRole);
//        } else if (Objects.equals(user.getRole().getName(), "USER")) {
//            userRole = roleService.getByName("USER");
//            userInDB.get().setRole(userRole);
//        }
//        userService.createUser(userInDB.get());
//        return new ApiResponse<>(HttpStatus.ACCEPTED, "setNewRole");
//    }
//}