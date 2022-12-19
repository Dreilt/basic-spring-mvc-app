package pl.dreilt.basicspringmvcapp.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dreilt.basicspringmvcapp.dto.AppUserAccountDataEditAPDto;
import pl.dreilt.basicspringmvcapp.dto.AppUserProfileDataEditAPDto;
import pl.dreilt.basicspringmvcapp.dto.AppUserTableAPDto;
import pl.dreilt.basicspringmvcapp.entity.AppUser;
import pl.dreilt.basicspringmvcapp.exception.AppUserNotFoundException;
import pl.dreilt.basicspringmvcapp.mapper.AppUserAccountDataEditAPDtoMapper;
import pl.dreilt.basicspringmvcapp.mapper.AppUserProfileDataEditAPDtoMapper;
import pl.dreilt.basicspringmvcapp.mapper.AppUserTableAPDtoMapper;
import pl.dreilt.basicspringmvcapp.repository.AdminRepository;

import java.util.Optional;

@Service
public class AdminService {
    private final AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public Page<AppUserTableAPDto> findAllUsers(Pageable pageable) {
        return AppUserTableAPDtoMapper.mapToAppUserTableAPDtos(adminRepository.findAllUsers(pageable));
    }

    public Page<AppUserTableAPDto> findUsersBySearch(String searchQuery, Pageable pageable) {
        if (searchQuery.equals("")) {
            return Page.empty();
        } else {
            searchQuery = searchQuery.toLowerCase();
            String[] searchWords = searchQuery.split(" ");

            if (searchWords.length == 1) {
                return AppUserTableAPDtoMapper.mapToAppUserTableAPDtos(
                        adminRepository.findUsersBySearch(searchQuery, pageable));
            } else if (searchWords.length == 2) {
                return AppUserTableAPDtoMapper.mapToAppUserTableAPDtos(
                        adminRepository.findUsersBySearch(searchWords[0], searchWords[1], pageable));
            } else {
                Optional<Sort.Order> order = pageable.getSort().stream().findFirst();
                Sort.Direction direction = order.get().getDirection();
                PageRequest newPageRequest = PageRequest
                        .of(pageable.getPageNumber(),
                                pageable.getPageSize(),
                                Sort.by(direction, "last_name"));
                return AppUserTableAPDtoMapper.mapToAppUserTableAPDtos(
                        adminRepository.findUsersBySearch(searchWords, newPageRequest));
            }
        }
    }

    public AppUserAccountDataEditAPDto findUserAccountDataToEdit(Long id) {
        return adminRepository.findById(id)
                .map(AppUserAccountDataEditAPDtoMapper::mapToAppUserAccountDataEditAPDto)
                .orElseThrow(() -> new AppUserNotFoundException("User with ID " + id + " not found"));
    }

    @Transactional
    public Object updateUserAccountData(Long id, AppUserAccountDataEditAPDto userAccountDataEditAPDto) {
        return adminRepository.findById(id)
                .map(target -> setUserAccountDataFields(userAccountDataEditAPDto, target))
                .map(AppUserAccountDataEditAPDtoMapper::mapToAppUserAccountDataEditAPDto)
                .orElseThrow(() -> new AppUserNotFoundException("User with ID " + id + " not found"));
    }

    private AppUser setUserAccountDataFields(AppUserAccountDataEditAPDto source, AppUser target) {
        if (source.isEnabled() != target.isEnabled()) {
            target.setEnabled(source.isEnabled());
        }
        if (source.isAccountNonLocked() != target.isAccountNonLocked()) {
            target.setAccountNonLocked(source.isAccountNonLocked());
        }
        target.setRoles(source.getRoles());
        return target;
    }

    public AppUserProfileDataEditAPDto findUserProfileDataToEdit(Long id) {
        return adminRepository.findById(id)
                .map(AppUserProfileDataEditAPDtoMapper::mapToAppUserProfileDataEditAPDto)
                .orElseThrow(() -> new AppUserNotFoundException("User with ID " + id + " not found"));
    }

    @Transactional
    public AppUserProfileDataEditAPDto updateUserProfile(Long id, AppUserProfileDataEditAPDto userProfileDataEditAPDto) {
        return adminRepository.findById(id)
                .map(target -> setUserProfileDataFields(userProfileDataEditAPDto, target))
                .map(AppUserProfileDataEditAPDtoMapper::mapToAppUserProfileDataEditAPDto)
                .orElseThrow(() -> new AppUserNotFoundException("User with ID " + id + " not found"));
    }

    private AppUser setUserProfileDataFields(AppUserProfileDataEditAPDto source, AppUser target) {
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
