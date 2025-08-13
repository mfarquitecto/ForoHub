package com.forohub.domain.respuesta;

public record DatosActualizarRespuesta(
        String respuesta,
        Boolean solucion,
        Boolean activa
) {
    public DatosActualizarRespuesta(Respuesta respuesta) {
        this(
                respuesta.getRespuesta(),
                respuesta.getSolucion(),
                respuesta.getActiva()
        );
    }
}

