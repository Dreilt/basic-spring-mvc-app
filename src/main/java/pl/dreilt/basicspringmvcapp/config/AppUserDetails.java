package pl.dreilt.basicspringmvcapp.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AppUserDetails extends User {
    private String firstName;
    private String lastName;
    private String avatarType;
    private String avatarData;

    public AppUserDetails(AppUserBuilder appUserBuilder) {
        super(appUserBuilder.username, appUserBuilder.password, appUserBuilder.enabled, true, true, appUserBuilder.accountNonLocked, appUserBuilder.authorities);
        this.firstName = appUserBuilder.firstName;
        this.lastName = appUserBuilder.lastName;
        this.avatarType = appUserBuilder.avatarType;
        this.avatarData = appUserBuilder.avatarData;
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

    public String getAvatarType() {
        return avatarType;
    }

    public void setAvatarType(String avatarType) {
        this.avatarType = avatarType;
    }

    public String getAvatarData() {
        return avatarData;
    }

    public void setAvatarData(String avatarData) {
        this.avatarData = avatarData;
    }

    public static final class AppUserBuilder {
        private String firstName;
        private String lastName;
        private String username;
        private String password;
        private String avatarType;
        private String avatarData;
        private boolean enabled;
        private boolean accountNonLocked;
        private List<GrantedAuthority> authorities;

        public AppUserBuilder firstName(String firstName) {
            Assert.notNull(firstName, "firstName cannot be null");
            this.firstName = firstName;
            return this;
        }

        public AppUserBuilder lastName(String lastName) {
            Assert.notNull(lastName, "lastName cannot be null");
            this.lastName = lastName;
            return this;
        }

        public AppUserBuilder username(String username) {
            Assert.notNull(username, "username cannot be null");
            this.username = username;
            return this;
        }

        public AppUserBuilder password(String password) {
            Assert.notNull(password, "password cannot be null");
            this.password = password;
            return this;
        }

        public AppUserBuilder avatarType(String avatarType) {
            Assert.notNull(avatarType, "avatarType cannot be null");
            this.avatarType = avatarType;
            return this;
        }

        public AppUserBuilder avatarData(String avatarData) {
            Assert.notNull(avatarData, "avatarData cannot be null");
            this.avatarData = avatarData;
            return this;
        }

        public AppUserBuilder enabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public AppUserBuilder accountNonLocked(boolean accountNonLocked) {
            this.accountNonLocked = accountNonLocked;
            return this;
        }

        public AppUserBuilder roles(String... roles) {
            List<GrantedAuthority> authorities = new ArrayList(roles.length);
            String[] var3 = roles;
            int var4 = roles.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                String role = var3[var5];
                Assert.isTrue(!role.startsWith("ROLE_"), () -> {
                    return role + " cannot start with ROLE_ (it is automatically added)";
                });
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
            }

            return this.authorities((Collection)authorities);
        }

        public AppUserBuilder authorities(Collection<? extends GrantedAuthority> authorities) {
            this.authorities = new ArrayList(authorities);
            return this;
        }

        public AppUserDetails build() {
            return new AppUserDetails(this);
        }
    }
}
