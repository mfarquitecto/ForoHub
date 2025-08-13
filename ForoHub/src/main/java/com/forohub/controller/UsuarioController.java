package com.forohub.controller;

import com.forohub.domain.usuario.*;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Operation(summary = "Crear un Nuevo Usuario")
    @PostMapping
    public ResponseEntity registrarUsuario(@RequestBody @Valid DatosRegistrarUsuario datos, UriComponentsBuilder uriBuilder) {
        if (usuarioRepository.existsByLogin(datos.login())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "El login " + datos.login() + " Ya está en uso"));
        }

        if (usuarioRepository.existsByEmail(datos.email())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "El email " + datos.email() + " Ya está registrado"));
        }

        Usuario usuario = new Usuario(
                null,
                datos.login(),
                passwordEncoder.encode(datos.contrasena()),
                datos.email(),
                true,
                new ArrayList<>(),
                new ArrayList<>()
        );

        usuarioRepository.save(usuario);

        DatosRespuestaUsuario datosRespuestaUsuario = new DatosRespuestaUsuario(usuario);

        URI uri = uriBuilder.path("/usuarios/{id}").buildAndExpand(usuario.getId()).toUri();

        return ResponseEntity.created(uri).body(Map.of(
                "mensaje", "Usuario Creado Exitosamente",
                "Usuario", datosRespuestaUsuario
        ));
    }

    @Operation(summary = "Listar a los Usuarios Activos paginados")
    @GetMapping
    public Page<DatosListarUsuario> listar(@PageableDefault(size = 5, sort = {"login"}) Pageable paginacion) {
        return usuarioRepository.findAllByActivoTrue(paginacion).map(DatosListarUsuario::new);
    }

    @Operation(summary = "Actualizar los Datos de un Usuario")
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity actualizar(@PathVariable Long id, @RequestBody @Valid DatosActualizarUsuario datos) {

        if (!usuarioRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "El Usuario con id:" + id + ", No Existe en la BD"));
        }
        Usuario usuario = usuarioRepository.getReferenceById(id);

        usuario.actualizarUsuario(datos, passwordEncoder);
        return ResponseEntity.ok(Map.of("Usuario Actualizado", new DatosActualizarUsuario(usuario)));
    }

    @Operation(summary = "Desactivar a un Usuario")
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity eliminarUsuario(@PathVariable Long id) {
        if (!usuarioRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "El Usuario con id:" + id + ", No Existe en la BD"));
        }
        Usuario usuario = usuarioRepository.getReferenceById(id);
        usuario.desactivarUsuario();
        return ResponseEntity.ok(Map.of("mensaje", "El Usuario con id:" + id + ", Ha sido Desactivado"));
    }
}
