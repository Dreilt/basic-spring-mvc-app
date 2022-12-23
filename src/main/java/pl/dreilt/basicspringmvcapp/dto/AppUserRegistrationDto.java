package pl.dreilt.basicspringmvcapp.dto;

import pl.dreilt.basicspringmvcapp.annotation.PasswordValueMatch;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@PasswordValueMatch.List({
        @PasswordValueMatch(
                field = "password",
                fieldMatch = "confirmPassword"
        )
})
public class AppUserRegistrationDto {
    @NotNull(message = "{form.field.firstName.error.notNull.message}")
    @NotEmpty(message = "{form.field.firstName.error.notEmpty.message}")
    @Size(min = 2, max = 50, message = "{form.field.firstName.error.size.message}")
    private String firstName;
    @NotNull(message = "{form.field.lastName.error.notNull.message}")
    @NotEmpty(message = "{form.field.lastName.error.notEmpty.message}")
    @Size(min = 2, max = 50, message = "{form.field.lastName.error.size.message}")
    private String lastName;
    @NotNull(message = "{form.field.email.error.notNull.message}")
    @NotEmpty(message = "{form.field.email.error.notEmpty.message}")
    @Email
    private String email;
    @NotNull(message = "{form.field.password.error.notNull.message}")
    @NotEmpty(message = "{form.field.password.error.notEmpty.message}")
    @Size(min = 5, max = 100, message = "{form.field.password.error.size.message}")
    private String password;
    @NotNull(message = "{form.field.confirmPassword.error.notNull.message}")
    @NotEmpty(message = "{form.field.confirmPassword.error.notEmpty.message}")
    @Size(min = 5, max = 100, message = "{form.field.confirmPassword.error.size.message}")
    private String confirmPassword;

    public AppUserRegistrationDto() {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public static class AppUserRegistrationDtoBuilder {
        private String firstName;
        private String lastName;
        private String email;
        private String password;
        private String confirmPassword;

        public AppUserRegistrationDtoBuilder() {
        }

        public AppUserRegistrationDtoBuilder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public AppUserRegistrationDtoBuilder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public AppUserRegistrationDtoBuilder withEmail(String email) {
            this.email = email;
            return this;
        }

        public AppUserRegistrationDtoBuilder withPassword(String password) {
            this.password = password;
            return this;
        }

        public AppUserRegistrationDtoBuilder withConfirmPassword(String confirmPassword) {
            this.confirmPassword = confirmPassword;
            return this;
        }

        public AppUserRegistrationDto build() {
            AppUserRegistrationDto userRegistrationDto = new AppUserRegistrationDto();
            userRegistrationDto.firstName = firstName;
            userRegistrationDto.lastName = lastName;
            userRegistrationDto.email = email;
            userRegistrationDto.password = password;
            userRegistrationDto.confirmPassword = confirmPassword;
            return userRegistrationDto;
        }
    }
}
