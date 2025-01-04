package tomaat.security;

import com.auth0.jwt.JWT;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private final JWTFilter jwtFilter;
    private final MyUserDetailsService myUserDetailsService;

    public SecurityConfiguration(MyUserDetailsService myUserDetailsService) {
        this.myUserDetailsService = myUserDetailsService;
        this.jwtFilter = new JWTFilter(myUserDetailsService, new JWTUtil());
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configure(http))
                .authorizeHttpRequests(auth -> auth
                        // Authentication
                        .requestMatchers("/auth/**").permitAll()
                        // Beers
                        .requestMatchers(HttpMethod.POST, "/beer/newBeer").hasRole("Admin")
                        .requestMatchers(HttpMethod.GET, "/beer/getBeers").permitAll()

                        .requestMatchers(HttpMethod.POST, "/user").permitAll()
                        .requestMatchers(HttpMethod.GET, "/user").permitAll()
                        // Temp
                        .requestMatchers("/user/**").permitAll()
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/auth/register").permitAll()
                        .requestMatchers("/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST,"/auth/**").permitAll()
                        .requestMatchers(HttpMethod.POST,"/auth/register").permitAll()
                        .requestMatchers(HttpMethod.POST,"/auth/login").permitAll()

//                        .anyRequest().authenticated()
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
