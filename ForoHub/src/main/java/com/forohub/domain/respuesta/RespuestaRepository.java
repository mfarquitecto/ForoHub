package com.forohub.domain.respuesta;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RespuestaRepository extends JpaRepository<Respuesta, Long> {
    Page<Respuesta> findAllByActivaTrue(Pageable paginacion);

    boolean existsByRespuestaAndTopicoIdAndIdNot(String respuesta, Long topicoId, Long id);

}
