package pl.dreilt.basicspringmvcapp.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dreilt.basicspringmvcapp.dto.*;
import pl.dreilt.basicspringmvcapp.entity.AppUser;
import pl.dreilt.basicspringmvcapp.entity.AppUserRole;
import pl.dreilt.basicspringmvcapp.exception.NoSuchRoleException;
import pl.dreilt.basicspringmvcapp.mapper.AppUserAdminPanelDtoMapper;
import pl.dreilt.basicspringmvcapp.mapper.AppUserBasicDataAdminPanelDtoMapper;
import pl.dreilt.basicspringmvcapp.mapper.AppUserCredentialsDtoMapper;
import pl.dreilt.basicspringmvcapp.mapper.AppUserProfileDtoMapper;
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

    public Page<AppUserAdminPanelDto> findAllAppUsers(Pageable pageable) {
        return AppUserAdminPanelDtoMapper.mapToAppUserAdminPanelDtoPage(appUserRepository.findAllAppUsers(pageable));
    }

    public Optional<AppUserBasicDataAdminPanelDto> findAppUserById(Long id) {
        return appUserRepository.findById(id)
                .map(AppUserBasicDataAdminPanelDtoMapper::mapToAppUserBasicDataAdminPanelDto);
    }

    @Transactional
    public Optional<AppUserBasicDataAdminPanelDto> updateAppUserBasicData(Long id, AppUserBasicDataAdminPanelDto appUserBasicDataAdminPanel) {
        return appUserRepository.findById(id)
                .map(target -> setAppUserData(appUserBasicDataAdminPanel, target))
                .map(AppUserBasicDataAdminPanelDtoMapper::mapToAppUserBasicDataAdminPanelDto);
    }

    private AppUser setAppUserData(AppUserBasicDataAdminPanelDto source, AppUser target) {
        if (source.getFirstName() != null && !source.getFirstName().equals(target.getFirstName())) {
            target.setFirstName(source.getFirstName());
        }
        if (source.getLastName() != null && !source.getLastName().equals(target.getLastName())) {
            target.setLastName(source.getLastName());
        }
        if (source.isEnabled() != target.isEnabled()) {
            target.setEnabled(source.isEnabled());
        }
        if (source.isAccountNonLocked() != target.isAccountNonLocked()) {
            target.setAccountNonLocked(source.isAccountNonLocked());
        }
        target.setRoles(source.getRoles());
        return target;
    }

    public void deleteAppUser(Long id) {
        appUserRepository.deleteById(id);
    }

    public Page<AppUserAdminPanelDto> findAppUsersBySearch(String searchQuery, Pageable pageable) {
        if (searchQuery.equals("")) {
            return Page.empty();
        } else {
            searchQuery = searchQuery.toLowerCase();
            String[] searchWords = searchQuery.split(" ");

            if (searchWords.length == 1) {
                return AppUserAdminPanelDtoMapper.mapToAppUserAdminPanelDtoPage(
                        appUserRepository.findAppUsersBySearch(searchQuery, pageable));
            } else if (searchWords.length == 2) {
                return AppUserAdminPanelDtoMapper.mapToAppUserAdminPanelDtoPage(
                        appUserRepository.findAppUsersBySearch(searchWords[0], searchWords[1], pageable));
            } else {
                return AppUserAdminPanelDtoMapper.mapToAppUserAdminPanelDtoPage(
                        appUserRepository.findAppUsersBySearch(searchWords, pageable));
            }
        }
    }

    public Optional<AppUserProfileDto> findAppUserProfile(String email) {
        return appUserRepository.findByEmail(email)
                .map(AppUserProfileDtoMapper::mapToAppUserProfileDto);
    }
}
