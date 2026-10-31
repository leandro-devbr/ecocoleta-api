package br.com.ecocoleta.ecocoleta.service;

import br.com.ecocoleta.ecocoleta.dto.descarte.DescarteRequest;
import br.com.ecocoleta.ecocoleta.dto.descarte.DescarteResponse;
import br.com.ecocoleta.ecocoleta.exception.BusinessConflictException;
import br.com.ecocoleta.ecocoleta.exception.ResourceNotFoundException;
import br.com.ecocoleta.ecocoleta.models.PontoColeta;
import br.com.ecocoleta.ecocoleta.models.PontoStatus;
import br.com.ecocoleta.ecocoleta.models.RegistroDescarte;
import br.com.ecocoleta.ecocoleta.models.Usuario;
import br.com.ecocoleta.ecocoleta.repository.PontoColetaRepository;
import br.com.ecocoleta.ecocoleta.repository.RegistroDescarteRepository;
import br.com.ecocoleta.ecocoleta.repository.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class RegistroDescarteService {

    private final RegistroDescarteRepository descarteRepository;
    private final PontoColetaRepository pontoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PontoColetaService pontoService;

    public RegistroDescarteService(
            RegistroDescarteRepository descarteRepository,
            PontoColetaRepository pontoRepository,
            UsuarioRepository usuarioRepository,
            PontoColetaService pontoService
    ) {
        this.descarteRepository = descarteRepository;
        this.pontoRepository = pontoRepository;
        this.usuarioRepository = usuarioRepository;
        this.pontoService = pontoService;
    }

    @Transactional
    public DescarteResponse create(DescarteRequest request, String email) {
        Usuario usuario = findUserByEmail(email);
        PontoColeta ponto = pontoRepository.findByIdForUpdate(request.pontoColetaId())
                .orElseThrow(() -> new ResourceNotFoundException("Ponto de coleta não encontrado."));

        var novaQuantidade = ponto.getQuantidadeAtualKg().add(request.quantidadeKg());
        if (novaQuantidade.compareTo(ponto.getCapacidadeMaximaKg()) > 0) {
            throw new BusinessConflictException("A quantidade excede a capacidade disponível no ponto de coleta.");
        }

        ponto.setQuantidadeAtualKg(novaQuantidade);
        if (novaQuantidade.compareTo(ponto.getCapacidadeMaximaKg()) >= 0) {
            ponto.setStatus(PontoStatus.CHEIO);
        }
        pontoRepository.save(ponto);

        RegistroDescarte descarte = new RegistroDescarte();
        descarte.setPontoColeta(ponto);
        descarte.setUsuario(usuario);
        descarte.setQuantidadeKg(request.quantidadeKg());
        descarte.setDataDescarte(LocalDateTime.now());
        return toResponse(descarteRepository.save(descarte));
    }

    @Transactional(readOnly = true)
    public PagedModel<DescarteResponse> findByPoint(Long pontoId, int page, int size) {
        if (!pontoRepository.existsById(pontoId)) {
            throw new ResourceNotFoundException("Ponto de coleta não encontrado.");
        }
        Page<DescarteResponse> descartes = descarteRepository.findByPontoColetaIdOrderByDataDescarteDesc(
                pontoId, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dataDescarte"))
        ).map(this::toResponse);
        return new PagedModel<>(descartes);
    }

    private Usuario findUserByEmail(String email) {
        return usuarioRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));
    }

    private DescarteResponse toResponse(RegistroDescarte descarte) {
        return new DescarteResponse(
                descarte.getId(),
                pontoService.toResponse(descarte.getPontoColeta()),
                descarte.getUsuario().getId(),
                descarte.getUsuario().getNome(),
                descarte.getQuantidadeKg(),
                descarte.getDataDescarte()
        );
    }
}
