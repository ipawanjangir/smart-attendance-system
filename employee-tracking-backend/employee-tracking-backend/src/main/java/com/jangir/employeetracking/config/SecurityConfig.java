package com.jangir.employeetracking.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            // 1. CORS enable karo taaki Browser Pre-flight requests block na hon
            .cors(Customizer.withDefaults())
            
            // 2. CSRF disable karo (Stateless REST API ke liye)
            .csrf(AbstractHttpConfigurer::disable)

            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            .authorizeHttpRequests(auth -> auth
                // Browser ke sabhi OPTIONS (Preflight) requests ko allow karo
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                
                // Auth endpoints (Login) public hain
                .requestMatchers("/api/auth/**").permitAll()
                
                // Employees API ke liye ADMIN role (Role ya Authority dono support ke liye hasAnyAuthority use kiya hai)
                .requestMatchers("/api/employees/**").hasAnyAuthority("ADMIN", "ROLE_ADMIN")
                
                // Client records endpoints
                .requestMatchers("/api/client-records/my").hasAnyAuthority("EMPLOYEE", "ROLE_EMPLOYEE")
                .requestMatchers(HttpMethod.POST, "/api/client-records").hasAnyAuthority("EMPLOYEE", "ROLE_EMPLOYEE")
                .requestMatchers(HttpMethod.GET, "/api/client-records").hasAnyAuthority("ADMIN", "ROLE_ADMIN")
                
                .anyRequest().authenticated()
            )

            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}