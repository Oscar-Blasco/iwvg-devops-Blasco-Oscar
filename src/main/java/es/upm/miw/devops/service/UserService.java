package es.upm.miw.devops.service;

import es.upm.miw.devops.model.User;
import es.upm.miw.devops.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public List<User> search(String name, Boolean billable) {
        String normalizedName = normalize(name);

        return userRepository.findAll().stream()
                .filter(user -> matchesName(user, normalizedName))
                .filter(user -> billable == null || user.isBillable() == billable)
                .toList();
    }

    private boolean matchesName(User user, String name) {
        if (name == null) {
            return true;
        }
        return containsIgnoreCase(user.getName(), name)
                || containsIgnoreCase(user.getFamilyName(), name);
    }
    private boolean containsIgnoreCase(String value, String search) {
        return value != null
                && value.toLowerCase(Locale.ROOT).contains(search);
    }

    private String normalize(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim().toLowerCase(Locale.ROOT);
    }

    public User updateActive(String id) {
        User user = findById(id);
        user.setActive(!user.getActive());
        return userRepository.save(user);
    }

    public void delete(String id) {
        User user = findById(id);
        userRepository.delete(user);
    }
}
