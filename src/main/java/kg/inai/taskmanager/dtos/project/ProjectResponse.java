package kg.inai.taskmanager.dtos.project;

import kg.inai.taskmanager.dtos.EnumDto;

public record ProjectResponse(String code,

                              String name,
                              String imageUrl,
                              EnumDto status) {
}
