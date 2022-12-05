package pl.dreilt.basicspringmvcapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.dreilt.basicspringmvcapp.enums.AdmissionType;
import pl.dreilt.basicspringmvcapp.enums.EventType;
import pl.dreilt.basicspringmvcapp.service.EventService;
import pl.dreilt.basicspringmvcapp.dto.CityDto;
import pl.dreilt.basicspringmvcapp.dto.CreateEventDto;
import pl.dreilt.basicspringmvcapp.dto.EventRowDto;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@Controller
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/events")
    public String getAllEvents(Model model) {
        List<CityDto> cities = eventService.findAllCities();
        model.addAttribute("cities", cities);
        List<EventRowDto> upcomingEvents = eventService.findAllUpcomingEvents();
        model.addAttribute("upcomingEvents", upcomingEvents);
        List<EventRowDto> pastEvents = eventService.findAllPastEvents();
        model.addAttribute("pastEvents", pastEvents);
        return "events";
    }

    @GetMapping("/events/cities/{city}")
    public String getEventsByCity(@PathVariable String city, Model model) {
        List<CityDto> cities = eventService.findAllCities();
        model.addAttribute("cities", cities);
        String cityName = getCityName(cities, city);
        model.addAttribute("cityName", cityName);
        List<EventRowDto> upcomingEvents = eventService.findUpcomingEventsByCity(cityName);
        model.addAttribute("upcomingEvents", upcomingEvents);
        List<EventRowDto> pastEvents = eventService.findPastEventsByCity(cityName);
        model.addAttribute("pastEvents", pastEvents);
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
    public String getEvent(@PathVariable Long id, Model model) {
        model.addAttribute("event", eventService.findEventById(id));
        return "event";
    }

    @GetMapping("/events/create_event")
    public String showCreateEventForm(Model model) {
        model.addAttribute("createEventDto", new CreateEventDto());
        model.addAttribute("eventTypeList", Arrays.asList(EventType.values()));
        model.addAttribute("admissionTypeList", Arrays.asList(AdmissionType.values()));
        return "forms/create-event-form";
    }

    @PostMapping("/events")
    public String createEvent(@Valid @ModelAttribute("createEventDto") CreateEventDto createEventDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("eventTypeList", Arrays.asList(EventType.values()));
            model.addAttribute("admissionTypeList", Arrays.asList(AdmissionType.values()));
            return "forms/create-event-form";
        } else {
            eventService.createEvent(createEventDto);
            return "redirect:/events";
        }
    }

    @PatchMapping("/events/{id}/join")
    public String joinToEvent(@PathVariable Long id, Model model) {
        eventService.joinToEvent(id);
        return "redirect:/events/" + id;
    }
}
