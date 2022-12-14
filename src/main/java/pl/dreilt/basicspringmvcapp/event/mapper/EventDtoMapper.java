package pl.dreilt.basicspringmvcapp.event.mapper;

import pl.dreilt.basicspringmvcapp.event.Event;
import pl.dreilt.basicspringmvcapp.event.dto.EventDto;

import java.time.format.DateTimeFormatter;
import java.util.Base64;

public class EventDtoMapper {

    public static EventDto mapToEventDto(Event event) {
        EventDto eventDto = new EventDto();
        eventDto.setId(event.getId());
        eventDto.setName(event.getName());
        eventDto.setImageType(event.getEventImage().getFileType());
        eventDto.setImageData(Base64.getEncoder().encodeToString(event.getEventImage().getFileData()));
        eventDto.setEventType(event.getEventType());
        eventDto.setDate(event.getDateAndTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        eventDto.setHour(event.getDateAndTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        eventDto.setLanguage(event.getLanguage());
        eventDto.setAdmission(event.getAdmission());
        eventDto.setCity(event.getCity());
        eventDto.setLocation(event.getLocation());
        eventDto.setAddress(event.getAddress());
        eventDto.setOrganizerId(event.getOrganizer().getId());
        eventDto.setOrganizerImageType(event.getOrganizer().getProfileImage().getFileType());
        eventDto.setOrganizerImageData(Base64.getEncoder().encodeToString(event.getOrganizer().getProfileImage().getFileData()));
        eventDto.setOrganizerName(event.getOrganizer().getFirstName() + " " + event.getOrganizer().getLastName());
        eventDto.setDescription(event.getDescription());
        eventDto.setParticipants(event.getParticipants());
        return eventDto;
    }
}
