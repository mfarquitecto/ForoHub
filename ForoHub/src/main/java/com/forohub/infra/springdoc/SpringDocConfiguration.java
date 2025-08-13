package com.forohub.infra.springdoc;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SpringDocConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList("bearer-key"))
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));
    }

    @Bean
    public OpenApiCustomizer superSwaggerCustomizer() {
        return openApi -> {

            openApi.getInfo()
                    .title("API REST / F O R O - H U B")
                    .description(
                            "**API REST** para la gestión de un **Foro Académico** de práctica.\n\n" +
                                    "Permite administrar Usuarios, Cursos, Tópicos y sus Respuestas.\n\n" +
                                    "Además, realiza operaciones de autenticación y paginación de resultados.\n\n"
                    )
                    .version("v1.0.0")
                    .contact(new Contact()
                            .name("mfarquitecto")
                            .url("https://github.com/mfarquitecto/"));

            openApi.getPaths().forEach((ruta, pathItem) -> {
                pathItem.readOperations().forEach(op -> {
                    List<String> tagsOriginales = op.getTags();
                    if (tagsOriginales != null && !tagsOriginales.isEmpty()) {
                        List<String> tagsRenombrados = tagsOriginales.stream()
                                .map(tag -> switch (tag.toLowerCase()) {
                                    case "autenticacion-controller" -> "Autenticacion";
                                    case "usuario-controller" -> "Usuarios";
                                    case "curso-controller" -> "Cursos";
                                    case "topico-controller" -> "Topicos";
                                    case "respuesta-controller" -> "Respuestas";
                                    default -> tag;
                                })
                                .toList();
                        op.setTags(tagsRenombrados);
                    }
                });
            });

            List<Tag> tagsPersonalizados = List.of(
                    new Tag().name("Autenticacion").description("""
                            - Proceso de verificación de credenciales para permitir el acceso a la API.
                            - Utiliza tokens JWT para autenticar y autorizar las solicitudes.
                            """),
                    new Tag().name("Usuarios").description("""
                            - Entidad que representa a las personas registradas en el sistema.
                            - Contiene información de identificación, credenciales de acceso y estado de actividad.
                            - Un usuario puede participar creando tópicos y publicando respuestas.
                            """),
                    new Tag().name("Cursos").description("""
                            - Categorías o áreas temáticas que agrupan los tópicos del foro.
                            - Cada curso puede tener múltiples tópicos relacionados y sirve como clasificación principal del contenido.
                            """),
                    new Tag().name("Topicos").description("""
                            - Publicaciones iniciales creadas por usuarios dentro de un curso.
                            - Un tópico define un tema de discusión, incluye título, mensaje, fecha de creación y estado de actividad.
                            - Puede recibir múltiples respuestas.
                            """),
                    new Tag().name("Respuestas").description("""
                            - Comentarios o soluciones publicadas dentro de un tópico por los usuarios.
                            - Una respuesta puede marcarse como solución y se asocia a un único tópico.
                            """)
            );
            openApi.setTags(tagsPersonalizados);
        };
    }
}





