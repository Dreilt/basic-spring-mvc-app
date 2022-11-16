package pl.dreilt.basicspringmvcapp.service;

import pl.dreilt.basicspringmvcapp.dto.AppUserAccountEditAdminPanelDto;
import pl.dreilt.basicspringmvcapp.dto.AppUserProfileEditAdminPanelDto;
import pl.dreilt.basicspringmvcapp.entity.AppUser;
import pl.dreilt.basicspringmvcapp.entity.AppUserRole;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class AdminServiceTestHelper {

    static List<AppUser> createUserList() {
        return Arrays.asList(
                createUser(1L, "Jan", "Kowalski", "jankowalski@example.com"),
                createUser(2L, "Patryk", "Kowalski", "patrykkowalski@example.com"),
                createUser(3L, "Jan", "Nowak", "jannowak@example.com")
        );
    }

    private static AppUser createUser(Long id, String firstName, String lastName, String email) {
        AppUser user = new AppUser();
        user.setId(id);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword("{bcrypt}$2a$10$1rXMx0b4caUy/SN3Xg4j4u43gDqVJO/R.zXGCGWc/wr7bsmmSEk2C");
        user.setBio("");
        user.setCity("");
        user.setEnabled(true);
        user.setAccountNonLocked(true);
        return user;
    }

    static List<AppUser> createUserListBySearch(String searchQuery) {
        List<AppUser> userList = createUserList();
        List<AppUser> newUserList = new ArrayList<>();

        searchQuery = searchQuery.toLowerCase();
        String[] searchWords = searchQuery.split(" ");

        for (AppUser user : userList) {
            if (searchWords.length == 1) {
                if (user.getFirstName().toLowerCase().contains(searchWords[0]) ||
                        user.getLastName().toLowerCase().contains(searchWords[0]) ||
                        user.getEmail().toLowerCase().contains(searchWords[0])) {
                    newUserList.add(user);
                }
            }

            if (searchWords.length == 2) {
                if (user.getFirstName().toLowerCase().contains(searchWords[0]) && user.getLastName().toLowerCase().contains(searchWords[1]) ||
                        user.getFirstName().toLowerCase().contains(searchWords[1]) && user.getLastName().toLowerCase().contains(searchWords[0]) ||
                        user.getEmail().toLowerCase().contains(searchWords[0] + searchWords[1]) ||
                        user.getFirstName().toLowerCase().contains(searchWords[0]) && user.getEmail().toLowerCase().contains(searchWords[1]) ||
                        user.getFirstName().toLowerCase().contains(searchWords[1]) && user.getEmail().toLowerCase().contains(searchWords[0]) ||
                        user.getLastName().toLowerCase().contains(searchWords[0]) && user.getEmail().toLowerCase().contains(searchWords[1]) ||
                        user.getLastName().toLowerCase().contains(searchWords[1]) && user.getEmail().toLowerCase().contains(searchWords[0])) {
                    newUserList.add(user);
                }
            }
        }

        return newUserList;
    }

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

    static AppUserAccountEditAdminPanelDto createUserAccountEditAdminPanelDto() {
        AppUserAccountEditAdminPanelDto userAccountEditAdminPanelDto = new AppUserAccountEditAdminPanelDto();
        userAccountEditAdminPanelDto.setEnabled(true);
        userAccountEditAdminPanelDto.setAccountNonLocked(false);
        Set<AppUserRole> userRole = Set.of(
                createUserRole()
        );
        userAccountEditAdminPanelDto.setRoles(userRole);
        return userAccountEditAdminPanelDto;
    }

    static AppUserProfileEditAdminPanelDto createUserProfileEditAdminPanelDto() {
        AppUserProfileEditAdminPanelDto userProfileEditAdminPanelDto = new AppUserProfileEditAdminPanelDto();
        userProfileEditAdminPanelDto.setFirstName("Jan");
        userProfileEditAdminPanelDto.setLastName("Kowalski");
        userProfileEditAdminPanelDto.setBio("Cześć! Nazywam się Jan Kowalski i niedawno przeprowadziłem się do Krakowa.");
        userProfileEditAdminPanelDto.setCity("Kraków");
        return userProfileEditAdminPanelDto;
    }
}
