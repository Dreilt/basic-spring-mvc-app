package pl.dreilt.basicspringmvcapp.event;

import org.springframework.stereotype.Service;
import pl.dreilt.basicspringmvcapp.event.dto.CreateEventDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class EventService {
    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public void createEvent(CreateEventDto createEventDto) {
        Event newEvent = new Event();
        newEvent.setTypeOfEvent(createEventDto.getTypeOfEvent());
        newEvent.setName(createEventDto.getName());
        newEvent.setDescription(createEventDto.getDescription());
        newEvent.setDateAndTime(LocalDateTime.parse(createEventDto.getDateAndTime(), DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        newEvent.setCity(createEventDto.getCity());
        newEvent.setLocation(createEventDto.getLocation());
        eventRepository.save(newEvent);
    }
}
