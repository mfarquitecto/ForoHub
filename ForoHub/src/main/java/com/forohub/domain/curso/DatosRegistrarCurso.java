package com.forohub.domain.curso;

import jakarta.validation.constraints.NotBlank;

public record DatosRegistrarCurso(
        @NotBlank
        String nombre
) {

}
