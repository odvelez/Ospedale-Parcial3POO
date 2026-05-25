# Ospedale

Parcial 3 — POO. Sistema hospitalario en Java (Swing + MVC).

## Integrantes

- Luis Villarreal — NRC 2040
- Joel Trespalacios — NRC 2040
- Oscar Velez — NRC 2040

## Cómo ejecutar

1. Abrir el proyecto en **NetBeans**.
2. **Clean and Build**.
3. Ejecutar la clase `main.Main`.

Al iniciar se cargan los usuarios de `json/users.json` y aparece el login.

## Roles

- **Admin:** registra doctores y puede ver la app como paciente o doctor (botón Back para volver).
- **Paciente:** perfil, citas y hospitalizaciones.
- **Doctor:** perfil, citas (aceptar, reagendar, completar), prescripciones y hospitalizaciones.

## Datos

- Usuarios: vienen del JSON al arrancar.
- Citas y hospitalizaciones nuevas: solo en memoria (no se guardan en el JSON al cerrar).

## Rama de entrega

`main`


## Rama de trabajo

`test`
