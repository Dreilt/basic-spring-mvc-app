package pl.dreilt.basicspringmvcapp.config;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import pl.dreilt.basicspringmvcapp.dto.AppUserCredentialsDto;
import pl.dreilt.basicspringmvcapp.service.AppUserService;

@Component
public class AppUserDetailsService implements UserDetailsService {

    private final AppUserService appUserService;

    public AppUserDetailsService(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return appUserService.findAppUserCredentialsByEmail(username)
                .map(this::createAppUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with email %s not found", username)));
    }

    private UserDetails createAppUserDetails(AppUserCredentialsDto appUserCredentialsDto) {
        return new AppUserDetails.AppUserBuilder()
                .firstName(appUserCredentialsDto.getFirstName())
                .lastName(appUserCredentialsDto.getLastName())
                .username(appUserCredentialsDto.getEmail())
                .password(appUserCredentialsDto.getPassword())
                .avatarType(appUserCredentialsDto.getAvatarType())
                .avatar(appUserCredentialsDto.getAvatar())
                .disabled(!appUserCredentialsDto.isEnabled())
                .accountLocked(!appUserCredentialsDto.isAccountNonLocked())
                .roles(appUserCredentialsDto.getRoles().toArray(String[]::new))
                .build();
    }
}
