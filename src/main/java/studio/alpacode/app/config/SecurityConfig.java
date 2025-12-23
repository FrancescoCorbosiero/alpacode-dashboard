package studio.alpacode.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import studio.alpacode.app.domain.Role;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

            if (isAdmin) {
                response.sendRedirect("/admin/clients");
            } else {
                response.sendRedirect("/dashboard");
            }
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Public resources
                .requestMatchers("/login", "/login/**").permitAll()
                .requestMatchers("/invite/**").permitAll()
                .requestMatchers("/css/**", "/js/**", "/webfonts/**", "/images/**").permitAll()
                .requestMatchers("/webjars/**").permitAll()
                .requestMatchers("/error").permitAll()

                // Admin area
                .requestMatchers("/admin/**").hasRole(Role.ADMIN.name())

                // Customer area (CLIENT only)
                .requestMatchers("/invoices/**").hasRole(Role.CLIENT.name())
                .requestMatchers("/resources/**").hasRole(Role.CLIENT.name())
                .requestMatchers("/projects/**").hasRole(Role.CLIENT.name())
                .requestMatchers("/subscriptions/**").hasRole(Role.CLIENT.name())

                // Dashboard - accessible by both ADMIN and CLIENT
                .requestMatchers("/dashboard/**").hasAnyRole(Role.ADMIN.name(), Role.CUSTOMER.name())
                // Everything else requires authentication
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .successHandler(authenticationSuccessHandler())
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            );

        return http.build();
    }
}
