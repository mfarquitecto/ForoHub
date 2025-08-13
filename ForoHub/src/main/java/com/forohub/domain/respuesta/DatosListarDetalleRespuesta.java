package com.forohub.domain.respuesta;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public record DatosListarDetalleRespuesta (
        Long id,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
        LocalDateTime fecha_creacion,
        String usuario,
        String curso,
        String topico,
        String respuesta,
        Boolean solucion
){
    public DatosListarDetalleRespuesta (Respuesta respuesta) {
        this(
                respuesta.getId(),
                respuesta.getFecha_creacion(),
                respuesta.getUsuario().getLogin(),
                respuesta.getTopico().getCurso().getNombre(),
                respuesta.getTopico().getTitulo(),
                respuesta.getRespuesta(),
                respuesta.getSolucion()
        );
    }
}
