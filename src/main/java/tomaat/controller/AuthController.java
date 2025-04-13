package tomaat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tomaat.model.ApiResponse;
import tomaat.model.LoginCredentials;
import tomaat.model.User;
import tomaat.security.JWTUtil;
import tomaat.service.UserService;
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
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserService userService, JWTUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public Map<String, Object> loginHandler(@RequestBody LoginCredentials body) {
        try {
            Optional<User> userOpt = this.userService.getByEmail(body.getEmail());
            if (userOpt.isPresent()) {
                User user = userOpt.get();

                String hashPassword = createHashPassword(body.getPassword(), user.getSalt());
                if (!passwordEncoder.matches(hashPassword, user.getPassword())) {
                    throw new RuntimeException("Invalid Login Credentials");
                }

                String token = jwtUtil.generateToken(user.getUUID());
                Map<String, Object> map = new HashMap<>();
                map.put("jwt-token", token);
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
        user.setPassword(encodedPass);
        userService.createUser(user);
        String token = jwtUtil.generateToken(user.getUUID());
        Map<String, Object> map = new HashMap<>();
        map.put("jwt-token", token);
        return new ApiResponse<>(HttpStatus.ACCEPTED, map);
    }
}