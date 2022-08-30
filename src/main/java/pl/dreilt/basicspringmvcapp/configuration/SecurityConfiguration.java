package pl.dreilt.basicspringmvcapp.configuration;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import pl.dreilt.basicspringmvcapp.handler.CustomAuthenticationFailureHandler;

import javax.sql.DataSource;

@Configuration
public class SecurityConfiguration {

    private final DataSource dataSource;
    private final CustomAuthenticationProvider customAuthenticationProvider;

    public SecurityConfiguration(DataSource dataSource, CustomAuthenticationProvider customAuthenticationProvider) {
        this.dataSource = dataSource;
        this.customAuthenticationProvider = customAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(requests -> requests
                .antMatchers("/h2-console/**").permitAll()
                .mvcMatchers("/").permitAll()
                .mvcMatchers("/login**").permitAll()
                .mvcMatchers("/register", "/confirmation").permitAll()
                .mvcMatchers("/admin_panel/**").hasRole("ADMIN")
                .mvcMatchers("/profile").hasAnyRole("ADMIN", "USER")
                .anyRequest().authenticated());
        http.authenticationProvider(customAuthenticationProvider);
        http.formLogin(login -> login
                .loginPage("/login").permitAll()
                .failureHandler(new CustomAuthenticationFailureHandler())
        );
        http.logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout/**", HttpMethod.GET.name()))
                .logoutSuccessUrl("/login?logout").permitAll()
        );
        http.rememberMe().tokenRepository(persistentTokenRepository());
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
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages/messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}
