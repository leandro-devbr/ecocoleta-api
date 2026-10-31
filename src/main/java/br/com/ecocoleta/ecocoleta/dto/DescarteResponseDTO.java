package br.com.ecocoleta.ecocoleta.dto;

import br.com.ecocoleta.ecocoleta.models.PontoColeta;
import br.com.ecocoleta.ecocoleta.models.PontoStatus;
import br.com.ecocoleta.ecocoleta.models.RegistroDescarte;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DescarteResponseDTO(
        Long id,
        Long pontoColetaId,
        String nomePonto,
        Long usuarioId,
        BigDecimal quantidadeKg,
        LocalDateTime dataDescarte,
        PontoStatus statusPonto
) {
    public static DescarteResponseDTO from(RegistroDescarte d) {
        PontoColeta p = d.getPontoColeta();
        return new DescarteResponseDTO(d.getId(), p.getId(), p.getNome(), d.getUsuario().getId(),
                d.getQuantidadeKg(), d.getDataDescarte(), p.getStatus());
    }
}