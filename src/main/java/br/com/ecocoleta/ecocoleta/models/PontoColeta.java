package br.com.ecocoleta.ecocoleta.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Table(name = "tb_ponto_coleta")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PontoColeta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String endereco;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_residuo")
    private TipoResiduo tipoResiduo;

    @Column(name = "capacidade_maxima_kg")
    private BigDecimal capacidadeMaximaKg;

    @Column(name = "quantidade_atual_kg")
    private BigDecimal quantidadeAtualKg = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    private PontoStatus status = PontoStatus.NORMAL;
}
