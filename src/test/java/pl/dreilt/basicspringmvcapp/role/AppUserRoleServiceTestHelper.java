package pl.dreilt.basicspringmvcapp.role;

public class AppUserRoleServiceTestHelper {

    public static AppUserRole createAdminRole() {
        return new AppUserRole.AppUserRoleBuilder()
                .withId(1L)
                .withName("ADMIN")
                .withDisplayName("Administrator")
                .withDescription("Ma dostęp do wszystkiego")
                .build();
    }

    public static AppUserRole createOrganizerRole() {
        return new AppUserRole.AppUserRoleBuilder()
                .withId(2L)
                .withName("ORGANIZER")
                .withDisplayName("Organizator")
                .withDescription("Może organizować wydarzenia")
                .build();
    }

    public static AppUserRole createUserRole() {
        return new AppUserRole.AppUserRoleBuilder()
                .withId(3L)
                .withName("USER")
                .withDisplayName("Użytkownik")
                .withDescription("Dostęp ograniczony")
                .build();
    }
}
