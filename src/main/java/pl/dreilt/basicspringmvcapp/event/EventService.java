package pl.dreilt.basicspringmvcapp.event;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pl.dreilt.basicspringmvcapp.ei.EventImage;
import pl.dreilt.basicspringmvcapp.ei.EventImageRepository;
import pl.dreilt.basicspringmvcapp.event.dto.*;
import pl.dreilt.basicspringmvcapp.event.enumeration.AdmissionType;
import pl.dreilt.basicspringmvcapp.event.enumeration.EventType;
import pl.dreilt.basicspringmvcapp.event.mapper.EditEventDtoMapper;
import pl.dreilt.basicspringmvcapp.event.mapper.EventBoxDtoMapper;
import pl.dreilt.basicspringmvcapp.event.mapper.EventDtoMapper;
import pl.dreilt.basicspringmvcapp.event.mapper.ParticipantDtoMapper;
import pl.dreilt.basicspringmvcapp.exception.DefaultEventImageNotFoundException;
import pl.dreilt.basicspringmvcapp.exception.EventNotFoundException;
import pl.dreilt.basicspringmvcapp.user.AppUser;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class EventService {
    private final EventRepository eventRepository;
    private final EventImageRepository eventImageRepository;

    public EventService(EventRepository eventRepository, EventImageRepository eventImageRepository) {
        this.eventRepository = eventRepository;
        this.eventImageRepository = eventImageRepository;
    }

    public EventDto createEvent(AppUser organizer, NewEventDto newEvent) {
        Event event = new Event();
        event.setName(newEvent.getName());
        if (newEvent.getEventImage() == null || newEvent.getEventImage().isEmpty()) {
            setDefaultEventImage(event);
        } else {
            setEventImage(newEvent.getEventImage(), event);
        }
        event.setEventType(EventType.valueOf(newEvent.getEventType().toUpperCase()).getDisplayName());
        event.setDateAndTime(LocalDateTime.parse(newEvent.getDateAndTime(), DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        event.setLanguage(newEvent.getLanguage());
        event.setAdmission(AdmissionType.valueOf(newEvent.getAdmission().toUpperCase()).getDisplayName());
        event.setCity(newEvent.getCity());
        event.setLocation(newEvent.getLocation());
        event.setAddress(newEvent.getAddress());
        event.setOrganizer(organizer);
        event.setDescription(newEvent.getDescription());
        return EventDtoMapper.mapToEventDto(eventRepository.save(event));
    }

    private void setDefaultEventImage(Event event) {
        ClassPathResource resource = new ClassPathResource("static/images/default_profile_image.png");
        try (InputStream defaultEventImage = resource.getInputStream()) {
            EventImage eventImage = new EventImage();
            eventImage.setFileName(resource.getFilename());
            eventImage.setFileType("image/png");
            eventImage.setFileData(defaultEventImage.readAllBytes());
            eventImageRepository.save(eventImage);
            event.setEventImage(eventImage);
        } catch (IOException e) {
            throw new DefaultEventImageNotFoundException("File " + resource.getPath() + " not found");
        }
    }

    private void setEventImage(MultipartFile image, Event event) {
        EventImage eventImage = new EventImage();
        try (InputStream is = image.getInputStream()) {
            if (eventImage.getFileData() != is.readAllBytes()) {
                eventImage.setFileName(image.getOriginalFilename());
                eventImage.setFileType(image.getContentType());
                eventImage.setFileData(image.getBytes());
                eventImageRepository.save(eventImage);
                event.setEventImage(eventImage);
            }
        } catch (IOException e) {
            throw new DefaultEventImageNotFoundException("File not found");
        }
    }

    public EventDto findEvent(Long id) {
        return eventRepository.findById(id)
                .map(EventDtoMapper::mapToEventDto)
                .orElseThrow(() -> new EventNotFoundException("Event with ID " + id + " not found"));
    }

    public boolean checkIfUserIsParticipant(AppUser user, EventDto event) {
        return event.getParticipants().contains(user);
    }

    public EventDto findEvent(AppUser organizer, Long id) {
        Optional<Event> eventOpt = eventRepository.findById(id);
        if (eventOpt.isEmpty()) {
            throw new EventNotFoundException("Event with ID " + id + " not found");
        }
        Event event = eventOpt.get();
        if (!organizer.equals(event.getOrganizer())) {
            throw new AccessDeniedException("Nie masz dostępu do tej zawartości");
        }

        return EventDtoMapper.mapToEventDto(event);
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

    public Map<String, List<EventBoxDto>> findEventsByUser(AppUser user) {
        List<Event> events = eventRepository.findEventsByUser(user);
        return createEventsMap(events);
    }

    public Map<String, List<EventBoxDto>> findEventsByUserAndCity(AppUser user, String city) {
        List<Event> events = eventRepository.findEventsByUserAndCity(user, city);
        return createEventsMap(events);
    }

    public Map<String, List<EventBoxDto>> findEventsByOrganizer(AppUser organizer) {
        List<Event> organizerEvents = eventRepository.findEventsByOrganizer(organizer);
        return createEventsMap(organizerEvents);
    }

    public Map<String, List<EventBoxDto>> findEventsByOrganizerAndCity(AppUser organizer, String city) {
        List<Event> organizerEvents = eventRepository.findEventsByOrganizerAndCity(organizer, city);
        return createEventsMap(organizerEvents);
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

    public EditEventDto findEventToEdit(AppUser organizer, Long id) {
        Optional<Event> eventOpt = eventRepository.findById(id);
        if (eventOpt.isEmpty()) {
            throw new EventNotFoundException("Event with ID " + id + " not found");
        }
        Event event = eventOpt.get();
        if (!organizer.equals(event.getOrganizer())) {
            throw new AccessDeniedException("Nie masz dostępu do tej zawartości");
        }

        return EditEventDtoMapper.mapToEditEventDto(event);
    }

    @Transactional
    public void updateEvent(AppUser organizer, EditEventDto editEventDto) {
        Optional<Event> eventOpt = eventRepository.findById(editEventDto.getId());
        if (eventOpt.isEmpty()) {
            throw new EventNotFoundException("Event with ID " + editEventDto.getId() + " not found");
        }
        Event event = eventOpt.get();
        if (!organizer.equals(event.getOrganizer())) {
            throw new AccessDeniedException("Nie masz dostępu do tej zawartości");
        }

        setEventFields(editEventDto, event);
    }

    private void setEventFields(EditEventDto source, Event target) {
        if (source.getName() != null && !source.getName().equals(target.getName())) {
            target.setName(source.getName());
        }
        if (!source.getEventImage().isEmpty()) {
            setEventImage(source.getEventImage(), target);
        }
        if (source.getDateAndTime() != null && !source.getDateAndTime().equals(target.getDateAndTime().toString())) {
            target.setDateAndTime(LocalDateTime.parse(source.getDateAndTime(), DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        }
        if (source.getEventType() != null && !EventType.valueOf(source.getEventType().toUpperCase()).getDisplayName().equals(target.getEventType())) {
            target.setEventType(EventType.valueOf(source.getEventType().toUpperCase()).getDisplayName());
        }
        if (source.getLanguage() != null && !source.getLanguage().equals(target.getLanguage())) {
            target.setLanguage(source.getLanguage());
        }
        if (source.getAdmission() != null && !AdmissionType.valueOf(source.getAdmission().toUpperCase()).getDisplayName().equals(target.getAdmission())) {
            target.setAdmission(AdmissionType.valueOf(source.getAdmission().toUpperCase()).getDisplayName());
        }
        if (source.getCity() != null && !source.getCity().equals(target.getCity())) {
            target.setCity(source.getCity());
        }
        if (source.getLocation() != null && !source.getLocation().equals(target.getLocation())) {
            target.setLocation(source.getLocation());
        }
        if (source.getAddress() != null && !source.getAddress().equals(target.getAddress())) {
            target.setAddress(source.getAddress());
        }
        if (source.getDescription() != null && !source.getDescription().equals(target.getDescription())) {
            target.setDescription(source.getDescription());
        }
    }

    public void deleteEvent(AppUser organizer, Long id) {
        Optional<Event> eventOpt = eventRepository.findById(id);
        if (eventOpt.isEmpty()) {
            throw new EventNotFoundException("Event with ID " + id + " not found");
        }
        Event event = eventOpt.get();
        if (!organizer.equals(event.getOrganizer())) {
            throw new AccessDeniedException("Nie masz dostępu do tej zawartości");
        }

        eventRepository.deleteById(id);
    }

    @Transactional
    public void addUserToEventParticipantsList(AppUser user, Long id) {
        Optional<Event> eventOpt = eventRepository.findById(id);
        if (eventOpt.isEmpty()) {
            throw new EventNotFoundException("Event with ID " + id + " not found");
        }
        Event event = eventOpt.get();
        event.getParticipants().add(user);
    }

    @Transactional
    public void removeUserFromEventParticipantsList(AppUser user, Long id) {
        Optional<Event> eventOpt = eventRepository.findById(id);
        if (eventOpt.isEmpty()) {
            throw new EventNotFoundException("Event with ID " + id + " not found");
        }
        Event event = eventOpt.get();
        event.getParticipants().remove(user);
    }

    public Page<ParticipantDto> findEventParticipantsList(AppUser organizer, Long id, Pageable pageable) {
        Optional<Event> eventOpt = eventRepository.findById(id);
        if (eventOpt.isEmpty()) {
            throw new EventNotFoundException("Event with ID " + id + " not found");
        }
        Event event = eventOpt.get();
        if (!organizer.equals(event.getOrganizer())) {
            throw new AccessDeniedException("Nie masz dostępu do tej zawartości");
        }

        return ParticipantDtoMapper.mapToParticipantDtos(event.getParticipants(), pageable);
    }
}
