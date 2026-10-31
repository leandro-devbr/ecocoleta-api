package br.com.ecocoleta.ecocoleta.dto.ponto;

import br.com.ecocoleta.ecocoleta.models.PontoStatus;
import br.com.ecocoleta.ecocoleta.models.TipoResiduo;

import java.math.BigDecimal;

public record PontoResponse(
        Long id,
        String nome,
        String endereco,
        TipoResiduo tipoResiduo,
        BigDecimal capacidadeMaximaKg,
        BigDecimal quantidadeAtualKg,
        PontoStatus status
) {
}
