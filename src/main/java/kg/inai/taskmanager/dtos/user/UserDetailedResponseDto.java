package kg.inai.taskmanager.dtos.user;

import kg.inai.taskmanager.dtos.team.TeamResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailedResponseDto extends UserResponseDto {

    private String phoneNumber;
    private String jobTitle;
    private String department;
    private TeamResponseDto team;
}
