package kg.inai.taskmanager.dtos.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import kg.inai.taskmanager.dtos.EnumDto;
import kg.inai.taskmanager.dtos.project.ProjectResponseDto;
import kg.inai.taskmanager.dtos.team.TeamResponseDto;
import kg.inai.taskmanager.dtos.user.UserShortResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public record TaskDetailedResponseDto(String id,
                                      ProjectResponseDto project,
                                      EnumDto type,
                                      EnumDto priority,
                                      EnumDto status,
                                      TeamResponseDto teamResponse,
                                      UserShortResponseDto assignedTo,
                                      UserShortResponseDto createdBy,
                                      @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
                                      LocalDateTime createdAt,
                                      @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
                                      LocalDateTime updatedAt,
                                      String title,
                                      String description,
                                      TaskTimeProgressDto progress,
                                      List<SubtaskResponseDto> subtasks) {
}
