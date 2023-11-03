package dk.kea.project.config;

import dk.kea.project.entity.User;
import dk.kea.project.repository.UserRepository;
import dk.kea.security.entity.Role;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeveloperData implements ApplicationRunner {
    UserRepository userRepository;

    public DeveloperData(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public void run(ApplicationArguments args) throws Exception {

    User admin1 = new User("admin", "admin@admin", "password123", "firstName", "lastName");
    admin1.addRole(Role.ADMIN);
    userRepository.save(admin1);

    User user1 = new User("user", "user@user", "password123", "firstName", "lastName");
    user1.addRole(Role.USER);
    userRepository.save(user1);
    }
}
