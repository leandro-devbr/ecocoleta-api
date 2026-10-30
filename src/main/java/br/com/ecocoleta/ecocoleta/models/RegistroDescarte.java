package br.com.ecocoleta.ecocoleta.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Table(name = "tb_registro_descarte")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistroDescarte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "ponto_coleta_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private PontoColeta pontoColeta;

    @JoinColumn(name = "usuario_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario usuario;

    @Column(name = "quantidade_kg")
    private BigDecimal quantidadeKg;

    @Column(name = "data_descarte")
    private LocalDateTime dataDescarte;
}
