package com.forohub.controller;

import com.forohub.domain.respuesta.*;
import com.forohub.domain.topico.Topico;
import com.forohub.domain.topico.TopicoRepository;
import com.forohub.domain.usuario.Usuario;
import com.forohub.domain.usuario.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/respuestas")
public class RespuestaController {

    @Autowired
    private RespuestaRepository respuestaRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private TopicoRepository topicoRepository;

    @Operation(summary = "Crear una nueva Respuesta a un Tópico")
    @PostMapping
    @Transactional
    public ResponseEntity registrar(@RequestBody @Valid DatosRegistrarRespuesta datos, UriComponentsBuilder uriBuilder) {
        Usuario usuario = usuarioRepository.findById(datos.idUsuario())
                .orElseThrow(() -> new EntityNotFoundException("Usuario No Encontrado"));
        if (!usuario.getActivo()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Usuario Desactivado. No puede Crear Respuestas"));
        }

        Topico topico = topicoRepository.findById(datos.idTopico())
                .orElseThrow(() -> new EntityNotFoundException("Topico No Encontrado"));

        Respuesta respuesta = new Respuesta(datos, usuario, topico);
        respuestaRepository.save(respuesta);
        DatosRespuestaRespuesta datosRespuestaRespuesta = new DatosRespuestaRespuesta(respuesta);

        URI uri = uriBuilder.path("/respuestas/{id}").buildAndExpand(respuesta.getId()).toUri();

        return ResponseEntity.created(uri).body(Map.of(
                "mensaje", "Respuesta Creada Exitosamente",
                "Respuesta", datosRespuestaRespuesta
        ));
    }

    @Operation(summary = "Listar el Detalle de un Respuesta")
    @GetMapping("/{id}")
    public ResponseEntity listarDetalle(@PathVariable Long id) {

        Respuesta respuesta = respuestaRepository.findById(id).orElse(null);

        if (respuesta == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "La Respuesta con id:" + id + ", No Existe en la BD"));
        }
        if (!respuesta.getActiva()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "La Respuesta con id:" + id + ", Está Desactivada"));
        }
        return ResponseEntity.ok(new DatosListarDetalleRespuesta(respuesta));
    }

    @Operation(summary = "Actualizar los Datos de una Respuesta")
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity actualizar(@PathVariable Long id, @RequestBody @Valid DatosActualizarRespuesta datos) {

        if (!respuestaRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "La Respuesta con id:" + id + ", No Existe en la BD"));
        }
        Respuesta respuesta = respuestaRepository.getReferenceById(id);
        Long topicoId = respuesta.getTopico().getId();

        if (!respuesta.getRespuesta().equals(datos.respuesta()) &&
                respuestaRepository.existsByRespuestaAndTopicoIdAndIdNot(datos.respuesta(), topicoId, id)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Ya existe otra respuesta con ese contenido en el mismo tópico"));
        }
        respuesta.actualizarRespuesta(datos);
        return ResponseEntity.ok(Map.of("La Respuesta con id:" + id + ", Ha sido Actualizada", new DatosActualizarRespuesta(respuesta)));
    }

    @Operation(summary = "Desactivar una Respuesta")
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity eliminar(@PathVariable Long id) {
        if (!respuestaRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "La Respuesta con id:" + id + ", No Existe en la BD"));
        }

        Respuesta respuesta = respuestaRepository.getReferenceById(id);
        respuesta.desactivarRespuesta();
        return ResponseEntity.ok(Map.of("mensaje", "La Respuesta con id:" + id + ", Ha sido Desactivada"));
    }

}
