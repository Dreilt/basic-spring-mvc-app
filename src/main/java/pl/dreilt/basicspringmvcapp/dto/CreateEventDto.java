package pl.dreilt.basicspringmvcapp.dto;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class CreateEventDto {

    private String name;
    @NotNull(message = "{form.field.eventType.error.notNull.message}")
    @NotEmpty(message = "{form.field.eventType.error.notEmpty.message}")
    private String eventType;
    private String dateAndTime;
    private String language;
    @NotNull(message = "{form.field.admission.error.notNull.message}")
    @NotEmpty(message = "{form.field.admission.error.notEmpty.message}")
    private String admission;
    private String city;
    private String location;
    private String address;
    private String description;

    private MultipartFile eventImage;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public MultipartFile getEventImage() {
        return eventImage;
    }

    public void setEventImage(MultipartFile eventImage) {
        this.eventImage = eventImage;
    }
}
