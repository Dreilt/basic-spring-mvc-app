package pl.dreilt.basicspringmvcapp.mapper;

import org.springframework.data.domain.Page;
import pl.dreilt.basicspringmvcapp.dto.AppUserAdminPanelDto;
import pl.dreilt.basicspringmvcapp.entity.AppUser;

public class AppUserAdminPanelDtoMapper {

    public static Page<AppUserAdminPanelDto> mapToAppUserAdminPanelDtoPage(Page<AppUser> appUsers) {
        return appUsers.map(AppUserAdminPanelDtoMapper::mapToAppUserAdminPanelDto);
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
