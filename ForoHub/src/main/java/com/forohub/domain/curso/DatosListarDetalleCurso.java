package com.forohub.domain.curso;

import com.forohub.domain.topico.DatosResumenTopico;
import com.forohub.domain.topico.Topico;
import org.springframework.data.domain.Page;

public record DatosListarDetalleCurso(
        Long id,
        String nombre,
        Page<DatosResumenTopico> topicos
) {

    public DatosListarDetalleCurso(Curso curso, Page<Topico> topicosPage) {
        this(
                curso.getId(),
                curso.getNombre(),
                topicosPage.map(DatosResumenTopico::new)
        );
    }
}

