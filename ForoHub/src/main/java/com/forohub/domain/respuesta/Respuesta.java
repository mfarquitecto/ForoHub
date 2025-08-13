package com.forohub.domain.respuesta;

import com.forohub.domain.topico.Topico;
import com.forohub.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "respuestas")
@Entity(name = "Respuesta")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")

public class Respuesta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "fecha_creacion", updatable = false, insertable = false)
    private LocalDateTime fecha_creacion;
    private String respuesta;
    private Boolean solucion;
    private Boolean activa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuarios_id")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topicos_id")
    private Topico topico;

    public Respuesta(DatosRegistrarRespuesta datos, Usuario usuario, Topico topico) {
        this.respuesta = datos.respuesta();
        this.solucion = false;
        this.activa = true;
        this.usuario = usuario;
        this.topico = topico;
    }

    public void actualizarRespuesta(DatosActualizarRespuesta datos) {
        if (datos.respuesta() != null && !datos.respuesta().isBlank()) {
            this.respuesta = datos.respuesta();
        }
        if (datos.solucion() != null) {
            this.solucion = datos.solucion();
        }
        if (datos.activa() != null) {
            this.activa = datos.activa();
        }
    }

    public void activarRespuesta() {
        this.activa = true;
    }

    public void desactivarRespuesta() {
        this.activa = false;
    }

}
