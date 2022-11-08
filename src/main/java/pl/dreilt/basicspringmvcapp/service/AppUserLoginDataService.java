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
import org.springframework.transaction.annotation.Transactional;
import pl.dreilt.basicspringmvcapp.entity.AppUser;
import pl.dreilt.basicspringmvcapp.exception.AppUserNotFoundException;
import pl.dreilt.basicspringmvcapp.repository.AppUserRepository;

import java.util.Optional;

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

    @Transactional
    public void changePassword(String oldPassword, String newPassword) {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        if (currentUser == null) {
            throw new AccessDeniedException("Can't change password as no Authentication object found in context for current user.");
        }
        String email = currentUser.getName();
        this.logger.debug(LogMessage.format("Changing password for user '%s'", email));
        if (this.authenticationManager != null) {
            this.logger.debug(LogMessage.format("Reauthenticating user '%s' for password change request.", email));
            this.authenticationManager.authenticate(UsernamePasswordAuthenticationToken.unauthenticated(email, oldPassword));
        } else {
            this.logger.debug("No authentication manager set. Password won't be re-checked.");
        }

        Optional<AppUser> appUser = appUserRepository.findByEmail(email);
        if (appUser.isPresent()) {
            String passwordHash = passwordEncoder.encode(newPassword);
            appUser.get().setPassword(passwordHash);
        } else {
            throw new AppUserNotFoundException("Current user doesn't exist in database.");
        }
    }

    @Transactional
    public void changePassword(Long id, String newPassword) {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        if (currentUser != null && !currentUser.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            throw new AccessDeniedException("Can't change password as no admin role user found in context for current user.");
        }

        Optional<AppUser> user = appUserRepository.findById(id);
        if (user.isPresent()) {
            String passwordHash = passwordEncoder.encode(newPassword);
            user.get().setPassword(passwordHash);
        } else {
            throw new AppUserNotFoundException(String.format("User with ID %s not found", id));
        }
    }
}
