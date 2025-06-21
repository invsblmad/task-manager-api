package kg.inai.taskmanager.mappers;

import kg.inai.taskmanager.dtos.EnumDto;
import kg.inai.taskmanager.dtos.auth.SignUpRequest;
import kg.inai.taskmanager.dtos.user.UserDetailedResponse;
import kg.inai.taskmanager.dtos.user.UserResponse;
import kg.inai.taskmanager.dtos.user.UserShortResponse;
import kg.inai.taskmanager.dtos.user.UserUpdateRequest;
import kg.inai.taskmanager.entities.User;
import kg.inai.taskmanager.enums.UserStatus;
import kg.inai.taskmanager.services.MinioService;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(SignUpRequest signUpRequest);
    User toUpdatedEntity(UserUpdateRequest request, @MappingTarget User user);
    @Mapping(target = "avatarUrl", expression = "java(minioService.getPublicUrl(user.getAvatarPath()))")
    UserResponse toDto(User user, @Context MinioService minioService);
    @Mapping(target = "avatarUrl", expression = "java(minioService.getPublicUrl(user.getAvatarPath()))")
    UserShortResponse toShortDto(User user, @Context MinioService minioService);
    @Mapping(target = "avatarUrl", expression = "java(minioService.getPublicUrl(user.getAvatarPath()))")
    UserDetailedResponse toDetailedDto(User user, @Context MinioService minioService);

    default EnumDto toDto(UserStatus status) {
        if (status == null) return null;
        return new EnumDto(status.name(), status.getDescription());
    }
}
