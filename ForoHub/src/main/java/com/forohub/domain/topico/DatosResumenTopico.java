package com.forohub.domain.topico;

public record DatosResumenTopico(
        long id,
        String titulo
) {

    public DatosResumenTopico(Topico topico) {
        this(
                topico.getId(),
                topico.getTitulo()
        );
    }
}


