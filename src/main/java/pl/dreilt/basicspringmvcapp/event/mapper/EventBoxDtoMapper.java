package pl.dreilt.basicspringmvcapp.event.mapper;

import pl.dreilt.basicspringmvcapp.event.Event;
import pl.dreilt.basicspringmvcapp.event.dto.EventBoxDto;

import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class EventBoxDtoMapper {

    public static List<EventBoxDto> mapToEventBoxDtos(List<Event> events) {
        return events
                .stream()
                .map(EventBoxDtoMapper::mapToEventBoxDto)
                .collect(Collectors.toList());
    }

    private static EventBoxDto mapToEventBoxDto(Event event) {
        EventBoxDto eventBoxDto = new EventBoxDto();
        eventBoxDto.setId(event.getId());
        eventBoxDto.setDate(event.getDateAndTime().format(DateTimeFormatter.ofPattern("dd.MM")));
        eventBoxDto.setDayOfWeek(event.getDateAndTime().getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.getDefault()));
        eventBoxDto.setName(event.getName());
        eventBoxDto.setCity(event.getCity());
        eventBoxDto.setEventType(event.getEventType());
        eventBoxDto.setAdmission(event.getAdmission());
        return eventBoxDto;
    }
}
