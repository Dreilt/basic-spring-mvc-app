package pl.dreilt.basicspringmvcapp.user.dto;

import pl.dreilt.basicspringmvcapp.role.AppUserRole;

import java.util.Set;

public class AppUserAccountEditAPDto {
    private boolean enabled;
    private boolean accountNonLocked;
    private Set<AppUserRole> roles;

    private AppUserAccountEditAPDto() {
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

    public static class AppUserAccountEditAPDtoBuilder {
        private boolean enabled;
        private boolean accountNonLocked;
        private Set<AppUserRole> roles;

        public AppUserAccountEditAPDtoBuilder() {
        }

        public AppUserAccountEditAPDtoBuilder withEnabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public AppUserAccountEditAPDtoBuilder withAccountNonLocked(boolean accountNonLocked) {
            this.accountNonLocked = accountNonLocked;
            return this;
        }

        public AppUserAccountEditAPDtoBuilder withRoles(Set<AppUserRole> roles) {
            this.roles = roles;
            return this;
        }

        public AppUserAccountEditAPDto build() {
            AppUserAccountEditAPDto userAccountEditAP = new AppUserAccountEditAPDto();
            userAccountEditAP.enabled = enabled;
            userAccountEditAP.accountNonLocked = accountNonLocked;
            userAccountEditAP.roles = roles;
            return userAccountEditAP;
        }
    }
}
