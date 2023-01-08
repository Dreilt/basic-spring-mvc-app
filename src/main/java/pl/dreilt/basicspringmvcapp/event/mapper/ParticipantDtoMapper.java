package pl.dreilt.basicspringmvcapp.event.mapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import pl.dreilt.basicspringmvcapp.event.dto.ParticipantDto;
import pl.dreilt.basicspringmvcapp.user.AppUser;

import java.util.List;
import java.util.stream.Collectors;

public class ParticipantDtoMapper {

    public static Page<ParticipantDto> mapToParticipantDtos(List<AppUser> participants, Pageable pageable) {
        List<ParticipantDto> participantsList = participants
                .stream()
                .map(ParticipantDtoMapper::mapToParticipantDto)
                .collect(Collectors.toList());
        return new PageImpl<>(participantsList, pageable, participantsList.size());
    }

    private static ParticipantDto mapToParticipantDto(AppUser user) {
        return new ParticipantDto.ParticipantDtoBuilder()
                .withFirstName(user.getFirstName())
                .withLastName(user.getLastName())
                .build();
    }
}
