package br.com.ecocoleta.ecocoleta.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtServiceTest {

    private final JwtService jwtService =
            new JwtService("MDEyMzQ1Njc4OWFiY2RlZjAxMjM0NTY3ODlhYmNkZWY=");

    @Test
    void acceptsTokenForItsUser() {
        UserDetails user = user("user@ecocoleta.com");
        String token = jwtService.generateToken(user);

        assertTrue(jwtService.isTokenValid(token, user));
    }

    @Test
    void rejectsTokenForAnotherUser() {
        String token = jwtService.generateToken(user("user@ecocoleta.com"));

        assertFalse(jwtService.isTokenValid(token, user("other@ecocoleta.com")));
    }

    @Test
    void rejectsMalformedToken() {
        assertFalse(jwtService.isTokenValid("not.a.jwt", user("user@ecocoleta.com")));
    }

    private UserDetails user(String email) {
        return User.withUsername(email).password("unused").roles("USER").build();
    }
}
