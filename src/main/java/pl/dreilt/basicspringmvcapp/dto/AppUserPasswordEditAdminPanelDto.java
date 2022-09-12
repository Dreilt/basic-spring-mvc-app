package pl.dreilt.basicspringmvcapp.dto;

import pl.dreilt.basicspringmvcapp.annotation.PasswordValueMatch;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@PasswordValueMatch.List({
        @PasswordValueMatch(
                field = "newPassword",
                fieldMatch = "confirmNewPassword"
        )
})
public class AppUserPasswordEditAdminPanelDto {
    @NotNull(message = "{form.newPassword.fieldEmpty.message}")
    @Size(min = 5, max = 100)
    private String newPassword;
    @NotNull(message = "{form.confirmNewPassword.fieldEmpty.message}")
    @Size(min = 5, max = 100)
    private String confirmNewPassword;

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
