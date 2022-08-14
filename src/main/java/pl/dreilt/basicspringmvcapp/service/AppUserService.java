package pl.dreilt.basicspringmvcapp.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dreilt.basicspringmvcapp.dto.AppUserCredentialsDto;
import pl.dreilt.basicspringmvcapp.dto.AppUserRegistrationDto;
import pl.dreilt.basicspringmvcapp.entity.AppUser;
import pl.dreilt.basicspringmvcapp.entity.AppUserRole;
import pl.dreilt.basicspringmvcapp.exception.NoSuchRoleException;
import pl.dreilt.basicspringmvcapp.mapper.AppUserCredentialsDtoMapper;
import pl.dreilt.basicspringmvcapp.repository.AppUserRepository;
import pl.dreilt.basicspringmvcapp.repository.AppUserRoleRepository;

import java.util.Optional;

@Service
public class AppUserService {

    private static final String USER_ROLE = "USER";
    private final AppUserRepository appUserRepository;
    private final AppUserRoleRepository appUserRoleRepository;
    private final PasswordEncoder passwordEncoder;

    public AppUserService(AppUserRepository appUserRepository, AppUserRoleRepository appUserRoleRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.appUserRoleRepository = appUserRoleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<AppUserCredentialsDto> findAppUserCredentialsByEmail(String email) {
        return appUserRepository.findByEmail(email)
                .map(AppUserCredentialsDtoMapper::mapToAppUserCredentialsDto);
    }

    @Transactional
    public void register(AppUserRegistrationDto appUserRegistrationDto) {
        AppUser appUser = new AppUser();
        appUser.setFirstName(appUserRegistrationDto.getFirstName());
        appUser.setLastName(appUserRegistrationDto.getLastName());
        appUser.setEmail(appUserRegistrationDto.getEmail());
        String passwordHash = passwordEncoder.encode(appUserRegistrationDto.getPassword());
        appUser.setPassword(passwordHash);
        appUser.setEnabled(true);
        appUser.setAccountNonLocked(true);
        Optional<AppUserRole> userRole = appUserRoleRepository.findByName(USER_ROLE);
        userRole.ifPresentOrElse(
                role -> appUser.getRoles().add(role),
                () -> {
                    throw new NoSuchRoleException("Invalid role: " + USER_ROLE, USER_ROLE);
                }
        );
        appUserRepository.save(appUser);
    }
}
