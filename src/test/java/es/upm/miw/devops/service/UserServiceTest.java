package es.upm.miw.devops.service;

import es.upm.miw.devops.model.Role;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    @Test
    void deleteShouldRemoveExistingUser() {
        User user = new User(
                "1",
                "Oscar",
                "Blasco",
                "oscar@example.com",
                "12345678A",
                "Calle Mayor 1",
                "Madrid",
                "Madrid",
                "28001",
                Role.ADMIN,
                true
        );

        when(userRepository.findById("1")).thenReturn(Optional.of(user));

        userService.delete("1");

        verify(userRepository).findById("1");
        verify(userRepository).delete(user);
    }

    @Test
    void updateActiveShouldToggleFromTrueToFalse() {
        User user = new User(
                "1", "Oscar", "Blasco", "oscar@example.com", "12345678A",
                "Calle Mayor 1", "Madrid", "Madrid", "28001",
                Role.ADMIN, true
        );

        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.updateActive("1");

        assertFalse(result.getActive());
        verify(userRepository).save(user);
    }

    @Test
    void updateActiveShouldToggleFromFalseToTrue() {
        User user = new User(
                "1", "Oscar", "Blasco", "oscar@example.com", "12345678A",
                "Calle Mayor 1", "Madrid", "Madrid", "28001",
                Role.ADMIN, false
        );

        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.updateActive("1");

        assertTrue(result.getActive());
        verify(userRepository).save(user);
    }

    @Test
    void updateActiveShouldThrowExceptionWhenUserDoesNotExist() {
        when(userRepository.findById("999")).thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> userService.updateActive("999")
        );

        verify(userRepository).findById("999");
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void deleteShouldThrowExceptionWhenUserDoesNotExist() {
        when(userRepository.findById("999")).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.delete("999")
        );

        assertEquals("User not found: 999", exception.getMessage());
    }
}
