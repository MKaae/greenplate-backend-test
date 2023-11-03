package dk.kea.project.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import dk.kea.project.entity.User;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRequest {
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;

    public static User getUserEntity(UserRequest userRequest){
        return new User(userRequest.username,
                        userRequest.email,
                        userRequest.password,
                        userRequest.firstName,
                        userRequest.lastName);
    }
}
