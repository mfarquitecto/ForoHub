package com.forohub.domain.curso;

import jakarta.validation.constraints.*;

public record DatosActualizarCurso(
        String nombre,
        Boolean activo
) {

    public DatosActualizarCurso(Curso curso) {
        this(
                curso.getNombre(),
                curso.getActivo()
        );
    }
}
