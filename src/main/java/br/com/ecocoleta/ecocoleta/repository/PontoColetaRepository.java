package br.com.ecocoleta.ecocoleta.repository;

import br.com.ecocoleta.ecocoleta.models.PontoColeta;
import br.com.ecocoleta.ecocoleta.models.PontoStatus;
import br.com.ecocoleta.ecocoleta.models.TipoResiduo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PontoColetaRepository extends JpaRepository<PontoColeta, Long> {
    Page<PontoColeta> findByTipoResiduo(TipoResiduo tipoResiduo, Pageable pageable);
    Page<PontoColeta> findByStatus(PontoStatus status, Pageable pageable);
    Page<PontoColeta> findByTipoResiduoAndStatus(TipoResiduo tipoResiduo, PontoStatus status, Pageable pageable);
    List<PontoColeta> findByStatus(PontoStatus status);
}
