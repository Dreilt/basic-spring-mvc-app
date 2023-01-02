package pl.dreilt.basicspringmvcapp.mapper;

import pl.dreilt.basicspringmvcapp.dto.EditEventDto;
import pl.dreilt.basicspringmvcapp.entity.Event;

import java.util.Base64;

public class EditEventDtoMapper {

    public static EditEventDto mapToEditEventDto(Event event) {
        EditEventDto editEventDto = new EditEventDto();
        editEventDto.setId(event.getId());
        editEventDto.setName(event.getName());

        editEventDto.setEventImageType(event.getEventImage().getFileType());
        editEventDto.setEventImageData(Base64.getEncoder().encodeToString(event.getEventImage().getFileData()));

        editEventDto.setEventType(event.getEventType());
        editEventDto.setDateAndTime(event.getDateAndTime().toString());
        editEventDto.setLanguage(event.getLanguage());
        editEventDto.setAdmission(event.getAdmission());
        editEventDto.setCity(event.getCity());
        editEventDto.setLocation(event.getLocation());
        editEventDto.setAddress(event.getAddress());
        editEventDto.setDescription(event.getDescription());
        return editEventDto;
    }
}
