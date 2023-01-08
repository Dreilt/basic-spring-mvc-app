package pl.dreilt.basicspringmvcapp.event.enumeration;

public enum EventType {
    MEETING("meeting", "Spotkanie"),
    CONFERENCE("conference", "Konferencja");

    public final String name;
    public final String displayName;

    EventType(String name, String displayName) {
        this.name = name;
        this.displayName = displayName;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }
}
