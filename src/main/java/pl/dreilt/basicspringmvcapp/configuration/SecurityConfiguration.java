package pl.dreilt.basicspringmvcapp.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import pl.dreilt.basicspringmvcapp.handler.CustomAuthenticationFailureHandler;

@Configuration
public class SecurityConfiguration {

    private final PersistentTokenRepository persistentTokenRepository;

    public SecurityConfiguration(PersistentTokenRepository persistentTokenRepository) {
        this.persistentTokenRepository = persistentTokenRepository;
    }
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(requests -> requests
                .antMatchers("/h2-console/**").permitAll()
                .mvcMatchers("/").permitAll()
                .mvcMatchers("/login**").permitAll()
                .mvcMatchers("/register", "/confirmation").permitAll()
                .mvcMatchers("/admin_panel/**").hasRole("ADMIN")
                .mvcMatchers("/profile").hasAnyRole("ADMIN", "USER")
                .anyRequest().authenticated());
        http.formLogin(login -> login
                .loginPage("/login").permitAll()
                .failureHandler(new CustomAuthenticationFailureHandler())
        );
        http.logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout/**", HttpMethod.GET.name()))
                .logoutSuccessUrl("/login?logout").permitAll()
        );
        http.rememberMe().tokenRepository(persistentTokenRepository);
        http.csrf().disable();
        http.headers().frameOptions().disable();
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().mvcMatchers(
                "/scripts/**", "/styles/**"
        );
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
