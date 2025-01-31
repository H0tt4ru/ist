package com.example.api_core.config;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.api_core.filter.JwtAuthenticationFilter;

import org.springframework.security.web.AuthenticationEntryPoint;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

        private final JwtAuthenticationFilter jwtAuthFilter;
        private final AuthenticationProvider authenticationProvider;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
                httpSecurity
                                .csrf(csrf -> csrf.disable())
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                                .requestMatchers("/student/**").hasRole("USER")
                                                .anyRequest().permitAll())
                                .exceptionHandling(exceptionHandling -> exceptionHandling
                                                .authenticationEntryPoint(new CustomAuthenticationEntryPoint()))
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authenticationProvider(authenticationProvider)
                                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

                return httpSecurity.build();
        }

        public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

                @Override
                public void commence(HttpServletRequest request, HttpServletResponse response,
                                AuthenticationException authenticationException) throws IOException, ServletException {
                        response.setContentType("application/json");
                        response.setStatus(HttpStatus.OK.value());
                        response.getWriter().write(
                                        "{\"responseCode\": 4902, \"responseMessage\": \"Unauthorized request to external service\"}");
                }
        }
}
