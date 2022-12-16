package pl.dreilt.basicspringmvcapp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.dreilt.basicspringmvcapp.dto.CityDto;
import pl.dreilt.basicspringmvcapp.dto.EventBoxDto;
import pl.dreilt.basicspringmvcapp.dto.EventDto;
import pl.dreilt.basicspringmvcapp.entity.AppUser;
import pl.dreilt.basicspringmvcapp.entity.Event;
import pl.dreilt.basicspringmvcapp.exception.AppUserNotFoundException;
import pl.dreilt.basicspringmvcapp.mapper.EventDtoMapper;
import pl.dreilt.basicspringmvcapp.repository.AppUserRepository;
import pl.dreilt.basicspringmvcapp.repository.EventRepository;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static pl.dreilt.basicspringmvcapp.service.EventServiceTestHelper.*;

@ExtendWith(SpringExtension.class)
class EventServiceTest {
    static final Clock fixed = Clock.fixed(Instant.parse("2020-12-27T18:00:00.000Z"), ZoneId.systemDefault());
    static final LocalDateTime DATE_TIME = LocalDateTime.now(fixed);
    static final String userEmail = "jannowak@example.com";
    @Mock
    private EventRepository eventRepository;
    @Mock
    private AppUserRepository appUserRepository;
    @InjectMocks
    private EventService eventService;

    @BeforeEach
    void setUp() {
        Mockito.when(appUserRepository.findByEmail(userEmail)).thenReturn(Optional.of(createUser(2L,"Jan", "Nowak", "jannowak@example.com")));
    }

