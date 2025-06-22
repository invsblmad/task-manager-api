package kg.inai.taskmanager.dtos.task;

import lombok.Builder;

@Builder
public record TaskTimeProgressDto(String estimatedTime,
                                  String spentTime,
                                  String remainingTime,
                                  int spentPercent,
                                  int remainingPercent) {
}
