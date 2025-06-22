package kg.inai.taskmanager.mappers;

import kg.inai.taskmanager.entities.Team;
import kg.inai.taskmanager.dtos.team.TeamRequestDto;
import kg.inai.taskmanager.dtos.team.TeamResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TeamMapper {

    TeamResponseDto toDto(Team team);
    Team toEntity(TeamRequestDto teamRequest);
}
