package pl.dreilt.basicspringmvcapp.mapper;

import pl.dreilt.basicspringmvcapp.dto.AppUserAccountEditAPDto;
import pl.dreilt.basicspringmvcapp.entity.AppUser;

public class AppUserAccountEditAPDtoMapper {

    private AppUserAccountEditAPDtoMapper() {
    }

    public static AppUserAccountEditAPDto mapToAppUserAccountEditAPDto(AppUser user) {
        return new AppUserAccountEditAPDto.AppUserAccountEditAPDtoBuilder()
                .withEnabled(user.isEnabled())
                .withAccountNonLocked(user.isAccountNonLocked())
                .withRoles(user.getRoles())
                .build();
    }
}
