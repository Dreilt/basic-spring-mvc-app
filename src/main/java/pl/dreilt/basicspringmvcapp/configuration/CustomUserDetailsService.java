package pl.dreilt.basicspringmvcapp.configuration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.log.LogMessage;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pl.dreilt.basicspringmvcapp.dto.AppUserCredentialsDto;
import pl.dreilt.basicspringmvcapp.service.AppUserService;

public class CustomUserDetailsService implements UserDetailsService {
    protected final Log logger = LogFactory.getLog(this.getClass());
    private AppUserService appUserService;
    private AuthenticationManager authenticationManager;

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return appUserService.findAppUserCredentialsByEmail(username)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with email %s not found", username)));
    }

    private UserDetails createUserDetails(AppUserCredentialsDto appUserCredentialsDto) {
        return User.builder()
                .username(appUserCredentialsDto.getEmail())
                .password(appUserCredentialsDto.getPassword())
                .disabled(!appUserCredentialsDto.isEnabled())
                .accountLocked(!appUserCredentialsDto.isAccountNonLocked())
                .roles(appUserCredentialsDto.getRoles().toArray(String[]::new))
                .build();
    }

    public void changePassword(String oldPassword, String newPassword) {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        if (currentUser == null) {
            throw new AccessDeniedException("Can't change password as no Authentication object found in context for current user.");
        } else {
            String username = currentUser.getName();
            this.logger.debug(LogMessage.format("Changing password for user '%s'", username));
            if (this.authenticationManager != null) {
                this.logger.debug(LogMessage.format("Reauthenticating user '%s' for password change request.", username));
                this.authenticationManager.authenticate(UsernamePasswordAuthenticationToken.unauthenticated(username, oldPassword));
            } else {
                this.logger.debug("No authentication manager set. Password won't be re-checked.");
            }

//            MutableUserDetails user = (MutableUserDetails)this.users.get(username);
//            Assert.state(user != null, "Current user doesn't exist in database.");
//            user.setPassword(newPassword);
        }
    }
}
