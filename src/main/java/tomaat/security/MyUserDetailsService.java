package tomaat.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import tomaat.DAO.UserRepository;
import tomaat.model.User;
import tomaat.service.UserService;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Component
public class MyUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserService userService;

    public MyUserDetailsService(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userRes = userRepository.findByEmail(email);
        if (userRes.isEmpty()) {
            throw new UsernameNotFoundException("Could not find user with email " + email);
        }
        User user = userRes.get();
        String grantedRole = "ROLE_" + user.getRole().getName();
        return new org.springframework.security.core.userdetails.User(
                email,
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(grantedRole))
        );
    }

    public UserDetails loadUserById(String id) throws UsernameNotFoundException {
        UUID uuid = UUID.fromString(id);
        Optional<User> userOpt = userService.getById(uuid);
        if (userOpt.isEmpty()) {
            throw new UsernameNotFoundException("Could not find user with id: " + id);
        }
        User user = userOpt.get();
        String grantedRole = "ROLE_" + user.getRole().getName();
        return new org.springframework.security.core.userdetails.User(
                id,
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(grantedRole)));
    }
}