    @Test
    void shouldGetThreeUpcomingEvents() {
        // given
        List<Event> upcomingEventsList = EventServiceTestHelper.createUpcomingEventsList();
        Mockito.when(eventRepository.findAllUpcomingEvents(DATE_TIME)).thenReturn(upcomingEventsList);
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
        Mockito.when(eventRepository.findAllPastEvents(DATE_TIME)).thenReturn(pastEventsList);
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
        Mockito.when(eventRepository.findUpcomingEventsByCity(DATE_TIME, city)).thenReturn(upcomingEventsList);
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
        Mockito.when(eventRepository.findPastEventsByCity(DATE_TIME, city)).thenReturn(pastEventsList);
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

    // findUpcomingEventsByUser()
    @Test
    @WithMockUser(username = "jannowak@example.com", password = "user1", roles = {"USER"})
    void shouldGetThreeUpcomingUserEvents() {
        // given
        AppUser participant = createUser(2L, "Jan", "Nowak", "jannowak@example.com");
        List<Event> upcomingEventsList = EventServiceTestHelper.createUpcomingEventsList();
        Mockito.when(appUserRepository.findByEmail("jannowak@example.com")).thenReturn(Optional.of(participant));
        Mockito.when(eventRepository.findUpcomingEventsByUser(DATE_TIME, participant)).thenReturn(upcomingEventsList);
        // when
        List<EventBoxDto> upcomingEvents = eventService.findUpcomingEventsByUser(DATE_TIME);
        // then
        assertThat(upcomingEvents).isNotEmpty();
        assertThat(upcomingEvents).hasSize(3);
        assertThat(upcomingEvents.get(0).getDate()).isEqualTo(DATE_TIME.plusWeeks(1L).format(DateTimeFormatter.ofPattern("dd.MM")));
        assertThat(upcomingEvents.get(1).getDate()).isEqualTo(DATE_TIME.plusWeeks(2L).format(DateTimeFormatter.ofPattern("dd.MM")));
        assertThat(upcomingEvents.get(2).getDate()).isEqualTo(DATE_TIME.plusWeeks(3L).format(DateTimeFormatter.ofPattern("dd.MM")));
    }

    // findPastEventsByUser()
    @Test
    @WithMockUser(username = "jannowak@example.com", password = "user1", roles = {"USER"})
    void shouldGetThreePastUserEvents() {
        // given
        AppUser participant = createUser(2L, "Jan", "Nowak", "jannowak@example.com");
        List<Event> pastEventsList = EventServiceTestHelper.createPastEventsList();
        Mockito.when(appUserRepository.findByEmail("jannowak@example.com")).thenReturn(Optional.of(participant));
        Mockito.when(eventRepository.findPastEventsByUser(DATE_TIME, participant)).thenReturn(pastEventsList);
        // when
        List<EventBoxDto> pastEvents = eventService.findPastEventsByUser(DATE_TIME);
        // then
        assertThat(pastEvents).isNotEmpty();
        assertThat(pastEvents).hasSize(3);
        assertThat(pastEvents.get(0).getDate()).isEqualTo(DATE_TIME.minusWeeks(1L).format(DateTimeFormatter.ofPattern("dd.MM")));
        assertThat(pastEvents.get(1).getDate()).isEqualTo(DATE_TIME.minusWeeks(2L).format(DateTimeFormatter.ofPattern("dd.MM")));
        assertThat(pastEvents.get(2).getDate()).isEqualTo(DATE_TIME.minusWeeks(3L).format(DateTimeFormatter.ofPattern("dd.MM")));
    }

    // findUpcomingEventsByUserAndCity()
    @Test
    @WithMockUser(username = "jannowak@example.com", password = "user1", roles = {"USER"})
    void shouldGetThreeUpcomingUserEventsByCity() { // inna nazwa
        // given
        String city = "Rzeszów";
        AppUser participant = createUser(2L, "Jan", "Nowak", "jannowak@example.com");
        List<Event> upcomingEventsList = EventServiceTestHelper.createUpcomingEventsList();
        Mockito.when(appUserRepository.findByEmail("jannowak@example.com")).thenReturn(Optional.of(participant));
        Mockito.when(eventRepository.findUpcomingEventsByUserAndCity(DATE_TIME, participant, city)).thenReturn(upcomingEventsList);
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

    // findPastEventsByUserAndCity()
    @Test
    @WithMockUser(username = "jannowak@example.com", password = "user1", roles = {"USER"})
    void shouldGetThreePastUserEventsByCity() { // inna nazwa
        // given
        String city = "Rzeszów";
        AppUser participant = createUser(2L, "Jan", "Nowak", "jannowak@example.com");
        List<Event> pastEventsList = EventServiceTestHelper.createPastEventsList();
        Mockito.when(appUserRepository.findByEmail("jannowak@example.com")).thenReturn(Optional.of(participant));
        Mockito.when(eventRepository.findPastEventsByUserAndCity(DATE_TIME, participant, city)).thenReturn(pastEventsList);
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

    // checkIfUserIsParticipant()
    @Test
    void shouldThrowExceptionIfUserIsNotAuthenticated2() {
        // given
        AppUser participant = createUser(2L, "Jan", "Nowak", "jannowak@example.com");
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
    @WithMockUser(username = "jannowak@example.com", password = "user1", roles = {"USER"})
    void shouldReturnTrueIfUserIsEventParticipant() {
        // given
        AppUser participant = createUser(2L, "Jan", "Nowak", "jannowak@example.com");
        List<AppUser> participants = Arrays.asList(
                participant
        );
        Event event = createEvent(1L,"Java Dev Talks #1", DATE_TIME.plusWeeks(1L), participants);
        Mockito.when(appUserRepository.findByEmail("jannowak@example.com")).thenReturn(Optional.of(participant));
        // when
        boolean isParticipant = eventService.checkIfUserIsParticipant(EventDtoMapper.mapToEventDto(event));
        // then
        assertThat(isParticipant).isTrue();
    }

    @Test
    @WithMockUser(username = "patrykkowalski@example.com", password = "user1", roles = {"USER"})
    void shouldReturnFalseIfUserIsNotEventParticipant() {
        // given
        AppUser participant = createUser(2L, "Jan", "Nowak", "jannowak@example.com");
        List<AppUser> participants = Arrays.asList(
                participant
        );
        Event event = createEvent(1L,"Java Dev Talks #1", DATE_TIME.plusWeeks(1L), participants);
        AppUser user = createUser(3L, "Patryk", "Kowalski", "patrykkowalski@example.com");
        Mockito.when(appUserRepository.findByEmail("patrykkowalski@example.com")).thenReturn(Optional.of(user));
        // when
        boolean isParticipant = eventService.checkIfUserIsParticipant(EventDtoMapper.mapToEventDto(event));
        // then
        assertThat(isParticipant).isFalse();
    }

    @Test
    @WithMockUser(username = "patrykkowalski@example.com", password = "user1", roles = {"USER"})
    void shouldThrowExceptionIfUserIsNotFound() {
        // given
        String email = "patrykkowalski@example.com";
        AppUser participant = createUser(2L, "Jan", "Nowak", "jannowak@example.com");
        List<AppUser> participants = Arrays.asList(
                participant
        );
        Event event = createEvent(1L,"Java Dev Talks #1", DATE_TIME.plusWeeks(1L), participants);
        Mockito.when(appUserRepository.findByEmail(email)).thenReturn(Optional.empty());
        // then
        assertThatThrownBy(() -> eventService.checkIfUserIsParticipant(EventDtoMapper.mapToEventDto(event)))
                .isInstanceOf(AppUserNotFoundException.class)
                .hasMessage("User with email " + email + " not found");
    }

    @Test
    @WithMockUser(username = "jannowak@example.com", password = "user1", roles = {"USER"})
    void shouldAddUserToEvent() {
        // given
        AppUser user = createUser(3L, "Jan", "Nowak", "jannowak@example.com");
        Mockito.when(appUserRepository.findByEmail("jannowak@example.com")).thenReturn(Optional.of(user));
        Long eventId = 1L;
        Event event = createEvent(eventId,"Java Dev Talks #1", DATE_TIME.plusWeeks(1L), new ArrayList<>());
        Mockito.when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        // when
        eventService.joinToEvent(eventId);
        // then
        Mockito.verify(eventRepository, Mockito.times(1)).findById(eq(eventId));
    }

    @Test
    void shouldGetAllCities() {
        // given
        Mockito.when(eventRepository.findAllCities()).thenReturn(createCitiesList());
        // when
        List<CityDto> cities = eventService.findAllCities();
        // then
        assertThat(cities).isNotEmpty();
        assertThat(cities).hasSize(9);
    }

    @Test
    void shouldGetOneEvent() {
        // given
        Long eventId = 1L;
        Mockito.when(eventRepository.findById(eventId)).thenReturn(Optional.of(createEvent(eventId, "Java Dev Talks #1", DATE_TIME)));
        // when
        EventDto event = eventService.findEventById(eventId);
        // then
        assertThat(event.getId()).isEqualTo(eventId);
    }
}
