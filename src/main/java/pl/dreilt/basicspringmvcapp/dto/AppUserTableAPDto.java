package pl.dreilt.basicspringmvcapp.dto;

import pl.dreilt.basicspringmvcapp.entity.AppUserRole;

import java.util.Set;

public class AppUserTableAPDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private boolean enabled;
    private boolean accountNonLocked;
    private Set<AppUserRole> roles;

    private AppUserTableAPDto() {
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public Set<AppUserRole> getRoles() {
        return roles;
    }

    public static class AppUserTableAPDtoBuilder {
        private Long id;
        private String firstName;
        private String lastName;
        private String email;
        private boolean enabled;
        private boolean accountNonLocked;
        private Set<AppUserRole> roles;

        public AppUserTableAPDtoBuilder() {
        }

        public AppUserTableAPDtoBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public AppUserTableAPDtoBuilder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public AppUserTableAPDtoBuilder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public AppUserTableAPDtoBuilder withEmail(String email) {
            this.email = email;
            return this;
        }

        public AppUserTableAPDtoBuilder withEnabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public AppUserTableAPDtoBuilder withAccountNonLocked(boolean accountNonLocked) {
            this.accountNonLocked = accountNonLocked;
            return this;
        }

        public AppUserTableAPDtoBuilder withRoles(Set<AppUserRole> roles) {
            this.roles = roles;
            return this;
        }

        public AppUserTableAPDto build() {
            AppUserTableAPDto userTableAPDto = new AppUserTableAPDto();
            userTableAPDto.id = id;
            userTableAPDto.firstName = firstName;
            userTableAPDto.lastName = lastName;
            userTableAPDto.email = email;
            userTableAPDto.enabled = enabled;
            userTableAPDto.accountNonLocked = accountNonLocked;
            userTableAPDto.roles = roles;
            return userTableAPDto;
        }
    }
}
