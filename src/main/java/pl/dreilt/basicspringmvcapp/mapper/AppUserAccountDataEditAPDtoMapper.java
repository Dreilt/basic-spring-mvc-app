package pl.dreilt.basicspringmvcapp.mapper;

import pl.dreilt.basicspringmvcapp.dto.AppUserAccountDataEditAPDto;
import pl.dreilt.basicspringmvcapp.entity.AppUser;

public class AppUserAccountDataEditAPDtoMapper {

    private AppUserAccountDataEditAPDtoMapper() {
    }

    public static AppUserAccountDataEditAPDto mapToAppUserAccountDataEditAPDto(AppUser appUser) {
        return new AppUserAccountDataEditAPDto.AppUserAccountDataEditAPDtoBuilder()
                .withEnabled(appUser.isEnabled())
                .withAccountNonLocked(appUser.isAccountNonLocked())
                .withRoles(appUser.getRoles())
                .build();
    }
}
