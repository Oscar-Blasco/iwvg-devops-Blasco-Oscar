package es.upm.miw.devops.rest.exceptionshandler;

import es.upm.miw.devops.service.AdminUserCannotBeDeactivatedException;
import es.upm.miw.devops.service.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import static org.assertj.core.api.Assertions.assertThat;

class ApiExceptionHandlerTest {

    private final ApiExceptionHandler handler = new ApiExceptionHandler();

    @Test
    void userNotFoundReturnsNotFoundMessage() {
        ErrorMessage result = handler.userNotFound(new UserNotFoundException("42"));

        assertThat(result.getCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(result.getError()).isEqualTo("UserNotFoundException");
        assertThat(result.getMessage()).isEqualTo("User not found: 42");
    }

    @Test
    void adminUserCannotBeDeactivatedReturnsConflictMessage() {
        ErrorMessage result = handler.adminUserCannotBeDeactivated(
                new AdminUserCannotBeDeactivatedException("1")
        );

        assertThat(result.getCode()).isEqualTo(HttpStatus.CONFLICT.value());
        assertThat(result.getError()).isEqualTo("AdminUserCannotBeDeactivatedException");
        assertThat(result.getMessage()).isEqualTo("Admin user cannot be deactivated: 1");
    }

    @Test
    void noResourceFoundReturnsNotFoundMessage() {
        ErrorMessage result = handler.noResourceFoundRequest(
                new ResponseStatusException(HttpStatus.NOT_FOUND));

        assertThat(result.getCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(result.getError()).isEqualTo("RuntimeException");
        assertThat(result.getMessage()).contains("Ruta no encontrada");
    }

    @Test
    void noResourceFoundHandlerAcceptsNoResourceFoundException() {
        ErrorMessage result = handler.noResourceFoundRequest(
                new NoResourceFoundException(HttpMethod.GET, "/missing"));

        assertThat(result.getCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void genericExceptionReturnsInternalServerError() {
        ErrorMessage result = handler.exception(new IllegalStateException("failure"));

        assertThat(result.getCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(result.getError()).isEqualTo("RuntimeException");
        assertThat(result.getMessage()).isEqualTo("ERROR");
    }
}
