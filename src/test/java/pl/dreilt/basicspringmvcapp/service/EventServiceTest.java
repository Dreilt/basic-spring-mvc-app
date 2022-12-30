package pl.dreilt.basicspringmvcapp.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.dreilt.basicspringmvcapp.dto.CityDto;
import pl.dreilt.basicspringmvcapp.dto.EventBoxDto;
import pl.dreilt.basicspringmvcapp.dto.EventDto;
import pl.dreilt.basicspringmvcapp.dto.NewEventDto;
import pl.dreilt.basicspringmvcapp.entity.AppUser;
import pl.dreilt.basicspringmvcapp.entity.Event;
import pl.dreilt.basicspringmvcapp.exception.AppUserNotFoundException;
import pl.dreilt.basicspringmvcapp.mapper.EventDtoMapper;
import pl.dreilt.basicspringmvcapp.repository.AppUserRepository;
import pl.dreilt.basicspringmvcapp.repository.EventImageRepository;
import pl.dreilt.basicspringmvcapp.repository.EventRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static pl.dreilt.basicspringmvcapp.core.AppUserHelper.*;
import static pl.dreilt.basicspringmvcapp.service.AppUserServiceTestHelper.createAppUserDetails;
import static pl.dreilt.basicspringmvcapp.service.EventServiceTestHelper.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {
    static final LocalDateTime DATE_TIME = LocalDateTime.now();
    @Mock
    private EventRepository eventRepository;
    @Mock
    private EventImageRepository eventImageRepository;
    @Mock
    private AppUserRepository appUserRepository;
    @InjectMocks
    private EventService eventService;

    @Test
    void shouldCreatedNewEvent() {
        // given
        Authentication auth = new UsernamePasswordAuthenticationToken(createAppUserDetails("Jan", "Kowalski", "jankowalski@example.com", "ORGANIZER"), null);
        SecurityContextHolder.getContext().setAuthentication(auth);
        AppUser user = createAppUser(1L, "Jan", "Kowalski", "jankowalski@example.com", createOrganizerRole());
        when(appUserRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        NewEventDto newEvent = createNewEventDto();
        // when
        EventDto eventCreated = eventService.createEvent(newEvent);
        // then
        Mockito.verify(eventRepository, Mockito.times(1)).save(argThat((Event saved) -> {
            Assertions.assertAll("Testing created event",
                    () -> assertNull(saved.getId()),
                    () -> assertEquals(newEvent.getName(), saved.getName()),
                    () -> assertEquals(newEvent.getEventType(), saved.getEventType()),
                    () -> assertEquals(newEvent.getDateAndTime(), saved.getDateAndTime())
            );
            return true;
        }));
    }

    @Test
    void shouldGetOneEvent() {
        // given
        Long eventId = 1L;
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(createEvent(eventId, "Java Dev Talks #1", DATE_TIME, createParticipantsEventList())));
        // when
        EventDto event = eventService.findEventById(eventId);
        // then
        assertThat(event.getId()).isEqualTo(eventId);
    }

    @Test
    void shouldGetAllCities() {
        // given
        when(eventRepository.findAllCities()).thenReturn(createCitiesList());
        // when
        List<CityDto> cities = eventService.findAllCities();
        // then
        assertThat(cities).isNotEmpty();
        assertThat(cities).hasSize(9);
    }

    @Test
    void shouldGetThreeUpcomingEvents() {
        // given
        List<Event> upcomingEventsList = EventServiceTestHelper.createUpcomingEventsList();
        when(eventRepository.findAllUpcomingEvents(DATE_TIME)).thenReturn(upcomingEventsList);
        // when
        List<EventBoxDto> upcomingEvents = eventService.findAllUpcomingEvents(DATE_TIME);
        // then
        assertThat(upcomingEvents).isNotEmpty();
        assertThat(upcomingEvents).hasSize(3);
        assertThat(upcomingEvents.get(0).getDate()).isEqualTo(DATE_TIME.plusWeeks(1L).format(DateTimeFormatter.ofPattern("dd.MM")));
        assertThat(upcomingEvents.get(1).getDate()).isEqualTo(DATE_TIME.plusWeeks(2L).format(DateTimeFormatter.ofPattern("dd.MM")));
        assertThat(upcomingEvents.get(2).getDate()).isEqualTo(DATE_TIME.plusWeeks(3L).format(DateTimeFormatter.ofPattern("dd.MM")));
    }

    @Test
    void shouldGetThreePastEvents() {
        // given
        List<Event> pastEventsList = EventServiceTestHelper.createPastEventsList();
        when(eventRepository.findAllPastEvents(DATE_TIME)).thenReturn(pastEventsList);
        // when
        List<EventBoxDto> pastEvents = eventService.findAllPastEvents(DATE_TIME);
        // then
        assertThat(pastEvents).isNotEmpty();
        assertThat(pastEvents).hasSize(3);
        assertThat(pastEvents.get(0).getDate()).isEqualTo(DATE_TIME.minusWeeks(1L).format(DateTimeFormatter.ofPattern("dd.MM")));
        assertThat(pastEvents.get(1).getDate()).isEqualTo(DATE_TIME.minusWeeks(2L).format(DateTimeFormatter.ofPattern("dd.MM")));
        assertThat(pastEvents.get(2).getDate()).isEqualTo(DATE_TIME.minusWeeks(3L).format(DateTimeFormatter.ofPattern("dd.MM")));
    }

    @Test
    void shouldGetThreeUpcomingEventsIfCityIsEqualRzeszow() {
        // given
        String city = "Rzeszów";
        List<Event> upcomingEventsList = EventServiceTestHelper.createUpcomingEventsList();
        when(eventRepository.findUpcomingEventsByCity(DATE_TIME, city)).thenReturn(upcomingEventsList);
        // when
        List<EventBoxDto> upcomingEvents = eventService.findUpcomingEventsByCity(DATE_TIME, city);
        // then
        assertThat(upcomingEvents).isNotEmpty();
        assertThat(upcomingEvents).hasSize(3);
        assertThat(upcomingEvents.get(0).getDate()).isEqualTo(DATE_TIME.plusWeeks(1L).format(DateTimeFormatter.ofPattern("dd.MM")));
        assertThat(upcomingEvents.get(0).getCity()).isEqualTo(city);
        assertThat(upcomingEvents.get(1).getDate()).isEqualTo(DATE_TIME.plusWeeks(2L).format(DateTimeFormatter.ofPattern("dd.MM")));
        assertThat(upcomingEvents.get(1).getCity()).isEqualTo(city);
        assertThat(upcomingEvents.get(2).getDate()).isEqualTo(DATE_TIME.plusWeeks(3L).format(DateTimeFormatter.ofPattern("dd.MM")));
        assertThat(upcomingEvents.get(2).getCity()).isEqualTo(city);
    }

    @Test
    void shouldGetThreePastEventsIfCityIsEqualRzeszow() {
        // given
        String city = "Rzeszów";
        List<Event> pastEventsList = EventServiceTestHelper.createPastEventsList();
        when(eventRepository.findPastEventsByCity(DATE_TIME, city)).thenReturn(pastEventsList);
        // when
        List<EventBoxDto> pastEvents = eventService.findPastEventsByCity(DATE_TIME, city);
        // then
        assertThat(pastEvents).isNotEmpty();
        assertThat(pastEvents).hasSize(3);
        assertThat(pastEvents.get(0).getDate()).isEqualTo(DATE_TIME.minusWeeks(1L).format(DateTimeFormatter.ofPattern("dd.MM")));
        assertThat(pastEvents.get(0).getCity()).isEqualTo(city);
        assertThat(pastEvents.get(1).getDate()).isEqualTo(DATE_TIME.minusWeeks(2L).format(DateTimeFormatter.ofPattern("dd.MM")));
        assertThat(pastEvents.get(1).getCity()).isEqualTo(city);
        assertThat(pastEvents.get(2).getDate()).isEqualTo(DATE_TIME.minusWeeks(3L).format(DateTimeFormatter.ofPattern("dd.MM")));
        assertThat(pastEvents.get(2).getCity()).isEqualTo(city);
    }

    @Test
    void shouldGetThreeUpcomingUserEvents() {
        // given
        Authentication auth = new UsernamePasswordAuthenticationToken(createAppUserDetails("Patryk", "Kowalski", "patrykkowalski@example.com", "USER"), null);
        SecurityContextHolder.getContext().setAuthentication(auth);
        AppUser participant = createAppUser(2L, "Patryk", "Kowalski", "patrykkowalski@example.com", createUserRole());
        when(appUserRepository.findByEmail("patrykkowalski@example.com")).thenReturn(Optional.of(participant));
        List<Event> upcomingEventsList = EventServiceTestHelper.createUpcomingEventsList();
        when(eventRepository.findUpcomingEventsByUser(DATE_TIME, participant)).thenReturn(upcomingEventsList);
        // when
        List<EventBoxDto> upcomingEvents = eventService.findUpcomingEventsByUser(DATE_TIME);
        // then
        assertThat(upcomingEvents).isNotEmpty();
        assertThat(upcomingEvents).hasSize(3);
        assertThat(upcomingEvents.get(0).getDate()).isEqualTo(DATE_TIME.plusWeeks(1L).format(DateTimeFormatter.ofPattern("dd.MM")));
        assertThat(upcomingEvents.get(1).getDate()).isEqualTo(DATE_TIME.plusWeeks(2L).format(DateTimeFormatter.ofPattern("dd.MM")));
        assertThat(upcomingEvents.get(2).getDate()).isEqualTo(DATE_TIME.plusWeeks(3L).format(DateTimeFormatter.ofPattern("dd.MM")));
    }

    @Test
    void shouldGetThreePastUserEvents() {
        // given
        Authentication auth = new UsernamePasswordAuthenticationToken(createAppUserDetails("Patryk", "Kowalski", "patrykkowalski@example.com", "USER"), null);
        SecurityContextHolder.getContext().setAuthentication(auth);
        AppUser participant = createAppUser(2L, "Patryk", "Kowalski", "patrykkowalski@example.com", createUserRole());
        List<Event> pastEventsList = EventServiceTestHelper.createPastEventsList();
        when(appUserRepository.findByEmail("patrykkowalski@example.com")).thenReturn(Optional.of(participant));
        when(eventRepository.findPastEventsByUser(DATE_TIME, participant)).thenReturn(pastEventsList);
        // when
        List<EventBoxDto> pastEvents = eventService.findPastEventsByUser(DATE_TIME);
        // then
        assertThat(pastEvents).isNotEmpty();
        assertThat(pastEvents).hasSize(3);
        assertThat(pastEvents.get(0).getDate()).isEqualTo(DATE_TIME.minusWeeks(1L).format(DateTimeFormatter.ofPattern("dd.MM")));
        assertThat(pastEvents.get(1).getDate()).isEqualTo(DATE_TIME.minusWeeks(2L).format(DateTimeFormatter.ofPattern("dd.MM")));
        assertThat(pastEvents.get(2).getDate()).isEqualTo(DATE_TIME.minusWeeks(3L).format(DateTimeFormatter.ofPattern("dd.MM")));
    }

    @Test
    void shouldGetThreeUpcomingUserEventsByCity() {
        // given
        String city = "Rzeszów";
        Authentication auth = new UsernamePasswordAuthenticationToken(createAppUserDetails("Patryk", "Kowalski", "patrykkowalski@example.com", "USER"), null);
        SecurityContextHolder.getContext().setAuthentication(auth);
        AppUser participant = createAppUser(2L, "Patryk", "Kowalski", "patrykkowalski@example.com", createUserRole());
        List<Event> upcomingEventsList = EventServiceTestHelper.createUpcomingEventsList();
        when(appUserRepository.findByEmail("patrykkowalski@example.com")).thenReturn(Optional.of(participant));
        when(eventRepository.findUpcomingEventsByUserAndCity(DATE_TIME, participant, city)).thenReturn(upcomingEventsList);
        // when
        List<EventBoxDto> upcomingEvents = eventService.findUpcomingEventsByUserAndCity(DATE_TIME, city);
        // then
        assertThat(upcomingEvents).isNotEmpty();
        assertThat(upcomingEvents).hasSize(3);
        assertThat(upcomingEvents.get(0).getDate()).isEqualTo(DATE_TIME.plusWeeks(1L).format(DateTimeFormatter.ofPattern("dd.MM")));
        assertThat(upcomingEvents.get(0).getCity()).isEqualTo(city);
        assertThat(upcomingEvents.get(1).getDate()).isEqualTo(DATE_TIME.plusWeeks(2L).format(DateTimeFormatter.ofPattern("dd.MM")));
        assertThat(upcomingEvents.get(0).getCity()).isEqualTo(city);
        assertThat(upcomingEvents.get(2).getDate()).isEqualTo(DATE_TIME.plusWeeks(3L).format(DateTimeFormatter.ofPattern("dd.MM")));
        assertThat(upcomingEvents.get(0).getCity()).isEqualTo(city);
    }

    @Test
    void shouldGetThreePastUserEventsByCity() {
        // given
        String city = "Rzeszów";
        Authentication auth = new UsernamePasswordAuthenticationToken(createAppUserDetails("Patryk", "Kowalski", "patrykkowalski@example.com", "USER"), null);
        SecurityContextHolder.getContext().setAuthentication(auth);
        AppUser participant = createAppUser(2L, "Patryk", "Kowalski", "patrykkowalski@example.com", createUserRole());
        when(appUserRepository.findByEmail("patrykkowalski@example.com")).thenReturn(Optional.of(participant));
        List<Event> pastEventsList = EventServiceTestHelper.createPastEventsList();
        when(eventRepository.findPastEventsByUserAndCity(DATE_TIME, participant, city)).thenReturn(pastEventsList);
        // when
        List<EventBoxDto> pastEvents = eventService.findPastEventsByUserAndCity(DATE_TIME, city);
        // then
        assertThat(pastEvents).isNotEmpty();
        assertThat(pastEvents).hasSize(3);
        assertThat(pastEvents.get(0).getDate()).isEqualTo(DATE_TIME.minusWeeks(1L).format(DateTimeFormatter.ofPattern("dd.MM")));
        assertThat(pastEvents.get(0).getCity()).isEqualTo(city);
        assertThat(pastEvents.get(1).getDate()).isEqualTo(DATE_TIME.minusWeeks(2L).format(DateTimeFormatter.ofPattern("dd.MM")));
        assertThat(pastEvents.get(0).getCity()).isEqualTo(city);
        assertThat(pastEvents.get(2).getDate()).isEqualTo(DATE_TIME.minusWeeks(3L).format(DateTimeFormatter.ofPattern("dd.MM")));
        assertThat(pastEvents.get(0).getCity()).isEqualTo(city);
    }

    @Test
    void shouldThrowExceptionIfUserIsNotAuthenticated2() {
        // given
        SecurityContextHolder.getContext().setAuthentication(null);
        AppUser participant = createAppUser(2L, "Patryk", "Kowalski", "patrykkowalski@example.com", createUserRole());
        List<AppUser> participants = Arrays.asList(
                participant
        );
        Event event = createEvent(1L,"Java Dev Talks #1", DATE_TIME.plusWeeks(1L), participants);
        // then
        assertThatThrownBy(() -> eventService.checkIfUserIsParticipant(EventDtoMapper.mapToEventDto(event)))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage("Odmowa dostępu");
    }

    @Test
    void shouldReturnTrueIfUserIsEventParticipant() {
        // given
        Authentication auth = new UsernamePasswordAuthenticationToken(createAppUserDetails("Patryk", "Kowalski", "patrykkowalski@example.com", "USER"), null);
        SecurityContextHolder.getContext().setAuthentication(auth);
        AppUser participant = createAppUser(2L, "Patryk", "Kowalski", "patrykkowalski@example.com", createUserRole());
        when(appUserRepository.findByEmail("patrykkowalski@example.com")).thenReturn(Optional.of(participant));
        List<AppUser> participants = Arrays.asList(
                participant
        );
        Event event = createEvent(1L,"Java Dev Talks #1", DATE_TIME.plusWeeks(1L), participants);
        // when
        boolean isParticipant = eventService.checkIfUserIsParticipant(EventDtoMapper.mapToEventDto(event));
        // then
        assertThat(isParticipant).isTrue();
    }

    @Test
    void shouldReturnFalseIfUserIsNotEventParticipant() {
        // given
        Authentication auth = new UsernamePasswordAuthenticationToken(createAppUserDetails("Patryk", "Kowalski", "patrykkowalski@example.com", "USER"), null);
        SecurityContextHolder.getContext().setAuthentication(auth);
        AppUser participant = createAppUser(2L, "Patryk", "Kowalski", "patrykkowalski@example.com", createUserRole());
        when(appUserRepository.findByEmail("patrykkowalski@example.com")).thenReturn(Optional.of(participant));
        List<AppUser> participants = new ArrayList<>();
        Event event = createEvent(1L,"Java Dev Talks #1", DATE_TIME.plusWeeks(1L), participants);
        // when
        boolean isParticipant = eventService.checkIfUserIsParticipant(EventDtoMapper.mapToEventDto(event));
        // then
        assertThat(isParticipant).isFalse();
    }

    @Test
    void shouldThrowExceptionIfUserIsNotFound() {
        // given
        Authentication auth = new UsernamePasswordAuthenticationToken(createAppUserDetails("Patryk", "Kowalski", "patrykkowalski@example.com", "USER"), null);
        SecurityContextHolder.getContext().setAuthentication(auth);
        AppUser participant = createAppUser(2L, "Patryk", "Kowalski", "patrykkowalski@example.com", createUserRole());
        when(appUserRepository.findByEmail(participant.getEmail())).thenReturn(Optional.empty());
        List<AppUser> participants = Arrays.asList(
                participant
        );
        Event event = createEvent(1L,"Java Dev Talks #1", DATE_TIME.plusWeeks(1L), participants);
        // then
        assertThatThrownBy(() -> eventService.checkIfUserIsParticipant(EventDtoMapper.mapToEventDto(event)))
                .isInstanceOf(AppUserNotFoundException.class)
                .hasMessage("User with email " + participant.getEmail() + " not found");
    }

    @Test
    void shouldAddUserToEvent() {
        // given
        Authentication auth = new UsernamePasswordAuthenticationToken(createAppUserDetails("Patryk", "Kowalski", "patrykkowalski@example.com", "USER"), null);
        SecurityContextHolder.getContext().setAuthentication(auth);
        AppUser user = createAppUser(2L, "Patryk", "Kowalski", "patrykkowalski@example.com", createUserRole());
        when(appUserRepository.findByEmail("patrykkowalski@example.com")).thenReturn(Optional.of(user));
        Long eventId = 1L;
        Event event = createEvent(eventId,"Java Dev Talks #1", DATE_TIME.plusWeeks(1L), new ArrayList<>());
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        // when
        eventService.joinToEvent(eventId);
        // then
        Mockito.verify(eventRepository, Mockito.times(1)).findById(eq(eventId));
    }
}