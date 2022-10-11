package pl.dreilt.basicspringmvcapp.dto;

import java.util.Set;

public class AppUserCredentialsDto {
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String password;
    private final String avatarType;
    private final String avatar;
    private final boolean enabled;
    private final boolean accountNonLocked;
    private final Set<String> roles;

    public AppUserCredentialsDto(String firstName, String lastName, String email, String password, String avatarType, String avatar, boolean enabled, boolean accountNonLocked, Set<String> roles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.avatarType = avatarType;
        this.avatar = avatar;
        this.enabled = enabled;
        this.accountNonLocked = accountNonLocked;
        this.roles = roles;
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

    public String getPassword() {
        return password;
    }

    public String getAvatarType() {
        return avatarType;
    }

    public String getAvatar() {
        return avatar;
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
