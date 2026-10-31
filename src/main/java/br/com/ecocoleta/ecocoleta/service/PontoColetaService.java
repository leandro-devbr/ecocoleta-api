package br.com.ecocoleta.ecocoleta.service;

import br.com.ecocoleta.ecocoleta.dto.ponto.PontoRequest;
import br.com.ecocoleta.ecocoleta.dto.ponto.PontoResponse;
import br.com.ecocoleta.ecocoleta.exception.BusinessConflictException;
import br.com.ecocoleta.ecocoleta.exception.ResourceNotFoundException;
import br.com.ecocoleta.ecocoleta.models.PontoColeta;
import br.com.ecocoleta.ecocoleta.models.PontoStatus;
import br.com.ecocoleta.ecocoleta.models.TipoResiduo;
import br.com.ecocoleta.ecocoleta.repository.PontoColetaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class PontoColetaService {

    private final PontoColetaRepository pontoRepository;

    public PontoColetaService(PontoColetaRepository pontoRepository) {
        this.pontoRepository = pontoRepository;
    }

    @Transactional(readOnly = true)
    public PagedModel<PontoResponse> findAll(TipoResiduo tipoResiduo, PontoStatus status, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));
        Page<PontoColeta> pontos;
        if (tipoResiduo != null && status != null) {
            pontos = pontoRepository.findByTipoResiduoAndStatus(tipoResiduo, status, pageable);
        } else if (tipoResiduo != null) {
            pontos = pontoRepository.findByTipoResiduo(tipoResiduo, pageable);
        } else if (status != null) {
            pontos = pontoRepository.findByStatus(status, pageable);
        } else {
            pontos = pontoRepository.findAll(pageable);
        }
        return new PagedModel<>(pontos.map(this::toResponse));
    }

    @Transactional(readOnly = true)
    public PontoResponse findById(Long id) {
        return toResponse(getPoint(id));
    }

    @Transactional
    public PontoResponse create(PontoRequest request) {
        PontoColeta ponto = new PontoColeta();
        ponto.setNome(request.nome().trim());
        ponto.setEndereco(request.endereco().trim());
        ponto.setTipoResiduo(request.tipoResiduo());
        ponto.setCapacidadeMaximaKg(request.capacidadeMaximaKg());
        ponto.setQuantidadeAtualKg(BigDecimal.ZERO);
        ponto.setStatus(PontoStatus.NORMAL);
        return toResponse(pontoRepository.save(ponto));
    }

    @Transactional
    public PontoResponse update(Long id, PontoRequest request) {
        PontoColeta ponto = getPoint(id);
        if (request.capacidadeMaximaKg().compareTo(ponto.getQuantidadeAtualKg()) < 0) {
            throw new BusinessConflictException("A capacidade não pode ser menor que a quantidade já armazenada.");
        }
        ponto.setNome(request.nome().trim());
        ponto.setEndereco(request.endereco().trim());
        ponto.setTipoResiduo(request.tipoResiduo());
        ponto.setCapacidadeMaximaKg(request.capacidadeMaximaKg());
        ponto.setStatus(ponto.getQuantidadeAtualKg().compareTo(request.capacidadeMaximaKg()) >= 0
                ? PontoStatus.CHEIO : PontoStatus.NORMAL);
        return toResponse(pontoRepository.save(ponto));
    }

    @Transactional
    public PontoResponse completeCollection(Long id) {
        PontoColeta ponto = pontoRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ponto de coleta não encontrado."));
        ponto.setQuantidadeAtualKg(BigDecimal.ZERO);
        ponto.setStatus(PontoStatus.NORMAL);
        return toResponse(pontoRepository.save(ponto));
    }

    @Transactional(readOnly = true)
    public java.util.List<PontoResponse> findFull() {
        return pontoRepository.findByStatusOrderByIdAsc(PontoStatus.CHEIO).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public void delete(Long id) {
        PontoColeta ponto = getPoint(id);
        pontoRepository.delete(ponto);
    }

    @Transactional(readOnly = true)
    public PontoColeta getPoint(Long id) {
        return pontoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ponto de coleta não encontrado."));
    }

    public PontoResponse toResponse(PontoColeta ponto) {
        return new PontoResponse(
                ponto.getId(), ponto.getNome(), ponto.getEndereco(), ponto.getTipoResiduo(),
                ponto.getCapacidadeMaximaKg(), ponto.getQuantidadeAtualKg(), ponto.getStatus()
        );
    }
}
