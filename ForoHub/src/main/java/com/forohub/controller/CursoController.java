package com.forohub.controller;

import com.forohub.domain.curso.*;
import com.forohub.domain.curso.DatosRespuestaCurso;
import com.forohub.domain.topico.Topico;
import com.forohub.domain.topico.TopicoRepository;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/cursos")
public class CursoController {

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private TopicoRepository topicoRepository;

    @Operation(summary = "Crear un Nuevo Curso")
    @PostMapping
    @Transactional
    public ResponseEntity registrar(@RequestBody @Valid DatosRegistrarCurso datos, UriComponentsBuilder uriBuilder) {
        Curso curso = new Curso(datos);
        cursoRepository.save(curso);
        DatosRespuestaCurso datosRespuestaCurso = new DatosRespuestaCurso(curso);

        URI uri = uriBuilder.path("/cursos/{id}").buildAndExpand(curso.getId()).toUri();

        return ResponseEntity.created(uri).body(Map.of(
                "mensaje", "Curso Creado Exitosamente",
                "Curso", datosRespuestaCurso
        ));
    }

    @Operation(summary = "Listar el Detalle de un Curso, con sus Tópicos Asociados")
    @GetMapping("/{id}")
    public ResponseEntity listarDetalle(@PathVariable Long id, @PageableDefault(size = 5) Pageable paginacion) {

        Curso curso = cursoRepository.findById(id).orElse(null);

        if (curso == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "El Curso con id:" + id + ", No Existe en la BD"));
        }

        if (!curso.getActivo()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "El Curso con id:" + id + ", Está Desactivado"));
        }

        Page<Topico> topicosPage = topicoRepository.findByCursoIdAndActivoTrue(id, paginacion);

        return ResponseEntity.ok(new DatosListarDetalleCurso(curso, topicosPage));
    }

    @Operation(summary = "Actualizar los Datos de un Curso")
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity actualizar(@PathVariable Long id, @RequestBody @Valid DatosActualizarCurso datos) {

        if (!cursoRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "El Curso con id:" + id + ", No Existe en la BD"));
        }
        Curso curso = cursoRepository.getReferenceById(id);

        if (!curso.getNombre().equals(datos.nombre()) &&
                cursoRepository.existsByNombre(datos.nombre())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "El Curso " + datos.nombre() + ", ya Existe en la BD"));
        }
        curso.actualizarCurso(datos);
        return ResponseEntity.ok(Map.of("El Curso con id:" + id + ", Ha sido Actualizado", new DatosActualizarCurso(curso)));
    }

    @Operation(summary = "Desactivar un Curso y sus Tópicos")
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity eliminar(@PathVariable Long id) {
        if (!cursoRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "El Curso con id:" + id + ", No Existe en la BD"));
        }
        Curso curso = cursoRepository.getReferenceById(id);
        curso.desactivarCurso();
        return ResponseEntity.ok(Map.of("mensaje", "El Curso con id:" + id + ", Ha sido Desactivado"));
    }
}


