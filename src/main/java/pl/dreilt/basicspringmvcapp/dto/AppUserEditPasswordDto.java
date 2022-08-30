package pl.dreilt.basicspringmvcapp.dto;

import pl.dreilt.basicspringmvcapp.annotation.PasswordValueEqual;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@PasswordValueEqual.List({
        @PasswordValueEqual(
                field = "newPassword",
                fieldEquals = "confirmNewPassword"
        )
})
public class AppUserEditPasswordDto {
    @NotNull
    @Size(min = 5, max = 100)
    private String currentPassword;
    @NotNull
    @Size(min = 5, max = 100)
    private String newPassword;
    @NotNull
    @Size(min = 5, max = 100)
    private String confirmNewPassword;

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmNewPassword() {
        return confirmNewPassword;
    }

    public void setConfirmNewPassword(String confirmNewPassword) {
        this.confirmNewPassword = confirmNewPassword;
    }
}
