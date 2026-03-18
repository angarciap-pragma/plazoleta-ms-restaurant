# ms-restaurant

Microservicio encargado de la gestión de restaurantes del reto Plazoleta.

## HUs implementadas

- HU2: crear restaurante
- HU3: crear plato
- HU4: modificar plato
- HU5: autenticación y autorización por roles
- HU7: activar e inactivar plato
- HU9: listar restaurantes
- HU10: listar platos por restaurante

## Endpoints disponibles

- `POST /restaurants`
- `POST /restaurants/{restaurantId}/dishes`
- `PATCH /dishes/{dishId}`
- `PATCH /dishes/{dishId}/status`
- `GET /restaurants`
- `GET /restaurants/{restaurantId}/dishes`
- `GET /restaurants/internal/{restaurantId}`

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

Actuator local:

```text
http://localhost:8083/actuator/health
```

## Validación

```bash
bash gradlew clean build
bash gradlew test
bash gradlew jacocoTestReport
```

## Dependencias compartidas

Este microservicio consume `plazoleta-common-lib` desde `mavenLocal()`.
