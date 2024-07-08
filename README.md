# TirateUnPaso
TirateUnPaso es una aplicación que cuenta pasos utilizando la API de Android Health. Además, cuenta con un selector que permite visualizar varios gráficos y un calendario integrado.
También tiene una funcionalidad de logros y otra para mostrar distintos consejos. 

## Base de datos SQLite

Mediante la herramienta DBeaver, creamos una base de datos SQLite (el archivo `/app/src/main/assets/tirateunpaso.db`) y sobre esta base ejecutamos el script SQL `/sqlite/prepopulate.sql` para crear las tablas e insertar los datos iniciales necesarios para utilizar la aplicación.

## Health Connect

En versiones de Android 13 o menor debe instalarse la aplicación Health Connect desde el Google Play Store y dar permisos a la aplicación.
