package pl.dreilt.basicspringmvcapp.dto;

import pl.dreilt.basicspringmvcapp.entity.AppUserRole;

import java.util.Set;

public class AppUserAccountEditAdminPanelDto {
    private boolean enabled;
    private boolean accountNonLocked;
    private Set<AppUserRole> roles;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public Set<AppUserRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<AppUserRole> roles) {
        this.roles = roles;
    }
}
