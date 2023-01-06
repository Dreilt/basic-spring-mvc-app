package pl.dreilt.basicspringmvcapp.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pl.dreilt.basicspringmvcapp.dto.*;
import pl.dreilt.basicspringmvcapp.entity.AppUser;
import pl.dreilt.basicspringmvcapp.entity.Event;
import pl.dreilt.basicspringmvcapp.entity.EventImage;
import pl.dreilt.basicspringmvcapp.enumeration.AdmissionType;
import pl.dreilt.basicspringmvcapp.enumeration.EventType;
import pl.dreilt.basicspringmvcapp.exception.DefaultEventImageNotFoundException;
import pl.dreilt.basicspringmvcapp.exception.EventNotFoundException;
import pl.dreilt.basicspringmvcapp.mapper.EditEventDtoMapper;
import pl.dreilt.basicspringmvcapp.mapper.EventBoxDtoMapper;
import pl.dreilt.basicspringmvcapp.mapper.EventDtoMapper;
import pl.dreilt.basicspringmvcapp.mapper.ParticipantDtoMapper;
import pl.dreilt.basicspringmvcapp.repository.EventImageRepository;
import pl.dreilt.basicspringmvcapp.repository.OrganizerRepository;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class OrganizerService {
    private final OrganizerRepository organizerRepository;
    private final EventImageRepository eventImageRepository;

    public OrganizerService(OrganizerRepository organizerRepository, EventImageRepository eventImageRepository) {
        this.organizerRepository = organizerRepository;
        this.eventImageRepository = eventImageRepository;
    }

    public EventDto createEvent(AppUser organizer, NewEventDto newEventDto) {
        Event newEvent = new Event();
        newEvent.setName(newEventDto.getName());
        if (newEventDto.getEventImage() == null || newEventDto.getEventImage().isEmpty()) {
            setDefaultEventImage(newEvent);
        } else {
            setEventImage(newEventDto.getEventImage(), newEvent);
        }
        newEvent.setEventType(EventType.valueOf(newEventDto.getEventType().toUpperCase()).getDisplayName());
        newEvent.setDateAndTime(LocalDateTime.parse(newEventDto.getDateAndTime(), DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        newEvent.setLanguage(newEventDto.getLanguage());
        newEvent.setAdmission(AdmissionType.valueOf(newEventDto.getAdmission().toUpperCase()).getDisplayName());
        newEvent.setCity(newEventDto.getCity());
        newEvent.setLocation(newEventDto.getLocation());
        newEvent.setAddress(newEventDto.getAddress());
        newEvent.setOrganizer(organizer);
        newEvent.setDescription(newEventDto.getDescription());
        return EventDtoMapper.mapToEventDto(organizerRepository.save(newEvent));
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

    public Map<String, List<EventBoxDto>> findEventsByOrganizer(AppUser organizer) {
        List<Event> organizerEvents = organizerRepository.findEventsByOrganizer(organizer);
        return createOrganizerEventsMap(organizerEvents);
    }

    public Map<String, List<EventBoxDto>> findEventsByOrganizerAndCity(AppUser organizer, String city) {
        List<Event> organizerEvents = organizerRepository.findEventsByOrganizerAndCity(organizer, city);
        return createOrganizerEventsMap(organizerEvents);
    }

    private Map<String, List<EventBoxDto>> createOrganizerEventsMap(List<Event> organizerEvents) {
        Map<String, List<EventBoxDto>> organizerEventsMap = new HashMap<>();
        List<Event> upcomingEvents = new ArrayList<>();
        List<Event> pastEvents = new ArrayList<>();
        LocalDateTime currentDateTime = LocalDateTime.now();

        for (Event event : organizerEvents) {
            if (event.getDateAndTime().isAfter(currentDateTime)) {
                upcomingEvents.add(event);
            } else {
                pastEvents.add(event);
            }
        }

        organizerEventsMap.put("upcomingEvents", EventBoxDtoMapper.mapToEventBoxDtos(upcomingEvents));
        organizerEventsMap.put("pastEvents", EventBoxDtoMapper.mapToEventBoxDtos(pastEvents));
        return organizerEventsMap;
    }

    public EventDto findEvent(AppUser organizer, Long id) {
        Optional<Event> eventOpt = organizerRepository.findById(id);
        if (eventOpt.isEmpty()) {
            throw new EventNotFoundException("Event with ID " + id + " not found");
        }
        Event event = eventOpt.get();
        if (!organizer.equals(event.getOrganizer())) {
            throw new AccessDeniedException("Nie masz dostępu do tej zawartości");
        }

        return EventDtoMapper.mapToEventDto(event);
    }

    public EditEventDto findEventToEdit(AppUser organizer, Long id) {
        Optional<Event> eventOpt = organizerRepository.findById(id);
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
        Optional<Event> eventOpt = organizerRepository.findById(editEventDto.getId());
        if (eventOpt.isEmpty()) {
            throw new EventNotFoundException("Event with ID " + editEventDto.getId() + " not found");
        }
        Event event = eventOpt.get();
        if (!organizer.equals(event.getOrganizer())) {
            throw new AccessDeniedException("Nie masz dostępu do tej zawartości");
        }

        setEventData(editEventDto, event);
    }

    private void setEventData(EditEventDto source, Event target) {
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
        Optional<Event> eventOpt = organizerRepository.findById(id);
        if (eventOpt.isEmpty()) {
            throw new EventNotFoundException("Event with ID " + id + " not found");
        }
        Event event = eventOpt.get();
        if (!organizer.equals(event.getOrganizer())) {
            throw new AccessDeniedException("Nie masz dostępu do tej zawartości");
        }

        organizerRepository.deleteById(id);
    }

    public Page<ParticipantDto> findEventParticipantsList(AppUser organizer, Long id, Pageable pageable) {
        Optional<Event> eventOpt = organizerRepository.findById(id);
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
