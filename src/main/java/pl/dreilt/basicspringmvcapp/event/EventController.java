package pl.dreilt.basicspringmvcapp.event;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.dreilt.basicspringmvcapp.event.dto.CreateEventDto;

import javax.validation.Valid;

@Controller
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

//    @GetMapping("/events")
//    public String getAllEvents() {
//        eventService.findAllEvents();
//        return "index";
//    }

    @GetMapping("/events/create_event")
    public String showCreateEventForm(Model model) {
        model.addAttribute("createEventDto", new CreateEventDto());
        return "forms/create-event-form";
    }

    @PostMapping("/events")
    public String createEvent(@Valid @ModelAttribute("createEventDto") CreateEventDto createEventDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "forms/create-event-form";
        } else {
            eventService.createEvent(createEventDto);
            return "redirect:/events";
        }
    }
}
