package tomaat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tomaat.model.ApiResponse;
import tomaat.model.LoginCredentials;
import tomaat.model.Role;
import tomaat.model.User;
import tomaat.security.JWTUtil;
import tomaat.service.UserService;

import javax.security.sasl.AuthenticationException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static tomaat.service.PasswordService.createHashPassword;
import static tomaat.service.PasswordService.createSalt;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final JWTUtil jwtUtil;
    private final AuthenticationManager authManager;
    private final PasswordEncoder passwordEncoder;
//    private final RoleService roleService;

    public AuthController(UserService userService, JWTUtil jwtUtil, AuthenticationManager authManager, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.authManager = authManager;
        this.passwordEncoder = passwordEncoder;
//        this.roleService = roleService;
    }

    @PostMapping("/login")
    public Map<String, Object> loginHandler(@RequestBody LoginCredentials body) {
        try {
            Optional<User> userOpt = this.userService.getByEmail(body.getEmail());
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                if (passwordEncoder.matches(body.getPassword(), user.getPassword())) {
                    UsernamePasswordAuthenticationToken authInputToken =
                            new UsernamePasswordAuthenticationToken(body.getEmail(), body.getPassword());
                    authManager.authenticate(authInputToken);
                }
                String token = jwtUtil.generateToken(user.getUUID());

                Map<String, Object> map = new HashMap<>();
                map.put("jwt-token", token);
                if (body.getPassword().matches("^[a-zA-Z0-9]*$")) {
                    map.put("temporary-password", true);
                }
                return map;
            } else {
                throw new RuntimeException("Invalid login credentials");
            }
        } catch (Exception e) {
            throw new RuntimeException("Invalid Login Credentials");
        }

    }

    @PostMapping("/register")
    public ApiResponse<Map<String, Object>> registerHandler(@RequestBody Map<String, Object> body) throws NoSuchAlgorithmException {
        String email = String.valueOf(body.get("email"));
        Optional<User> userOpt = userService.getByEmail(email);
        if (userOpt.isPresent()) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST, "This email is already taken");
        }
        User user = new User((String) body.get("name"), (String) body.get("email"), (String) body.get("password"));
        user.setSalt(createSalt());
        String encodedPass = passwordEncoder.encode(createHashPassword(user.getPassword(), user.getSalt()));
//        Role userRole = roleDAO.getByName("USER");
//        user.setRole(userRole);
        user.setPassword(encodedPass);
        userService.createUser(user);
        String token = jwtUtil.generateToken(user.getUUID());
        Map<String, Object> map = new HashMap<>();
        map.put("jwt-token", token);
        return new ApiResponse<>(HttpStatus.ACCEPTED, map);
    }
}