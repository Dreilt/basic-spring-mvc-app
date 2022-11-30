package pl.dreilt.basicspringmvcapp.event.dto;

public class CityDto {
    private String nameWithoutPlCharacters;
    private String displayName;

    public String getNameWithoutPlCharacters() {
        return nameWithoutPlCharacters;
    }

    public void setNameWithoutPlCharacters(String nameWithoutPlCharacters) {
        this.nameWithoutPlCharacters = nameWithoutPlCharacters;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
