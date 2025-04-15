package tomaat.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuration class for Spring Security settings
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    // Path constants for authorization rules
    private static final String[] PUBLIC_AUTH_PATHS = {"/auth/login", "/auth/register"};
    private static final String[] PUBLIC_BEER_GET_PATHS = {"/beer/getBeers", "/beer/type/**", "/beer/brewery/**", "/beer/*"};
    private static final String[] PUBLIC_BEERTYPE_GET_PATHS = {"/beerType/all", "/beerType/*"};
    private static final String[] ADMIN_BEER_PATHS = {"/beer/**"};
    private static final String[] ADMIN_BEERTYPE_PATHS = {"/beerType/**"};
    private static final String[] USER_PATHS = {"/user/**"};
    private static final String USER_INFO_PATH = "/auth/me";

    private final JWTFilter jwtFilter;
    private final MyUserDetailsService myUserDetailsService;

    @Autowired
    public SecurityConfiguration(MyUserDetailsService myUserDetailsService, JWTUtil jwtUtil) {
        this.myUserDetailsService = myUserDetailsService;
        this.jwtFilter = new JWTFilter(myUserDetailsService, jwtUtil);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configure(http))
                .authorizeHttpRequests(auth -> auth
                        // Authentication
                        .requestMatchers(PUBLIC_AUTH_PATHS).permitAll()

                        // Public beer
                        .requestMatchers(HttpMethod.GET, PUBLIC_BEER_GET_PATHS).permitAll()
                        .requestMatchers(HttpMethod.GET, PUBLIC_BEERTYPE_GET_PATHS).permitAll()

                        // Admin beer
                        .requestMatchers(HttpMethod.POST, "/beer/newBeer").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, ADMIN_BEER_PATHS).hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, ADMIN_BEER_PATHS).hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, ADMIN_BEER_PATHS).hasRole("ADMIN")

                        // Admin beerType
                        .requestMatchers(HttpMethod.POST, "/beerType/new").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, ADMIN_BEERTYPE_PATHS).hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, ADMIN_BEERTYPE_PATHS).hasRole("ADMIN")

                        // User
                        .requestMatchers(USER_PATHS).hasAnyRole("USER", "ADMIN")
                        .requestMatchers(USER_INFO_PATH).authenticated()
                )
                .userDetailsService(myUserDetailsService)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) ->
                                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")
                        )
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}