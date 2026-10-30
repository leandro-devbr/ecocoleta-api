package br.com.ecocoleta.ecocoleta.repository;

import br.com.ecocoleta.ecocoleta.models.RegistroDescarte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RegistroDescarteRepository extends JpaRepository<RegistroDescarte, Long> {

    // Consulta para obter os registros de descarte de um ponto de coleta específico, ordenados pela data de descarte em ordem decrescente
    List<RegistroDescarte> findByPontoColetaIdOrderByDataDescarteDesc(Long pontoColeta);
}
