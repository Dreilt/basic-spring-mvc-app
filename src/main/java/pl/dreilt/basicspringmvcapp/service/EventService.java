package pl.dreilt.basicspringmvcapp.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pl.dreilt.basicspringmvcapp.dto.CityDto;
import pl.dreilt.basicspringmvcapp.dto.EventBoxDto;
import pl.dreilt.basicspringmvcapp.dto.EventDto;
import pl.dreilt.basicspringmvcapp.dto.NewEventDto;
import pl.dreilt.basicspringmvcapp.entity.AppUser;
import pl.dreilt.basicspringmvcapp.entity.Event;
import pl.dreilt.basicspringmvcapp.entity.EventImage;
import pl.dreilt.basicspringmvcapp.enumeration.AdmissionType;
import pl.dreilt.basicspringmvcapp.enumeration.EventType;
import pl.dreilt.basicspringmvcapp.exception.AppUserNotFoundException;
import pl.dreilt.basicspringmvcapp.exception.DefaultProfileImageNotFoundException;
import pl.dreilt.basicspringmvcapp.exception.EventNotFoundException;
import pl.dreilt.basicspringmvcapp.mapper.EventBoxDtoMapper;
import pl.dreilt.basicspringmvcapp.mapper.EventDtoMapper;
import pl.dreilt.basicspringmvcapp.repository.AppUserRepository;
import pl.dreilt.basicspringmvcapp.repository.EventImageRepository;
import pl.dreilt.basicspringmvcapp.repository.EventRepository;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {
    private final EventRepository eventRepository;
    private final EventImageRepository eventImageRepository;
    private final AppUserRepository appUserRepository;

    public EventService(EventRepository eventRepository, EventImageRepository eventImageRepository, AppUserRepository appUserRepository) {
        this.eventRepository = eventRepository;
        this.eventImageRepository = eventImageRepository;
        this.appUserRepository = appUserRepository;
    }

    public EventDto findEventById(Long id) {
        return eventRepository.findById(id)
                .map(EventDtoMapper::mapToEventDto)
                .orElseThrow(() -> new EventNotFoundException("Event with ID " + id + " not found"));
    }

    public EventDto createEvent(NewEventDto newEventDto) {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        if (currentUser == null || currentUser.getPrincipal().equals("anonymousUser")) {
            throw new AccessDeniedException("Odmowa dostępu");
        }
        String email = currentUser.getName();
        Optional<AppUser> userOpt = appUserRepository.findByEmail(email);
        if (userOpt.isPresent()) {
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
            newEvent.setOrganizer(userOpt.get());
            newEvent.setDescription(newEventDto.getDescription());
            return EventDtoMapper.mapToEventDto(eventRepository.save(newEvent));
        } else {
            throw new AppUserNotFoundException("User with email " + email + " not found");
        }
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
            throw new DefaultProfileImageNotFoundException("File " + resource.getPath() + " not found");
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
            throw new DefaultProfileImageNotFoundException("File not found");
        }
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
        if (city.contains(" ")) {
            city = city.replace(" ", "-");
        }
        city = deleteAllPlCharacters(city);

        return city;
    }

    private String deleteAllPlCharacters(String word) {
        if (word.contains("ę")) {
            word = word.replace("ę", "e");
        }
        if (word.contains("ó")) {
            word = word.replace("ó", "o");
        }
        if (word.contains("ą")) {
            word = word.replace("ą", "a");
        }
        if (word.contains("ś")) {
            word = word.replace("ś", "s");
        }
        if (word.contains("ł")) {
            word = word.replace("ł", "l");
        }
        if (word.contains("ż")) {
            word = word.replace("ż", "z");
        }
        if (word.contains("ź")) {
            word = word.replace("ź", "z");
        }
        if (word.contains("ć")) {
            word = word.replace("ć", "c");
        }
        if (word.contains("ń")) {
            word = word.replace("ń", "n");
        }
        return word;
    }

    public List<EventBoxDto> findAllUpcomingEvents(LocalDateTime currentDateAndTime) {
        return EventBoxDtoMapper.mapToEventBoxDtos(eventRepository.findAllUpcomingEvents(currentDateAndTime));
    }

    public List<EventBoxDto> findAllPastEvents(LocalDateTime currentDateAndTime) {
        return EventBoxDtoMapper.mapToEventBoxDtos(eventRepository.findAllPastEvents(currentDateAndTime));
    }

    public List<EventBoxDto> findUpcomingEventsByCity(LocalDateTime currentDateAndTime, String city) {
        return EventBoxDtoMapper.mapToEventBoxDtos(eventRepository.findUpcomingEventsByCity(currentDateAndTime, city));
    }

    public List<EventBoxDto> findPastEventsByCity(LocalDateTime currentDateAndTime, String city) {
        return EventBoxDtoMapper.mapToEventBoxDtos(eventRepository.findPastEventsByCity(currentDateAndTime, city));
    }

    public List<EventBoxDto> findUpcomingEventsByUser(LocalDateTime currentDateAndTime) {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        if (currentUser == null || currentUser.getPrincipal().equals("anonymousUser")) {
            throw new AccessDeniedException("Odmowa dostępu");
        }
        String email = currentUser.getName();
        Optional<AppUser> userOpt = appUserRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            AppUser user = userOpt.get();
            return EventBoxDtoMapper.mapToEventBoxDtos(eventRepository.findUpcomingEventsByUser(currentDateAndTime, user));
        } else {
            throw new AppUserNotFoundException("User with email " + email + " not found");
        }
    }

    public List<EventBoxDto> findPastEventsByUser(LocalDateTime currentDateAndTime) {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        if (currentUser == null || currentUser.getPrincipal().equals("anonymousUser")) {
            throw new AccessDeniedException("Odmowa dostępu");
        }
        String email = currentUser.getName();
        Optional<AppUser> userOpt = appUserRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            AppUser user = userOpt.get();
            return EventBoxDtoMapper.mapToEventBoxDtos(eventRepository.findPastEventsByUser(currentDateAndTime, user));
        } else {
            throw new AppUserNotFoundException("User with email " + email + " not found");
        }
    }

    public List<EventBoxDto> findUpcomingEventsByUserAndCity(LocalDateTime currentDateAndTime, String city) {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        if (currentUser == null || currentUser.getPrincipal().equals("anonymousUser")) {
            throw new AccessDeniedException("Odmowa dostępu");
        }
        String email = currentUser.getName();
        Optional<AppUser> userOpt = appUserRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            AppUser user = userOpt.get();
            return EventBoxDtoMapper.mapToEventBoxDtos(eventRepository.findUpcomingEventsByUserAndCity(currentDateAndTime, user, city));
        } else {
            throw new AppUserNotFoundException("User with email " + email + " not found");
        }
    }

    public List<EventBoxDto> findPastEventsByUserAndCity(LocalDateTime currentDateAndTime, String city) {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        if (currentUser == null || currentUser.getPrincipal().equals("anonymousUser")) {
            throw new AccessDeniedException("Odmowa dostępu");
        }
        String email = currentUser.getName();
        Optional<AppUser> userOpt = appUserRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            AppUser user = userOpt.get();
            return EventBoxDtoMapper.mapToEventBoxDtos(eventRepository.findPastEventsByUserAndCity(currentDateAndTime, user, city));
        } else {
            throw new AppUserNotFoundException("User with email " + email + " not found");
        }
    }

    public boolean checkIfUserIsParticipant(EventDto event) {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        if (currentUser == null || currentUser.getPrincipal().equals("anonymousUser")) {
            throw new AccessDeniedException("Odmowa dostępu");
        }
        String email = currentUser.getName();
        Optional<AppUser> userOpt = appUserRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            AppUser user = userOpt.get();
            return event.getParticipants().contains(user);
        } else {
            throw new AppUserNotFoundException("User with email " + email + " not found");
        }
    }

    @Transactional
    public void joinToEvent(Long eventId) {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        if (currentUser == null || currentUser.getPrincipal().equals("anonymousUser")) {
            throw new AccessDeniedException("Odmowa dostępu");
        }
        String email = currentUser.getName();
        Optional<AppUser> userOpt = appUserRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            AppUser user = userOpt.get();
            addUserToEvent(eventId, user);
        } else {
            throw new AppUserNotFoundException("User with email " + email + " not found");
        }
    }

    private void addUserToEvent(Long eventId, AppUser user) {
        Optional<Event> eventOpt = eventRepository.findById(eventId);
        if (eventOpt.isPresent()) {
            Event event = eventOpt.get();
            event.getParticipants().add(user);
        } else {
            throw new EventNotFoundException("Event with ID " + eventId + " not found");
        }
    }
}
