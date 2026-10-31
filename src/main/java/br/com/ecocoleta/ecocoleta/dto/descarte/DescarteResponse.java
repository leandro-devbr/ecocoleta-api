package br.com.ecocoleta.ecocoleta.dto.descarte;

import br.com.ecocoleta.ecocoleta.dto.ponto.PontoResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DescarteResponse(
        Long id,
        PontoResponse pontoColeta,
        Long usuarioId,
        String nomeUsuario,
        BigDecimal quantidadeKg,
        LocalDateTime dataDescarte
) {
}
