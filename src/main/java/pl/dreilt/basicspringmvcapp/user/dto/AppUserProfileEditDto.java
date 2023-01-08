package pl.dreilt.basicspringmvcapp.user.dto;

import org.springframework.web.multipart.MultipartFile;
import pl.dreilt.basicspringmvcapp.core.Image;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AppUserProfileEditDto {
    @NotNull(message = "{form.field.firstName.error.notNull.message}")
    @Size(min = 2, max = 50, message = "{form.field.firstName.error.size.message}")
    private String firstName;
    @NotNull(message = "{form.field.lastName.error.notNull.message}")
    @Size(min = 2, max = 50, message = "{form.field.lastName.error.size.message}")
    private String lastName;
    @Image(width = 500, height = 500)
    private MultipartFile profileImage;
    @Size(max = 1000, message = "{form.field.bio.error.size.message}")
    private String bio;
    @Size(max = 50, message = "{form.field.city.error.size.message}")
    private String city;

    private AppUserProfileEditDto() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public MultipartFile getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(MultipartFile profileImage) {
        this.profileImage = profileImage;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public static class AppUserProfileEditDtoBuilder {
        private String firstName;
        private String lastName;
        private MultipartFile profileImage;
        private String bio;
        private String city;

        public AppUserProfileEditDtoBuilder() {
        }

        public AppUserProfileEditDtoBuilder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public AppUserProfileEditDtoBuilder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public AppUserProfileEditDtoBuilder withProfileImage(MultipartFile profileImage) {
            this.profileImage = profileImage;
            return this;
        }

        public AppUserProfileEditDtoBuilder withBio(String bio) {
            this.bio = bio;
            return this;
        }

        public AppUserProfileEditDtoBuilder withCity(String city) {
            this.city = city;
            return this;
        }

        public AppUserProfileEditDto build() {
            AppUserProfileEditDto userProfileEditDto = new AppUserProfileEditDto();
            userProfileEditDto.firstName = firstName;
            userProfileEditDto.lastName = lastName;
            userProfileEditDto.profileImage = profileImage;
            userProfileEditDto.bio = bio;
            userProfileEditDto.city = city;
            return userProfileEditDto;
        }
    }
}
