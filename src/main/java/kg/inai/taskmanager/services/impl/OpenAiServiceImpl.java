package kg.inai.taskmanager.services.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kg.inai.taskmanager.dtos.task.GeneratedSubtaskDto;
import kg.inai.taskmanager.exceptions.TaskManagerException;
import kg.inai.taskmanager.services.OpenAiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class OpenAiServiceImpl implements OpenAiService {

    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;
    private static final String promptTemplate = """
        Разбей следующую задачу на максимум 3 подзадачи. Ответ строго в виде JSON-массива объектов, где каждый объект содержит:
        - number: порядковый номер подзадачи,
        - title: заголовок до 10 слов,
        - description: описание до 30 слов.
        Задача: %s
        Ответ: только JSON, без пояснений..
    """;


    public OpenAiServiceImpl(ChatClient.Builder builder, ObjectMapper objectMapper) {
        this.chatClient = builder.build();
        this.objectMapper = objectMapper;
    }

    @Override
    public List<GeneratedSubtaskDto> generateSubtasks(String taskDescription) {
        String prompt = promptTemplate.formatted(taskDescription);

        String jsonResponse = chatClient.prompt()
                .user(prompt)
                .call()
                .content();

        log.info("OpenAI JSON response: {}", jsonResponse);

        try {
            return objectMapper.readValue(jsonResponse, new TypeReference<>() {});
        } catch (Exception e) {
            throw new TaskManagerException("Ошибка при парсинге JSON от OpenAI: " + e.getMessage());
        }
    }
}
