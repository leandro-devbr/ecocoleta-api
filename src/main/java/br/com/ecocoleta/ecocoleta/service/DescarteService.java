package br.com.ecocoleta.ecocoleta.service;

import br.com.ecocoleta.ecocoleta.dto.DescarteRequestDTO;
import br.com.ecocoleta.ecocoleta.dto.DescarteResponseDTO;
import br.com.ecocoleta.ecocoleta.exceptions.*;
import br.com.ecocoleta.ecocoleta.models.PontoColeta;
import br.com.ecocoleta.ecocoleta.models.PontoStatus;
import br.com.ecocoleta.ecocoleta.models.RegistroDescarte;
import br.com.ecocoleta.ecocoleta.models.Usuario;
import br.com.ecocoleta.ecocoleta.repository.PontoColetaRepository;
import br.com.ecocoleta.ecocoleta.repository.RegistroDescarteRepository;
import br.com.ecocoleta.ecocoleta.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class DescarteService {

    // Regra extra definida pelo Membro 2: limite de peso por descarte individual
    private static final BigDecimal LIMITE_DESCARTE_KG = new BigDecimal("500");

    private final RegistroDescarteRepository descarteRepository;
    private final PontoColetaRepository pontoRepository;
    private final UsuarioRepository usuarioRepository;

    public DescarteService(RegistroDescarteRepository descarteRepository,
                           PontoColetaRepository pontoRepository,
                           UsuarioRepository usuarioRepository) {
        this.descarteRepository = descarteRepository;
        this.pontoRepository = pontoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public DescarteResponseDTO registrar(DescarteRequestDTO dto) {
        if (dto.quantidadeKg().compareTo(LIMITE_DESCARTE_KG) > 0) {
            throw new RegraNegocioException(
                    "Não é permitido descartar mais de 500 Kg de uma só vez por medida de segurança ambiental.");
        }

        PontoColeta ponto = pontoRepository.findById(dto.pontoColetaId()).orElseThrow(
                () -> new RecursoNaoEncontradoException("Ponto de coleta não encontrado: " + dto.pontoColetaId()));
        Usuario usuario = usuarioRepository.findById(dto.usuarioId()).orElseThrow(
                () -> new RecursoNaoEncontradoException("Usuário não encontrado: " + dto.usuarioId()));

        if (ponto.getStatus() == PontoStatus.CHEIO) {
            throw new RegraNegocioException(
                    "Este ponto está cheio. Registre a coleta antes de novos descartes.");
        }

        BigDecimal novaQuantidade = ponto.getQuantidadeAtualKg().add(dto.quantidadeKg());
        ponto.setQuantidadeAtualKg(novaQuantidade);
        if (novaQuantidade.compareTo(ponto.getCapacidadeMaximaKg()) >= 0) {
            ponto.setStatus(PontoStatus.CHEIO);
        }
        pontoRepository.save(ponto);

        RegistroDescarte registro = new RegistroDescarte();
        registro.setPontoColeta(ponto);
        registro.setUsuario(usuario);
        registro.setQuantidadeKg(dto.quantidadeKg());
        registro.setDataDescarte(LocalDateTime.now());
        return DescarteResponseDTO.from(descarteRepository.save(registro));
    }

    @Transactional(readOnly = true)
    public List<DescarteResponseDTO> historico(Long pontoId) {
        if (!pontoRepository.existsById(pontoId)) {
            throw new RecursoNaoEncontradoException("Ponto de coleta não encontrado: " + pontoId);
        }
        return descarteRepository.findByPontoColetaIdOrderByDataDescarteDesc(pontoId).stream()
                .map(DescarteResponseDTO::from)
                .toList();
    }
}