# Ospedale

Parcial 3 — Programación Orientada a Objetos. Refactor del sistema hospitalario a arquitectura MVC con principios SOLID.

## Integrantes

| Nombre completo   | NRC  |
|-------------------|------|
| Luis Villarreal   | 2040 |
| Joel Trespalacios | 2040 |
| Oscar Velez       | 2040 |

## Ejecución

En NetBeans: ejecutar `main.Main`. Al abrir la app se cargan los usuarios de `json/users.json` y aparece la pantalla de login.

## Qué hace la aplicación

- **Inicio de sesión** según rol: administrador, paciente o doctor. Cerrar sesión vuelve al login.
- **Administrador:** registrar doctores; entrar a la vista de un paciente o de un doctor (impersonar) y volver con el botón Back.
- **Paciente:** ver y editar perfil; pedir citas y hospitalizaciones; ver listas; cancelar citas.
- **Doctor:** ver perfil; aceptar, completar, reagendar citas; prescribir; aprobar o rechazar hospitalizaciones; hospitalizar desde una cita pendiente.
- Los mensajes de éxito o error salen en ventanas emergentes. Tras una operación exitosa se limpian los campos que corresponda.

## Datos

- Al arrancar se leen los usuarios del JSON.
- Citas y hospitalizaciones nuevas quedan **solo en memoria** hasta cerrar la app (no se reescribe el archivo JSON).

## Bonificación SOLID (controladores)

Se reorganizó el código para no repetir lo mismo en varios controladores y para no acceder al almacén directamente desde todos lados:

- Clases auxiliares para buscar pacientes/doctores y leer los combos.
- Interfaces de “repositorio” para usuarios, citas y hospitalizaciones, con implementación sobre el almacén en memoria.

## Por hacer (bonificación y detalles)

- Actualizar las tablas solas cuando cambien citas u hospitalizaciones (patrón Observer).
- Que el paciente pueda cancelar una hospitalización solicitada (si el enunciado lo exige en la revisión final).
- Guardar cambios en JSON al cerrar (pendiente de confirmar con el curso).

## Ramas

- Desarrollo: `test`
- Entrega: `main`
