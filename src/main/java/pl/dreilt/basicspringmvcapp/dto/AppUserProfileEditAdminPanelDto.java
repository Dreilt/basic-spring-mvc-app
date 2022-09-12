package pl.dreilt.basicspringmvcapp.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AppUserProfileEditAdminPanelDto {
    @NotNull(message = "{form.firstName.fieldEmpty.message}")
    @Size(min = 2, max = 50)
    private String firstName;
    @NotNull(message = "{form.lastName.fieldEmpty.message}")
    @Size(min = 2, max = 50)
    private String lastName;
    private String bio;
    private String city;

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
}
