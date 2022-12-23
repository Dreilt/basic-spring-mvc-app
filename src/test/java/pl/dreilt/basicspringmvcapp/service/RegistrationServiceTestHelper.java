package pl.dreilt.basicspringmvcapp.service;

import pl.dreilt.basicspringmvcapp.dto.AppUserRegistrationDto;

public class RegistrationServiceTestHelper {

    static AppUserRegistrationDto createAppUserRegistrationDto(String firstName, String lastName, String email) {
        return new AppUserRegistrationDto.AppUserRegistrationDtoBuilder()
                .withFirstName(firstName)
                .withLastName(lastName)
                .withEmail(email)
                .withPassword("user1")
                .withConfirmPassword("user1")
                .build();
    }
}
