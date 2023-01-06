package pl.dreilt.basicspringmvcapp.service;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dreilt.basicspringmvcapp.dto.CityDto;
import pl.dreilt.basicspringmvcapp.dto.EventBoxDto;
import pl.dreilt.basicspringmvcapp.dto.EventDto;
import pl.dreilt.basicspringmvcapp.entity.AppUser;
import pl.dreilt.basicspringmvcapp.entity.Event;
import pl.dreilt.basicspringmvcapp.exception.EventNotFoundException;
import pl.dreilt.basicspringmvcapp.mapper.EventBoxDtoMapper;
import pl.dreilt.basicspringmvcapp.mapper.EventDtoMapper;
import pl.dreilt.basicspringmvcapp.repository.AppUserRepository;
import pl.dreilt.basicspringmvcapp.repository.EventRepository;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class EventService {
    private final EventRepository eventRepository;
    private final AppUserRepository appUserRepository;

    public EventService(EventRepository eventRepository, AppUserRepository appUserRepository) {
        this.eventRepository = eventRepository;
        this.appUserRepository = appUserRepository;
    }

    public List<CityDto> findAllCities() {
        List<String> cities = eventRepository.findAllCities();
        List<CityDto> cityDtos = new ArrayList<>();
        for (String city : cities) {
            CityDto cityDto = new CityDto();
            cityDto.setNameWithoutPlCharacters(getCityNameWithoutPlCharacters(city));
            cityDto.setDisplayName(city);
            cityDtos.add(cityDto);
        }

        return cityDtos;
    }

    private String getCityNameWithoutPlCharacters(String city) {
        city = city.toLowerCase();
        city = city.replace("\\s", "-");
        city = removeAllPlCharacters(city);

        return city;
    }

    private String removeAllPlCharacters(String city) {
        if (city.contains("ę")) {
            city = city.replace("ę", "e");
        }
        if (city.contains("ó")) {
            city = city.replace("ó", "o");
        }
        if (city.contains("ą")) {
            city = city.replace("ą", "a");
        }
        if (city.contains("ś")) {
            city = city.replace("ś", "s");
        }
        if (city.contains("ł")) {
            city = city.replace("ł", "l");
        }
        if (city.contains("ż")) {
            city = city.replace("ż", "z");
        }
        if (city.contains("ź")) {
            city = city.replace("ź", "z");
        }
        if (city.contains("ć")) {
            city = city.replace("ć", "c");
        }
        if (city.contains("ń")) {
            city = city.replace("ń", "n");
        }
        return city;
    }

    public Map<String, List<EventBoxDto>> findAllEvents() {
        List<Event> events = eventRepository.findAllEvents();
        return createEventsMap(events);
    }

    public Map<String, List<EventBoxDto>> findEventsByCity(String city) {
        List<Event> events = eventRepository.findEventsByCity(city);
        return createEventsMap(events);
    }

    public Map<String, List<EventBoxDto>> findEventsByUser() {
        List<Event> events = eventRepository.findEventsByUser(getAuthenticatedUser());
        return createEventsMap(events);
    }

    public Map<String, List<EventBoxDto>> findEventsByUserAndCity(String city) {
        List<Event> events = eventRepository.findEventsByUserAndCity(getAuthenticatedUser(), city);
        return createEventsMap(events);
    }

    private Map<String, List<EventBoxDto>> createEventsMap(List<Event> events) {
        Map<String, List<EventBoxDto>> eventsMap = new HashMap<>();
        List<Event> upcomingEvents = new ArrayList<>();
        List<Event> pastEvents = new ArrayList<>();
        LocalDateTime currentDateTime = LocalDateTime.now();

        for (Event event : events) {
            if (event.getDateAndTime().isAfter(currentDateTime)) {
                upcomingEvents.add(event);
            } else {
                pastEvents.add(event);
            }
        }

        eventsMap.put("upcomingEvents", EventBoxDtoMapper.mapToEventBoxDtos(upcomingEvents));
        eventsMap.put("pastEvents", EventBoxDtoMapper.mapToEventBoxDtos(pastEvents));
        return eventsMap;
    }

    public EventDto findEvent(Long id) {
        return eventRepository.findById(id)
                .map(EventDtoMapper::mapToEventDto)
                .orElseThrow(() -> new EventNotFoundException("Event with ID " + id + " not found"));
    }

    public boolean checkIfUserIsParticipant(EventDto event) {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        if (currentUser == null || currentUser.getPrincipal().equals("anonymousUser")) {
            throw new AccessDeniedException("Nie masz dostępu do tej zawartości");
        }
        String email = currentUser.getName();
        Optional<AppUser> userOpt = appUserRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            AppUser user = userOpt.get();
            return event.getParticipants().contains(user);
        } else {
            throw new UsernameNotFoundException(String.format("User with email %s not found", email));
        }
    }

    @Transactional
    public void addUserToEventParticipantsList(Long id) {
        Optional<Event> eventOpt = eventRepository.findById(id);
        if (eventOpt.isEmpty()) {
            throw new EventNotFoundException("Event with ID " + id + " not found");
        }
        Event event = eventOpt.get();
        event.getParticipants().add(getAuthenticatedUser());
    }

    @Transactional
    public void removeUserFromEventParticipantsList(Long id) {
        Optional<Event> eventOpt = eventRepository.findById(id);
        if (eventOpt.isEmpty()) {
            throw new EventNotFoundException("Event with ID " + id + " not found");
        }
        Event event = eventOpt.get();
        event.getParticipants().remove(getAuthenticatedUser());
    }

    private AppUser getAuthenticatedUser() {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        if (currentUser == null || currentUser.getPrincipal().equals("anonymousUser")) {
            throw new AccessDeniedException("Nie masz dostępu do tej zawartości");
        }
        String email = currentUser.getName();
        Optional<AppUser> userOpt = appUserRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            throw new UsernameNotFoundException(String.format("User with email %s not found", email));
        }

        return userOpt.get();
    }
}
