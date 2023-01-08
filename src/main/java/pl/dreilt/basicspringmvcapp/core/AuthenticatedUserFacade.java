package pl.dreilt.basicspringmvcapp.core;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import pl.dreilt.basicspringmvcapp.user.AppUser;
import pl.dreilt.basicspringmvcapp.user.AppUserRepository;

import java.util.Optional;

@Component
public class AuthenticatedUserFacade implements IAuthenticatedUserFacade {
    private final AppUserRepository appUserRepository;

    public AuthenticatedUserFacade(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public AppUser getAuthenticatedUser() {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        if (currentUser == null || currentUser.getPrincipal().equals("anonymousUser")) {
            throw new AccessDeniedException("Nie masz dostępu do tej zawartości");
        }
        String email = currentUser.getName();
        Optional<AppUser> userOpt = appUserRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            throw new UsernameNotFoundException(String.format("User with email %s not found", email));
        }

        return userOpt.get();
    }
}
