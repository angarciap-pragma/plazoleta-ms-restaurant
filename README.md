# ms-restaurant

Microservicio encargado de la gestión de restaurantes del reto Plazoleta.

## HU implementada en este paso

- HU2: crear restaurante
- HU3: crear plato

## Endpoint disponible

- `POST /restaurants`
- `POST /restaurants/{restaurantId}/dishes`

## Ejecución local

```bash
bash gradlew bootRun
```

## Validación

```bash
bash gradlew clean build
bash gradlew test
bash gradlew jacocoTestReport
```

## Dependencias compartidas

Este microservicio consume `plazoleta-common-lib` desde `mavenLocal()`.
