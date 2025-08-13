CREATE TABLE respuestas (
    id BIGINT NOT NULL AUTO_INCREMENT,
    fecha_creacion DATETIME(0) DEFAULT CURRENT_TIMESTAMP,
    respuesta VARCHAR(500) UNIQUE NOT NULL,
    solucion TINYINT NOT NULL DEFAULT 0,
    activa TINYINT NOT NULL DEFAULT 1,

    usuarios_id BIGINT NOT NULL,
    topicos_id BIGINT NOT NULL,

    PRIMARY KEY(id),
    FOREIGN KEY (usuarios_id) REFERENCES usuarios(id),
    FOREIGN KEY (topicos_id) REFERENCES topicos(id)
);