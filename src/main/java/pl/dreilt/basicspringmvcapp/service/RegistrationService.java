package pl.dreilt.basicspringmvcapp.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dreilt.basicspringmvcapp.dto.AppUserRegistrationDto;
import pl.dreilt.basicspringmvcapp.entity.AppUser;
import pl.dreilt.basicspringmvcapp.entity.AppUserRole;
import pl.dreilt.basicspringmvcapp.entity.ProfileImage;
import pl.dreilt.basicspringmvcapp.exception.DefaultProfileImageNotFoundException;
import pl.dreilt.basicspringmvcapp.exception.NoSuchRoleException;
import pl.dreilt.basicspringmvcapp.repository.AppUserRoleRepository;
import pl.dreilt.basicspringmvcapp.repository.ProfileImageRepository;
import pl.dreilt.basicspringmvcapp.repository.RegistrationRepository;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Service
public class RegistrationService {
    private static final String USER_ROLE = "USER";
    private final RegistrationRepository registrationRepository;
    private final ProfileImageRepository profileImageRepository;
    private final AppUserRoleRepository appUserRoleRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationService(RegistrationRepository registrationRepository, ProfileImageRepository profileImageRepository, AppUserRoleRepository appUserRoleRepository, PasswordEncoder passwordEncoder) {
        this.registrationRepository = registrationRepository;
        this.profileImageRepository = profileImageRepository;
        this.appUserRoleRepository = appUserRoleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean checkIfAppUserExists(String email) {
        Optional<AppUser> user = registrationRepository.findByEmail(email);
        return user.isPresent();
    }

    @Transactional
    public void register(AppUserRegistrationDto appUserRegistration) {
        AppUser appUser = new AppUser();
        appUser.setFirstName(appUserRegistration.getFirstName());
        appUser.setLastName(appUserRegistration.getLastName());
        appUser.setEmail(appUserRegistration.getEmail());
        appUser.setPassword(passwordEncoder.encode(appUserRegistration.getPassword()));
        setDefaultProfileImage(appUser);
        appUser.setEnabled(true);
        appUser.setAccountNonLocked(true);
        Optional<AppUserRole> userRole = appUserRoleRepository.findRoleByName(USER_ROLE);
        userRole.ifPresentOrElse(
                role -> appUser.getRoles().add(role),
                () -> {
                    throw new NoSuchRoleException("Invalid role: " + USER_ROLE, USER_ROLE);
                }
        );
        registrationRepository.save(appUser);
    }

    private void setDefaultProfileImage(AppUser appUser) {
        ClassPathResource resource = new ClassPathResource("static/images/default_profile_image.png");
        try (InputStream defaultProfileImage = resource.getInputStream()) {
            ProfileImage profileImage = new ProfileImage();
            profileImage.setFileName(resource.getFilename());
            profileImage.setFileType("image/png");
            profileImage.setFileData(defaultProfileImage.readAllBytes());
            profileImageRepository.save(profileImage);
            appUser.setProfileImage(profileImage);
        } catch (IOException e) {
            throw new DefaultProfileImageNotFoundException("File " + resource.getPath() + " not found");
        }
    }
}
