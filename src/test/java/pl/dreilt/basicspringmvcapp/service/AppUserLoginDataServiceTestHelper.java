package pl.dreilt.basicspringmvcapp.service;

import pl.dreilt.basicspringmvcapp.entity.AppUser;
import pl.dreilt.basicspringmvcapp.entity.AppUserRole;

import java.util.Set;

public class AppUserLoginDataServiceTestHelper {

    static AppUser createUser() {
        AppUser user = new AppUser();
        user.setId(1L);
        user.setFirstName("Test");
        user.setLastName("Test");
        user.setEmail("test@example.com");
        user.setPassword("{bcrypt}$2a$10$HdTWfiLZkzlT9JXh/q3OhuPMsp972q0rp9oHba0wgaI0P.zRsLfb6");
        user.setBio("Cześć! Nazywam się Test Test i jestem z Rzeszowa. Obecnie jestem na trzecim roku informatyki na Politechnice Rzeszowskiej.");
        user.setCity("Rzeszów");
        user.setEnabled(false);
        user.setAccountNonLocked(true);
        Set<AppUserRole> roles = Set.of(
                createUserRole()
        );
        user.setRoles(roles);
        return user;
    }

    static AppUserRole createUserRole() {
        AppUserRole userRole = new AppUserRole();
        userRole.setId(2L);
        userRole.setName("USER");
        userRole.setVisibleName("Użytkownik");
        userRole.setDescription("Dostęp ograniczony");
        return userRole;
    }
}
