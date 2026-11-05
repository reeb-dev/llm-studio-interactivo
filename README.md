# LLM Studio Interactivo

Laboratorio en español para aprender los patrones que hacen que una integración con LLM sea un sistema de producción: arquitectura, conocimiento, seguridad, consistencia, operación y frontend Angular.

## Aplicación full-stack

`frontend/` es una aplicación Angular standalone: consume módulos, envía respuestas de retos y guarda el avance por learner. `backend/` es una API Spring Boot con validación, JPA/H2, CORS y endpoints para módulos, retos y progreso.

```bash
# terminal 1
cd backend && mvn spring-boot:run

# terminal 2
cd frontend && npm start
```

La UI queda en `http://localhost:4200` y la API en `http://localhost:8080`. GitHub Pages compila y publica el frontend; para producción, el `baseUrl` de Angular deberá apuntar a un host real de Spring Boot, ya que Pages no ejecuta Java.

## Versión estática previa

```bash
python3 -m http.server 8000 --directory dist
```

Abrí `http://localhost:8000`.

## Ruta de aprendizaje

La aplicación incluye 17 laboratorios interactivos. Cada uno presenta una decisión de diseño, una implementación mínima, fallos observables y un criterio de aceptación.

1. Arquitectura de un sistema LLM
2. Contexto, prompts y memoria
3. RAG y citas de evidencia
4. Funciones y herramientas seguras
5. JWT, sesión y autorización
6. Idempotencia
7. Outbox, eventos y sagas
8. Timeouts, backoff, circuit breaker y DLQ
9. Observabilidad y costos
10. Evaluaciones y regresiones
11. Guardrails, secretos y privacidad
12. Cache, cuotas y control de costo
13. Inbox Pattern
14. Optimistic Locking
15. UPSERT y claves naturales
16. Saga Pattern
17. Angular para frontend confiable

Leé la [guía de patrones y prácticas](/docs/ruta-de-aprendizaje.md) antes de implementar un flujo real.

## Contrato de aprendizaje

No basta con poder explicar un patrón. Cada práctica se completa cuando se puede provocar el fallo que evita, observar la evidencia y verificar el criterio de aceptación.
