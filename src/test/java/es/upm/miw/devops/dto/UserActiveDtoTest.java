package es.upm.miw.devops.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserActiveDtoTest {

    @Test
    void testDefaultConstructor() {
        UserActiveDto userActiveDto = new UserActiveDto();

        assertNull(userActiveDto.getId());
        assertNull(userActiveDto.getActive());
    }

    @Test
    void testGettersAndSetters() {
        UserActiveDto userActiveDto = new UserActiveDto();

        userActiveDto.setId("1");
        userActiveDto.setActive(false);

        assertEquals("1", userActiveDto.getId());
        assertFalse(userActiveDto.getActive());
    }

    @Test
    void testSetActiveTrue() {
        UserActiveDto userActiveDto = new UserActiveDto();

        userActiveDto.setActive(true);

        assertTrue(userActiveDto.getActive());
    }
}
