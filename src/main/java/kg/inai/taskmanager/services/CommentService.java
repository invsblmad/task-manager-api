package kg.inai.taskmanager.services;

import kg.inai.taskmanager.dtos.comment.CommentRequest;
import kg.inai.taskmanager.dtos.comment.CommentResponse;

import java.util.List;

public interface CommentService {

    List<CommentResponse> getAll(String taskId);
    List<CommentResponse> getAllActive(String taskId);

    CommentResponse getById(Long id);

    CommentResponse save(String taskId, CommentRequest request);

    void update(Long id, CommentRequest commentRequest);

    void delete(Long id);
}
