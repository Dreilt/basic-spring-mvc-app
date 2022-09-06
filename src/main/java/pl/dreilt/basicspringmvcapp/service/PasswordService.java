package pl.dreilt.basicspringmvcapp.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.log.LogMessage;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class PasswordService {
    protected final Log logger = LogFactory.getLog(this.getClass());
    private final AuthenticationManager authenticationManager;

    public PasswordService(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
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

        }
    }

}
