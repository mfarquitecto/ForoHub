package com.forohub.domain.usuario;

public record DatosListarUsuario (
        Long id,
        String login,
        String email
) {
    public DatosListarUsuario(Usuario usuario) {
        this(usuario.getId(),
                usuario.getLogin(),
                usuario.getEmail());
    }
}