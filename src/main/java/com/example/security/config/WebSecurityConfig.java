package com.example.security.config;

import com.example.security.AuthenticationFailure;
import com.example.security.AuthenticationSuccess;
import com.example.service.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig implements WebMvcConfigurer {
    private final AppUserService appUserService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationSuccess authenticationSuccess;
    private final AuthenticationFailure authenticationFailure;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors().and().csrf().disable()

            .authorizeRequests()
                .antMatchers("/videoMetas").permitAll()
                .antMatchers("/api/v*/registration/**").permitAll()
                .and()
                .formLogin().permitAll()
                .successHandler(authenticationSuccess)
                .failureHandler(authenticationFailure)

                .and()
                .authenticationProvider(daoAuthenticationProvider());  // custom authentication process
        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        provider.setUserDetailsService(appUserService);
        return provider;
    }
}
