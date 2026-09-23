package es.upm.miw.devops.service;

import es.upm.miw.devops.dto.UserDto;
import es.upm.miw.devops.model.Role;
import es.upm.miw.devops.model.User;
import es.upm.miw.devops.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
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
    private User billableUser;
    private User nonBillableUser;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository);

        billableUser = new User(
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

        nonBillableUser = new User(
                "2",
                "Luis",
                "Perez",
                null,
                "23456789B",
                "Calle Valencia 2",
                "Valencia",
                "Valencia",
                "46001",
                Role.CUSTOMER,
                false
        );
    }

    @Test
    void findByIdReturnsUserWhenItExists() {
        when(userRepository.findById("1")).thenReturn(Optional.of(nonBillableUser));

        assertThat(userService.findById("1")).isSameAs(nonBillableUser);
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
    void searchBillableUsers() {
        when(userRepository.findAll())
                .thenReturn(List.of(billableUser, nonBillableUser));

        assertThat(userService.search(null, true))
                .containsExactly(billableUser);
    }

    @Test
    void searchNonBillableUsers() {
        when(userRepository.findAll())
                .thenReturn(List.of(billableUser, nonBillableUser));

        assertThat(userService.search(null, false))
                .containsExactly(nonBillableUser);
    }

    @Test
    void searchWithoutBillableReturnsAllUsers() {
        when(userRepository.findAll())
                .thenReturn(List.of(billableUser, nonBillableUser));

        assertThat(userService.search(null, null))
                .containsExactly(billableUser, nonBillableUser);
    }

    @Test
    void searchByName() {
        when(userRepository.findAll())
                .thenReturn(List.of(billableUser, nonBillableUser));

        assertThat(userService.search("Oscar", null))
                .containsExactly(billableUser);
    }

    @Test
    void searchByFamilyName() {
        when(userRepository.findAll())
                .thenReturn(List.of(billableUser, nonBillableUser));

        assertThat(userService.search("Blasco", null))
                .containsExactly(billableUser);
    }

    @Test
    void searchCombinesNameAndBillable() {
        when(userRepository.findAll())
                .thenReturn(List.of(billableUser, nonBillableUser));

        assertThat(userService.search("Oscar", true))
                .containsExactly(billableUser);
    }

    @Test
    void searchIsCaseInsensitive() {
        when(userRepository.findAll())
                .thenReturn(List.of(billableUser));

        assertThat(userService.search("  OSCAR  ", null))
                .containsExactly(billableUser);
    }

    @Test
    void blankNameDoesNotFilter() {
        when(userRepository.findAll())
                .thenReturn(List.of(billableUser, nonBillableUser));

        assertThat(userService.search("   ", null))
                .containsExactly(billableUser, nonBillableUser);
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

    @Test
    void testUpdate() {
        User user = new User(
                "1", "Oscar", "Blasco", "oscar@example.com", "12345678A",
                "Calle Mayor 1", "Madrid", "Madrid", "28001",
                Role.ADMIN, false
        );

        UserDto dto = new UserDto();
        dto.setName("Marta");
        dto.setFamilyName("Lopes");
        dto.setEmail("Marta.lopes@sadas.sa");
        dto.setIdentity("6832163");
        dto.setAddress("calle mala 123");
        dto.setCity("Madrid");
        dto.setProvince("Madrid");
        dto.setPostalCode("28001");

        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.update("1", dto);

        assertEquals("1", result.getId());
        assertEquals("Marta", result.getName());
        assertEquals("Lopes", result.getFamilyName());
        assertEquals("Marta.lopes@sadas.sa", result.getEmail());
        assertEquals("6832163", result.getIdentity());
        assertEquals("calle mala 123", result.getAddress());
        assertEquals("Madrid", result.getCity());
        assertEquals("Madrid", result.getProvince());
        assertEquals("28001", result.getPostalCode());

        verify(userRepository).save(user);
    }

    @Test
    void testUpdateDoesNotChangeActiveOrRole() {
        User user = new User(
                "1", "Oscar", "Blasco", "oscar@example.com", "12345678A",
                "Calle Mayor 1", "Madrid", "Madrid", "28001",
                Role.ADMIN, true
        );

        UserDto dto = new UserDto();
        dto.setName("Marta");
        dto.setFamilyName("Lopes");
        dto.setEmail("marta@email.com");
        dto.setIdentity("12345678");
        dto.setAddress("Calle 1");
        dto.setCity("Madrid");
        dto.setProvince("Madrid");
        dto.setPostalCode("28001");
        dto.setRole(Role.AUTHENTICATED);

        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.update("1", dto);

        assertTrue(result.getActive());
        assertEquals(Role.ADMIN, result.getRole());

        verify(userRepository).save(user);
    }

    @Test
    void testUpdateUserNotFound() {
        UserDto dto = new UserDto();
        dto.setName("Marta");

        when(userRepository.findById("999")).thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> userService.update("999", dto)
        );

        verify(userRepository, never()).save(any());
    }
}
