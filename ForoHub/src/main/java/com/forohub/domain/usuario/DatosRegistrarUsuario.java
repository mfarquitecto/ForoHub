package com.forohub.domain.usuario;

import jakarta.validation.constraints.NotBlank;

public record DatosRegistrarUsuario(
        @NotBlank
        String login,
        @NotBlank
        String contrasena,
        @NotBlank
        String email
) {
}
