package com.forohub.domain.topico;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.forohub.domain.respuesta.Respuesta;

import java.time.LocalDateTime;


public record DatosListarTopico(
        Long id,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
        LocalDateTime fecha_creacion,
        String usuario,
        String curso,
        String titulo,
        String mensaje,
        Status status,
        long numeroRespuestas
) {
    public DatosListarTopico(Topico topico) {
        this(
                topico.getId(),
                topico.getFecha_creacion(),
                topico.getUsuario().getLogin(),
                topico.getCurso().getNombre(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getStatus(),
                topico.getRespuestas().stream().filter(Respuesta::getActiva).count()
        );
    }
}
