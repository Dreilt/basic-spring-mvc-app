package pl.dreilt.basicspringmvcapp.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.dreilt.basicspringmvcapp.dto.EventDto;
import pl.dreilt.basicspringmvcapp.dto.NewEventDto;
import pl.dreilt.basicspringmvcapp.entity.AppUser;
import pl.dreilt.basicspringmvcapp.entity.Event;
import pl.dreilt.basicspringmvcapp.repository.AppUserRepository;
import pl.dreilt.basicspringmvcapp.repository.EventImageRepository;
import pl.dreilt.basicspringmvcapp.repository.OrganizerRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;
import static pl.dreilt.basicspringmvcapp.core.AppUserHelper.createAppUser;
import static pl.dreilt.basicspringmvcapp.core.AppUserHelper.createOrganizerRole;
import static pl.dreilt.basicspringmvcapp.service.AppUserServiceTestHelper.createAppUserDetails;
import static pl.dreilt.basicspringmvcapp.service.EventServiceTestHelper.createNewEventDto;

@ExtendWith(MockitoExtension.class)
class OrganizerServiceTest {
    @Mock
    private OrganizerRepository organizerRepository;
    @Mock
    private EventImageRepository eventImageRepository;
    @Mock
    private AppUserRepository appUserRepository;
    @InjectMocks
    private OrganizerService organizerService;

    @Test
    void shouldCreatedNewEvent() {
        // given
        Authentication auth = new UsernamePasswordAuthenticationToken(createAppUserDetails("Jan", "Kowalski", "jankowalski@example.com", "ORGANIZER"), null);
        SecurityContextHolder.getContext().setAuthentication(auth);
        AppUser user = createAppUser(1L, "Jan", "Kowalski", "jankowalski@example.com", createOrganizerRole());
        when(appUserRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        NewEventDto newEvent = createNewEventDto();
        // when
        EventDto eventCreated = organizerService.createEvent(newEvent);
        // then
        Mockito.verify(organizerRepository, Mockito.times(1)).save(argThat((Event saved) -> {
            Assertions.assertAll("Testing created event",
                    () -> assertNull(saved.getId()),
                    () -> assertEquals(newEvent.getName(), saved.getName()),
                    () -> assertEquals(newEvent.getEventType(), saved.getEventType()),
                    () -> assertEquals(newEvent.getDateAndTime(), saved.getDateAndTime())
            );
            return true;
        }));
    }
}