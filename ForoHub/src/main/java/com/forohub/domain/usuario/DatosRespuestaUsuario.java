package com.forohub.domain.usuario;

public record DatosRespuestaUsuario(
        Long id,
        String login,
        String email
) {

    public DatosRespuestaUsuario(Usuario usuario) {
        this(usuario.getId(),
                usuario.getLogin(),
                usuario.getEmail());
    }
}