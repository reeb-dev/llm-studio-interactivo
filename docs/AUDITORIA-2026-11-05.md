# Auditoría del laboratorio — 2026-11-05

## Verificado en esta revisión

| Área | Estado | Evidencia |
| --- | --- | --- |
| Cobertura didáctica estática | Corregido | 30 módulos, mini-retos, objetivo, práctica, criterio y reflexión. |
| Paridad API / Angular | Corregido | Spring entrega el mismo reto específico, guía y feedback que consume Angular para los 30 módulos. |
| Self-RAG estructural | Implementado y probado | `POST /api/self-rag/review`, `SelfRagVerifierTest` cubre cita ausente, válida y desconocida. |
| Contrato Self-RAG | Verificado en ejecución | Respuesta grounded con cita recuperada; `evidence-fallback` sin cita. |
| Compilación | Verificada | `mvn test` y `NG_BUILD_MAX_WORKERS=2 npm run build`. |
| Laboratorio publicado | Verificado | Pages sirve `dist/` con árbol de proyecto, editor temporal y documentación. |

## Qué hace realmente Self-RAG aquí

El verificador es deliberadamente determinista y limitado: exige evidencia recuperada y que toda cita pertenezca a ese conjunto. Si falla, devuelve `evidence-fallback`. Esto evita inventar soporte, pero **no demuestra que una afirmación sea semánticamente verdadera**. Para ese nivel se necesita un set dorado, rúbrica de crítica, evaluación humana y métricas de falsos positivos/negativos.

## Estado actual y límites abiertos

1. **GitHub Pages no ejecuta Spring Boot.** Publica `dist/`, que es un laboratorio interactivo estático. La aplicación Angular + Spring se ejecuta localmente; para producción hacen falta hosting de API, URL por entorno y CORS restringido al dominio final.
2. **JWT está listo para un emisor real, no configurado públicamente.** Existe `SecurityFilterChain` condicional y perfil PostgreSQL con resource server JWT. Faltan un issuer/JWK real, roles de dominio, pruebas 401/403 y un deploy de API antes de declararlo protegido en producción.
3. **H2 sigue siendo efímera para el modo local.** PostgreSQL, Flyway y Docker Compose ya están preparados; falta desplegar esa configuración y asociar el progreso a una identidad autenticada.
4. **El verificador no evalúa semántica.** Hay RAG lexical ejecutable, evidencia, citas y fallback; faltan ingesta versionada, embeddings/híbrido, aislamiento por tenant y evaluación automática de groundedness.
5. **Editor de Pages es sandbox.** Comprueba criterios pedagógicos y no escribe en Git/GitHub ni ejecuta Java en el navegador. Los cambios reales se hacen en `frontend/` y `backend/`, con sus builds/tests.

## Actualización posterior verificada

- Se añadió RAG lexical ejecutable, con evidencia, citas y fallback validado por Self-RAG.
- Se añadió Docker Compose para PostgreSQL y Flyway con una migración inicial.
- Se añadió un perfil postgres que activa Spring Security Resource Server cuando existe un emisor JWT real.
- Se agregó traceId a cada respuesta HTTP y un workflow de calidad para backend, Angular y laboratorio estático.
- Angular y Spring ahora comparten para cada módulo objetivo, prerrequisitos, práctica, criterio de aceptación, reflexión y un reto específico validado del lado del servidor.
- La publicación versiona el asset JavaScript para reducir la posibilidad de que Pages muestre una ruta antigua desde caché.

## Próximo corte recomendado

1. Configurar un issuer real, roles de dominio y pruebas 401/403.
2. Desplegar PostgreSQL/Flyway y persistir progreso por usuario autenticado.
3. Implementar ingesta versionada, retrieval híbrido y corpus aislado por tenant.
4. Crear set dorado y pipeline de evaluación: `hit@k`, validez de citas, fallback y tasa de errores del crítico.
5. Desplegar API y Angular por separado; cambiar el `baseUrl` por configuración de entorno y probar el flujo completo publicado.
