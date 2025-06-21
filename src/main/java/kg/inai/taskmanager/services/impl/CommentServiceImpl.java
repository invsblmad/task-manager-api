package kg.inai.taskmanager.services.impl;

import kg.inai.taskmanager.dtos.comment.CommentRequest;
import kg.inai.taskmanager.dtos.comment.CommentResponse;
import kg.inai.taskmanager.entities.Comment;
import kg.inai.taskmanager.entities.Task;
import kg.inai.taskmanager.enums.CommentStatus;
import kg.inai.taskmanager.exceptions.NotFoundException;
import kg.inai.taskmanager.mappers.CommentMapper;
import kg.inai.taskmanager.repositories.CommentRepository;
import kg.inai.taskmanager.repositories.TaskRepository;
import kg.inai.taskmanager.services.AuthService;
import kg.inai.taskmanager.services.CommentService;
import kg.inai.taskmanager.utils.TaskIdParsesUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final CommentMapper commentMapper;
    private final AuthService authService;

    @Override
    public List<CommentResponse> getAll(String taskId) {
        Task task = taskRepository.findById(TaskIdParsesUtil.parse(taskId))
                .orElseThrow(() -> new NotFoundException("Задача не найдена"));

        return commentRepository.findAllByTask(task).stream()
                .map(commentMapper::toDto)
                .toList();
    }

    @Override
    public List<CommentResponse> getAllActive(String taskId) {
        Task task = taskRepository.findById(TaskIdParsesUtil.parse(taskId))
                .orElseThrow(() -> new NotFoundException("Задача не найдена"));

        return commentRepository.findAllByTaskAndStatusNot(task, CommentStatus.DELETED).stream()
                .map(commentMapper::toDto)
                .toList();
    }

    @Override
    public CommentResponse getById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Комментарий не найден"));

        return commentMapper.toDto(comment);
    }

    @Override
    public CommentResponse save(String taskId, CommentRequest request) {
        Task task = taskRepository.findById(TaskIdParsesUtil.parse(taskId))
                .orElseThrow(() -> new NotFoundException("Задача не найдена"));

        Comment comment = commentMapper.toEntity(request);
        comment.setUser(authService.getAuthenticatedUser());
        comment.setStatus(CommentStatus.ACTIVE);
        comment.setTask(task);

        comment = commentRepository.save(comment);
        return commentMapper.toDto(comment);
    }

    @Override
    public void update(Long id, CommentRequest commentRequest) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Комментарий не найден"));

        comment.setText(commentRequest.text());
        comment.setStatus(CommentStatus.EDITED);
        commentRepository.save(comment);
    }

    @Override
    public void delete(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Комментарий не найден"));

        comment.setStatus(CommentStatus.DELETED);
        commentRepository.save(comment);
    }
}
