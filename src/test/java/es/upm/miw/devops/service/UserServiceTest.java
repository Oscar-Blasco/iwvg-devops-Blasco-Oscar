package es.upm.miw.devops.service;

import es.upm.miw.devops.model.User;
import es.upm.miw.devops.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserService userService;
    private User user;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository);
        user = new User("1", "Oscar", "Blasco", "oscar@example.com", "12345678A",
                "Calle Mayor 1", "Madrid", "Madrid", "28001");
    }

    @Test
    void findByIdReturnsUserWhenItExists() {
        when(userRepository.findById("1")).thenReturn(Optional.of(user));

        assertThat(userService.findById("1")).isSameAs(user);
        verify(userRepository).findById("1");
    }

    @Test
    void findByIdThrowsWhenUserDoesNotExist() {
        when(userRepository.findById("999")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findById("999"))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User not found: 999");
        verify(userRepository).findById("999");
    }
}
