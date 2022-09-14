package pl.dreilt.basicspringmvcapp.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dreilt.basicspringmvcapp.dto.AppUserAccountDataEditAdminPanelDto;
import pl.dreilt.basicspringmvcapp.dto.AppUserAdminPanelDto;
import pl.dreilt.basicspringmvcapp.dto.AppUserProfileEditAdminPanelDto;
import pl.dreilt.basicspringmvcapp.entity.AppUser;
import pl.dreilt.basicspringmvcapp.exception.AppUserNotFoundException;
import pl.dreilt.basicspringmvcapp.mapper.AppUserAccountDataEditAdminPanelDtoMapper;
import pl.dreilt.basicspringmvcapp.mapper.AppUserAdminPanelDtoMapper;
import pl.dreilt.basicspringmvcapp.mapper.AppUserProfileEditAdminPanelDtoMapper;
import pl.dreilt.basicspringmvcapp.repository.AdminRepository;

@Service
public class AdminService {

    private final AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public Page<AppUserAdminPanelDto> findAllUsers(Pageable pageable) {
        return AppUserAdminPanelDtoMapper.mapToAppUserAdminPanelDtos(adminRepository.findAllUsers(pageable));
    }

    public Page<AppUserAdminPanelDto> findUsersBySearch(String searchQuery, Pageable pageable) {
        if (searchQuery.equals("")) {
            return Page.empty();
        } else {
            searchQuery = searchQuery.toLowerCase();
            String[] searchWords = searchQuery.split(" ");

            if (searchWords.length == 1) {
                return AppUserAdminPanelDtoMapper.mapToAppUserAdminPanelDtos(
                        adminRepository.findUsersBySearch(searchQuery, pageable));
            } else if (searchWords.length == 2) {
                return AppUserAdminPanelDtoMapper.mapToAppUserAdminPanelDtos(
                        adminRepository.findUsersBySearch(searchWords[0], searchWords[1], pageable));
            } else {
                return AppUserAdminPanelDtoMapper.mapToAppUserAdminPanelDtos(
                        adminRepository.findUsersBySearch(searchWords, pageable));
            }
        }
    }

    public AppUserAccountDataEditAdminPanelDto findUserAccountDataToEdit(Long id) {
        return adminRepository.findById(id)
                .map(AppUserAccountDataEditAdminPanelDtoMapper::mapToAppUserAccountDataEditAdminPanelDto)
                .orElseThrow(() -> new AppUserNotFoundException("User with ID " + id + " not found"));
    }

    @Transactional
    public AppUserAccountDataEditAdminPanelDto updateUserAccountData(Long id, AppUserAccountDataEditAdminPanelDto userAccountDataEditAdminPanelDto) {
        return adminRepository.findById(id)
                .map(target -> setUserAccountDataFields(userAccountDataEditAdminPanelDto, target))
                .map(AppUserAccountDataEditAdminPanelDtoMapper::mapToAppUserAccountDataEditAdminPanelDto)
                .orElseThrow(() -> new AppUserNotFoundException("User with ID " + id + " not found"));
    }

    private AppUser setUserAccountDataFields(AppUserAccountDataEditAdminPanelDto source, AppUser target) {
        if (source.isEnabled() != target.isEnabled()) {
            target.setEnabled(source.isEnabled());
        }
        if (source.isAccountNonLocked() != target.isAccountNonLocked()) {
            target.setAccountNonLocked(source.isAccountNonLocked());
        }
        target.setRoles(source.getRoles());
        return target;
    }

    public AppUserProfileEditAdminPanelDto findUserProfileToEdit(Long id) {
        return adminRepository.findById(id)
                .map(AppUserProfileEditAdminPanelDtoMapper::mapToAppUserProfileEditAdminPanelDto)
                .orElseThrow(() -> new AppUserNotFoundException("User with ID " + id + " not found"));
    }

    @Transactional
    public AppUserProfileEditAdminPanelDto updateUserProfile(Long id, AppUserProfileEditAdminPanelDto userProfileEditAdminPanelDto) {
        return adminRepository.findById(id)
                .map(target -> setUserProfileFields(userProfileEditAdminPanelDto, target))
                .map(AppUserProfileEditAdminPanelDtoMapper::mapToAppUserProfileEditAdminPanelDto)
                .orElseThrow(() -> new AppUserNotFoundException("User with ID " + id + " not found"));
    }

    private AppUser setUserProfileFields(AppUserProfileEditAdminPanelDto source, AppUser target) {
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
        adminRepository.deleteById(id);
    }
}
