package pl.dreilt.basicspringmvcapp.dto;

import java.util.Set;

public class AppUserCredentialsDto {
    private final String email;
    private final String password;
    private final boolean enabled;
    private final boolean accountNonLocked;
    private final Set<String> roles;

    public AppUserCredentialsDto(String email, String password, boolean enabled, boolean accountNonLocked, Set<String> roles) {
        this.email = email;
        this.password = password;
        this.enabled = enabled;
        this.accountNonLocked = accountNonLocked;
        this.roles = roles;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public Set<String> getRoles() {
        return roles;
    }
}
