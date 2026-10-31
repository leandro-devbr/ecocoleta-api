package br.com.ecocoleta.ecocoleta.dto.ponto;

import br.com.ecocoleta.ecocoleta.models.TipoResiduo;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record PontoRequest(
        @NotBlank(message = "Nome é obrigatório.")
        @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres.") String nome,
        @NotBlank(message = "Endereço é obrigatório.")
        @Size(max = 200, message = "Endereço deve ter no máximo 200 caracteres.") String endereco,
        @NotNull(message = "Tipo de resíduo é obrigatório.") TipoResiduo tipoResiduo,
        @NotNull(message = "Capacidade máxima é obrigatória.")
        @Positive(message = "Capacidade máxima deve ser maior que zero.")
        @Digits(integer = 8, fraction = 2, message = "Capacidade aceita até 8 dígitos inteiros e 2 decimais.")
        BigDecimal capacidadeMaximaKg
) {
}
