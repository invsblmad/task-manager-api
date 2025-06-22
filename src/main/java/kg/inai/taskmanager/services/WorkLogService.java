package kg.inai.taskmanager.services;

import kg.inai.taskmanager.dtos.work_log.WorkLogRequestDto;
import kg.inai.taskmanager.dtos.work_log.WorkLogResponseDto;

import java.util.List;

public interface WorkLogService {

    List<WorkLogResponseDto> getAllActive(String taskId);

    List<WorkLogResponseDto> getAll(String taskId);

    WorkLogResponseDto getById(Long id);

    WorkLogResponseDto save(String taskId, WorkLogRequestDto request);

    void update(Long id, WorkLogRequestDto request);

    void delete(Long id);
}
