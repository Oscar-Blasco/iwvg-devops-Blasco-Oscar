package es.upm.miw.devops.service;

import es.upm.miw.devops.dto.UserActiveDto;
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
    void testUpdateActiveBulk() {
        User user1 = new User(
                "1", "Oscar", "Blasco", "oscar@example.com", "12345678A",
                "Calle Mayor 1", "Madrid", "Madrid", "28001",
                Role.ADMIN, true
        );

        User user2 = new User(
                "1", "Maria", "Antonieta", "maria@example.com", "12345678A",
                "Calle Mayor 2", "Madrid", "Madrid", "28002",
                Role.ADMIN, false
        );

        UserActiveDto dto1 = new UserActiveDto();
        dto1.setId("1");
        dto1.setActive(false);

        UserActiveDto dto2 = new UserActiveDto();
        dto2.setId("2");
        dto2.setActive(true);

        List<UserActiveDto> dtos = List.of(dto1, dto2);

        when(userRepository.findById("1")).thenReturn(Optional.of(user1));
        when(userRepository.findById("2")).thenReturn(Optional.of(user2));
        when(userRepository.saveAll(List.of(user1, user2)))
                .thenReturn(List.of(user1, user2));

        List<User> result = userService.updateActive(dtos);

        assertEquals(2, result.size());
        assertFalse(result.get(0).getActive());
        assertTrue(result.get(1).getActive());

        verify(userRepository).findById("1");
        verify(userRepository).findById("2");
        verify(userRepository).saveAll(List.of(user1, user2));
    }

    @Test
    void testUpdateActiveBulkUserNotFound() {
        UserActiveDto dto = new UserActiveDto();
        dto.setId("999");
        dto.setActive(false);

        when(userRepository.findById("999")).thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> userService.updateActive(List.of(dto))
        );

        verify(userRepository, never()).saveAll(any());
    }

    @Test
    void testUpdateActiveBulkChangesActiveValue() {
        User user = new User(
                "1", "Oscar", "Blasco", "oscar@example.com", "12345678A",
                "Calle Mayor 1", "Madrid", "Madrid", "28001",
                Role.ADMIN, true
        );

        UserActiveDto dto = new UserActiveDto();
        dto.setId("1");
        dto.setActive(false);

        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        when(userRepository.saveAll(List.of(user)))
                .thenReturn(List.of(user));

        List<User> result = userService.updateActive(List.of(dto));

        assertFalse(result.get(0).getActive());

        verify(userRepository).saveAll(List.of(user));
    }
}
