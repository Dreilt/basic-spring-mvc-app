package pl.dreilt.basicspringmvcapp.user.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AppUserProfileEditAPDto {
    @NotNull(message = "{form.field.firstName.error.notNull.message}")
    @NotEmpty(message = "{form.field.firstName.error.notEmpty.message}")
    @Size(min = 2, max = 50, message = "{form.field.firstName.error.size.message}")
    private String firstName;
    @NotNull(message = "{form.field.lastName.error.notNull.message}")
    @NotEmpty(message = "{form.field.lastName.error.notEmpty.message}")
    @Size(min = 2, max = 50, message = "{form.field.lastName.error.size.message}")
    private String lastName;
    @Size(max = 1000, message = "{form.field.bio.error.size.message}")
    private String bio;
    @Size(max = 50, message = "{form.field.city.error.size.message}")
    private String city;

    private AppUserProfileEditAPDto() {
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getBio() {
        return bio;
    }

    public String getCity() {
        return city;
    }

    public static class AppUserProfileEditAPDtoBuilder {
        private String firstName;
        private String lastName;
        private String bio;
        private String city;

        public AppUserProfileEditAPDtoBuilder() {
        }

        public AppUserProfileEditAPDtoBuilder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public AppUserProfileEditAPDtoBuilder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public AppUserProfileEditAPDtoBuilder withBio(String bio) {
            this.bio = bio;
            return this;
        }

        public AppUserProfileEditAPDtoBuilder withCity(String city) {
            this.city = city;
            return this;
        }

        public AppUserProfileEditAPDto build() {
            AppUserProfileEditAPDto userProfileEditAPDto = new AppUserProfileEditAPDto();
            userProfileEditAPDto.firstName = firstName;
            userProfileEditAPDto.lastName = lastName;
            userProfileEditAPDto.bio = bio;
            userProfileEditAPDto.city = city;
            return userProfileEditAPDto;
        }
    }
}
