//package tomaat.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import tomaat.security.JWTUtil;
//import tomaat.service.RoleService;
//import tomaat.service.UserService;
//
//@RestController
//@RequestMapping("/auth")
//public class AuthController {
//    private final UserService userService;
//    private final JWTUtil jwtUtil;
//    private final AuthenticationManager authManager;
//    private final PasswordEncoder passwordEncoder;
//    private final RoleService roleService;
//
//    public AuthController(UserService userService, JWTUtil jwtUtil, AuthenticationManager authManager, PasswordEncoder passwordEncoder, RoleService roleService) {
//        this.userService = userService;
//        this.jwtUtil = jwtUtil;
//        this.authManager = authManager;
//        this.passwordEncoder = passwordEncoder;
//        this.roleService = roleService;
//    }
//}