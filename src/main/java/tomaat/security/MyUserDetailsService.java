package tomaat.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import tomaat.model.User;
import tomaat.service.UserService;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Component
public class MyUserDetailsService implements UserDetailsService {
    private static final String ROLE_PREFIX = "ROLE_";
    private static final String DEFAULT_ROLE = "USER";

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userOptional = userService.getByEmail(email);
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("Could not find user with email = " + email);
        }

        User user = userOptional.get();

        String roleName = user.getRole() != null ? user.getRole().getName() : DEFAULT_ROLE;
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(ROLE_PREFIX + roleName);

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(authority)
        );
    }

    public UserDetails loadUserById(String id) throws UsernameNotFoundException {
        Optional<User> userOptional = userService.getById(UUID.fromString(id));
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("Could not find user with id = " + id);
        }

        User user = userOptional.get();

        String roleName = user.getRole() != null ? user.getRole().getName() : DEFAULT_ROLE;
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(ROLE_PREFIX + roleName);

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(authority)
        );
    }
}