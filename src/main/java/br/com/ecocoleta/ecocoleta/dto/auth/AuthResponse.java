package br.com.ecocoleta.ecocoleta.dto.auth;

public record AuthResponse(String token, String tipoToken, long expiraEmSegundos, UsuarioResponse usuario) {
}
