package pl.dreilt.basicspringmvcapp.user;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pl.dreilt.basicspringmvcapp.config.AppUserDetailsService;
import pl.dreilt.basicspringmvcapp.exception.AppUserNotFoundException;
import pl.dreilt.basicspringmvcapp.exception.DefaultProfileImageNotFoundException;
import pl.dreilt.basicspringmvcapp.exception.NoSuchRoleException;
import pl.dreilt.basicspringmvcapp.pi.ProfileImage;
import pl.dreilt.basicspringmvcapp.pi.ProfileImageRepository;
import pl.dreilt.basicspringmvcapp.role.AppUserRole;
import pl.dreilt.basicspringmvcapp.role.AppUserRoleRepository;
import pl.dreilt.basicspringmvcapp.specification.AppUserSpecification;
import pl.dreilt.basicspringmvcapp.user.dto.*;
import pl.dreilt.basicspringmvcapp.user.mapper.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Service
public class AppUserService {
    private static final String USER_ROLE = "USER";
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProfileImageRepository profileImageRepository;
    private final AppUserRoleRepository appUserRoleRepository;
    private final AppUserDetailsService appUserDetailsService;

    public AppUserService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder, ProfileImageRepository profileImageRepository, AppUserRoleRepository appUserRoleRepository, AppUserDetailsService appUserDetailsService) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.profileImageRepository = profileImageRepository;
        this.appUserRoleRepository = appUserRoleRepository;
        this.appUserDetailsService = appUserDetailsService;
    }

    public boolean checkIfAppUserExists(String email) {
        Optional<AppUser> user = appUserRepository.findByEmail(email);
        return user.isPresent();
    }

    @Transactional
    public void register(AppUserRegistrationDto userRegistration) {
        AppUser user = new AppUser();
        user.setFirstName(userRegistration.getFirstName());
        user.setLastName(userRegistration.getLastName());
        user.setEmail(userRegistration.getEmail());
        user.setPassword(passwordEncoder.encode(userRegistration.getPassword()));
        setDefaultProfileImage(user);
        user.setEnabled(true);
        user.setAccountNonLocked(true);
        Optional<AppUserRole> userRole = appUserRoleRepository.findRoleByName(USER_ROLE);
        userRole.ifPresentOrElse(
                role -> user.getRoles().add(role),
                () -> {
                    throw new NoSuchRoleException("Invalid role: " + USER_ROLE, USER_ROLE);
                }
        );
        appUserRepository.save(user);
    }

    private void setDefaultProfileImage(AppUser user) {
        ClassPathResource resource = new ClassPathResource("static/images/default_profile_image.png");
        try (InputStream defaultProfileImage = resource.getInputStream()) {
            ProfileImage profileImage = new ProfileImage();
            profileImage.setFileName(resource.getFilename());
            profileImage.setFileType("image/png");
            profileImage.setFileData(defaultProfileImage.readAllBytes());
            profileImageRepository.save(profileImage);
            user.setProfileImage(profileImage);
        } catch (IOException e) {
            throw new DefaultProfileImageNotFoundException("File " + resource.getPath() + " not found");
        }
    }

    public Page<AppUserTableAPDto> findAllUsers(Pageable pageable) {
        return AppUserTableAPDtoMapper.mapToAppUserTableAPDtos(appUserRepository.findAllUsers(pageable));
    }

    public Page<AppUserTableAPDto> findUsersBySearch(String searchQuery, Pageable pageable) {
        searchQuery = searchQuery.toLowerCase();
        String[] searchWords = searchQuery.split(" ");

        if (searchWords.length == 1 && "".equals(searchWords[0])) {
            return Page.empty();
        }

        if (searchWords.length == 1) {
            return AppUserTableAPDtoMapper
                    .mapToAppUserTableAPDtos(appUserRepository.findAll(AppUserSpecification.bySearch(searchWords[0]), pageable));
        }

        if (searchWords.length == 2) {
            return AppUserTableAPDtoMapper
                    .mapToAppUserTableAPDtos(appUserRepository.findAll(AppUserSpecification.bySearch(searchWords[0], searchWords[1]), pageable));
        }

        return Page.empty();
    }

    public AppUserProfileDto findUserProfileByUserId(Long id) {
        return appUserRepository.findById(id)
                .map(AppUserProfileDtoMapper::mapToAppUserProfileDto)
                .orElseThrow(() -> new AppUserNotFoundException("User with id " + id + " not found"));
    }

    public AppUserProfileDto findUserProfile(AppUser user) {
        return AppUserProfileDtoMapper.mapToAppUserProfileDto(user);
    }

    public AppUserProfileEditDto findUserProfileToEdit(AppUser user) {
        return AppUserProfileEditDtoMapper.mapToAppUserProfileEditDto(user);
    }

    @Transactional
    public AppUserProfileEditDto updateUserProfile(AppUser user, AppUserProfileEditDto userProfileEdit) {
        return AppUserProfileEditDtoMapper.mapToAppUserProfileEditDto(setUserProfileFields(userProfileEdit, user));
    }

    private AppUser setUserProfileFields(AppUserProfileEditDto source, AppUser target) {
        boolean isAppUserDetailsEdited = false;

        if (source.getFirstName() != null && !source.getFirstName().equals(target.getFirstName())) {
            target.setFirstName(source.getFirstName());
            isAppUserDetailsEdited = true;
        }
        if (source.getLastName() != null && !source.getLastName().equals(target.getLastName())) {
            target.setLastName(source.getLastName());
            isAppUserDetailsEdited = true;
        }
        if (!source.getProfileImage().isEmpty()) {
            setNewProfileImage(target, source.getProfileImage());
        }
        if (source.getBio() != null && !source.getBio().equals(target.getBio())) {
            target.setBio(source.getBio());
        }
        if (source.getCity() != null && !source.getCity().equals(target.getCity())) {
            target.setCity(source.getCity());
        }

        if (isAppUserDetailsEdited) {
            appUserDetailsService.updateAppUserDetails(target);
        }

        return target;
    }

    private void setNewProfileImage(AppUser user, MultipartFile newProfileImage) {
        ProfileImage currentProfileImage = user.getProfileImage();
        try (InputStream is = newProfileImage.getInputStream()) {
            if (currentProfileImage.getFileData() != is.readAllBytes()) {
                currentProfileImage.setFileName(newProfileImage.getOriginalFilename());
                currentProfileImage.setFileType(newProfileImage.getContentType());
                currentProfileImage.setFileData(newProfileImage.getBytes());
                user.setProfileImage(currentProfileImage);
            }
        } catch (IOException e) {
            throw new DefaultProfileImageNotFoundException("File not found");
        }
    }

    public AppUserAccountEditAPDto findUserAccountToEdit(Long id) {
        return appUserRepository.findById(id)
                .map(AppUserAccountEditAPDtoMapper::mapToAppUserAccountEditAPDto)
                .orElseThrow(() -> new AppUserNotFoundException("User with ID " + id + " not found"));
    }

    @Transactional
    public AppUserAccountEditAPDto updateUserAccount(Long id, AppUserAccountEditAPDto userAccountEditAP) {
        return appUserRepository.findById(id)
                .map(target -> setUserAccountFields(userAccountEditAP, target))
                .map(AppUserAccountEditAPDtoMapper::mapToAppUserAccountEditAPDto)
                .orElseThrow(() -> new AppUserNotFoundException("User with ID " + id + " not found"));
    }

    private AppUser setUserAccountFields(AppUserAccountEditAPDto source, AppUser target) {
        if (source.isEnabled() != target.isEnabled()) {
            target.setEnabled(source.isEnabled());
        }
        if (source.isAccountNonLocked() != target.isAccountNonLocked()) {
            target.setAccountNonLocked(source.isAccountNonLocked());
        }
        target.setRoles(source.getRoles());
        return target;
    }

    public AppUserProfileEditAPDto findUserProfileToEdit(Long id) {
        return appUserRepository.findById(id)
                .map(AppUserProfileEditAPDtoMapper::mapToAppUserProfileEditAPDto)
                .orElseThrow(() -> new AppUserNotFoundException("User with ID " + id + " not found"));
    }

    @Transactional
    public AppUserProfileEditAPDto updateUserProfile(Long id, AppUserProfileEditAPDto userProfileEditAP) {
        return appUserRepository.findById(id)
                .map(target -> setUserProfileFields(userProfileEditAP, target))
                .map(AppUserProfileEditAPDtoMapper::mapToAppUserProfileEditAPDto)
                .orElseThrow(() -> new AppUserNotFoundException("User with ID " + id + " not found"));
    }

    private AppUser setUserProfileFields(AppUserProfileEditAPDto source, AppUser target) {
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

    public void deleteUser(Long id) {
        appUserRepository.deleteById(id);
    }
}
