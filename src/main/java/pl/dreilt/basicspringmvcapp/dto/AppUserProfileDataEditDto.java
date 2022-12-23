package pl.dreilt.basicspringmvcapp.dto;

import org.springframework.web.multipart.MultipartFile;
import pl.dreilt.basicspringmvcapp.annotation.Image;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AppUserProfileDataEditDto {
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

    private AppUserProfileDataEditDto() {
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

    public static class AppUserProfileDataEditDtoBuilder {
        private String firstName;
        private String lastName;
        private MultipartFile profileImage;
        private String bio;
        private String city;

        public AppUserProfileDataEditDtoBuilder() {
        }

        public AppUserProfileDataEditDtoBuilder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public AppUserProfileDataEditDtoBuilder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public AppUserProfileDataEditDtoBuilder withProfileImage(MultipartFile profileImage) {
            this.profileImage = profileImage;
            return this;
        }

        public AppUserProfileDataEditDtoBuilder withBio(String bio) {
            this.bio = bio;
            return this;
        }

        public AppUserProfileDataEditDtoBuilder withCity(String city) {
            this.city = city;
            return this;
        }

        public AppUserProfileDataEditDto build() {
            AppUserProfileDataEditDto userProfileDataEditDto = new AppUserProfileDataEditDto();
            userProfileDataEditDto.firstName = firstName;
            userProfileDataEditDto.lastName = lastName;
            userProfileDataEditDto.profileImage = profileImage;
            userProfileDataEditDto.bio = bio;
            userProfileDataEditDto.city = city;
            return userProfileDataEditDto;
        }
    }
}
