package pl.dreilt.basicspringmvcapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.dreilt.basicspringmvcapp.dto.CityDto;
import pl.dreilt.basicspringmvcapp.dto.EditEventDto;
import pl.dreilt.basicspringmvcapp.dto.EventDto;
import pl.dreilt.basicspringmvcapp.dto.NewEventDto;
import pl.dreilt.basicspringmvcapp.enumeration.AdmissionType;
import pl.dreilt.basicspringmvcapp.enumeration.EventType;
import pl.dreilt.basicspringmvcapp.service.EventService;
import pl.dreilt.basicspringmvcapp.service.OrganizerService;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@Controller
public class OrganizerController {
    private final EventService eventService;
    private final OrganizerService organizerService;

    public OrganizerController(EventService eventService, OrganizerService organizerService) {
        this.eventService = eventService;
        this.organizerService = organizerService;
    }

    @GetMapping("/organizer_panel/events/{id}")
    public String getEvent(@PathVariable Long id, Model model) {
        EventDto organizerEvent = organizerService.findEventById(id);
        model.addAttribute("event", organizerEvent);
        return "organizer/event";
    }

    @GetMapping("/events/create_event")
    public String showCreateEventForm(Model model) {
        model.addAttribute("newEventDto", new NewEventDto());
        model.addAttribute("eventTypeList", Arrays.asList(EventType.values()));
        model.addAttribute("admissionTypeList", Arrays.asList(AdmissionType.values()));
        return "organizer/forms/create-event-form";
    }

    @PostMapping("/events")
    public String createEvent(@Valid @ModelAttribute("newEventDto") NewEventDto newEventDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("eventTypeList", Arrays.asList(EventType.values()));
            model.addAttribute("admissionTypeList", Arrays.asList(AdmissionType.values()));
            return "organizer/forms/create-event-form";
        } else {
            EventDto newEvent = organizerService.createEvent(newEventDto);
            return "redirect:/events/" + newEvent.getId();
        }
    }

    @GetMapping("/organizer_panel/events/{id}/edit")
    public String showEditEventForm(@PathVariable Long id, Model model) {
        model.addAttribute("editEventDto", organizerService.findEventToEdit(id));
        model.addAttribute("eventTypeList", Arrays.asList(EventType.values()));
        model.addAttribute("admissionTypeList", Arrays.asList(AdmissionType.values()));
        return "organizer/forms/edit-event-form";
    }

    @PatchMapping("/organizer_panel/events")
    public String updateEvent(@Valid @ModelAttribute("editEventDto") EditEventDto editEventDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "organizer/forms/edit-event-form";
        } else {
            organizerService.updateEvent(editEventDto);
            return "redirect:/organizer_panel/events/" + editEventDto.getId();
        }
    }

    @GetMapping("/organizer_panel/events")
    public String getEventsByOrganizer(@RequestParam(name = "city", required = false) String city, Model model) {
        List<CityDto> cities = eventService.findAllCities();
        model.addAttribute("cities", cities);
        if (city != null) {
            String cityName = getCityName(cities, city);
            model.addAttribute("cityName", cityName);
            model.addAttribute("upcomingEvents", organizerService.findUpcomingEventsByOrganizerAndCity(cityName));
            model.addAttribute("pastEvents", organizerService.findPastEventsByOrganizerAndCity(cityName));
            return "organizer/events";
        }
        model.addAttribute("upcomingEvents", organizerService.findUpcomingEventsByOrganizer());
        model.addAttribute("pastEvents", organizerService.findPastEventsByOrganizer());
        return "organizer/events";
    }

    @DeleteMapping("/organizer_panel/events/{id}")
    public String deleteEvent(@PathVariable Long id) {
        organizerService.deleteEventById(id);
        return "redirect:/organizer_panel/events";
    }

    private String getCityName(List<CityDto> cities, String city) {
        for (CityDto cityDto : cities) {
            if (cityDto.getNameWithoutPlCharacters().equals(city)) {
                return cityDto.getDisplayName();
            }
        }

        return null;
    }
}
