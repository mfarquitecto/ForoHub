# <img src="docs/icons/Icon_0B.jpg" width="75"/> API REST :   F  O  R  O     H  U  B

​	**FORO - Hub** es una API REST desarrollada en Java con Spring Boot, creada como un ejercicio de laboratorio para un sistema de foro académico. Se centra en la gestión de cuatro entidades principales: **`Usuarios`, `Cursos`, `Tópicos` y `Respuestas`.**
La API sigue las mejores prácticas de diseño RESTful y utiliza una base de datos MySQL para la persistencia de la información. La documentación navegable es generada automáticamente mediante SpringDoc y Swagger UI.

---

## <img src="docs/icons/Icon_1B.jpg" width="50"/> Características Principales

* **API RESTful**: Endpoints diseñados siguiendo los principios REST, utilizando los verbos HTTP `POST`, `GET`, `PUT` y `DELETE` para una interacción clara y coherente con los recursos.
* **Gestión de Contenido (CRUD)**: Permite a los usuarios crear, leer, actualizar y eliminar (desactivar) recursos como usuarios, cursos, tópicos y respuestas.
* **Borrado Lógico**: En lugar de la eliminación física de registros, se utiliza un campo `activo` para marcar los recursos como desactivados, preservando así la información histórica.
* **Paginación**: Implementada para la consulta de listados extensos, como los tópicos dentro de un curso, optimizando el rendimiento y la eficiencia.
* **Validación de Datos**: Usa validación de datos en el servidor con `records` para garantizar la integridad de la información recibida.
* **Manejo de Errores Centralizado**: Una clase dedicada a la gestión de errores centraliza y gestiona las respuestas de error de la API, proporcionando mensajes claros al cliente.
* **Autenticación / Autorización:** Implementa un proceso de verificación de credenciales para permitir el acceso a la API, además utiliza `tokens JWT` para autenticar y autorizar las solicitudes.
* **Documentación Automática**: Integra SpringDoc para generar documentación dinámica y actualizada de la API, accesible a través de Swagger UI. Esto permite explorar y probar los endpoints directamente desde el navegador.

---
## <img src="docs/icons/Icon_2B.jpg" width="50"/> Tecnologías Utilizadas

* **Java 21**
* **Spring Boot**
* **Spring Data JPA**
* **Hibernate**
* **MySQL**
* **Flyway**
* **Lombok**

---
## <img src="docs/icons/Icon_3B.jpg" width="50"/> Requisitos

* **Java 21** 

* **Maven** 

* **MySQL**

* **Postman o Insomnia** (opcional)

---

## <img src="docs/icons/Icon_4B.jpg" width="50"/> Estructura del Proyecto

```tex
🗄 forohub
│ 
├── 📁 controller
│   │               
├── 📁 domain
│   ├───  📁 curso                     
│   ├───  📁 respuesta                     
│   ├───  📁 topico                                
│   └───  📁 usuario                         
├── 📁 infra
│   ├───  📁 exceptions
│   ├───  📁 security
│   └───  📁 springdoc
│
└───── ForoHubApiApplication.java  

```

---

## <img src="docs/icons/Icon_5B.jpg" width="60"/> Configuración y Ejecución

1. **Clonar el repositorio:**

   ```properties
   git clone https://github.com/mfarquitecto/ForoHub.git
   cd ForoHub
   ```

2. **Crear una Base de Datos en MySQL**:

   Crea un Usuario y una Base de Datos para el proyecto

   ```properties
   CREATE DATABASE foro_hub;
   ```
   
3. **Abre el archivo** `application.properties` y actualiza las credenciales de tu Base de Datos MySQL:
   ```properties
   spring.datasource.url==jdbc:mysql://localhost:3306/foro_hub
   spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
   spring.datasource.username= tu_usuario_de_MySQL
   spring.datasource.password= tu_contraseña_de_MySQL
    
   ```
4.  **Compilar y Ejecutar**:

    * **Desde tu IDE (IntelliJ IDEA, Eclipse, VS Code):**
      
        * Importa el proyecto como un proyecto Maven.
        * Busca la clase `ForoHubApiApplication.java` en  `/src/main/java/com/forohub/`
        * Haz clic derecho y selecciona "Run `ForoHubApiApplication.main()`".
        
    * **Desde la terminal (usando Maven):**
        Navega a la raíz del proyecto en tu terminal y ejecuta:
        
   ```properties
   mvn clean install
   mvn spring-boot:run
   ```
---
## <img src="docs/icons/Icon_6B.jpg" width="45"/> Como Utilizar

Una vez que la aplicación se esté ejecutando:

- Todos los endpoints estarán disponible bajo la ruta base: `http://localhost:8080`

- La documentación completa, generada automáticamente con SpringDoc, puede consultarse desde **[Swagger UI](http://localhost:8080/swagger-ui/index.html)**  `http://localhost:8080/swagger-ui/index.html`
- A continuación, se listan los endpoints por entidad:

  #### USUARIOS
  - **`POST /usuarios`**: Registra un nuevo Usuario.
  - **`GET /usuarios`**: Lista todos los Usuarios activos de forma paginada.
  - **`PUT /usuarios/{id}`**: Actualiza la información de un Usuario.
  - **`DELETE /usuarios/{id}`**: Desactiva un Usuario.
  ---

  #### CURSOS
  - **`POST /cursos`**: Registra un nuevo Curso.
  - **`GET /cursos/{id}`**: Obtiene los detalles de un Curso específico, incluyendo sus Tópicos paginados.
  - **`PUT /cursos/{id}`**: Actualiza la información de un Curso.
  - **`DELETE /cursos/{id}`**: Desactiva un Curso y todos sus Tópicos asociados.
  ---

  #### TOPICOS
  - **`POST /topicos`**: Registra un nuevo Tópico en un curso.
  - **`GET /topicos`**: Lista todos los Tópicos de forma paginada.
  - **`GET /topicos/{id}`**: Obtiene los detalles de un Tópico específico, con todas sus respuestas asociadas.
  - **`GET /status/{status}`**: Lista todos los Tópicos clasificados por su Status, de forma paginada.
  - **`GET /topicos/filtrar`**: Lista todos los Tópicos de acuerdo al filtro: curso y/o  año específico.
  - **`PUT /topicos/{id}`**: Actualiza la información de un Tópico.
  - **`DELETE /topicos/{id}`**: Desactiva un Tópico y todas sus respuestas asociadas.
  ---

  #### RESPUESTAS
  - **`POST /respuestas`**: Registra una nueva Respuesta a un Tópico.
  - **`GET /respuestas/{id}`**: Obtiene los detalles de un Respuesta específica.
  - **`PUT /respuestas/{id}`**: Actualiza la información de una Respuesta.
  - **`DELETE /respuestas/{id}`**: Desactiva una Respuesta.
  ---

> **Nota**: Todos los endpoints están protegidos por autenticación JWT. Para utilizarlos (incluido Swagger UI), primero realiza un Post `/login` y utiliza el token JWT en el encabezado `Authorize`.
---


## <img src="docs/icons/Icon_7B.jpg" width="70"/> Autor

[![mfarquitecto](https://github.com/mfarquitecto.png?size=65)](https://github.com/mfarquitecto)  

##### [mfarquitecto](https://github.com/mfarquitecto)

Este proyecto fue desarrollado como parte del programa One Oracle Next Education (ONE) + Alura Latam.

---

## <img src="docs/icons/Icon_8B.jpg" width="60"/> Licencia

Este proyecto está bajo la licencia MIT.

