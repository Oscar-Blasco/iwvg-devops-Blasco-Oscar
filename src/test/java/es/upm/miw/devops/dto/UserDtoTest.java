package es.upm.miw.devops.dto;

import es.upm.miw.devops.model.Role;
import es.upm.miw.devops.model.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserDtoTest {

    @Test
    void mapsAllUserFields() {
        User user = new User("1", "Oscar", "Blasco", "oscar@example.com", "12345678A",
                "Calle Mayor 1", "Madrid", "Madrid", "28001", Role.ADMIN, true);

        UserDto dto = new UserDto(user);

        assertThat(dto.getId()).isEqualTo("1");
        assertThat(dto.getName()).isEqualTo("Oscar");
        assertThat(dto.getFamilyName()).isEqualTo("Blasco");
        assertThat(dto.getEmail()).isEqualTo("oscar@example.com");
        assertThat(dto.getIdentity()).isEqualTo("12345678A");
        assertThat(dto.getAddress()).isEqualTo("Calle Mayor 1");
        assertThat(dto.getCity()).isEqualTo("Madrid");
        assertThat(dto.getProvince()).isEqualTo("Madrid");
        assertThat(dto.getPostalCode()).isEqualTo("28001");
        assertThat(dto.getRole()).isEqualTo(Role.ADMIN);
        assertThat(dto.getActive()).isTrue();
    }

    @Test
    void mapsNullOptionalFields() {
        User user = new User("2", "Luis", "Perez", null, null,
                null, null, null, null);

        UserDto dto = new UserDto(user);

        assertThat(dto.getEmail()).isNull();
        assertThat(dto.getIdentity()).isNull();
        assertThat(dto.getAddress()).isNull();
        assertThat(dto.getCity()).isNull();
        assertThat(dto.getProvince()).isNull();
        assertThat(dto.getPostalCode()).isNull();
        assertThat(dto.getRole()).isNull();
        assertThat(dto.getActive()).isNull();
    }

    @Test
    void emptyConstructorCreatesDtoWithNullFields() {
        UserDto dto = new UserDto();

        assertThat(dto.getId()).isNull();
        assertThat(dto.getName()).isNull();
        assertThat(dto.getFamilyName()).isNull();
        assertThat(dto.getEmail()).isNull();
        assertThat(dto.getIdentity()).isNull();
        assertThat(dto.getAddress()).isNull();
        assertThat(dto.getCity()).isNull();
        assertThat(dto.getProvince()).isNull();
        assertThat(dto.getPostalCode()).isNull();
        assertThat(dto.getRole()).isNull();
        assertThat(dto.getActive()).isNull();
        assertThat(dto.isBillable()).isNull();
    }

    @Test
    void constructorWithFieldsMapsAllProvidedFields() {
        UserDto dto = new UserDto(
                "Oscar",
                "Blasco",
                "oscar@example.com",
                "12345678A",
                "Calle Mayor 1",
                "Madrid",
                "Madrid",
                "28001",
                Role.ADMIN
        );

        assertThat(dto.getId()).isNull();
        assertThat(dto.getName()).isEqualTo("Oscar");
        assertThat(dto.getFamilyName()).isEqualTo("Blasco");
        assertThat(dto.getEmail()).isEqualTo("oscar@example.com");
        assertThat(dto.getIdentity()).isEqualTo("12345678A");
        assertThat(dto.getAddress()).isEqualTo("Calle Mayor 1");
        assertThat(dto.getCity()).isEqualTo("Madrid");
        assertThat(dto.getProvince()).isEqualTo("Madrid");
        assertThat(dto.getPostalCode()).isEqualTo("28001");
        assertThat(dto.getRole()).isEqualTo(Role.ADMIN);
        assertThat(dto.getActive()).isNull();
        assertThat(dto.isBillable()).isNull();
    }

    @Test
    void mapsInactiveAndNotBillableUser() {
        User user = new User("3", "Ana", "Garcia", "ana@example.com", "87654321B",
                "Calle Sol 2", "Madrid", "", "28002", Role.AUTHENTICATED, false);

        UserDto dto = new UserDto(user);

        assertThat(dto.getActive()).isFalse();
        assertThat(dto.isBillable()).isFalse();
    }
}
