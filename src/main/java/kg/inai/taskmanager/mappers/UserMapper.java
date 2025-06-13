package kg.inai.taskmanager.mappers;

import kg.inai.taskmanager.entities.User;
import kg.inai.taskmanager.models.auth.SignUpRequest;
import kg.inai.taskmanager.models.user.UserDetailedResponse;
import kg.inai.taskmanager.models.user.UserResponse;
import kg.inai.taskmanager.models.user.UserUpdateRequest;
import kg.inai.taskmanager.services.MinioService;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(SignUpRequest signUpRequest);
    User toUpdatedEntity(UserUpdateRequest request, @MappingTarget User user);
    @Mapping(target = "avatarUrl", expression = "java(minioService.getPublicUrl(user.getAvatarPath()))")
    UserResponse toDto(User user, @Context MinioService minioService);
    @Mapping(target = "avatarUrl", expression = "java(minioService.getPublicUrl(user.getAvatarPath()))")
    UserDetailedResponse toDetailedDto(User user, @Context MinioService minioService);
}
