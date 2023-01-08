package pl.dreilt.basicspringmvcapp.user.dto;

import pl.dreilt.basicspringmvcapp.user.PasswordValueMatch;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@PasswordValueMatch.List({
        @PasswordValueMatch(
                field = "newPassword",
                fieldMatch = "confirmNewPassword"
        )
})
public class AppUserPasswordEditDto {
    @NotNull(message = "{form.field.currentPassword.error.notNull.message}")
    @NotEmpty(message = "{form.field.currentPassword.error.notEmpty.message}")
    @Size(min = 5, max = 100, message = "{form.field.currentPassword.error.size.message}")
    private String currentPassword;
    @NotNull(message = "{form.field.newPassword.error.notNull.message}")
    @NotEmpty(message = "{form.field.newPassword.error.notEmpty.message}")
    @Size(min = 5, max = 100, message = "{form.field.newPassword.error.size.message}")
    private String newPassword;
    @NotNull(message = "{form.field.confirmNewPassword.error.notNull.message}")
    @NotEmpty(message = "{form.field.confirmNewPassword.error.notEmpty.message}")
    @Size(min = 5, max = 100, message = "{form.field.confirmNewPassword.error.size.message}")
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
