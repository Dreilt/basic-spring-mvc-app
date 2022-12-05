package pl.dreilt.basicspringmvcapp.service;

import pl.dreilt.basicspringmvcapp.entity.AppUserRole;

public class AppUserRoleServiceTestHelper {

    static AppUserRole createAdminRole() {
        AppUserRole adminRole = new AppUserRole();
        adminRole.setId(1L);
        adminRole.setName("ADMIN");
        adminRole.setDisplayName("Administrator");
        adminRole.setDescription("Ma dostęp do wszystkiego");
        return adminRole;
    }

    static AppUserRole createUserRole() {
        AppUserRole userRole = new AppUserRole();
        userRole.setId(2L);
        userRole.setName("USER");
        userRole.setDisplayName("Użytkownik");
        userRole.setDescription("Dostęp ograniczony");
        return userRole;
    }
}
