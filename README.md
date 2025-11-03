# HospitalitoTP3: Sistema de Gestión Hospitalaria

## 1. Descripción General

**Hospitalito** es una aplicación web desarrollada en Java con Spring Boot diseñada para la administración eficiente y centralizada de la atención hospitalaria. Su principal objetivo es facilitar la gestión de recursos clave, como camas, enfermeras y pacientes, permitiendo un control óptimo de las asignaciones y la disponibilidad.

## 2. Tecnologías y Dependencias

El proyecto se basa en el stack Java con Spring Boot y utiliza las siguientes tecnologías:

| Tecnología | Versión / Propósito | Fuente |
| :--- | :--- | :--- |
| **Java** | 21 |[Java Donwloads](https://www.oracle.com/latam/java/technologies/downloads/)|
| **Spring Boot** | 3.5.6 |[Spring Initlizr](https://start.spring.io/)|
| **Motor de Plantillas** | Thymeleaf (para vistas HTML) |[Thymeleaf](https://www.thymeleaf.org/)|
| **Base de Datos** | MySQL |[MySQL Community](https://dev.mysql.com/downloads/)|
| **Persistencia** | Spring Data JPA |[Spring Initlizr](https://start.spring.io/)|
| **Seguridad** | Spring Security (Autenticación y control de acceso) |[Spring Initlizr](https://start.spring.io/)|
| **Gestor de Dependencias** | Maven |[Maven Download](https://maven.apache.org/download.cgi)|
| **Productividad** | Lombok (para reducción de código *boilerplate*) |[Lombok Download](https://projectlombok.org/download)|

## 3. Arquitectura del Sistema

La aplicación sigue una arquitectura **Modelo-Vista-Controlador (MVC)** con una clara separación de responsabilidades:

* **Controladores:** Manejan las peticiones del usuario y redirigen el flujo.
* **Servicios:** Contienen la lógica de negocio principal de la aplicación.
* **Repositorios:** Gestionan la persistencia de datos a través de JPA.
* **Entidades:** Representan las tablas de la base de datos.
* **DTOs:** Objetos de transferencia de datos utilizados para el intercambio seguro de información entre capas.

## 4. Base de Datos y Reglas de Negocio

La base de datos `hospitalito` consta de las siguientes tablas principales: `categorias`, `camas`, `personal` (enfermeras), `historiales_clinicos` y `asignaciones`.

Se implementan las siguientes **Reglas de Negocio** a nivel de la base de datos (mediante *triggers*):

* Las enfermeras no pueden superar un límite de 8 camas asignadas.
* Las camas de una misma `categoria` no pueden superar un máximo de enfermeras asignadas a esa categoría.
* La disponibilidad de las camas se actualiza automáticamente.
* Se eliminan todas las asignaciones existentes si una enfermera es marcada como inactiva.

## 5. Instalación y Ejecución

Sigue estos pasos para levantar la aplicación localmente:

### Prerrequisitos
* **Java 21**
* **Maven**
* **MySQL** configurado

### Configuración de la Base de Datos

1.  **Importar el Dump:**
    Utiliza el archivo dump de la base de datos (que se encuentra en la carpeta de entrega) para crear la base de datos, las tablas, los *triggers* y cargar los datos iniciales.
    ```bash
    mysql -u root -p < "C:\ruta\a\hospitalito_dump.sql"
    ```
    *(Reemplaza la ruta con la ubicación de tu archivo dump)*.

2.  **Configurar Credenciales:**
    Asegúrate de ajustar las credenciales de conexión en el archivo `src/main/resources/application.properties` con tu usuario y contraseña de MySQL.

    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/hospitalito
    spring.datasource.username=TU_USUARIO
    spring.datasource.password=TU_CONTRASEÑA
    ```

### Construcción y Ejecución

1.  Abre la terminal en la carpeta raíz del proyecto.
2.  Construye el proyecto e instala las dependencias:
    ```bash
    ./mvnw clean install
    ```
3.  Ejecuta la aplicación Spring Boot:
    ```bash
    ./mvnw spring-boot:run
    ```

### Acceso a la Aplicación
Una vez iniciada, la aplicación estará disponible en:
[http://localhost:8080](http://localhost:8080)

## 6. Credenciales de Acceso

Para acceder al sistema (Spring Security en memoria):

| Campo | Valor |
| :--- | :--- |
| **Usuario** | `jorge` |
| **Contraseña** | `1234` |

## 7. Endpoints Principales y Funcionalidades

El sistema ofrece las siguientes secciones principales para la gestión hospitalaria:

| Sección | Endpoint | Descripción |
| :--- | :--- | :--- |
| **Dashboard** | `/hospitalito` | Vista principal con resumen de camas y enfermeras. |
| **Camas** | `/hospitalito/camas` | Listado y gestión de todas las camas. |
| **Enfermeras** | `/hospitalito/enfermeras` | Listado y gestión de las enfermeras. |
| **Asignaciones** | `/hospitalito/asignaciones` | Vista para asociar enfermeras a camas. |
| **Pacientes** | `/hospitalito/pacientes` | Listado y gestión de historiales clínicos de pacientes. |

**Operaciones clave:**

* **Camas:** Crear, editar (`/editar`), eliminar (`/eliminar`), alojar paciente (`/alojar`) y desalojar paciente (`/desalojar`).
* **Enfermeras:** Agregar/actualizar (`/editar`) y eliminar (`/eliminar`).
* **Pacientes:** Crear/actualizar (`/editar`) y eliminar (`/eliminar`) historial clínico.

## 8. Autores

Trabajo realizado por:

* **Kevin Sánchez**
* **Tomas Stutz**
