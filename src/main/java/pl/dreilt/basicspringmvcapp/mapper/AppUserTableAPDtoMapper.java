package pl.dreilt.basicspringmvcapp.mapper;

import org.springframework.data.domain.Page;
import pl.dreilt.basicspringmvcapp.dto.AppUserTableAPDto;
import pl.dreilt.basicspringmvcapp.entity.AppUser;

public class AppUserTableAPDtoMapper {

    private AppUserTableAPDtoMapper() {
    }

    public static Page<AppUserTableAPDto> mapToAppUserTableAPDtos(Page<AppUser> users) {
        return users.map(AppUserTableAPDtoMapper::mapToAppUserTableAPDto);
    }

    private static AppUserTableAPDto mapToAppUserTableAPDto(AppUser user) {
        return new AppUserTableAPDto.AppUserTableAPDtoBuilder()
                .withId(user.getId())
                .withFirstName(user.getFirstName())
                .withLastName(user.getLastName())
                .withEmail(user.getEmail())
                .withEnabled(user.isEnabled())
                .withAccountNonLocked(user.isAccountNonLocked())
                .withRoles(user.getRoles())
                .build();

    }
}
