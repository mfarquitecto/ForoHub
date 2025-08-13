package com.forohub.domain.respuesta;

public record DatosRespuestaRespuesta(
        Long id,
        String usuario,
        String topico,
        String respuesta) {

    public DatosRespuestaRespuesta(Respuesta respuesta) {
        this(respuesta.getId(),
                respuesta.getUsuario().getLogin(),
                respuesta.getTopico().getTitulo(),
                respuesta.getRespuesta());
    }
}
