package com.forohub.domain.curso;

public record DatosRespuestaCurso(
        Long id,
        String nombre) {

    public DatosRespuestaCurso(Curso curso) {
        this(curso.getId(),
                curso.getNombre());
    }
}


