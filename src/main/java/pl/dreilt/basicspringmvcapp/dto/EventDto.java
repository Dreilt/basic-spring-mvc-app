package pl.dreilt.basicspringmvcapp.dto;

import pl.dreilt.basicspringmvcapp.entity.AppUser;

import java.util.List;

public class EventDto {
    private Long id;
    private String name;
    private String eventType;
    private String date;
    private String hour;
    private String language;
    private String admission;
    private String city;
    private String location;
    private String address;
    private Long organizerId;
    private String organizerImageType;
    private String organizerImageData;
    private String organizerName;
    private String description;
    private String imageType;
    private String imageData;
    private List<AppUser> participants;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
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

    public Long getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(Long organizerId) {
        this.organizerId = organizerId;
    }

    public String getOrganizerImageType() {
        return organizerImageType;
    }

    public void setOrganizerImageType(String organizerImageType) {
        this.organizerImageType = organizerImageType;
    }

    public String getOrganizerImageData() {
        return organizerImageData;
    }

    public void setOrganizerImageData(String organizerImageData) {
        this.organizerImageData = organizerImageData;
    }

    public String getOrganizerName() {
        return organizerName;
    }

    public void setOrganizerName(String organizerName) {
        this.organizerName = organizerName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public String getImageData() {
        return imageData;
    }

    public void setImageData(String imageData) {
        this.imageData = imageData;
    }

    public List<AppUser> getParticipants() {
        return participants;
    }

    public void setParticipants(List<AppUser> participants) {
        this.participants = participants;
    }
}
