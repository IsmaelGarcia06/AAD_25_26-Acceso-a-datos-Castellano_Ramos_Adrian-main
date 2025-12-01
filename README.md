# AAD_25_26-Acceso-a-datos-García-Camacho-Ismael

# Que es un conector y su panel de aplicacion

Un conector es un componente de software que permite la comunicación y transferencia de datos entre diferentes sistemas,
aplicaciones o servicios. Actúa como un puente que facilita la integración y el intercambio de información, permitiendo
que los datos fluyan de manera eficiente y segura entre las distintas plataformas.

# Cómo has levantado el servicio PostgreSQL.

He levantado el servicio PostgreSQL utilizando Docker. Primero, he creado un contenedor de PostgreSQL con la imagen
oficial de PostgreSQL disponible en Docker Hub. He configurado las variables de entorno necesarias, como el nombre de
usuario, la contraseña y el nombre de la base de datos, para asegurar que el servicio esté listo para su uso.

# Qué variables has utilizado y por qué.

He utilizado las siguientes variables de entorno al levantar el contenedor de PostgreSQL:

# Cómo probar la conexión.

Para probar la conexión al servicio PostgreSQL, he utilizado una herramienta de cliente de base de datos como pgAdmin
o la línea de comandos de psql. He ingresado las credenciales configuradas (nombre de usuario, contraseña y nombre de la
base
de datos) junto con la dirección del host y el puerto donde se está ejecutando el contenedor de PostgreSQL.
Luego, he intentado conectarme a la base de datos para verificar que la conexión sea exitosa y que pueda realizar
operaciones básicas
como crear tablas, insertar datos y realizar consultas.

# Conclusión.

En conclusión, los conectores son esenciales para facilitar la integración entre diferentes sistemas y aplicaciones,
permitiendo un flujo de datos eficiente. Levantar un servicio PostgreSQL utilizando Docker es una forma rápida y
sencilla
de configurar una base de datos para pruebas y desarrollo. Al utilizar las variables de entorno adecuadas, se puede
asegurar que el servicio esté correctamente configurado y listo para su uso. de datos y el puerto asignado.
Estas variables son cruciales para establecer una conexión exitosa y segura con la base de datos.

- POSTGRES_USER: Define el nombre de usuario para acceder a la base de datos.
- POSTGRES_PASSWORD: Establece la contraseña para el usuario definido.
- POSTGRES_DB: Especifica el nombre de la base de datos que se creará al iniciar el contenedor.
- PORT: Define el puerto en el que el servicio PostgreSQL escuchará las conexiones entrantes.
- HOST: Indica la dirección del host donde se está ejecutando el contenedor de PostgreSQL.

# Ejemplo de comando Docker utilizado:

```bash
docker run --name mi_postgres -e POSTGRES_USER=mi_usuario -e POSTGRES_PASSWORD=mi_contraseña -e POSTGRES_DB=mi_base_de_datos -p 5432:5432 -d postgres
```

Este comando crea y ejecuta un contenedor de PostgreSQL con las variables de entorno especificadas
y mapea el puerto 5432 del contenedor al puerto 5432 del host.

# Ejemplo de comando para probar la conexión:

```bash
psql -h localhost -p 5432 -U mi_usuario -d mi_base_de_datos
```

Este comando intenta conectarse a la base de datos PostgreSQL utilizando las credenciales y la información
proporcionadas.

# Resultado esperado:

Si la conexión es exitosa, se debería ver un mensaje de bienvenida de PostgreSQL y un prompt para ejecutar comandos SQL.

## Rama: feature/act_3_0

Esta rama introduce la migración del acceso a datos mediante JDBC hacia el uso de JPA con soporte para PostgreSQL.
Los cambios principales incluyen:

- Eliminación de la dependencia `spring-boot-starter-jdbc`.
- Inclusión de las dependencias:
    - `spring-boot-starter-data-jpa`
    - `postgresql`
- Preparación del proyecto para el uso de entidades JPA, repositorios y operaciones ORM.

El objetivo de esta rama es modernizar la capa de acceso a datos y preparar el entorno para trabajar con JPA en unidades
posteriores.

