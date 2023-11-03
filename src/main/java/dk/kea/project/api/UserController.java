package dk.kea.project.api;

import dk.kea.project.dto.UserRequest;
import dk.kea.project.dto.UserResponse;
import dk.kea.project.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    //Admin
    @GetMapping()
    public List<UserResponse> getAllUsers(){
        return userService.getAllUsers();
    }
    //User
    @GetMapping("/user-as-authenticated")
    public UserResponse getUserById(Principal principal){
        return userService.getUserById(principal.getName());
    }
    //User
    @PutMapping("/user-as-authenticated")
    public ResponseEntity<Boolean> updateUser(Principal principal, @RequestBody UserRequest userRequest){
        return userService.updateUser(principal.getName(), userRequest);
    }
    //User
    @DeleteMapping("/user-as-authenticated")
    public void deleteUserByUser(Principal principal){
        userService.deleteUserByUser(principal.getName());
    }
    //Anonymous
    @PostMapping()
    public UserResponse createUser(@RequestBody UserRequest userRequest){
        return userService.createUser(userRequest);
    }
    //Admin
    @DeleteMapping("/{username}")
    public void deleteUser(@PathVariable String username){
        userService.deleteUser(username);
    }
}
