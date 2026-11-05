# Auditoría del laboratorio — 2026-11-05

## Verificado en esta revisión

| Área | Estado | Evidencia |
| --- | --- | --- |
| Cobertura didáctica estática | Corregido | 18 módulos y 18 mini-retos, incluido `18_self-rag.md`. |
| Paridad API / Angular | Corregido | Spring siembra y valida los mismos 18 módulos que muestra Angular. |
| Self-RAG estructural | Implementado y probado | `POST /api/self-rag/review`, `SelfRagVerifierTest` cubre cita ausente, válida y desconocida. |
| Contrato Self-RAG | Verificado en ejecución | Respuesta grounded con cita recuperada; `evidence-fallback` sin cita. |
| Compilación | Verificada | `mvn test` y `NG_BUILD_MAX_WORKERS=2 npm run build`. |
| Laboratorio publicado | Verificado | Pages sirve `dist/` con árbol de proyecto, editor temporal y documentación. |

## Qué hace realmente Self-RAG aquí

El verificador es deliberadamente determinista y limitado: exige evidencia recuperada y que toda cita pertenezca a ese conjunto. Si falla, devuelve `evidence-fallback`. Esto evita inventar soporte, pero **no demuestra que una afirmación sea semánticamente verdadera**. Para ese nivel se necesita un set dorado, rúbrica de crítica, evaluación humana y métricas de falsos positivos/negativos.

## Hallazgos que siguen abiertos

1. **GitHub Pages no ejecuta Spring Boot.** Publica `dist/`, que es un laboratorio interactivo estático. La aplicación Angular + Spring se ejecuta localmente; para producción hacen falta hosting de API, URL por entorno y CORS restringido al dominio final.
2. **JWT está enseñado, no aplicado como seguridad real.** Angular tiene interceptor de demostración, pero Spring no tiene un `SecurityFilterChain`, emisor/JWK, validación de tokens, roles ni pruebas 401/403. No se debe presentar esta API como protegida.
3. **H2 es efímera.** El progreso se pierde al reiniciar la API. PostgreSQL, migraciones y una identidad autenticada son necesarios para una experiencia persistente/multiusuario.
4. **El verificador no evalúa semántica.** Todavía no hay ingesta, embeddings, vector store, reindexado, corpus por tenant ni evaluación automática de groundedness. Es el siguiente incremento, no una promesa ya cumplida.
5. **Editor de Pages es sandbox.** Comprueba criterios pedagógicos y no escribe en Git/GitHub ni ejecuta Java en el navegador. Los cambios reales se hacen en `frontend/` y `backend/`, con sus builds/tests.

## Actualización posterior

- Se añadió RAG lexical ejecutable, con evidencia, citas y fallback validado por Self-RAG.
- Se añadió Docker Compose para PostgreSQL y Flyway con una migración inicial.
- Se añadió un perfil postgres que activa Spring Security Resource Server cuando existe un emisor JWT real.
- Se agregó traceId a cada respuesta HTTP y un workflow de calidad para backend, Angular y laboratorio estático.
- Los puntos históricos de arriba se conservan como auditoría inicial; esta sección representa el estado actual.

## Próximo corte recomendado

1. Añadir Spring Security como resource server y pruebas de autorización.
2. Reemplazar H2 por PostgreSQL + Flyway y persistir el progreso por usuario autenticado.
3. Implementar ingesta versionada, retrieval híbrido y corpus aislado por tenant.
4. Crear set dorado y pipeline de evaluación: `hit@k`, validez de citas, fallback y tasa de errores del crítico.
5. Desplegar API y Angular por separado; cambiar el `baseUrl` por configuración de entorno y probar el flujo completo publicado.
