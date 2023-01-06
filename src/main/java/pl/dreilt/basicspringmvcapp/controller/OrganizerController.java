package pl.dreilt.basicspringmvcapp.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.dreilt.basicspringmvcapp.config.AuthenticatedUserFacade;
import pl.dreilt.basicspringmvcapp.dto.*;
import pl.dreilt.basicspringmvcapp.entity.AppUser;
import pl.dreilt.basicspringmvcapp.enumeration.AdmissionType;
import pl.dreilt.basicspringmvcapp.enumeration.EventType;
import pl.dreilt.basicspringmvcapp.service.EventService;
import pl.dreilt.basicspringmvcapp.service.OrganizerService;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
public class OrganizerController {
    private final EventService eventService;
    private final OrganizerService organizerService;
    private final AuthenticatedUserFacade authenticatedUserFacade;

    public OrganizerController(EventService eventService, OrganizerService organizerService, AuthenticatedUserFacade authenticatedUserFacade) {
        this.eventService = eventService;
        this.organizerService = organizerService;
        this.authenticatedUserFacade = authenticatedUserFacade;
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
            EventDto newEvent = organizerService.createEvent(authenticatedUserFacade.getAuthenticatedUser(), newEventDto);
            return "redirect:/events/" + newEvent.getId();
        }
    }

    @GetMapping("/organizer_panel/events")
    public String getEventsByOrganizer(@RequestParam(name = "city", required = false) String city, Model model) {
        AppUser organizer = authenticatedUserFacade.getAuthenticatedUser();
        List<CityDto> cities = eventService.findAllCities();
        model.addAttribute("cities", cities);
        if (city != null) {
            String cityName = getCityName(cities, city);
            model.addAttribute("cityName", cityName);
            Map<String, List<EventBoxDto>> organizerEvents = organizerService.findEventsByOrganizerAndCity(organizer, cityName);
            model.addAttribute("upcomingEvents", organizerEvents.get("upcomingEvents"));
            model.addAttribute("pastEvents", organizerEvents.get("pastEvents"));
            return "organizer/events";
        }
        Map<String, List<EventBoxDto>> organizerEvents = organizerService.findEventsByOrganizer(organizer);
        model.addAttribute("upcomingEvents", organizerEvents.get("upcomingEvents"));
        model.addAttribute("pastEvents", organizerEvents.get("pastEvents"));
        return "organizer/events";
    }

    @GetMapping("/organizer_panel/events/{id}")
    public String getEvent(@PathVariable Long id, Model model) {
        EventDto organizerEvent = organizerService.findEvent(authenticatedUserFacade.getAuthenticatedUser(), id);
        model.addAttribute("event", organizerEvent);
        return "organizer/event";
    }

    @GetMapping("/organizer_panel/events/{id}/edit")
    public String showEditEventForm(@PathVariable Long id, Model model) {
        model.addAttribute("editEventDto", organizerService.findEventToEdit(authenticatedUserFacade.getAuthenticatedUser(), id));
        model.addAttribute("eventTypeList", Arrays.asList(EventType.values()));
        model.addAttribute("admissionTypeList", Arrays.asList(AdmissionType.values()));
        return "organizer/forms/edit-event-form";
    }

    @PatchMapping("/organizer_panel/events")
    public String updateEvent(@Valid @ModelAttribute("editEventDto") EditEventDto editEventDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "organizer/forms/edit-event-form";
        } else {
            organizerService.updateEvent(authenticatedUserFacade.getAuthenticatedUser(), editEventDto);
            return "redirect:/organizer_panel/events/" + editEventDto.getId();
        }
    }

    @DeleteMapping("/organizer_panel/events/{id}")
    public String deleteEvent(@PathVariable Long id) {
        organizerService.deleteEvent(authenticatedUserFacade.getAuthenticatedUser(), id);
        return "redirect:/organizer_panel/events";
    }

    @GetMapping("/organizer_panel/events/{id}/participants")
    public String getEventParticipantsList(@RequestParam(name = "page", required = false) Integer pageNumber,
                                           @PathVariable Long id,
                                           Model model) {
        int page = pageNumber != null ? pageNumber : 1;
        PageRequest pageRequest = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.fromString("asc"), "lastName"));
        Page<ParticipantDto> participants = organizerService.findEventParticipantsList(authenticatedUserFacade.getAuthenticatedUser(), id, pageRequest);
        model.addAttribute("id", id);
        model.addAttribute("participants", participants);
        return "organizer/participants-list";
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
