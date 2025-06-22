package kg.inai.taskmanager.services;

import kg.inai.taskmanager.dtos.team.TeamRequestDto;
import kg.inai.taskmanager.dtos.team.TeamResponseDto;

import java.util.List;

public interface TeamService {

    List<TeamResponseDto> getAll();

    List<TeamResponseDto> getAllActive();

    TeamResponseDto save(TeamRequestDto teamRequest);

    void update(Long id, TeamRequestDto teamRequest);

    void delete(Long id);
}
