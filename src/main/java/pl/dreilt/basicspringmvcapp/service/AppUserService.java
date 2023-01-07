package pl.dreilt.basicspringmvcapp.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pl.dreilt.basicspringmvcapp.config.AppUserDetailsService;
import pl.dreilt.basicspringmvcapp.dto.AppUserProfileDataEditDto;
import pl.dreilt.basicspringmvcapp.dto.AppUserProfileDto;
import pl.dreilt.basicspringmvcapp.entity.AppUser;
import pl.dreilt.basicspringmvcapp.entity.ProfileImage;
import pl.dreilt.basicspringmvcapp.exception.AppUserNotFoundException;
import pl.dreilt.basicspringmvcapp.exception.DefaultProfileImageNotFoundException;
import pl.dreilt.basicspringmvcapp.mapper.AppUserProfileDataEditDtoMapper;
import pl.dreilt.basicspringmvcapp.mapper.AppUserProfileDtoMapper;
import pl.dreilt.basicspringmvcapp.repository.AppUserRepository;

import java.io.IOException;
import java.io.InputStream;

@Service
public class AppUserService {
    private final AppUserRepository appUserRepository;
    private final AppUserDetailsService appUserDetailsService;

    public AppUserService(AppUserRepository appUserRepository, AppUserDetailsService appUserDetailsService) {
        this.appUserRepository = appUserRepository;
        this.appUserDetailsService = appUserDetailsService;
    }

    public AppUserProfileDto findUserProfile(AppUser user) {
        return AppUserProfileDtoMapper.mapToAppUserProfileDto(user);
    }

    public AppUserProfileDataEditDto findUserProfileToEdit(AppUser user) {
        return AppUserProfileDataEditDtoMapper.mapToAppUserProfileEditDto(user);
    }

    @Transactional
    public AppUserProfileDataEditDto updateUserProfile(AppUser user, AppUserProfileDataEditDto newUserProfileData) {
        return AppUserProfileDataEditDtoMapper.mapToAppUserProfileEditDto(setUserProfileFields(user, newUserProfileData));
    }

    private AppUser setUserProfileFields(AppUser target, AppUserProfileDataEditDto source) {
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
            setNewProfileImage(source.getProfileImage(), target);
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

    private void setNewProfileImage(MultipartFile profileImage, AppUser user) {
        ProfileImage currentProfileImage = user.getProfileImage();
        try (InputStream is = profileImage.getInputStream()) {
            if (currentProfileImage.getFileData() != is.readAllBytes()) {
                currentProfileImage.setFileName(profileImage.getOriginalFilename());
                currentProfileImage.setFileType(profileImage.getContentType());
                currentProfileImage.setFileData(profileImage.getBytes());
                user.setProfileImage(currentProfileImage);
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
