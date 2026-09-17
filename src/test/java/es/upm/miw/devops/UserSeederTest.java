package es.upm.miw.devops;

import es.upm.miw.devops.model.Role;
import es.upm.miw.devops.model.User;
import es.upm.miw.devops.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class UserSeederTest {

    @Test
    void seedsExpectedUsersWhenRepositoryIsEmpty() {
        UserRepository repository = mock(UserRepository.class);
        when(repository.count()).thenReturn(0L);

        new UserSeeder(repository).run();

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(repository, times(4)).save(captor.capture());

        List<User> users = captor.getAllValues();
        assertThat(users).extracting(User::getId)
                .containsExactly("1", "2", "3", "4");
        assertThat(users.get(0).getRole()).isEqualTo(Role.ADMIN);
        assertThat(users.get(1).getRole()).isEqualTo(Role.CUSTOMER);
        assertThat(users.get(2).getRole()).isEqualTo(Role.CUSTOMER);
        assertThat(users.get(3).getRole()).isEqualTo(Role.AUTHENTICATED);
        assertThat(users.get(0).getActive()).isTrue();
        assertThat(users.get(2).getEmail()).isNull();
    }

    @Test
    void doesNotSeedWhenRepositoryAlreadyContainsUsers() {
        UserRepository repository = mock(UserRepository.class);
        when(repository.count()).thenReturn(1L);

        new UserSeeder(repository).run();

        verify(repository, never()).save(any(User.class));
    }
}
