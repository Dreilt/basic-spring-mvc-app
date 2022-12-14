package pl.dreilt.basicspringmvcapp.role;

import pl.dreilt.basicspringmvcapp.core.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class AppUserRole extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String displayName;
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static class AppUserRoleBuilder {
        private Long id;
        private String name;
        private String displayName;
        private String description;

        public AppUserRoleBuilder() {
        }

        public AppUserRoleBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public AppUserRoleBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public AppUserRoleBuilder withDisplayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        public AppUserRoleBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public AppUserRole build() {
            AppUserRole userRole = new AppUserRole();
            userRole.id = id;
            userRole.name = name;
            userRole.displayName = displayName;
            userRole.description = description;
            return userRole;
        }
    }
}
