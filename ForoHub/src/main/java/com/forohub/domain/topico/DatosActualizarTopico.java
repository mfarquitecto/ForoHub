package com.forohub.domain.topico;

public record DatosActualizarTopico(
        String mensaje,
        Status status,
        Boolean activo
) {
    public DatosActualizarTopico(Topico topico) {
        this(
                topico.getMensaje(),
                topico.getStatus(),
                topico.getActivo()
        );
    }
}