package kg.inai.taskmanager.mappers;

import kg.inai.taskmanager.entities.Team;
import kg.inai.taskmanager.models.team.TeamRequest;
import kg.inai.taskmanager.models.team.TeamResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TeamMapper {

    TeamResponse toModel(Team team);
    Team toEntity(TeamRequest teamRequest);
}
