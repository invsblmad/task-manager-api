package kg.inai.taskmanager.mappers;

import kg.inai.taskmanager.entities.User;
import kg.inai.taskmanager.models.auth.SignUpRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(SignUpRequest signUpRequest);
}
