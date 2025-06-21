package kg.inai.taskmanager.mappers;

import kg.inai.taskmanager.entities.Team;
import kg.inai.taskmanager.dtos.team.TeamRequest;
import kg.inai.taskmanager.dtos.team.TeamResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TeamMapper {

    TeamResponse toDto(Team team);
    Team toEntity(TeamRequest teamRequest);
}
