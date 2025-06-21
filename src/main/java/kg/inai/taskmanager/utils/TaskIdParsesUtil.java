package kg.inai.taskmanager.utils;

import kg.inai.taskmanager.entities.TaskId;

public class TaskIdParsesUtil {

    public static TaskId parse(String taskId) {
        if (taskId == null || taskId.isBlank()) {
            throw new IllegalArgumentException("Id задачи не должен быть пустым");
        }

        String[] parts = taskId.split("-");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Неверный формат id, ожидаемый вид: PROJECT-SEQ");
        }

        String projectCode = parts[0];
        long sequenceNumber;

        try {
            sequenceNumber = Long.parseLong(parts[1]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Id задачи содержит неверное число: " + taskId, e);
        }

        return new TaskId(projectCode, sequenceNumber);
    }

    public static String format(TaskId taskId) {
        if (taskId == null || taskId.getProjectCode() == null || taskId.getSequenceNumber() == null) {
            throw new IllegalArgumentException("Id задачи содержит пустые значения");
        }

        return taskId.getProjectCode() + "-" + taskId.getSequenceNumber();
    }
}
