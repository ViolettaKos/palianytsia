package com.example.palianytsia.config.security;

import com.example.palianytsia.model.UserRoles;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig {


    private CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }


    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable().authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/guest/*").permitAll();
                    auth.requestMatchers("/user/*").hasAuthority(UserRoles.USER.name());
                    auth.requestMatchers("/admin/*").hasAuthority(UserRoles.ADMIN.name());
                    auth.requestMatchers("/manager/*").hasAuthority(UserRoles.MANAGER.name());
                    try {
                        auth.anyRequest().authenticated().and().exceptionHandling().accessDeniedPage("/noAccess.html");
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }).formLogin((form) -> form
                        .loginPage("/guest/signIn")
                        .defaultSuccessUrl("/user/profile", true)
                        .usernameParameter("email")
                        .permitAll()
                )

                .logout(logout -> logout.logoutUrl("/logout")
                        .logoutSuccessUrl("/guest/mainPage")
                        .permitAll())
                .build();
    }
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/assets/**");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(8);
    }

}
