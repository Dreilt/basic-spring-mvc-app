package pl.dreilt.basicspringmvcapp.event.dto;

import org.springframework.web.multipart.MultipartFile;
import pl.dreilt.basicspringmvcapp.core.Image;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class NewEventDto {
    @NotNull(message = "{form.field.name.error.notNull.message}")
    @NotEmpty(message = "{form.field.name.error.notEmpty.message}")
    private String name;
    @Image(width = 480, height = 270)
    private MultipartFile eventImage;
    @NotNull(message = "{form.field.eventType.error.notNull.message}")
    @NotEmpty(message = "{form.field.eventType.error.notEmpty.message}")
    private String eventType;
    @NotNull(message = "{form.field.dateAndTime.error.notNull.message}")
    @NotEmpty(message = "{form.field.dateAndTime.error.notEmpty.message}")
    private String dateAndTime;
    @NotNull(message = "{form.field.language.error.notNull.message}")
    @NotEmpty(message = "{form.field.language.error.notEmpty.message}")
    private String language;
    @NotNull(message = "{form.field.admission.error.notNull.message}")
    @NotEmpty(message = "{form.field.admission.error.notEmpty.message}")
    private String admission;
    @NotNull(message = "{form.field.city.error.notNull.message}")
    @NotEmpty(message = "{form.field.city.error.notEmpty.message}")
    private String city;
    @NotNull(message = "{form.field.location.error.notNull.message}")
    @NotEmpty(message = "{form.field.location.error.notEmpty.message}")
    private String location;
    @NotNull(message = "{form.field.address.error.notNull.message}")
    @NotEmpty(message = "{form.field.address.error.notEmpty.message}")
    private String address;
    @NotNull(message = "{form.field.description.error.notNull.message}")
    @NotEmpty(message = "{form.field.description.error.notEmpty.message}")
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MultipartFile getEventImage() {
        return eventImage;
    }

    public void setEventImage(MultipartFile eventImage) {
        this.eventImage = eventImage;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getAdmission() {
        return admission;
    }

    public void setAdmission(String admission) {
        this.admission = admission;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static class NewEventDtoBuilder {
        private String name;
        private MultipartFile eventImage;
        private String eventType;
        private String dateAndTime;
        private String language;
        private String admission;
        private String city;
        private String location;
        private String address;
        private String description;

        public NewEventDtoBuilder() {
        }

        public NewEventDtoBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public NewEventDtoBuilder withEventImage(MultipartFile eventImage) {
            this.eventImage = eventImage;
            return this;
        }

        public NewEventDtoBuilder withEventType(String eventType) {
            this.eventType = eventType;
            return this;
        }

        public NewEventDtoBuilder withDateAndTime(String dateAndTime) {
            this.dateAndTime = dateAndTime;
            return this;
        }

        public NewEventDtoBuilder withLanguage(String language) {
            this.language = language;
            return this;
        }

        public NewEventDtoBuilder withAdmission(String admission) {
            this.admission = admission;
            return this;
        }

        public NewEventDtoBuilder withCity(String city) {
            this.city = city;
            return this;
        }

        public NewEventDtoBuilder withLocation(String location) {
            this.location = location;
            return this;
        }

        public NewEventDtoBuilder withAddress(String address) {
            this.address = address;
            return this;
        }

        public NewEventDtoBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public NewEventDto build() {
            NewEventDto newEventDto = new NewEventDto();
            newEventDto.name = name;
            newEventDto.eventImage = eventImage;
            newEventDto.eventType = eventType;
            newEventDto.dateAndTime = dateAndTime;
            newEventDto.language = language;
            newEventDto.admission = admission;
            newEventDto.city = city;
            newEventDto.location = location;
            newEventDto.address = address;
            newEventDto.description = description;
            return newEventDto;
        }
    }
}
