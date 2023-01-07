package pl.dreilt.basicspringmvcapp.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pl.dreilt.basicspringmvcapp.config.AppUserDetailsService;
import pl.dreilt.basicspringmvcapp.dto.AppUserProfileEditDto;
import pl.dreilt.basicspringmvcapp.dto.AppUserProfileDto;
import pl.dreilt.basicspringmvcapp.entity.AppUser;
import pl.dreilt.basicspringmvcapp.entity.ProfileImage;
import pl.dreilt.basicspringmvcapp.exception.AppUserNotFoundException;
import pl.dreilt.basicspringmvcapp.exception.DefaultProfileImageNotFoundException;
import pl.dreilt.basicspringmvcapp.mapper.AppUserProfileEditDtoMapper;
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

    public AppUserProfileDto findUserProfileByUserId(Long id) {
        return appUserRepository.findById(id)
                .map(AppUserProfileDtoMapper::mapToAppUserProfileDto)
                .orElseThrow(() -> new AppUserNotFoundException("User with id " + id + " not found"));
    }
}
