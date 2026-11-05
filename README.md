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

La UI queda en `http://localhost:4200` y la API en `http://localhost:8080`. GitHub Pages publica el laboratorio estático de `dist/`; no compila ni ejecuta `frontend/` o `backend/`. Para publicar la aplicación Angular real, el `baseUrl` deberá apuntar a un host real de Spring Boot y el workflow deberá generar `frontend/dist/`.

## Versión estática previa

```bash
python3 -m http.server 8000 --directory dist
```

Abrí `http://localhost:8000`.

## Ruta de aprendizaje

La aplicación incluye 30 laboratorios interactivos. Cada uno presenta una decisión de diseño, una implementación mínima, fallos observables y un criterio de aceptación.

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
18. Self-RAG: recuperar, criticar y responder con evidencia
19. Fundamentos de IA, datos y probabilidad
20. Machine Learning supervisado
21. Redes neuronales y entrenamiento
22. Transformers, tokens y atención
23. Embeddings y bases vectoriales
24. Agentes, tools y MCP
25. IA multimodal
26. Fine-tuning, adapters y routing
27. MLOps: datos, modelos y despliegue
28. Seguridad, ética y gobierno de IA
29. Evaluaciones de modelos y productos IA
30. Proyecto integrador de IA

Leé la [guía de patrones y prácticas](/docs/ruta-de-aprendizaje.md) antes de implementar un flujo real.
Para el recorrido completo, usá el [temario ampliado de IA](/docs/temario-ia.md).
Para estudiar cada bloque con práctica y evidencia, seguí la [guía didáctica](/docs/GUIA-DIDACTICA.md).
Las referencias por módulo y su tipo de respaldo están en [fuentes verificables](/docs/FUENTES-OFICIALES.md).

## Entorno con PostgreSQL y JWT

    docker compose up -d postgres
    cd backend
    SPRING_PROFILES_ACTIVE=postgres JWT_ISSUER_URI=https://issuer.example.com mvn spring-boot:run

Flyway aplica migraciones SQL y el perfil postgres activa Spring Security como resource server JWT. El perfil local queda abierto sólo para aprender y probar; no representa una API protegida.

## RAG ejecutable

POST /api/rag/ask implementa un MVP de retrieval lexical sobre un corpus acotado y luego pasa su respuesta por el verificador Self-RAG. Expone evidencia, citas, razones y estrategia lexical-v1. No es todavía una base vectorial/híbrida ni un deployment multi-tenant.

    curl -X POST http://localhost:8080/api/rag/ask -H 'Content-Type: application/json' -d '{"question":"¿Cómo funcionan los reintentos?","topK":3}'

## Verificación

    cd backend && mvn test
    cd ../frontend && NG_BUILD_MAX_WORKERS=2 npm run build
    node --check ../dist/app.js

## Contrato de aprendizaje

No basta con poder explicar un patrón. Cada práctica se completa cuando se puede provocar el fallo que evita, observar la evidencia y verificar el criterio de aceptación.
