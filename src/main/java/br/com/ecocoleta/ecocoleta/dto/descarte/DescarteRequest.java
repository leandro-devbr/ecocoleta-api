package br.com.ecocoleta.ecocoleta.dto.descarte;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record DescarteRequest(
        @NotNull(message = "Ponto de coleta é obrigatório.") @Positive(message = "Ponto de coleta inválido.")
        Long pontoColetaId,
        @NotNull(message = "Quantidade é obrigatória.")
        @DecimalMin(value = "0.01", message = "Quantidade deve ser de pelo menos 0,01 kg.")
        @Digits(integer = 8, fraction = 2, message = "Quantidade aceita até 8 dígitos inteiros e 2 decimais.")
        BigDecimal quantidadeKg
) {
}
