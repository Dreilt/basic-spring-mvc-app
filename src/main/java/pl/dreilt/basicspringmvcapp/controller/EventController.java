package pl.dreilt.basicspringmvcapp.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.dreilt.basicspringmvcapp.dto.CityDto;
import pl.dreilt.basicspringmvcapp.dto.NewEventDto;
import pl.dreilt.basicspringmvcapp.dto.EventDto;
import pl.dreilt.basicspringmvcapp.enumeration.AdmissionType;
import pl.dreilt.basicspringmvcapp.enumeration.EventType;
import pl.dreilt.basicspringmvcapp.service.EventService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Controller
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/events")
    public String getAllEvents(@RequestParam(name = "city", required = false) String city, Model model) {
        List<CityDto> cities = eventService.findAllCities();
        model.addAttribute("cities", cities);
        if (city != null) {
            String cityName = getCityName(cities, city);
            model.addAttribute("cityName", cityName);
            model.addAttribute("upcomingEvents", eventService.findUpcomingEventsByCity(LocalDateTime.now(), cityName));
            model.addAttribute("pastEvents", eventService.findPastEventsByCity(LocalDateTime.now(), cityName));
            return "events";
        }
        model.addAttribute("upcomingEvents", eventService.findAllUpcomingEvents(LocalDateTime.now()));
        model.addAttribute("pastEvents", eventService.findAllPastEvents(LocalDateTime.now()));
        return "events";
    }

    private String getCityName(List<CityDto> cities, String city) {
        for (CityDto cityDto : cities) {
            if (cityDto.getNameWithoutPlCharacters().equals(city)) {
                return cityDto.getDisplayName();
            }
        }

        return null;
    }

    @GetMapping("/events/{id}")
    public String getEvent(Authentication authentication, @PathVariable Long id, Model model) {
        EventDto event = eventService.findEventById(id);
        model.addAttribute("event", event);
        if (authentication != null) {
            model.addAttribute("isParticipant", eventService.checkIfUserIsParticipant(event));
        }
        return "event";
    }

    @GetMapping("/events/create_event")
    public String showCreateEventForm(Model model) {
        model.addAttribute("newEventDto", new NewEventDto());
        model.addAttribute("eventTypeList", Arrays.asList(EventType.values()));
        model.addAttribute("admissionTypeList", Arrays.asList(AdmissionType.values()));
        return "forms/create-event-form";
    }

    @PostMapping("/events")
    public String createEvent(@Valid @ModelAttribute("newEventDto") NewEventDto newEventDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("eventTypeList", Arrays.asList(EventType.values()));
            model.addAttribute("admissionTypeList", Arrays.asList(AdmissionType.values()));
            return "forms/create-event-form";
        } else {
            EventDto newEvent = eventService.createEvent(newEventDto);
            return "redirect:/events/" + newEvent.getId();
        }
    }

    @PatchMapping("/events/{id}/join")
    public String joinToEvent(@PathVariable Long id, Model model) {
        eventService.joinToEvent(id);
        return "redirect:/events/" + id;
    }

    @GetMapping("/events/my_events")
    public String getEventsByUser(@RequestParam(name = "city", required = false) String city, Model model) {
        List<CityDto> cities = eventService.findAllCities();
        model.addAttribute("cities", cities);
        if (city != null) {
            String cityName = getCityName(cities, city);
            model.addAttribute("cityName", cityName);
            model.addAttribute("upcomingEvents", eventService.findUpcomingEventsByUserAndCity(LocalDateTime.now(), cityName));
            model.addAttribute("pastEvents", eventService.findPastEventsByUserAndCity(LocalDateTime.now(), cityName));
            return "app-user-events";
        }
        model.addAttribute("upcomingEvents", eventService.findUpcomingEventsByUser(LocalDateTime.now()));
        model.addAttribute("pastEvents", eventService.findPastEventsByUser(LocalDateTime.now()));
        return "app-user-events";
    }
}
