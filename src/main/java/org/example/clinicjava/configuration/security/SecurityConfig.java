package org.example.clinicjava.configuration.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    private final JwtRequestFilter jwtRequestFilter;

    public SecurityConfig(final JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, SecurityExceptionHandler securityExceptionHandler) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(customizer -> customizer.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/v1/auth/register", "/v1/auth/login").permitAll()
                        .requestMatchers("/v1/auth/logout").authenticated()
                        .requestMatchers("/v1/appointments/create", "/v1/appointments/cancel/{id}").hasAnyAuthority("ROLE_PATIENT")
                        .requestMatchers("/v1/appointments/complete/{id}").hasAnyAuthority("ROLE_DOCTOR")
                        .requestMatchers("/v1/appointments/list").hasAnyAuthority("ROLE_PATIENT", "ROLE_DOCTOR", "ROLE_ADMIN")
                        .requestMatchers("/v1/account/create", "v1/account/list", "v1/account/delete/{id}", "/v1/appointments/confirm/{id}").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/v1/account/update/{id}").hasAnyAuthority("ROLE_PATIENT", "ROLE_DOCTOR", "ROLE_ADMIN")
                        .requestMatchers("/v1/orders/**").hasAuthority("ROLE_PATIENT")
                        .requestMatchers("/v1/payment/**").hasAuthority("ROLE_PATIENT")
                        .requestMatchers("/notfication/**").permitAll() // WebSocket endpoint
                        .anyRequest().authenticated()
                )
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(securityExceptionHandler)
                        .accessDeniedHandler(securityExceptionHandler)
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
