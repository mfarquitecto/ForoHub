package com.forohub.domain.topico;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.forohub.domain.respuesta.DatosResumenRespuesta;
import com.forohub.domain.respuesta.Respuesta;
import java.time.LocalDateTime;
import java.util.List;

public record DatosListarDetalleTopico(
        Long id,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
        LocalDateTime fecha_creacion,
        String usuario,
        String curso,
        String titulo,
        String mensaje,
        Status status,
        List<DatosResumenRespuesta> respuestas
) {

    public DatosListarDetalleTopico(Topico topico) {
        this(
                topico.getId(),
                topico.getFecha_creacion(),
                topico.getUsuario().getLogin(),
                topico.getCurso().getNombre(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getStatus(),
                topico.getRespuestas().stream()
                        .filter(Respuesta::getActiva)
                        .map(DatosResumenRespuesta::new)
                        .toList()
        );
    }
}



