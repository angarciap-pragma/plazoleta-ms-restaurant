# ms-restaurant

Microservicio encargado de la gestión de restaurantes del reto Plazoleta.

## HU implementada en este paso

- HU2: crear restaurante
- HU3: crear plato
- HU4: modificar plato

## Endpoint disponible

- `POST /restaurants`
- `POST /restaurants/{restaurantId}/dishes`
- `PATCH /dishes/{dishId}`

## Ejecución local

```bash
bash gradlew bootRun
```

## Documentación OpenAPI

Swagger UI local:

```text
http://localhost:8083/swagger-ui.html
```

OpenAPI JSON local:

```text
http://localhost:8083/v3/api-docs
```

## Validación

```bash
bash gradlew clean build
bash gradlew test
bash gradlew jacocoTestReport
```

## Dependencias compartidas

Este microservicio consume `plazoleta-common-lib` desde `mavenLocal()`.
