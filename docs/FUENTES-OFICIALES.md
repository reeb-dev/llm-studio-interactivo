# Fuentes verificables del laboratorio

Este laboratorio explica decisiones de arquitectura con lenguaje propio. No reemplaza normas, documentación de los proyectos ni investigación primaria. Cada lección enlaza sus fuentes dentro de la interfaz y este índice deja el respaldo explícito y mantenible.

## Cómo leer la etiqueta de una fuente

- **Estándar IETF / especificación oficial:** define un protocolo o contrato interoperable.
- **Documentación oficial:** mantenida por el proyecto que implementa la tecnología.
- **Guía de seguridad / marco oficial:** recomendación operativa de una organización reconocida; no sustituye una evaluación de riesgo propia.
- **Investigación primaria:** artículo que presenta un método; no es una garantía de que una implementación sea segura o correcta.
- **Patrón de industria:** práctica útil sin una norma única. El laboratorio lo presenta como decisión, no como ley.

## Backend, API y datos

- [Spring Boot — OAuth2 Resource Server](https://docs.spring.io/spring-boot/reference/security/oauth2.html): configuración JWT por issuer o JWK Set URI.
- [Angular — HTTP interceptors](https://angular.dev/guide/http/interceptors): interceptores funcionales y `provideHttpClient`.
- [RFC 7519 — JSON Web Token](https://www.rfc-editor.org/rfc/rfc7519): estructura y claims JWT.
- [RFC 9110 — HTTP Semantics](https://www.rfc-editor.org/rfc/rfc9110): semántica HTTP e idempotencia de métodos.
- [RFC 9111 — HTTP Caching](https://www.rfc-editor.org/rfc/rfc9111): comportamiento de cache HTTP.
- [PostgreSQL — INSERT / ON CONFLICT](https://www.postgresql.org/docs/current/sql-insert.html): UPSERT y constraints de unicidad.
- [Jakarta Persistence](https://jakarta.ee/specifications/persistence/): especificación JPA, incluyendo concurrencia y locking.
- [Resilience4j — CircuitBreaker](https://resilience4j.readme.io/docs/circuitbreaker): estados y configuración del patrón.
- [OpenTelemetry — Traces](https://opentelemetry.io/docs/concepts/signals/traces/): trazas distribuidas y contexto.

## LLM, RAG, agentes y seguridad

- [OWASP — LLM Prompt Injection Prevention](https://cheatsheetseries.owasp.org/cheatsheets/LLM_Prompt_Injection_Prevention_Cheat_Sheet.html): contenido no confiable, permisos en tools y defensa en profundidad.
- [OWASP — RAG Security](https://cheatsheetseries.owasp.org/cheatsheets/RAG_Security_Cheat_Sheet.html): riesgos al insertar documentos recuperados en el contexto.
- [OWASP — AI Agent Security](https://cheatsheetseries.owasp.org/cheatsheets/AI_Agent_Security_Cheat_Sheet.html): herramientas, excesiva agencia y pruebas adversariales.
- [Model Context Protocol — Specification](https://modelcontextprotocol.io/specification/2025-06-18): recursos, tools y contratos MCP. MCP no es una frontera de autorización.
- [Self-RAG — paper](https://arxiv.org/abs/2310.11511): fuente primaria del enfoque retrieve-generate-critique. El verificador de este repositorio sólo comprueba soporte estructural de citas.

## IA, aprendizaje automático y operación

- [Google Machine Learning Crash Course](https://developers.google.com/machine-learning/crash-course): fundamentos, generalización, métricas y entrenamiento.
- [PyTorch — Build the Neural Network](https://docs.pytorch.org/tutorials/beginner/basics/buildmodel_tutorial.html): capas y construcción de redes neuronales.
- [Hugging Face Transformers](https://huggingface.co/docs/transformers/index): APIs y conceptos de modelos transformer.
- [MLflow Tracking](https://mlflow.org/docs/latest/ml/tracking/): tracking de experimentos y artefactos.
- [NIST AI Risk Management Framework](https://www.nist.gov/itl/ai-risk-management-framework): gobierno, riesgos y confianza. Para IA generativa, revisar también el perfil [NIST AI 600-1](https://nvlpubs.nist.gov/nistpubs/ai/NIST.AI.600-1.pdf).

## Criterio de actualización

Antes de promover una explicación como definitiva:

1. Confirmar que el enlace sigue siendo oficial o primario.
2. Registrar la versión o fecha cuando el comportamiento dependa de una API concreta.
3. Separar lo que prescribe una fuente de la decisión específica del proyecto.
4. Agregar una prueba reproducible si la afirmación describe comportamiento ejecutable.
