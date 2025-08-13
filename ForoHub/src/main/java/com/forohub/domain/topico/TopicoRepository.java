package com.forohub.domain.topico;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TopicoRepository extends JpaRepository<Topico, Long> {

    Page<Topico> findByCursoIdAndActivoTrue(Long cursoId, Pageable paginacion);

    @Query("""
                SELECT new com.forohub.domain.topico.DatosListarTopico(
                    t.id,
                    t.fecha_creacion,
                    t.usuario.login,
                    t.curso.nombre,
                    t.titulo,
                    t.mensaje,
                    t.status,
                    COUNT(r.id)
                )
                FROM Topico t LEFT JOIN t.respuestas r ON r.activa = true
                WHERE t.activo = true
                GROUP BY t.id, t.fecha_creacion, t.usuario.login, t.curso.nombre, t.titulo, t.mensaje, t.status
            """)
    Page<DatosListarTopico> listarTopicosConContador(Pageable paginacion);

    @Query("""
        SELECT new com.forohub.domain.topico.DatosListarTopico(
            t.id,
            t.fecha_creacion,
            t.usuario.login,
            t.curso.nombre,
            t.titulo,
            t.mensaje,
            t.status,
            (SELECT COUNT(r) FROM Respuesta r WHERE r.topico = t AND r.activa = true)
        )
        FROM Topico t
        WHERE t.status = :status AND t.activo = true
    """)
    Page<DatosListarTopico> findByStatusAndActivoTrue(Status status, Pageable paginacion);

    @Query("""
        select new com.forohub.domain.topico.DatosListarTopico(t)
        from Topico t
        where (:cursoId is null or t.curso.id = :cursoId)
          and (:ano is null or function('year', t.fecha_creacion) = :ano)
          and t.activo = true
    """)
    Page<DatosListarTopico> findByCursoAndAno(
            @Param("cursoId") Long cursoId,
            @Param("ano") Integer ano,
            Pageable pageable
    );
}






