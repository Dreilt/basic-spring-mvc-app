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
    private final PasswordEncoder passwordEncoder;
    private final ProfileImageRepository profileImageRepository;
    private final AppUserRoleRepository appUserRoleRepository;

    public RegistrationService(RegistrationRepository registrationRepository, PasswordEncoder passwordEncoder, ProfileImageRepository profileImageRepository, AppUserRoleRepository appUserRoleRepository) {
        this.registrationRepository = registrationRepository;
        this.passwordEncoder = passwordEncoder;
        this.profileImageRepository = profileImageRepository;
        this.appUserRoleRepository = appUserRoleRepository;
    }

    public boolean checkIfAppUserExists(String email) {
        Optional<AppUser> user = registrationRepository.findByEmail(email);
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
        registrationRepository.save(user);
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
}
