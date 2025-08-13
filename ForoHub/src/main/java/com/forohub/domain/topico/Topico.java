package com.forohub.domain.topico;

import com.forohub.domain.curso.Curso;
import com.forohub.domain.respuesta.Respuesta;
import com.forohub.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Table(name = "topicos")
@Entity(name = "Topico")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")

public class Topico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    @Column(name = "fecha_creacion", updatable = false, insertable = false)
    private LocalDateTime fecha_creacion;
    @Enumerated(EnumType.STRING)
    private Status status;
    private String mensaje;
    private Boolean activo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuarios_id")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cursos_id")
    private Curso curso;

    @OneToMany(mappedBy = "topico", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Respuesta> respuestas;

    public Topico(DatosRegistrarTopico datos, Usuario usuario, Curso curso) {
        this.titulo = datos.titulo();
        this.status = Status.ABIERTO;
        this.mensaje = datos.mensaje();
        this.activo = true;
        this.usuario = usuario;
        this.curso = curso;
    }

    public void actualizarTopico(DatosActualizarTopico datos) {
        if (datos.mensaje() != null && !datos.mensaje().isBlank()) {
            this.mensaje = datos.mensaje();
        }
        if (datos.status() != null) {
            this.status = datos.status();
        }
        if (datos.activo() != null) {
            if (datos.activo()) {
                this.activarTopico();
            } else {
                this.desactivarTopico();
            }
        }
    }

    public void activarTopico() {
        this.activo = true;
        for (Respuesta respuesta : respuestas) {
            respuesta.activarRespuesta();
        }
    }

    public void desactivarTopico() {
        this.activo = false;
        for (Respuesta respuesta : respuestas) {
            respuesta.desactivarRespuesta();
        }
    }
}


