package br.com.ecocoleta.ecocoleta.dto.auth;

import br.com.ecocoleta.ecocoleta.models.PerfilUsuario;

public record UsuarioResponse(Long id, String nome, String email, PerfilUsuario perfil) {
}
