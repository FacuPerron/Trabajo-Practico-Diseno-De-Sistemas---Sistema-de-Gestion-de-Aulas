# Trabajo Practico Diseno-De-Sistemas - Sistema de Gestion de Aulas
Trabajo práctico grupal de la materia Diseño de Sistemas.
Integrantes:
   - Facundo Perron.
   - Joaquín Clausen.
   - Augusto Duarte.
   - Pedro Salvano.

Funcionalidades principales
   - Autenticación de usuarios.
   - Gestión de bedeles (alta, baja, modificación y búsqueda).
   - Gestión de aulas (búsqueda, modificación y estado).
   - Registro de reservas de aulas.
   - Consulta de disponibilidad de aulas.
   - Listado de reservas por día.
   - Listado de reservas por curso.

 El sistema fue desarrollado siguiendo una arquitectura en capas, separando responsabilidades para mejorar la mantenibilidad y escalabilidad.
Capas del sistema:
   - Capa de presentación: interacción con el usuario.
   - Capa de lógica de negocio (Gestores): contiene las reglas del sistema y coordina las operaciones.
   - Capa de acceso a datos (DAO): se encarga de la persistencia y comunicación con la base de datos.
Patrones utilizados
   - DAO (Data Access Object): encapsula el acceso a datos y consultas SQL.
   - DTO (Data Transfer Object): utilizado para transferir datos entre capas sin exponer la lógica interna.
   - Factory: permite desacoplar la creación de DAOs y facilita el cambio de implementación.

Esta arquitectura nos permite desacoplar la lógica de negocio de la persistencia, facilitando el mantenimiento y posibles cambios en la base de datos sin impactar el resto del sistema.
