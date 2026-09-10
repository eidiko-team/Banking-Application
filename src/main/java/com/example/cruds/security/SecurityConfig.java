package com.example.cruds.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Autowired
    private  UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {
        log.debug("SecurityConfig is loaded**************");
        System.out.println("🔥 SecurityConfig is loaded");

        return http
                .csrf(csrf ->csrf.disable())
                .authorizeHttpRequests(auth -> auth

//                        .requestMatchers(HttpMethod.GET,"/account/getAll").hasRole("CUSTOMER") //role and method based authentication
//                        .requestMatchers(HttpMethod.GET,"/account/getAll").hasAnyRole("USER","CUSTOMER")
//                        .requestMatchers(HttpMethod.GET,"/account/getAll").hasAuthority("ROLE_CUSTOMER")
//                        .requestMatchers(HttpMethod.GET,"/account/getAll").hasAnyAuthority("ROLE_CUSTOMER","ROLE_USER")

                        .requestMatchers(
                        "/auth/**",
                        "/auth/login",
                                "/auth/refresh",
                        "customer/getAll",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/v3/api-docs/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) //Without STATELESS, Spring Security is allowed to use the HTTP session (default: IF_REQUIRED).
                )


                // Add JWT filter before Spring Security's default filter
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
//                .httpBasic(Customizer.withDefaults())
                .build();
    }


//    In memory implementation
//    This tells Spring Security:
//    Use this method to get user information for authentication, before we will get the password in the console now we wont get it
//    @Bean
//    public UserDetailsService userDetailsService() {
//
//        UserDetails user = User
//                .withUsername("ram")
//                .password("{noop}12345") //It means: Don't encrypt the password.
//                .roles("USER")
//                .build();
//
//        return new InMemoryUserDetailsManager(user);
//    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    /*
     * Authentication manager bean
     * Required for programmatic authentication (e.g., in /generateToken)
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}