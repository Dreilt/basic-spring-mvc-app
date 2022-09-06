package pl.dreilt.basicspringmvcapp.configuration;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import pl.dreilt.basicspringmvcapp.repository.AppUserRepository;
import pl.dreilt.basicspringmvcapp.repository.AppUserRoleRepository;
import pl.dreilt.basicspringmvcapp.service.AppUserService;
import pl.dreilt.basicspringmvcapp.service.CustomUserDetailsService;
import pl.dreilt.basicspringmvcapp.service.PasswordService;

import javax.sql.DataSource;

@Configuration
public class CustomConfiguration {

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages/messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
    @Bean
    public PersistentTokenRepository persistentTokenRepository(DataSource dataSource) {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }

    @Bean
    AppUserService appUserService(AppUserRepository appUserRepository, AppUserRoleRepository appUserRoleRepository, PasswordEncoder passwordEncoder) {
        return new AppUserService(appUserRepository, appUserRoleRepository, passwordEncoder);
    }
    @Bean
    CustomUserDetailsService customUserDetailsService(AppUserService appUserService) {
        return new CustomUserDetailsService(appUserService);
    }

    @Bean
    PasswordService passwordService(AuthenticationManager authenticationManager) {
        return new PasswordService(authenticationManager);
    }
}
