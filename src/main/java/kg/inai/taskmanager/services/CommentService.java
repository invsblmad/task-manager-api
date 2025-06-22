package kg.inai.taskmanager.services;

import kg.inai.taskmanager.dtos.comment.CommentRequestDto;
import kg.inai.taskmanager.dtos.comment.CommentResponseDto;

import java.util.List;

public interface CommentService {

    List<CommentResponseDto> getAll(String taskId);
    List<CommentResponseDto> getAllActive(String taskId);

    CommentResponseDto getById(Long id);

    CommentResponseDto save(String taskId, CommentRequestDto request);

    void update(Long id, CommentRequestDto commentRequest);

    void delete(Long id);
}
