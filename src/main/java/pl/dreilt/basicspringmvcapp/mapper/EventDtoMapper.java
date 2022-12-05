package pl.dreilt.basicspringmvcapp.mapper;

import pl.dreilt.basicspringmvcapp.entity.Event;
import pl.dreilt.basicspringmvcapp.dto.EventDto;

import java.time.format.DateTimeFormatter;
import java.util.Base64;

public class EventDtoMapper {

    public static EventDto mapToEventDto(Event event) {
        EventDto eventDto = new EventDto();
        eventDto.setId(event.getId());
        eventDto.setName(event.getName());
        eventDto.setEventType(event.getEventType());
        eventDto.setDate(event.getDateAndTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        eventDto.setHour(event.getDateAndTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        eventDto.setLanguage(event.getLanguage());
        eventDto.setAdmission(event.getAdmission());
        eventDto.setCity(event.getCity());
        eventDto.setLocation(event.getLocation());
        eventDto.setAddress(event.getAddress());
        eventDto.setDescription(event.getDescription());
        eventDto.setImageType(event.getEventImage().getFileType());
        String imageData = Base64.getEncoder().encodeToString(event.getEventImage().getFileData());
        eventDto.setImageData(imageData);
        return eventDto;
    }
}
