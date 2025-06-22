package kg.inai.taskmanager.mappers;

import kg.inai.taskmanager.dtos.EnumDto;
import kg.inai.taskmanager.dtos.auth.SignUpRequestDto;
import kg.inai.taskmanager.dtos.user.UserDetailedResponseDto;
import kg.inai.taskmanager.dtos.user.UserResponseDto;
import kg.inai.taskmanager.dtos.user.UserShortResponseDto;
import kg.inai.taskmanager.dtos.user.UserUpdateRequestDto;
import kg.inai.taskmanager.entities.User;
import kg.inai.taskmanager.enums.UserStatus;
import kg.inai.taskmanager.services.MinioService;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(SignUpRequestDto signUpRequest);
    User toUpdatedEntity(UserUpdateRequestDto request, @MappingTarget User user);
    @Mapping(target = "avatarUrl", expression = "java(minioService.getPublicUrl(user.getAvatarPath()))")
    UserResponseDto toDto(User user, @Context MinioService minioService);
    @Mapping(target = "avatarUrl", expression = "java(minioService.getPublicUrl(user.getAvatarPath()))")
    UserShortResponseDto toShortDto(User user, @Context MinioService minioService);
    @Mapping(target = "avatarUrl", expression = "java(minioService.getPublicUrl(user.getAvatarPath()))")
    UserDetailedResponseDto toDetailedDto(User user, @Context MinioService minioService);

    default EnumDto toDto(UserStatus status) {
        if (status == null) return null;
        return new EnumDto(status.name(), status.getDescription());
    }
}
