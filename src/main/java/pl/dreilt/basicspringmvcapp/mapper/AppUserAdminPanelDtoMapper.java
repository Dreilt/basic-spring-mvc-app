package pl.dreilt.basicspringmvcapp.mapper;

import pl.dreilt.basicspringmvcapp.dto.AppUserAdminPanelDto;
import pl.dreilt.basicspringmvcapp.entity.AppUser;

import java.util.List;
import java.util.stream.Collectors;

public class AppUserAdminPanelDtoMapper {

    public static List<AppUserAdminPanelDto> mapToAppUserAdminPanelDtoList(List<AppUser> appUsers) {
        return appUsers.stream()
                .map(appUser -> mapToAppUserAdminPanelDto(appUser))
                .collect(Collectors.toList());
    }

    public static AppUserAdminPanelDto mapToAppUserAdminPanelDto(AppUser appUser) {
        // tutaj można zastosować wzorzec Builder w przyszłości
        AppUserAdminPanelDto appUserAdminPanelDto = new AppUserAdminPanelDto();
        appUserAdminPanelDto.setId(appUser.getId());
        appUserAdminPanelDto.setFirstName(appUser.getFirstName());
        appUserAdminPanelDto.setLastName(appUser.getLastName());
        appUserAdminPanelDto.setEmail(appUser.getEmail());
        appUserAdminPanelDto.setEnabled(appUser.isEnabled());
        appUserAdminPanelDto.setAccountNonLocked(appUser.isAccountNonLocked());
        appUserAdminPanelDto.setRoles(appUser.getRoles());
        return appUserAdminPanelDto;
    }
}
