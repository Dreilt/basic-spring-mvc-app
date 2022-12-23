package pl.dreilt.basicspringmvcapp.dto;

public class AppUserProfileDto {
    private String firstName;
    private String lastName;
    private String avatarType;
    private String avatarData;
    private String email;
    private String bio;
    private String city;

    private AppUserProfileDto() {
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

    public String getAvatarType() {
        return avatarType;
    }

    public void setAvatarType(String avatarType) {
        this.avatarType = avatarType;
    }

    public String getAvatarData() {
        return avatarData;
    }

    public void setAvatarData(String avatarData) {
        this.avatarData = avatarData;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public static class AppUserProfileDtoBuilder {
        private String firstName;
        private String lastName;
        private String avatarType;
        private String avatarData;
        private String email;
        private String bio;
        private String city;

        public AppUserProfileDtoBuilder() {
        }

        public AppUserProfileDtoBuilder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public AppUserProfileDtoBuilder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public AppUserProfileDtoBuilder withAvatarType(String avatarType) {
            this.avatarType = avatarType;
            return this;
        }

        public AppUserProfileDtoBuilder withAvatarData(String avatarData) {
            this.avatarData = avatarData;
            return this;
        }

        public AppUserProfileDtoBuilder withEmail(String email) {
            this.email = email;
            return this;
        }

        public AppUserProfileDtoBuilder withBio(String bio) {
            this.bio = bio;
            return this;
        }

        public AppUserProfileDtoBuilder withCity(String city) {
            this.city = city;
            return this;
        }

        public AppUserProfileDto build() {
            AppUserProfileDto userProfileDto = new AppUserProfileDto();
            userProfileDto.firstName = firstName;
            userProfileDto.lastName = lastName;
            userProfileDto.avatarType = avatarType;
            userProfileDto.avatarData = avatarData;
            userProfileDto.email = email;
            userProfileDto.bio = bio;
            userProfileDto.city = city;
            return userProfileDto;
        }
    }
}
