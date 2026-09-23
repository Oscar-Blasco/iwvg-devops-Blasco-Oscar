package es.upm.miw.devops.functionaltests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserResourceFT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void readsExistingSeededUser() throws Exception {
        mockMvc.perform(get("/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Oscar"))
                .andExpect(jsonPath("$.familyName").value("Blasco"))
                .andExpect(jsonPath("$.email").value("oscar.blasco@example.com"))
                .andExpect(jsonPath("$.identity").value("12345678A"))
                .andExpect(jsonPath("$.address").value("Calle Mayor 1"))
                .andExpect(jsonPath("$.city").value("Madrid"))
                .andExpect(jsonPath("$.province").value("Madrid"))
                .andExpect(jsonPath("$.postalCode").value("28001"))
                .andExpect(jsonPath("$.role").value("ADMIN"))
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    void returnsNotFoundForUnknownUser() throws Exception {
        mockMvc.perform(get("/user/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("UserNotFoundException"))
                .andExpect(jsonPath("$.message").value("User not found: 999"));
    }

    @Test
    void searchWithoutFiltersReturnsAllUsers() throws Exception {
        mockMvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4));
    }

    @Test
    void searchBillableUsers() throws Exception {
        mockMvc.perform(get("/user")
                        .param("billable", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].billable").value(true))
                .andExpect(jsonPath("$[1].billable").value(true))
                .andExpect(jsonPath("$[2].billable").value(true));
    }

    @Test
    void searchNonBillableUsers() throws Exception {
        mockMvc.perform(get("/user")
                        .param("billable", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value("3"))
                .andExpect(jsonPath("$[0].billable").value(false));
    }

    @Test
    void invalidBillableParameterReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/user")
                        .param("billable", "not-a-boolean"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testToggleActiveFromTrueToFalse() throws Exception {
        mockMvc.perform(put("/user/1/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.active").value(false));
    }

    @Test
    void testToggleActiveFromFalseToTrue() throws Exception {
        mockMvc.perform(put("/user/3/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("3"))
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    void testToggleActiveForNonExistingUser() throws Exception {
        mockMvc.perform(put("/user/999/active"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("UserNotFoundException"))
                .andExpect(jsonPath("$.message").value("User not found: 999"));
    }

    @Test
    void testDeleteExistingUser() throws Exception {
        mockMvc.perform(delete("/user/4"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/user/4"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteNonExistingUser() throws Exception {
        mockMvc.perform(delete("/user/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("UserNotFoundException"))
                .andExpect(jsonPath("$.message").value("User not found: 999"));
    }

    @Test
    void testUpdateActiveBulk() throws Exception {
        String body = """
            [
              {
                "id": "3",
                "active": true
              },
              {
                "id": "2",
                "active": true
              }
            ]
            """;

        mockMvc.perform(patch("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("3"))
                .andExpect(jsonPath("$[0].active").value(true))
                .andExpect(jsonPath("$[1].id").value("2"))
                .andExpect(jsonPath("$[1].active").value(true));
    }

    @Test
    void testUpdateActiveBulkUserNotFound() throws Exception {
        String body = """
            [
              {
                "id": "999",
                "active": false
              }
            ]
            """;

        mockMvc.perform(patch("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdate() throws Exception {
        String body = """
            {
              "name": "Marta",
              "familyName": "Lopes",
              "email": "Marta.lopes@sadas.sa",
              "identity": "6832163",
              "address": "calle mala 123",
              "city": "Madrid",
              "province": "Madrid",
              "postalCode": "28001"
            }
            """;

        mockMvc.perform(put("/user/4")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("4"))
                .andExpect(jsonPath("$.name").value("Marta"))
                .andExpect(jsonPath("$.familyName").value("Lopes"))
                .andExpect(jsonPath("$.email").value("Marta.lopes@sadas.sa"))
                .andExpect(jsonPath("$.identity").value("6832163"))
                .andExpect(jsonPath("$.address").value("calle mala 123"))
                .andExpect(jsonPath("$.city").value("Madrid"))
                .andExpect(jsonPath("$.province").value("Madrid"))
                .andExpect(jsonPath("$.postalCode").value("28001"))
                .andExpect(jsonPath("$.role").exists())
                .andExpect(jsonPath("$.active").exists())
                .andExpect(jsonPath("$.billable").exists())
                .andExpect(jsonPath("$.isBillable").doesNotExist());
    }

    @Test
    void testUpdateUserNotFound() throws Exception {
        String body = """
            {
              "name": "Marta",
              "familyName": "Lopes",
              "email": "Marta.lopes@sadas.sa",
              "identity": "6832163",
              "address": "calle mala 123",
              "city": "Madrid",
              "province": "Madrid",
              "postalCode": "28001"
            }
            """;

        mockMvc.perform(put("/user/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNotFound());
     }
}
