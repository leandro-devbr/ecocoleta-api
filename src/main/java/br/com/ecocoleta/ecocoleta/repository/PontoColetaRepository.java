package br.com.ecocoleta.ecocoleta.repository;

import br.com.ecocoleta.ecocoleta.models.PontoColeta;
import br.com.ecocoleta.ecocoleta.models.PontoStatus;
import br.com.ecocoleta.ecocoleta.models.TipoResiduo;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PontoColetaRepository extends JpaRepository<PontoColeta, Long> {
    Page<PontoColeta> findByTipoResiduo(TipoResiduo tipoResiduo, Pageable pageable);

    Page<PontoColeta> findByStatus(PontoStatus status, Pageable pageable);

    Page<PontoColeta> findByTipoResiduoAndStatus(TipoResiduo tipoResiduo, PontoStatus status, Pageable pageable);

    List<PontoColeta> findByStatus(PontoStatus status);

    List<PontoColeta> findByStatusOrderByIdAsc(PontoStatus status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select ponto from PontoColeta ponto where ponto.id = :id")
    Optional<PontoColeta> findByIdForUpdate(@Param("id") Long id);
}
