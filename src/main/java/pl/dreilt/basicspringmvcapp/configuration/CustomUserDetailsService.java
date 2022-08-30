package pl.dreilt.basicspringmvcapp.configuration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import pl.dreilt.basicspringmvcapp.dto.AppUserCredentialsDto;
import pl.dreilt.basicspringmvcapp.service.AppUserService;

@Component
public class CustomUserDetailsService implements UserDetailsService {
    protected final Log logger = LogFactory.getLog(this.getClass());
    private final AppUserService appUserService;

    public CustomUserDetailsService(AppUserService appUserService) {
        this.appUserService = appUserService;
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
//        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
//        if (currentUser == null) {
//            // This would indicate bad coding somewhere
//            throw new AccessDeniedException(
//                    "Can't change password as no Authentication object found in context " + "for current user.");
//        }
//        String username = currentUser.getName();
//        this.logger.debug(LogMessage.format("Changing password for user '%s'", username));
//        // If an authentication manager has been set, re-authenticate the user with the
//        // supplied password.
//        if (this.authenticationManager != null) {
//            this.logger.debug(LogMessage.format("Reauthenticating user '%s' for password change request.", username));
//            this.authenticationManager
//                    .authenticate(UsernamePasswordAuthenticationToken.unauthenticated(username, oldPassword));
//        }
//        else {
//            this.logger.debug("No authentication manager set. Password won't be re-checked.");
//        }
//        MutableUserDetails user = this.users.get(username);
//        Assert.state(user != null, "Current user doesn't exist in database.");
//        user.setPassword(newPassword);
    }
}
