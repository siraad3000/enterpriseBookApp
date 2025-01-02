package se.systementor.enterpriseBookBackend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import se.systementor.enterpriseBookBackend.services.CustomUserDetailsService;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig {

    @Autowired
    private DataSource dataSource;

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public AuthenticationManager authManager() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return new AuthenticationManager() {
            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                return authProvider.authenticate(authentication);
            }
        };
    }

    /*
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class).build();
    }

 */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOrigin("http://localhost:3000"); // Allow React frontend
        configuration.addAllowedMethod("*"); // Allow all HTTP methods
        configuration.addAllowedHeader("*"); // Allow all headers
        configuration.setAllowCredentials(true); // Allow cookies and credentials

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Apply to all endpoints


        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Use the custom CORS configuration
                .csrf().disable()  // Disable CSRF for simplicity
                .authorizeRequests(auth -> auth
                        .requestMatchers("/getReviews").permitAll()  // Require 'USER' role
                        .requestMatchers(HttpMethod.DELETE, "/deleteReview/*").hasRole("USER")
                        .requestMatchers("/postReview").hasRole("USER") // Allow without authentication
                        .requestMatchers("/loadDatabase").permitAll()  // Allow without authentication
                        .requestMatchers("/api/login").permitAll()  // Allow login
                        .requestMatchers("/api/register").permitAll()  // Allow registration
                        .requestMatchers("/searchBooks").permitAll()  // Allow search
                        .requestMatchers("/api/admin/").hasRole("ADMIN")  // Require 'ADMIN' role
                        .requestMatchers("/saveBook").hasRole("USER")  // Require 'USER' role
                        .requestMatchers("/api/user/").hasAnyRole("USER", "ADMIN")  // Allow 'USER' and 'ADMIN' roles
                        .requestMatchers("/api/logout").hasAnyRole("USER", "ADMIN")  // Allow 'USER' and 'ADMIN' roles
                        .anyRequest().authenticated()  // All other requests require authentication
                )
                .formLogin().disable()  // Disable form-based login
                .httpBasic().disable()  // Disable HTTP basic authentication
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // Stateless session management
                .addFilterBefore(new JwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class); // Add JWT filter

        return http.build();
    }



    @Bean
    public JdbcUserDetailsManager userDetailsService() {
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);

        // Custom queries for authentication and authorization
        manager.setUsersByUsernameQuery("SELECT username, password, true as enabled FROM users WHERE username = ?");
        manager.setAuthoritiesByUsernameQuery("SELECT u.username, r.name as authority " +
                "FROM users u " +
                "JOIN user_roles ur ON u.id = ur.user_id " +
                "JOIN roles r ON ur.role_id = r.id " +
                "WHERE u.username = ?");

        return manager;
    }
}
