package com.forohub.domain.usuario;

public record DatosActualizarUsuario(
        String contrasena,
        Boolean activo
) {
    public DatosActualizarUsuario(Usuario usuario) {
        this(
                usuario.getContrasena(),
                usuario.getActivo()
        );
    }
}
