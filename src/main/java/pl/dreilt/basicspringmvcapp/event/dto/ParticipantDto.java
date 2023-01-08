package pl.dreilt.basicspringmvcapp.event.dto;

public class ParticipantDto {
    private String firstName;
    private String lastName;

    private ParticipantDto() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public static class ParticipantDtoBuilder {
        private String firstName;
        private String lastName;

        public ParticipantDtoBuilder() {
        }

        public ParticipantDtoBuilder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public ParticipantDtoBuilder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public ParticipantDto build() {
            ParticipantDto participantDto = new ParticipantDto();
            participantDto.firstName = firstName;
            participantDto.lastName = lastName;
            return participantDto;
        }
    }
}
