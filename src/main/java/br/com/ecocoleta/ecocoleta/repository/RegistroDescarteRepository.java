package br.com.ecocoleta.ecocoleta.repository;

import br.com.ecocoleta.ecocoleta.models.RegistroDescarte;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistroDescarteRepository extends JpaRepository<RegistroDescarte, Long> {
    Page<RegistroDescarte> findByUsuarioIdOrderByDataDescarteDesc(Long usuarioId, Pageable pageable);

    Page<RegistroDescarte> findByPontoColetaIdOrderByDataDescarteDesc(Long pontoColetaId, Pageable pageable);
}
