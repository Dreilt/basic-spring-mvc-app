package pl.dreilt.basicspringmvcapp.event;

import pl.dreilt.basicspringmvcapp.event.dto.EventRowDto;

import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class EventRowDtoMapper {

    public static List<EventRowDto> mapToEventRowDtos(List<Event> events) {
        return events
                .stream()
                .map(EventRowDtoMapper::mapToEventRowDto)
                .collect(Collectors.toList());
    }

    private static EventRowDto mapToEventRowDto(Event event) {
        EventRowDto eventRowDto = new EventRowDto();
        eventRowDto.setId(event.getId());
        eventRowDto.setDate(event.getDateAndTime().format(DateTimeFormatter.ofPattern("dd.MM")));
        eventRowDto.setDayOfWeek(event.getDateAndTime().getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.getDefault()));
        eventRowDto.setName(event.getName());
        eventRowDto.setCity(event.getCity());
        eventRowDto.setEventType(event.getEventType());
        eventRowDto.setAdmission(event.getAdmission());
        return eventRowDto;
    }
}
