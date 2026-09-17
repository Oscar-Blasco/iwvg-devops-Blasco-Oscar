package es.upm.miw.devops.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Test
    void exposesAllUserFields() {
        User user = new User("1", "Oscar", "Blasco", "oscar@example.com", "12345678A",
                "Calle Mayor 1", "Madrid", "Madrid", "28001", Role.ADMIN, true);

        assertThat(user.getId()).isEqualTo("1");
        assertThat(user.getName()).isEqualTo("Oscar");
        assertThat(user.getFamilyName()).isEqualTo("Blasco");
        assertThat(user.getEmail()).isEqualTo("oscar@example.com");
        assertThat(user.getIdentity()).isEqualTo("12345678A");
        assertThat(user.getAddress()).isEqualTo("Calle Mayor 1");
        assertThat(user.getCity()).isEqualTo("Madrid");
        assertThat(user.getProvince()).isEqualTo("Madrid");
        assertThat(user.getPostalCode()).isEqualTo("28001");
        assertThat(user.getRole()).isEqualTo(Role.ADMIN);
        assertThat(user.getActive()).isTrue();
    }

    @Test
    void shortConstructorLeavesOptionalRoleAndActiveNull() {
        User user = new User("2", "Ana", "Garcia", "ana@example.com", "23456789B",
                "Calle Alcalá 2", "Madrid", "Madrid", "28002");

        assertThat(user.getRole()).isNull();
        assertThat(user.getActive()).isNull();
    }
}
