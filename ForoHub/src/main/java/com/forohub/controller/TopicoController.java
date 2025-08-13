package com.forohub.controller;

import com.forohub.domain.curso.Curso;
import com.forohub.domain.curso.CursoRepository;
import com.forohub.domain.topico.*;
import com.forohub.domain.usuario.Usuario;
import com.forohub.domain.usuario.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    @Autowired
    private TopicoRepository topicoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private CursoRepository cursoRepository;

    @Operation(summary = "Crear un Nuevo Tópico")
    @PostMapping
    @Transactional
    public ResponseEntity registrar(@RequestBody @Valid DatosRegistrarTopico datos, UriComponentsBuilder uriBuilder) {
        Usuario usuario = usuarioRepository.findById(datos.idUsuario())
                .orElseThrow(() -> new EntityNotFoundException("Usuario No Encontrado"));
        if (!usuario.getActivo()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Usuario Desactivado. No puede Crear Tópicos"));
        }

        Curso curso = cursoRepository.findById(datos.idCurso())
                .orElseThrow(() -> new EntityNotFoundException("Curso No Encontrado"));


        Topico topico = new Topico(datos, usuario, curso);
        topicoRepository.save(topico);

        DatosRespuestaTopico datosRespuestaTopico = new DatosRespuestaTopico(topico);

        URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();

        return ResponseEntity.created(uri).body(Map.of(
                "mensaje", "Topico Creado Exitosamente",
                "Topico", datosRespuestaTopico
        ));
    }

    @Operation(summary = "Listar todos los Tópicos Activos paginados")
    @GetMapping
    public Page<DatosListarTopico> listar(@PageableDefault(size = 10, sort = {"fecha_creacion"}) Pageable paginacion) {
        return topicoRepository.listarTopicosConContador(paginacion);
    }

    @Operation(summary = "Listar todos los Tópicos por Curso y Año específico")
    @GetMapping("/filtrar")
    public Page<DatosListarTopico> buscarTopicos(
            @RequestParam(required = false) Long cursoId,
            @RequestParam(required = false) Integer ano,
            @PageableDefault(size = 10, sort = "fecha_creacion") Pageable pageable
    ) {
        return topicoRepository.findByCursoAndAno(cursoId, ano, pageable);
    }

    @Operation(summary = "Listar el Detalle de un Tópico, con sus Respuestas Asociadas")
    @GetMapping("/{id}")
    public ResponseEntity listarDetalle(@PathVariable Long id) {

        Topico topico = topicoRepository.findById(id).orElse(null);

        if (topico == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "El Topico con id:" + id + ", No Existe en la BD"));
        }
        if (!topico.getActivo()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "El Tópico con id:" + id + ", Está Desactivado"));
        }
        return ResponseEntity.ok(new DatosListarDetalleTopico(topico));
    }

    @Operation(summary = "Listar todos los Tópicos Activos por Status, paginados")
    @GetMapping("/status/{status}")
    public Page<DatosListarTopico> listarPorStatus(@PathVariable Status status, @PageableDefault(size = 10, sort = {"id"}) Pageable paginacion) {
        return topicoRepository.findByStatusAndActivoTrue(status, paginacion);
    }

    @Operation(summary = "Actualizar los Datos de un Tópico")
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity actualizar(@PathVariable Long id, @RequestBody @Valid DatosActualizarTopico datos) {

        if (!topicoRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "El Topico con id:" + id + ", No Existe en la BD"));
        }
        Topico topico = topicoRepository.getReferenceById(id);

        topico.actualizarTopico(datos);
        return ResponseEntity.ok(Map.of("El Topico con id:" + id + ", Ha sido Actualizado", new DatosActualizarTopico(topico)));
    }

    @Operation(summary = "Desactivar un Tópico y sus Respuestas")
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity eliminar(@PathVariable Long id) {
        if (!topicoRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "El Tópico con id:" + id + ", No Existe en la BD"));
        }
        Topico topico = topicoRepository.getReferenceById(id);
        topico.desactivarTopico();
        return ResponseEntity.ok(Map.of("mensaje", "El Topico con id:" + id + ", Ha sido Desactivado"));
    }
}