package kg.inai.taskmanager.dtos.user;

import kg.inai.taskmanager.dtos.team.TeamResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailedResponse extends UserResponse {

    private String phoneNumber;
    private String jobTitle;
    private String department;
    private TeamResponse team;
}
