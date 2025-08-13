package com.forohub.domain.respuesta;

import jakarta.validation.constraints.*;

public record DatosRegistrarRespuesta(
        @NotNull
        Long idUsuario,
        @NotNull
        Long idTopico,
        @NotBlank
        String respuesta
) {
}

