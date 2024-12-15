package ar.credify.config;

import ar.credify.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.bind.annotation.GetMapping;

@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserAccountService userAccountService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/").permitAll()
                        .requestMatchers("/register").not().authenticated()
                        .requestMatchers("/login").not().authenticated()
                        .requestMatchers("/registrationSuccess").not().authenticated()
                        .requestMatchers("/studentDetails/view/**").hasAnyRole("Recruiter","Admission-Committee")
                        .requestMatchers("/student/**").hasRole("Student")
                        .requestMatchers("/university/**").hasRole("University-Admin")
                        .requestMatchers("/professor/**").hasRole("Professor")
                        .requestMatchers("/registrar/**").hasRole("Registrar")
                        .requestMatchers("/admissionCommittee/**").hasRole("Admission-Committee")
                        .requestMatchers("/employer/**").hasRole("Employer-Admin")
                        .requestMatchers("/recruiter/**").hasRole("Recruiter")
                        .requestMatchers("/hiringManager/**").hasRole("Hiring-Manager")
                        .anyRequest().authenticated()
                )
                .formLogin((login) -> login
                        .loginPage("/login")
                        .successHandler(authenticationSuccessHandler())
                        .permitAll()
                )
                .logout((logout) -> logout
                        .logoutUrl("/logout")
                        .invalidateHttpSession(true)
                        .logoutSuccessUrl("/login")
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .csrf((csrf) -> csrf.disable());

        return http.build();
    }

    private AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            Authentication auth = authentication;
            if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_Student"))) {
                response.sendRedirect("/student/dashboard");
            } else if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_University-Admin"))) {
                response.sendRedirect("/university/dashboard");
            } else if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_Professor"))) {
                response.sendRedirect("/professor/dashboard");
            } else if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_Registrar"))) {
                response.sendRedirect("/registrar/dashboard");
            } else if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_Admission-Committee"))) {
                response.sendRedirect("/admissionCommittee/dashboard");
            } else if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_Employer-Admin"))) {
                response.sendRedirect("/employer/dashboard");
            } else if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_Recruiter"))) {
                response.sendRedirect("/recruiter/dashboard");
            } else if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_Hiring-Manager"))) {
                response.sendRedirect("/hiringManager/dashboard");
            } else {
                response.sendRedirect("/");
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}