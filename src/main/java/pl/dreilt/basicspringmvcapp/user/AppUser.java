package pl.dreilt.basicspringmvcapp.user;

import pl.dreilt.basicspringmvcapp.core.BaseEntity;
import pl.dreilt.basicspringmvcapp.pi.ProfileImage;
import pl.dreilt.basicspringmvcapp.role.AppUserRole;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
public class AppUser extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "{form.field.firstName.error.notNull.message}")
    @Size(min = 2, max = 50, message = "{form.field.firstName.error.size.message}")
    private String firstName;
    @NotNull(message = "{form.field.lastName.error.notNull.message}")
    @Size(min = 2, max = 50, message = "{form.field.lastName.error.size.message}")
    private String lastName;
    @NotNull(message = "{form.field.email.error.notNull.message}")
    @NotEmpty(message = "{form.field.email.error.notEmpty.message}")
    @Email
    private String email;
    @NotNull(message = "{form.field.password.error.notNull.message}")
    @Size(min = 5, max = 100, message = "{form.field.password.error.size.message}")
    private String password;
    @OneToOne
    @JoinTable(
            name = "user_profile_image",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "profile_image_id", referencedColumnName = "id")
    )
    private ProfileImage profileImage;
    @Size(max = 1000, message = "{form.field.bio.error.size.message}")
    private String bio;
    @Size(max = 50, message = "{form.field.city.error.size.message}")
    private String city;
    private boolean enabled;
    private boolean accountNonLocked;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private Set<AppUserRole> roles = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public ProfileImage getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(ProfileImage profileImage) {
        this.profileImage = profileImage;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

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

    public static class AppUserBuilder {
        private Long id;
        private String firstName;
        private String lastName;
        private String email;
        private String password;
        private ProfileImage profileImage;
        private String bio;
        private String city;
        private boolean enabled;
        private boolean accountNonLocked;
        private Set<AppUserRole> roles;

        public AppUserBuilder() {
        }

        public AppUserBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public AppUserBuilder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public AppUserBuilder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public AppUserBuilder withEmail(String email) {
            this.email = email;
            return this;
        }

        public AppUserBuilder withPassword(String password) {
            this.password = password;
            return this;
        }

        public AppUserBuilder withProfileImage(ProfileImage profileImage) {
            this.profileImage = profileImage;
            return this;
        }

        public AppUserBuilder withBio(String bio) {
            this.bio = bio;
            return this;
        }

        public AppUserBuilder withCity(String city) {
            this.city = city;
            return this;
        }

        public AppUserBuilder withEnabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public AppUserBuilder withAccountNonLocked(boolean accountNonLocked) {
            this.accountNonLocked = accountNonLocked;
            return this;
        }

        public AppUserBuilder withRoles(Set<AppUserRole> roles) {
            this.roles = roles;
            return this;
        }

        public AppUser build() {
            AppUser user = new AppUser();
            user.id = id;
            user.firstName = firstName;
            user.lastName = lastName;
            user.email = email;
            user.password = password;
            user.profileImage = profileImage;
            user.bio = bio;
            user.city = city;
            user.enabled = enabled;
            user.accountNonLocked = accountNonLocked;
            user.roles = roles;
            return user;
        }
    }
}
