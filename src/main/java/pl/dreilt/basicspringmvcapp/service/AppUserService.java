package pl.dreilt.basicspringmvcapp.service;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dreilt.basicspringmvcapp.dto.*;
import pl.dreilt.basicspringmvcapp.entity.AppUser;
import pl.dreilt.basicspringmvcapp.entity.AppUserRole;
import pl.dreilt.basicspringmvcapp.exception.AppUserNotFoundException;
import pl.dreilt.basicspringmvcapp.exception.NoSuchRoleException;
import pl.dreilt.basicspringmvcapp.mapper.AppUserCredentialsDtoMapper;
import pl.dreilt.basicspringmvcapp.mapper.AppUserProfileEditDtoMapper;
import pl.dreilt.basicspringmvcapp.mapper.AppUserProfileDtoMapper;
import pl.dreilt.basicspringmvcapp.mapper.LoggedAppUserBasicDataDtoMapper;
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

    public boolean checkIfAppUserExists(String email) {
        Optional<AppUser> user = appUserRepository.findByEmail(email);
        if (user.isPresent()) {
            return true;
        }
        return false;
    }

    public Optional<AppUserCredentialsDto> findAppUserCredentialsByEmail(String email) {
        return appUserRepository.findByEmail(email)
                .map(AppUserCredentialsDtoMapper::mapToAppUserCredentialsDto);
    }

    @Transactional
    public void register(AppUserRegistrationDto appUserRegistration) {
        AppUser appUser = new AppUser();
        appUser.setFirstName(appUserRegistration.getFirstName());
        appUser.setLastName(appUserRegistration.getLastName());
        appUser.setEmail(appUserRegistration.getEmail());
        String passwordHash = passwordEncoder.encode(appUserRegistration.getPassword());
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

    public AppUserProfileDto findAppUserProfile(String email) {
        return appUserRepository.findByEmail(email)
                .map(AppUserProfileDtoMapper::mapToAppUserProfileDto)
                .orElseThrow(() -> new AppUserNotFoundException("User with email " + email + " not found"));
    }

    public AppUserProfileEditDto findUserProfileToEdit(String email) {
        return appUserRepository.findByEmail(email)
                .map(AppUserProfileEditDtoMapper::mapToAppUserProfileEditDto)
                .orElseThrow(() -> new AppUserNotFoundException("User with email " + email + " not found"));
    }

    @Transactional
    public AppUserProfileEditDto updateUserProfile(AppUserProfileEditDto appUserProfile) {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        if (currentUser == null) {
            throw new AccessDeniedException("Odmowa dostępu");
        }
        String email = currentUser.getName();
        return appUserRepository.findByEmail(email)
                .map(target -> setUserProfileFields(appUserProfile, target))
                .map(AppUserProfileEditDtoMapper::mapToAppUserProfileEditDto)
                .orElseThrow(() -> new AppUserNotFoundException("User with email " + email + " not found"));
    }

    private AppUser setUserProfileFields(AppUserProfileEditDto source, AppUser target) {
        if (source.getFirstName() != null && !source.getFirstName().equals(target.getFirstName())) {
            target.setFirstName(source.getFirstName());
        }
        if (source.getLastName() != null && !source.getLastName().equals(target.getLastName())) {
            target.setLastName(source.getLastName());
        }
        if (source.getBio() != null && !source.getBio().equals(target.getBio())) {
            target.setBio(source.getBio());
        }
        if (source.getCity() != null && !source.getCity().equals(target.getCity())) {
            target.setCity(source.getCity());
        }
        return target;
    }

    public LoggedAppUserBasicDataDto findLoggedAppUserBasicData(String email) {
        Optional<AppUser> appUser = appUserRepository.findByEmail(email);
        if (appUser.isPresent()) {
            return LoggedAppUserBasicDataDtoMapper.mapToLoggedAppUserBasicDataDto(appUser.get());
        } else {
            throw new AppUserNotFoundException("User with email " + email + " not found");
        }
    }
}
