package com.epiroom.api.config;

import com.epiroom.api.repository.UserRepository;
import com.epiroom.api.security.AuthFilter;
import com.epiroom.api.security.AccessDenied;
import com.epiroom.api.security.Oauth2FailureHandler;
import com.epiroom.api.security.Oauth2LoginSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    private final UserRepository userRepository;

    public SecurityConfiguration(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.addFilterBefore(new AuthFilter(userRepository), AnonymousAuthenticationFilter.class);
        http
            .requiresChannel(channel ->
                channel.anyRequest().requiresSecure())
            .authorizeHttpRequests((auth) -> auth
                .requestMatchers(HttpMethod.POST, "/campus/").hasAuthority("campus:create")
                .requestMatchers(HttpMethod.POST, "/campus/**/floors").hasAuthority("floor:create")
                .requestMatchers(HttpMethod.PATCH, "/campus/**/floors/main").hasAuthority("floor:create")
                .requestMatchers(HttpMethod.GET, "/campus/").hasAuthority("floor:create")
                .anyRequest().permitAll()
            )
            .oauth2Login(customizer -> {
                customizer.successHandler(new Oauth2LoginSuccessHandler(userRepository));
            })
            .exceptionHandling(customizer -> {
                customizer.accessDeniedHandler(new AccessDenied());
                customizer.authenticationEntryPoint(new Oauth2FailureHandler());
            });
        return http.build();
    }
}