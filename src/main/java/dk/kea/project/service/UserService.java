package dk.kea.project.service;

import dk.kea.project.dto.UserRequest;
import dk.kea.project.dto.UserResponse;
import dk.kea.project.entity.User;
import dk.kea.project.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService {
    UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(user -> new UserResponse(user)).toList();
    }

    public UserResponse getUserById(String username) {
        User user = getUser(username);
        return new UserResponse(user);
    }

    private User getUser(String username) {
        return userRepository.findById(username).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    public ResponseEntity<Boolean> updateUser(String username, UserRequest userRequest) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        User user = getUser(username);
        if(!userRequest.getUsername().equals(username)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot change username");
        }
        user.setPassword(userRequest.getPassword());
        if(!passwordEncoder.matches(user.getPassword(), userRequest.getPassword())){
            user.setPassword(userRequest.getPassword());
        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Passwords are the same");
        }
        user.setEmail(userRequest.getEmail());
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        userRepository.save(user);
        return ResponseEntity.ok(true);
    }

    public void deleteUserByUser(String username) {
        User user = getUser(username);
        userRepository.deleteById(username);
    }

    public UserResponse createUser(UserRequest userRequest) {
        User user = UserRequest.getUserEntity(userRequest);
        userRepository.save(user);
        return new UserResponse(user);
    }

    public void deleteUser(String username) {
        User user = getUser(username);
        userRepository.deleteById(username);
    }
}
