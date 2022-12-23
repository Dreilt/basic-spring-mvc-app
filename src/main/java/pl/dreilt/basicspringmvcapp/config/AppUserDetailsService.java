package pl.dreilt.basicspringmvcapp.config;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import pl.dreilt.basicspringmvcapp.dto.AppUserCredentialsDto;
import pl.dreilt.basicspringmvcapp.entity.AppUser;
import pl.dreilt.basicspringmvcapp.repository.AppUserRepository;
import pl.dreilt.basicspringmvcapp.service.AppUserService;

import java.util.Base64;

@Component
public class AppUserDetailsService implements UserDetailsService {
    private final AppUserService appUserService;

    public AppUserDetailsService(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    //    private final AppUserRepository appUserRepository;
//
//    public AppUserDetailsService(AppUserRepository appUserRepository) {
//        this.appUserRepository = appUserRepository;
//    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return appUserService.findAppUserCredentialsByEmail(username)
                .map(this::createAppUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with email %s not found", username)));
    }

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        return appUserRepository.findByEmail(username)
//                .map(this::createAppUserDetails)
//                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with email %s not found", username)));
//    }

//    private UserDetails createAppUserDetails(AppUser appUser) {
//        return new AppUserDetails.AppUserBuilder()
//                .firstName(appUser.getFirstName())
//                .lastName(appUser.getLastName())
//                .username(appUser.getEmail())
//                .password(appUser.getPassword())
//                .avatarType(appUser.getProfileImage().getFileType())
//                .avatarData(Base64.getEncoder().encodeToString(appUser.getProfileImage().getFileData()))
//                .enabled(appUser.isEnabled())
//                .accountNonLocked(appUser.isAccountNonLocked())
//                .roles(appUser.getRoles().toArray(String[]::new))
//                .build();
//    }

    private UserDetails createAppUserDetails(AppUserCredentialsDto appUserCredentialsDto) {
        return new AppUserDetails.AppUserBuilder()
                .firstName(appUserCredentialsDto.getFirstName())
                .lastName(appUserCredentialsDto.getLastName())
                .username(appUserCredentialsDto.getEmail())
                .password(appUserCredentialsDto.getPassword())
                .avatarType(appUserCredentialsDto.getAvatarType())
                .avatarData(appUserCredentialsDto.getAvatarData())
                .enabled(appUserCredentialsDto.isEnabled())
                .accountNonLocked(appUserCredentialsDto.isAccountNonLocked())
                .roles(appUserCredentialsDto.getRoles().toArray(String[]::new))
                .build();
    }
}
