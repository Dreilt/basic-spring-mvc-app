package pl.dreilt.basicspringmvcapp.event;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.dreilt.basicspringmvcapp.event.dto.CityDto;
import pl.dreilt.basicspringmvcapp.event.dto.CreateEventDto;
import pl.dreilt.basicspringmvcapp.event.dto.EventDto;
import pl.dreilt.basicspringmvcapp.event.dto.EventRowDto;
import pl.dreilt.basicspringmvcapp.exception.DefaultProfileImageNotFoundException;
import pl.dreilt.basicspringmvcapp.exception.EventNotFoundException;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class EventService {
    private final EventRepository eventRepository;
    private final EventImageRepository eventImageRepository;

    public EventService(EventRepository eventRepository, EventImageRepository eventImageRepository) {
        this.eventRepository = eventRepository;
        this.eventImageRepository = eventImageRepository;
    }

    public void createEvent(CreateEventDto createEventDto) {
        Event newEvent = new Event();
        newEvent.setName(createEventDto.getName());
        newEvent.setEventType(EventType.valueOf(createEventDto.getEventType().toUpperCase()).getDisplayName());
        newEvent.setDateAndTime(LocalDateTime.parse(createEventDto.getDateAndTime(), DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        newEvent.setLanguage(createEventDto.getLanguage());
        newEvent.setAdmission(AdmissionType.valueOf(createEventDto.getAdmission().toUpperCase()).getDisplayName());
        newEvent.setCity(createEventDto.getCity());
        newEvent.setLocation(createEventDto.getLocation());
        newEvent.setAddress(createEventDto.getAddress());
        newEvent.setDescription(createEventDto.getDescription());
        setEventImage(createEventDto.getEventImage(), newEvent);
        eventRepository.save(newEvent);
    }

    private void setEventImage(MultipartFile image, Event event) {
        EventImage eventImage = new EventImage();
        try (InputStream is = image.getInputStream()) {
            if (eventImage.getFileData() != is.readAllBytes()) {
                eventImage.setFileName(image.getOriginalFilename());
                eventImage.setFileType(image.getContentType());
                eventImage.setFileData(is.readAllBytes());
                eventImageRepository.save(eventImage);
                event.setEventImage(eventImage);
            }
        } catch (IOException e) {
            throw new DefaultProfileImageNotFoundException("File not found");
        }
    }

    public List<EventRowDto> findAllUpcomingEvents() {
        LocalDateTime currentDateAndTime = LocalDateTime.now();
        return EventRowDtoMapper.mapToEventRowDtos(eventRepository.findAllUpcomingEvents(currentDateAndTime));
    }

    public List<EventRowDto> findAllPastEvents() {
        LocalDateTime currentDateAndTime = LocalDateTime.now();
        return EventRowDtoMapper.mapToEventRowDtos(eventRepository.findAllPastEvents(currentDateAndTime));
    }

    public List<EventRowDto> findUpcomingEventsByCity(String city) {
        LocalDateTime currentDateAndTime = LocalDateTime.now();
        return EventRowDtoMapper.mapToEventRowDtos(eventRepository.findUpcomingEventsByCity(currentDateAndTime, city));
    }

    public List<EventRowDto> findPastEventsByCity(String city) {
        LocalDateTime currentDateAndTime = LocalDateTime.now();
        return EventRowDtoMapper.mapToEventRowDtos(eventRepository.findPastEventsByCity(currentDateAndTime, city));
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

    public EventDto findEventById(Long id) {
        return eventRepository.findById(id)
                .map(EventDtoMapper::mapToEventDto)
                .orElseThrow(() -> new EventNotFoundException("Event with ID " + id + " not found"));
    }
}
