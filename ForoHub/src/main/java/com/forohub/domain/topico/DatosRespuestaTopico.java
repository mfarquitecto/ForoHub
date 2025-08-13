package com.forohub.domain.topico;

public record DatosRespuestaTopico(
        Long id,
        String usuario,
        String curso,
        String titulo,
        String mensaje
) {

    public DatosRespuestaTopico(Topico topico) {
        this(topico.getId(),
                topico.getUsuario().getLogin(),
                topico.getCurso().getNombre(),
                topico.getTitulo(),
                topico.getMensaje());
    }
}
