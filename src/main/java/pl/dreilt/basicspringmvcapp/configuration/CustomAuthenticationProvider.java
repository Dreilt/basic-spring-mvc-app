package pl.dreilt.basicspringmvcapp.configuration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;

import java.util.Locale;

public class CustomAuthenticationProvider implements AuthenticationProvider {
    protected final Log logger = LogFactory.getLog(this.getClass());
    private final CustomUserDetailsService customUserDetailsService;
    private final MessageSource messageSource;
    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    public CustomAuthenticationProvider(CustomUserDetailsService customUserDetailsService, MessageSource messageSource) {
        this.customUserDetailsService = customUserDetailsService;
        this.messageSource = messageSource;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String username = (authentication.getPrincipal() == null) ? "NONE_PROVIDED" : authentication.getName();

        UserDetails appUser = null;
        try {
            appUser = customUserDetailsService.loadUserByUsername(username);
        } catch (UsernameNotFoundException exception) {
            this.logger.debug("Failed to find user '" + username + "'");
            throw new BadCredentialsException(messageSource.getMessage("login.loginForm.badCredentials", null, Locale.getDefault()));
        }
        Assert.notNull(appUser, "retrieveUser returned null - a violation of the interface contract");
        return createSuccessAuthentication(authentication, appUser);
    }

    protected Authentication createSuccessAuthentication(Authentication authentication, UserDetails appUser) {
        UsernamePasswordAuthenticationToken result = UsernamePasswordAuthenticationToken.authenticated(appUser.getUsername(), authentication.getCredentials(), this.authoritiesMapper.mapAuthorities(appUser.getAuthorities()));
        result.setDetails(authentication.getDetails());
        this.logger.debug("Authenticated user");
        return result;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return false;
    }
}
