package br.com.ecocoleta.ecocoleta.models;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "tb_usuario")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String email;
    private String senha;

    @Enumerated(EnumType.STRING)
    private PerfilUsuario perfil;
}
