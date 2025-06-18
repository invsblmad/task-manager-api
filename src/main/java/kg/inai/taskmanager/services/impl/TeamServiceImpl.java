package kg.inai.taskmanager.services.impl;

import kg.inai.taskmanager.entities.Team;
import kg.inai.taskmanager.exceptions.NotFoundException;
import kg.inai.taskmanager.mappers.TeamMapper;
import kg.inai.taskmanager.models.team.TeamRequest;
import kg.inai.taskmanager.models.team.TeamResponse;
import kg.inai.taskmanager.repositories.TeamRepository;
import kg.inai.taskmanager.services.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;

    @Override
    public List<TeamResponse> getAll() {
        return teamRepository.findAll().stream()
                .map(teamMapper::toModel)
                .toList();
    }

    @Override
    public List<TeamResponse> getAllActive() {
        return teamRepository.findAllByDeleted(false).stream()
                .map(teamMapper::toModel)
                .toList();
    }

    @Override
    public TeamResponse save(TeamRequest teamRequest) {
        Team team = teamMapper.toEntity(teamRequest);
        team = teamRepository.save(team);
        return teamMapper.toModel(team);
    }

    @Override
    public void update(Long id, TeamRequest teamRequest) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Команда не найдена"));

        team.setName(teamRequest.name());
        teamRepository.save(team);
    }

    @Override
    public void delete(Long id) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Команда не найдена"));

        team.setDeleted(true);
        teamRepository.save(team);
    }
}
