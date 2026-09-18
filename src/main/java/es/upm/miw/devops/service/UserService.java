package es.upm.miw.devops.service;

import es.upm.miw.devops.model.User;
import es.upm.miw.devops.repository.UserRepository;
import org.springframework.stereotype.Service;

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
