package br.com.ecocoleta.ecocoleta.dto;

import br.com.ecocoleta.ecocoleta.models.PerfilUsuario;

public record UsuarioResponseDTO(Long id, String nome, String email, PerfilUsuario perfil) {}