package com.forohub.infra.exceptions;

import com.forohub.domain.topico.Status;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GestorDeErrores {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, String>> manejarNoEncontrado(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "error", "No Encontrado",
                        "mensaje", ex.getMessage() != null ? ex.getMessage() : "El Recurso Solicitado No Existe"
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity gestionarError400(MethodArgumentNotValidException ex) {
        var errores = ex.getFieldErrors();
        return ResponseEntity.badRequest().body(errores.stream().map(DatosErrorValidacion::new).toList());
    }

    public record DatosErrorValidacion(String campo, String mensaje) {
        public DatosErrorValidacion(FieldError error) {
            this(error.getField(), error.getDefaultMessage());
        }
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateKey(DataIntegrityViolationException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Registro Duplicado");
        errorResponse.put("mensaje", "Ya Existe ese Registro");

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> manejarEnumInvalido(HttpMessageNotReadableException ex) {
        if (ex.getMessage().contains("Status")) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", "El valor proporcionado para 'status' No es válido." +
                            " Valores Permitidos: " + Status.valoresPermitidos()));
        }
        return ResponseEntity.badRequest().body(
                Map.of("error", "Error en el formato de la solicitud: " + ex.getMessage())
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> manejarEnumPathVariableInvalido(MethodArgumentTypeMismatchException ex) {
        if (ex.getRequiredType() != null && ex.getRequiredType().isEnum()
                && ex.getRequiredType().equals(Status.class)) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", "El valor proporcionado para 'status' No es válido." +
                            " Valores Permitidos: " + Status.valoresPermitidos()));
        }
        return ResponseEntity.badRequest().body(
                Map.of("error", "Parámetro Inválido: " + ex.getName()));
    }
}
