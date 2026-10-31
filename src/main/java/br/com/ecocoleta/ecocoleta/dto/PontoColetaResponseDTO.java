package br.com.ecocoleta.ecocoleta.dto;

import br.com.ecocoleta.ecocoleta.models.PontoColeta;
import br.com.ecocoleta.ecocoleta.models.PontoStatus;
import br.com.ecocoleta.ecocoleta.models.TipoResiduo;

import java.math.BigDecimal;

public record PontoColetaResponseDTO(
        Long id,
        String nome,
        String endereco,
        TipoResiduo tipoResiduo,
        BigDecimal capacidadeMaximaKg,
        BigDecimal quantidadeAtualKg,
        PontoStatus status
) {
    public static PontoColetaResponseDTO from(PontoColeta p) {
        return new PontoColetaResponseDTO(p.getId(), p.getNome(), p.getEndereco(), p.getTipoResiduo(),
                p.getCapacidadeMaximaKg(), p.getQuantidadeAtualKg(), p.getStatus());
    }
}