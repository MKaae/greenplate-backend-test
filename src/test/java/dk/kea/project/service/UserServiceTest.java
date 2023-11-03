package dk.kea.project.service;

import dk.kea.project.dto.UserRequest;
import dk.kea.project.dto.UserResponse;
import dk.kea.project.entity.User;
import dk.kea.project.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    UserService userService;

    @Mock
    UserRepository userRepository;

    @BeforeEach
    void setUp(){
        userService = new UserService(userRepository);
    }
    User testUser(){
        return new User("un", "em", "pw", "fn", "ln");
    }

    @Test
    void testGetAllUsers() {
        User user = testUser();
        when(userRepository.findAll()).thenReturn(List.of(user));
        List<UserResponse> res = userService.getAllUsers();
        assertEquals(1, res.size());
    }

    @Test
    void testGetUserByIdSucceed() {
        User user = testUser();
        when(userRepository.findById("un")).thenReturn(Optional.of(user));
        UserResponse res = userService.getUserById("un");
        assertEquals("un", res.getUsername());
    }
    @Test
    void testGetUserByIdFail() {
        when(userRepository.findById("wrongName")).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, ()-> userService.getUserById("wrongName"));
    }
    @Test
    void testUpdateUserSucceed() {
        UserRequest request = new UserRequest("un", "pw", "em", "fn", "ln");
        User expectedUser = new User("un1", "em1", "pw1", "fn1", "ln1");
        expectedUser.setFirstName("fn");
        expectedUser.setEmail("em");
        expectedUser.setLastName("ln");
        expectedUser.setPassword("pw");
        when(userRepository.save(any(User.class))).thenReturn(expectedUser);
        UserResponse response = userService.createUser(request);
        verify(userRepository).save(any(User.class));
        assertEquals(expectedUser.getFirstName(), response.getFirstName());
        assertEquals(expectedUser.getEmail(), response.getEmail());
        assertEquals(expectedUser.getLastName(), response.getLastName());
    }

    @Test
    void testCreateUser() {
        User user = new User("un", "pw", "em", "fn", "ln");
        when(userRepository.save(any(User.class))).thenReturn(user);
        UserRequest req = new UserRequest("un", "pw", "em", "fn", "ln");
        UserResponse res = userService.createUser(req);
        assertEquals("un", res.getUsername());
    }

    @Test
    void testDeleteUser() {
        User user = new User("un", "pw", "em", "fn", "ln");
        when(userRepository.save(any(User.class))).thenReturn(user);
        UserRequest req = new UserRequest("un", "pw", "em", "fn", "ln");
        UserResponse res = userService.createUser(req);
        String testId = "un";
        //assertEquals(Optional.empty(), userService.deleteUser(testId));
    }
}