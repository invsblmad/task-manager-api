package kg.inai.taskmanager.models.user;

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
}
