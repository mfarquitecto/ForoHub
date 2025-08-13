package com.forohub.domain.curso;

import com.forohub.domain.topico.Topico;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Table(name = "cursos")
@Entity(name = "Curso")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")

public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private Boolean activo;

    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Topico> topicos;

    public Curso(DatosRegistrarCurso datos) {
        this.nombre = datos.nombre();
        this.activo = true;
    }

    public void actualizarCurso(DatosActualizarCurso datos) {
        if (datos.nombre() != null && !datos.nombre().isBlank()) {
            this.nombre = datos.nombre();
        }
        if (datos.activo() != null) {
            if (datos.activo()) {
                this.activarCurso();
            } else {
                this.desactivarCurso();
            }
        }
    }

    public void activarCurso() {
        this.activo = true;
        if (topicos != null) {
            for (Topico topico : topicos) {
                topico.activarTopico();
            }
        }
    }

    public void desactivarCurso() {
        this.activo = false;
        if (topicos != null) {
            for (Topico topico : topicos) {
                topico.desactivarTopico();
            }
        }
    }
}
