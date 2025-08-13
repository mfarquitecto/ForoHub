package com.forohub.domain.respuesta;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record DatosResumenRespuesta(
        Long id,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
        LocalDateTime fecha_creacion,
        String usuario,
        String respuesta,
        Boolean solucion
) {

    public DatosResumenRespuesta(Respuesta respuesta) {
        this(
                respuesta.getId(),
                respuesta.getFecha_creacion(),
                respuesta.getUsuario().getLogin(),
                respuesta.getRespuesta(),
                respuesta.getSolucion()
        );
    }
}
