package kg.inai.taskmanager.controllers;

import jakarta.validation.Valid;
import kg.inai.taskmanager.models.team.TeamRequest;
import kg.inai.taskmanager.models.team.TeamResponse;
import kg.inai.taskmanager.services.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @GetMapping("/active")
    public ResponseEntity<List<TeamResponse>> getAllActive() {
        return ResponseEntity.ok(teamService.getAllActive());
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TeamResponse>> getAll() {
        return ResponseEntity.ok(teamService.getAll());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEAM_LEAD')")
    public ResponseEntity<TeamResponse> save(@Valid @RequestBody TeamRequest teamRequest) {
        return ResponseEntity.ok(teamService.save(teamRequest));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEAM_LEAD')")
    public void update(@PathVariable Long id, @Valid @RequestBody TeamRequest teamRequest) {
        teamService.update(id, teamRequest);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        teamService.delete(id);
    }
}
