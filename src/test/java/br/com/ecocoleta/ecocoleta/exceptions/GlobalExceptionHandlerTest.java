package br.com.ecocoleta.ecocoleta.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.BadCredentialsException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void mapsMissingResourcesToNotFound() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/pontos-coleta/999");

        var response = handler.handleNotFound(
                new RecursoNaoEncontradoException("Ponto de coleta não encontrado: 999"),
                request
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ApiErrorResponse body = response.getBody();
        assertNotNull(body);
        assertEquals(404, body.status());
        assertEquals("/pontos-coleta/999", body.path());
    }

    @Test
    void mapsBusinessRuleViolationsToBadRequest() {
        HttpServletRequest request = new MockHttpServletRequest();

        var response = handler.handleBusinessRule(
                new RegraNegocioException("A capacidade solicitada é inválida."),
                request
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ApiErrorResponse body = response.getBody();
        assertNotNull(body);
        assertEquals(400, body.status());
    }

    @Test
    void mapsAuthenticationFailuresToUnauthorized() {
        HttpServletRequest request = new MockHttpServletRequest();

        var response = handler.handleAuthenticationFailure(
                new BadCredentialsException("Bad credentials"),
                request
        );

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        ApiErrorResponse body = response.getBody();
        assertNotNull(body);
        assertEquals(401, body.status());
        assertEquals("E-mail ou senha inválidos.", body.message());
    }
}
