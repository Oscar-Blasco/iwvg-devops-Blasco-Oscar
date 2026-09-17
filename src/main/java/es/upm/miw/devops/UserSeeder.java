package es.upm.miw.devops;

import es.upm.miw.devops.model.Role;
import es.upm.miw.devops.model.User;
import es.upm.miw.devops.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class UserSeeder implements CommandLineRunner {

    private final UserRepository userRepository;

    public UserSeeder(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            userRepository.save(new User(
                    "1", "Oscar", "Blasco", "oscar.blasco@example.com", "12345678A",
                    "Calle Mayor 1", "Madrid", "Madrid", "28001", Role.ADMIN, true));

            userRepository.save(new User(
                    "2", "Ana", "Garcia", "ana.garcia@example.com", "23456789B",
                    "Calle Alcalá 2", "Madrid", "Madrid", "28002", Role.CUSTOMER, true));

            userRepository.save(new User(
                    "3", "Luis", "Perez", null, "34567890C",
                    "Calle Valencia 3", "Valencia", "Valencia", "46001", Role.CUSTOMER, false));

            userRepository.save(new User(
                    "4", "Marta", "Lopez", "marta.lopez@example.com", "45678901D",
                    "Calle Sevilla 4", "Sevilla", "Sevilla", "41001", Role.AUTHENTICATED, false));

        }

    }
}
