package es.upm.miw.devops.service;

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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
}
