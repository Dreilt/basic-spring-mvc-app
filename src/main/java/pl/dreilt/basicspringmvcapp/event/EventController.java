package pl.dreilt.basicspringmvcapp.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.dreilt.basicspringmvcapp.core.AuthenticatedUserFacade;
import pl.dreilt.basicspringmvcapp.event.dto.*;
import pl.dreilt.basicspringmvcapp.event.enumeration.AdmissionType;
import pl.dreilt.basicspringmvcapp.event.enumeration.EventType;
import pl.dreilt.basicspringmvcapp.user.AppUser;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
public class EventController {
    private final EventService eventService;
    private final AuthenticatedUserFacade authenticatedUserFacade;

    public EventController(EventService eventService, AuthenticatedUserFacade authenticatedUserFacade) {
        this.eventService = eventService;
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
            EventDto newEvent = eventService.createEvent(authenticatedUserFacade.getAuthenticatedUser(), newEventDto);
            return "redirect:/events/" + newEvent.getId();
        }
    }

    @GetMapping("/events/{id}")
    public String getEvent(Authentication authentication, @PathVariable Long id, Model model) {
        EventDto event = eventService.findEvent(id);
        model.addAttribute("event", event);
        if (authentication != null) {
            model.addAttribute("isParticipant", eventService.checkIfUserIsParticipant(authenticatedUserFacade.getAuthenticatedUser(), event));
        }
        return "event";
    }

    @GetMapping("/organizer_panel/events/{id}")
    public String getEvent(@PathVariable Long id, Model model) {
        EventDto organizerEvent = eventService.findEvent(authenticatedUserFacade.getAuthenticatedUser(), id);
        model.addAttribute("event", organizerEvent);
        return "organizer/event";
    }

    @GetMapping("/events")
    public String getAllEvents(@RequestParam(name = "city", required = false) String city, Model model) {
        List<CityDto> cities = eventService.findAllCities();
        model.addAttribute("cities", cities);
        if (city != null) {
            String cityName = getCityName(cities, city);
            model.addAttribute("cityName", cityName);
            Map<String, List<EventBoxDto>> events = eventService.findEventsByCity(cityName);
            model.addAttribute("upcomingEvents", events.get("upcomingEvents"));
            model.addAttribute("pastEvents", events.get("pastEvents"));
            return "events";
        }
        Map<String, List<EventBoxDto>> events = eventService.findAllEvents();
        model.addAttribute("upcomingEvents", events.get("upcomingEvents"));
        model.addAttribute("pastEvents", events.get("pastEvents"));
        return "events";
    }

    @GetMapping("/events/my_events")
    public String getEventsByUser(@RequestParam(name = "city", required = false) String city, Model model) {
        AppUser user = authenticatedUserFacade.getAuthenticatedUser();
        List<CityDto> cities = eventService.findAllCities();
        model.addAttribute("cities", cities);
        if (city != null) {
            String cityName = getCityName(cities, city);
            model.addAttribute("cityName", cityName);
            Map<String, List<EventBoxDto>> events = eventService.findEventsByUserAndCity(user, cityName);
            model.addAttribute("upcomingEvents", events.get("upcomingEvents"));
            model.addAttribute("pastEvents", events.get("pastEvents"));
            return "app-user-events";
        }
        Map<String, List<EventBoxDto>> events = eventService.findEventsByUser(user);
        model.addAttribute("upcomingEvents", events.get("upcomingEvents"));
        model.addAttribute("pastEvents", events.get("pastEvents"));
        return "app-user-events";
    }

    @GetMapping("/organizer_panel/events")
    public String getEventsByOrganizer(@RequestParam(name = "city", required = false) String city, Model model) {
        AppUser organizer = authenticatedUserFacade.getAuthenticatedUser();
        List<CityDto> cities = eventService.findAllCities();
        model.addAttribute("cities", cities);
        if (city != null) {
            String cityName = getCityName(cities, city);
            model.addAttribute("cityName", cityName);
            Map<String, List<EventBoxDto>> organizerEvents = eventService.findEventsByOrganizerAndCity(organizer, cityName);
            model.addAttribute("upcomingEvents", organizerEvents.get("upcomingEvents"));
            model.addAttribute("pastEvents", organizerEvents.get("pastEvents"));
            return "organizer/events";
        }
        Map<String, List<EventBoxDto>> organizerEvents = eventService.findEventsByOrganizer(organizer);
        model.addAttribute("upcomingEvents", organizerEvents.get("upcomingEvents"));
        model.addAttribute("pastEvents", organizerEvents.get("pastEvents"));
        return "organizer/events";
    }

    private String getCityName(List<CityDto> cities, String city) {
        for (CityDto cityDto : cities) {
            if (cityDto.getNameWithoutPlCharacters().equals(city)) {
                return cityDto.getDisplayName();
            }
        }

        return null;
    }

    @GetMapping("/organizer_panel/events/{id}/edit")
    public String showEditEventForm(@PathVariable Long id, Model model) {
        model.addAttribute("editEventDto", eventService.findEventToEdit(authenticatedUserFacade.getAuthenticatedUser(), id));
        model.addAttribute("eventTypeList", Arrays.asList(EventType.values()));
        model.addAttribute("admissionTypeList", Arrays.asList(AdmissionType.values()));
        return "organizer/forms/edit-event-form";
    }

    @PatchMapping("/organizer_panel/events")
    public String updateEvent(@Valid @ModelAttribute("editEventDto") EditEventDto editEventDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "organizer/forms/edit-event-form";
        } else {
            eventService.updateEvent(authenticatedUserFacade.getAuthenticatedUser(), editEventDto);
            return "redirect:/organizer_panel/events/" + editEventDto.getId();
        }
    }

    @DeleteMapping("/organizer_panel/events/{id}")
    public String deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(authenticatedUserFacade.getAuthenticatedUser(), id);
        return "redirect:/organizer_panel/events";
    }

    @PatchMapping("/events/{id}/join")
    public String joinToEvent(@PathVariable Long id, Model model) {
        eventService.addUserToEventParticipantsList(authenticatedUserFacade.getAuthenticatedUser(), id);
        return "redirect:/events/" + id;
    }

    @PatchMapping("/events/{id}/disjoin")
    public String disjoinFromEvent(@PathVariable Long id, Model model) {
        eventService.removeUserFromEventParticipantsList(authenticatedUserFacade.getAuthenticatedUser(), id);
        return "redirect:/events/" + id;
    }

    @GetMapping("/organizer_panel/events/{id}/participants")
    public String getEventParticipantsList(@RequestParam(name = "page", required = false) Integer pageNumber,
                                           @PathVariable Long id,
                                           Model model) {
        int page = pageNumber != null ? pageNumber : 1;
        PageRequest pageRequest = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.fromString("asc"), "lastName"));
        Page<ParticipantDto> participants = eventService.findEventParticipantsList(authenticatedUserFacade.getAuthenticatedUser(), id, pageRequest);
        model.addAttribute("id", id);
        model.addAttribute("participants", participants);
        return "organizer/participants-list";
    }
}
