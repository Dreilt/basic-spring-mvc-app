package pl.dreilt.basicspringmvcapp.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.log.LogMessage;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import pl.dreilt.basicspringmvcapp.entity.AppUser;
import pl.dreilt.basicspringmvcapp.repository.AppUserRepository;

@Service
public class AppUserLoginDataService {
    protected final Log logger = LogFactory.getLog(this.getClass());
    private final AuthenticationManager authenticationManager;
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AppUserLoginDataService(AuthenticationManager authenticationManager, AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void changePassword(String oldPassword, String newPassword) {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        if (currentUser == null) {
            // This would indicate bad coding somewhere
            throw new AccessDeniedException(
                    "Can't change password as no Authentication object found in context " + "for current user.");
        }
        String username = currentUser.getName();
        this.logger.debug(LogMessage.format("Changing password for user '%s'", username));
        // If an authentication manager has been set, re-authenticate the user with the supplied password.
        if (this.authenticationManager != null) {
            this.logger.debug(LogMessage.format("Reauthenticating user '%s' for password change request.", username));
            this.authenticationManager
                    .authenticate(UsernamePasswordAuthenticationToken.unauthenticated(username, oldPassword));
        }
        else {
            this.logger.debug("No authentication manager set. Password won't be re-checked.");
        }

        AppUser appUser = appUserRepository.findAppUserByUsername(username);
        Assert.state(appUser != null, "Current user doesn't exist in database.");
        String passwordHash = passwordEncoder.encode(newPassword);
        appUser.setPassword(passwordHash);
    }
}
