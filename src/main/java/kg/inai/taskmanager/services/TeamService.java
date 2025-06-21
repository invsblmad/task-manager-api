package kg.inai.taskmanager.services;

import kg.inai.taskmanager.dtos.team.TeamRequest;
import kg.inai.taskmanager.dtos.team.TeamResponse;

import java.util.List;

public interface TeamService {

    List<TeamResponse> getAll();

    List<TeamResponse> getAllActive();

    TeamResponse save(TeamRequest teamRequest);

    void update(Long id, TeamRequest teamRequest);

    void delete(Long id);
}
