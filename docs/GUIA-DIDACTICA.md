# Guía didáctica del laboratorio

Este proyecto no se completa leyendo todos los archivos ni marcando respuestas al azar. La unidad mínima de avance es:

1. **Nombrar:** explicar el concepto en palabras propias.
2. **Decidir:** elegir un diseño y reconocer su trade-off.
3. **Practicar:** provocar un caso de éxito o de fallo controlado.
4. **Verificar:** ejecutar una prueba, inspeccionar una traza o comprobar un contrato HTTP.
5. **Reflexionar:** justificar qué evidencia permite confiar en el resultado y qué límite queda abierto.

## Recorrido recomendado

### Bloque 1 — Construir una base segura

Módulos 1 a 6: arquitectura, contexto, RAG, herramientas, JWT e idempotencia.

Resultado: podés explicar quién autoriza, dónde se guardan los datos, cómo se limita el contexto y por qué un reintento no duplica un efecto.

### Bloque 2 — Operar con consistencia

Módulos 7 a 17: outbox, resiliencia, trazas, evaluación, seguridad, cache, inbox, concurrencia, UPSERT, sagas y Angular.

Resultado: podés seguir una petición completa, identificar un fallo y elegir un patrón de recuperación con una prueba asociada.

### Bloque 3 — IA verificable

Módulos 18 a 25: Self-RAG, fundamentos de IA, ML, redes, transformers, embeddings, agentes, MCP y multimodalidad.

Resultado: podés separar evidencia de probabilidad, delimitar fuentes no confiables y decidir cuándo un sistema debe fallar de forma segura.

### Bloque 4 — Llevar IA a producción

Módulos 26 a 30: fine-tuning, routing, MLOps, gobierno, evaluación y proyecto integrador.

Resultado: podés proponer un release con métricas, responsable, rollback, límites de costo y criterio de no promoción.

## Cómo usar cada módulo

En Angular, el módulo expone el objetivo, los conocimientos previos, una práctica, criterio de aceptación y una reflexión. El chequeo no es el final: al responder, leé la explicación y completá el siguiente paso propuesto.

En GitHub Pages, el editor es un **sandbox visual**. Sirve para leer contratos y ensayar criterios mínimos; no modifica el repositorio ni ejecuta Java. Para evidencia ejecutable, corré el proyecto real:

    docker compose up -d postgres
    cd backend && mvn test
    cd ../frontend && npm start

## Evaluación de avance

No marques un módulo como dominado hasta poder presentar estas cuatro evidencias:

- Una definición simple y un contraejemplo.
- Una decisión de diseño y su trade-off.
- Una prueba, llamada HTTP o traza observada.
- Un modo de fallo y su recuperación o fallback.

## Proyecto integrador

El cierre es un asistente sobre un corpus acotado con Angular, Spring Boot y PostgreSQL. Debe incluir JWT con permisos, RAG con citas, Self-RAG con fallback, una acción idempotente, una prueba de prompt injection, golden set, trazas y plan de rollback.

La rúbrica de aprobación es: seguridad de datos y permisos, evidencia de retrieval, contratos testeables, errores recuperables, observabilidad y explicación honesta de límites.
