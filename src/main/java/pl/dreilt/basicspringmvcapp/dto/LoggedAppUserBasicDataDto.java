package pl.dreilt.basicspringmvcapp.dto;

public class LoggedAppUserBasicDataDto {
    private String firstName;
    private String lastName;
    private String avatarType;
    private String avatar;

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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
