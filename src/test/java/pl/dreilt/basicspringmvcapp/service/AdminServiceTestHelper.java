package pl.dreilt.basicspringmvcapp.service;

import pl.dreilt.basicspringmvcapp.dto.AppUserAccountDataEditAPDto;
import pl.dreilt.basicspringmvcapp.dto.AppUserProfileDataEditAPDto;
import pl.dreilt.basicspringmvcapp.entity.AppUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static pl.dreilt.basicspringmvcapp.core.AppUserHelper.*;

public class AdminServiceTestHelper {

    static List<AppUser> createAppUserList() {
        return Arrays.asList(
                createAppUser(1L, "Jan", "Kowalski", "jankowalski@example.com", createOrganizerRole()),
                createAppUser(2L, "Patryk", "Kowalski", "patrykkowalski@example.com", createUserRole()),
                createAppUser(3L, "Jan", "Nowak", "jannowak@example.com", createUserRole())
        );
    }

    static List<AppUser> createAppUserListBySearch(String searchQuery) {
        List<AppUser> userList = createAppUserList();
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

    static AppUserAccountDataEditAPDto createAppUserAccountDataEditAPDto() {
        return new AppUserAccountDataEditAPDto.AppUserAccountDataEditAPDtoBuilder()
                .withEnabled(false)
                .withAccountNonLocked(false)
                .withRoles(Set.of(createOrganizerRole()))
                .build();
    }

    static AppUserProfileDataEditAPDto createAppUserProfileDataEditAPDto() {
        return new AppUserProfileDataEditAPDto.AppUserProfileDataEditAPDtoBuilder()
                .withFirstName("Jan")
                .withLastName("Kowalski")
                .withBio("Cześć! Nazywam się Jan Kowalski i niedawno przeprowadziłem się do Krakowa.")
                .withCity("Kraków")
                .build();
    }
}
