package com.example.delivery.global.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  private final JwtTokenProvider jwtTokenProvider;


  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http,
      AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
    http.httpBasic(HttpBasicConfigurer::disable)
        .csrf(CsrfConfigurer::disable)
        .cors(Customizer.withDefaults())
        .sessionManagement(
            configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(
            authorize ->
                authorize
                    .requestMatchers(
                        "/api/v1/auth/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/v3/api-docs/**",
                        "/v3/api-docs/swagger-config/**",
                        "/chat/**",
                        "/webjars/**",
                        "/ws-stomp/**")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .exceptionHandling(authenticationManager -> authenticationManager
            .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
            .accessDeniedHandler(new CustomAccessDeniedHandler())
        )
        .addFilterBefore(
            new JwtAuthenticationFilter(jwtTokenProvider),
            UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }
}
