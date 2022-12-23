package pl.dreilt.basicspringmvcapp.dto;

import pl.dreilt.basicspringmvcapp.entity.AppUserRole;

import java.util.Set;

public class AppUserAccountDataEditAPDto {
    private boolean enabled;
    private boolean accountNonLocked;
    private Set<AppUserRole> roles;

    private AppUserAccountDataEditAPDto() {
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

    public static class AppUserAccountDataEditAPDtoBuilder {
        private boolean enabled;
        private boolean accountNonLocked;
        private Set<AppUserRole> roles;

        public AppUserAccountDataEditAPDtoBuilder() {
        }

        public AppUserAccountDataEditAPDtoBuilder withEnabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public AppUserAccountDataEditAPDtoBuilder withAccountNonLocked(boolean accountNonLocked) {
            this.accountNonLocked = accountNonLocked;
            return this;
        }

        public AppUserAccountDataEditAPDtoBuilder withRoles(Set<AppUserRole> roles) {
            this.roles = roles;
            return this;
        }

        public AppUserAccountDataEditAPDto build() {
            AppUserAccountDataEditAPDto userAccountDataEditAPDto = new AppUserAccountDataEditAPDto();
            userAccountDataEditAPDto.enabled = enabled;
            userAccountDataEditAPDto.accountNonLocked = accountNonLocked;
            userAccountDataEditAPDto.roles = roles;
            return userAccountDataEditAPDto;
        }
    }
}
