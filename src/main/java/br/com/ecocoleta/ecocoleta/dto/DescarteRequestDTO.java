package br.com.ecocoleta.ecocoleta.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record DescarteRequestDTO(
        @NotNull(message = "O ID do ponto de coleta é obrigatório")
        Long pontoColetaId,

        @NotNull(message = "O ID do usuário é obrigatório")
        Long usuarioId,

        @NotNull(message = "A quantidade descartada é obrigatória")
        @Positive(message = "A quantidade deve ser maior que zero")
        BigDecimal quantidadeKg
) {}