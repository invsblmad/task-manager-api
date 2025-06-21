package kg.inai.taskmanager.dtos.project;

import kg.inai.taskmanager.dtos.EnumDto;

public record ProjectResponse(Long id,
                              String code,

                              String name,
                              String imageUrl,
                              EnumDto status) {
}
