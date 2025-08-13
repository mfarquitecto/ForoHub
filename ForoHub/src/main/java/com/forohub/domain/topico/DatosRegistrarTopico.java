package com.forohub.domain.topico;

import jakarta.validation.constraints.*;

public record DatosRegistrarTopico(
        @NotNull
        Long idUsuario,
        @NotNull
        Long idCurso,
        @NotBlank
        String titulo,
        @NotBlank
        String mensaje
) {
}
