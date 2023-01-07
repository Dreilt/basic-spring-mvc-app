package pl.dreilt.basicspringmvcapp.config;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import pl.dreilt.basicspringmvcapp.entity.AppUser;
import pl.dreilt.basicspringmvcapp.entity.AppUserRole;
import pl.dreilt.basicspringmvcapp.repository.AppUserRepository;

import java.util.Base64;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class AppUserDetailsService implements UserDetailsService {
    private final AppUserRepository appUserRepository;

    public AppUserDetailsService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return appUserRepository.findByEmail(username)
                .map(this::createAppUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with email %s not found", username)));
    }

    private UserDetails createAppUserDetails(AppUser user) {
        return new AppUserDetails.AppUserBuilder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getEmail())
                .password(user.getPassword())
                .avatarType(user.getProfileImage().getFileType())
                .avatarData(Base64.getEncoder().encodeToString(user.getProfileImage().getFileData()))
                .enabled(user.isEnabled())
                .accountNonLocked(user.isAccountNonLocked())
                .roles(getUserRolesArray(user.getRoles()))
                .build();
    }

    private String[] getUserRolesArray(Set<AppUserRole> roles) {
        Set<String> userRolesSet = roles
                .stream()
                .map(AppUserRole::getName)
                .collect(Collectors.toSet());
        return userRolesSet.toArray(String[]::new);
    }

    public void updateAppUserDetails(AppUser user) {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        Authentication newAuthenticationToken = new UsernamePasswordAuthenticationToken(createAppUserDetails(user), currentUser.getCredentials(), currentUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(newAuthenticationToken);
    }
}
