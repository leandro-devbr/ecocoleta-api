package br.com.ecocoleta.ecocoleta.service;

import br.com.ecocoleta.ecocoleta.dto.PontoColetaRequestDTO;
import br.com.ecocoleta.ecocoleta.dto.PontoColetaResponseDTO;
import br.com.ecocoleta.ecocoleta.exceptions.*;
import br.com.ecocoleta.ecocoleta.models.PontoColeta;
import br.com.ecocoleta.ecocoleta.models.PontoStatus;
import br.com.ecocoleta.ecocoleta.models.TipoResiduo;
import br.com.ecocoleta.ecocoleta.repository.PontoColetaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class PontoColetaService {

    private static final BigDecimal LIMITE_CAPACIDADE_KG = new BigDecimal("10000");

    private final PontoColetaRepository repository;

    public PontoColetaService(PontoColetaRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Page<PontoColetaResponseDTO> listar(TipoResiduo tipo, PontoStatus status, Pageable pageable) {
        Page<PontoColeta> pagina;
        if (tipo != null && status != null) {
            pagina = repository.findByTipoResiduoAndStatus(tipo, status, pageable);
        } else if (tipo != null) {
            pagina = repository.findByTipoResiduo(tipo, pageable);
        } else if (status != null) {
            pagina = repository.findByStatus(status, pageable);
        } else {
            pagina = repository.findAll(pageable);
        }
        return pagina.map(PontoColetaResponseDTO::from);
    }

    @Transactional(readOnly = true)
    public PontoColetaResponseDTO buscar(Long id) {
        return PontoColetaResponseDTO.from(buscarEntidade(id));
    }

    public PontoColetaResponseDTO criar(PontoColetaRequestDTO dto) {
        validarCapacidade(dto.capacidadeMaximaKg());
        PontoColeta ponto = new PontoColeta();
        preencher(ponto, dto);
        // quantidadeAtualKg = 0 e status = NORMAL já vêm como padrão na entidade
        return PontoColetaResponseDTO.from(repository.save(ponto));
    }

    public PontoColetaResponseDTO atualizar(Long id, PontoColetaRequestDTO dto) {
        validarCapacidade(dto.capacidadeMaximaKg());
        PontoColeta ponto = buscarEntidade(id);
        preencher(ponto, dto);
        // a capacidade pode ter mudado: recalcula o status
        if (ponto.getQuantidadeAtualKg().compareTo(ponto.getCapacidadeMaximaKg()) >= 0) {
            ponto.setStatus(PontoStatus.CHEIO);
        } else {
            ponto.setStatus(PontoStatus.NORMAL);
        }
        return PontoColetaResponseDTO.from(repository.save(ponto));
    }

    public void deletar(Long id) {
        repository.delete(buscarEntidade(id));
    }

    public PontoColetaResponseDTO registrarColeta(Long id) {
        PontoColeta ponto = buscarEntidade(id);
        ponto.setQuantidadeAtualKg(BigDecimal.ZERO);
        ponto.setStatus(PontoStatus.NORMAL);
        return PontoColetaResponseDTO.from(repository.save(ponto));
    }

    @Transactional(readOnly = true)
    public List<PontoColetaResponseDTO> alertas() {
        return repository.findByStatus(PontoStatus.CHEIO).stream()
                .map(PontoColetaResponseDTO::from)
                .toList();
    }

    private void preencher(PontoColeta ponto, PontoColetaRequestDTO dto) {
        ponto.setNome(dto.nome());
        ponto.setEndereco(dto.endereco());
        ponto.setTipoResiduo(dto.tipoResiduo());
        ponto.setCapacidadeMaximaKg(dto.capacidadeMaximaKg());
    }

    private void validarCapacidade(BigDecimal capacidade) {
        if (capacidade.compareTo(LIMITE_CAPACIDADE_KG) > 0) {
            throw new RegraNegocioException("A capacidade máxima não pode exceder 10.000 Kg por ponto de coleta.");
        }
    }

    private PontoColeta buscarEntidade(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new RecursoNaoEncontradoException("Ponto de coleta não encontrado: " + id));
    }
}