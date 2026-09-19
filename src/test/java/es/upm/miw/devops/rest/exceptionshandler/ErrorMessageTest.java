package es.upm.miw.devops.rest.exceptionshandler;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ErrorMessageTest {

    @Test
    void exposesErrorMessageAndCode() {
        ErrorMessage errorMessage = new ErrorMessage(
                new IllegalArgumentException("bad request"), 400);

        assertThat(errorMessage.getError()).isEqualTo("IllegalArgumentException");
        assertThat(errorMessage.getMessage()).isEqualTo("bad request");
        assertThat(errorMessage.getCode()).isEqualTo(400);
        assertThat(errorMessage.toString())
                .contains("IllegalArgumentException")
                .contains("bad request")
                .contains("400");
    }
}
