package kg.inai.taskmanager.dtos.user;

import kg.inai.taskmanager.dtos.EnumDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {

    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String avatarUrl;
    private String role;
    private EnumDto status;
}
