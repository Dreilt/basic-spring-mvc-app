package pl.dreilt.basicspringmvcapp.service;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pl.dreilt.basicspringmvcapp.config.AppUserDetails;
import pl.dreilt.basicspringmvcapp.dto.*;
import pl.dreilt.basicspringmvcapp.entity.AppUser;
import pl.dreilt.basicspringmvcapp.entity.ProfileImage;
import pl.dreilt.basicspringmvcapp.exception.AppUserNotFoundException;
import pl.dreilt.basicspringmvcapp.exception.DefaultProfileImageNotFoundException;
import pl.dreilt.basicspringmvcapp.mapper.AppUserCredentialsDtoMapper;
import pl.dreilt.basicspringmvcapp.repository.AppUserRepository;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Optional;

@Service
public class AppUserService {
    private final AppUserRepository appUserRepository;

    public AppUserService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    public Optional<AppUserCredentialsDto> findAppUserCredentialsByEmail(String email) {
        return appUserRepository.findByEmail(email)
                .map(AppUserCredentialsDtoMapper::mapToAppUserCredentialsDto);
    }

    public AppUserProfileDto findUserProfile(String email) {
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
                .map(target -> setUserProfileFields(currentUser, appUserProfile, target))
                .map(AppUserProfileEditDtoMapper::mapToAppUserProfileEditDto)
                .orElseThrow(() -> new AppUserNotFoundException("User with email " + email + " not found"));
    }

    private AppUser setUserProfileFields(Authentication currentUser, AppUserProfileEditDto source, AppUser target) {
        AppUserDetails appUserDetails = (AppUserDetails) currentUser.getPrincipal();
        boolean isAppUserDetailsEdited = false;

        if (source.getFirstName() != null && !source.getFirstName().equals(target.getFirstName())) {
            target.setFirstName(source.getFirstName());
            appUserDetails.setFirstName(source.getFirstName());
            isAppUserDetailsEdited = true;
        }
        if (source.getLastName() != null && !source.getLastName().equals(target.getLastName())) {
            target.setLastName(source.getLastName());
            appUserDetails.setLastName(source.getLastName());
            isAppUserDetailsEdited = true;
        }
        if (!source.getProfileImage().isEmpty()) {
            setNewProfileImage(appUserDetails, source.getProfileImage(), target);
        }
        if (source.getBio() != null && !source.getBio().equals(target.getBio())) {
            target.setBio(source.getBio());
        }
        if (source.getCity() != null && !source.getCity().equals(target.getCity())) {
            target.setCity(source.getCity());
        }

        if (isAppUserDetailsEdited) {
            Authentication newAuthenticationToken = new UsernamePasswordAuthenticationToken(appUserDetails, currentUser.getCredentials(), currentUser.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(newAuthenticationToken);
        }

        return target;
    }

    private void setNewProfileImage(AppUserDetails appUserDetails, MultipartFile profileImage, AppUser appUser) {
        ProfileImage currentProfileImage = appUser.getProfileImage();
        try (InputStream is = profileImage.getInputStream()) {
            if (currentProfileImage.getFileData() != is.readAllBytes()) {
                currentProfileImage.setFileName(profileImage.getOriginalFilename());
                currentProfileImage.setFileType(profileImage.getContentType());
                currentProfileImage.setFileData(profileImage.getBytes());
                appUser.setProfileImage(currentProfileImage);
                appUserDetails.setAvatarType(profileImage.getContentType());
                appUserDetails.setAvatarData(Base64.getEncoder().encodeToString(profileImage.getBytes()));
            }
        } catch (IOException e) {
            throw new DefaultProfileImageNotFoundException("File not found");
        }
    }

    public AppUserProfileDto findUserProfileByUserId(Long id) {
        return appUserRepository.findById(id)
                .map(AppUserProfileDtoMapper::mapToAppUserProfileDto)
                .orElseThrow(() -> new AppUserNotFoundException("User with id " + id + " not found"));
    }
}
