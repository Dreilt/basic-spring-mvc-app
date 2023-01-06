package pl.dreilt.basicspringmvcapp.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import pl.dreilt.basicspringmvcapp.dto.CityDto;
import pl.dreilt.basicspringmvcapp.dto.EventBoxDto;
import pl.dreilt.basicspringmvcapp.dto.EventDto;
import pl.dreilt.basicspringmvcapp.service.EventService;

import java.util.List;
import java.util.Map;

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
        List<CityDto> cities = eventService.findAllCities();
        model.addAttribute("cities", cities);
        if (city != null) {
            String cityName = getCityName(cities, city);
            model.addAttribute("cityName", cityName);
            Map<String, List<EventBoxDto>> events = eventService.findEventsByUserAndCity(cityName);
            model.addAttribute("upcomingEvents", events.get("upcomingEvents"));
            model.addAttribute("pastEvents", events.get("pastEvents"));
            return "app-user-events";
        }
        Map<String, List<EventBoxDto>> events = eventService.findEventsByUser();
        model.addAttribute("upcomingEvents", events.get("upcomingEvents"));
        model.addAttribute("pastEvents", events.get("pastEvents"));
        return "app-user-events";
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
        EventDto event = eventService.findEvent(id);
        model.addAttribute("event", event);
        if (authentication != null) {
            model.addAttribute("isParticipant", eventService.checkIfUserIsParticipant(event));
        }
        return "event";
    }

    @PatchMapping("/events/{id}/join")
    public String joinToEvent(@PathVariable Long id, Model model) {
        eventService.addUserToEventParticipantsList(id);
        return "redirect:/events/" + id;
    }

    @PatchMapping("/events/{id}/disjoin")
    public String disjoinFromEvent(@PathVariable Long id, Model model) {
        eventService.removeUserFromEventParticipantsList(id);
        return "redirect:/events/" + id;
    }
}
