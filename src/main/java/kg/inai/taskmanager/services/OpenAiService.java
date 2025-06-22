package kg.inai.taskmanager.services;

import kg.inai.taskmanager.dtos.task.GeneratedSubtaskDto;

import java.util.List;

public interface OpenAiService {

    List<GeneratedSubtaskDto> generateSubtasks(String taskDescription);
}
