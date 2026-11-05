# Temario ampliado de IA aplicada

Este recorrido no enseña IA como una colección de prompts. Va de decisión y datos a modelos, sistemas, operación y gobierno. Cada bloque tiene un resultado observable.

## Bloque A — Fundamentos para decidir

1. **IA, datos y probabilidad**: población, muestra, incertidumbre, sesgo, baseline y costo de error.
2. **ML supervisado**: features, labels, train/validation/test, leakage, matriz de confusión y calibración.
3. **Redes neuronales**: forward pass, loss, gradientes, optimización, regularización y overfitting.

Resultado: formular un problema medible antes de elegir una tecnología.

## Bloque B — Modelos generativos

4. **Transformers**: tokenización, atención, context window, decoding, temperatura y costo.
5. **Prompts y salidas estructuradas**: instrucciones, delimitación de datos, JSON schema, versionado y pruebas.
6. **Fine-tuning y adapters**: cuándo adaptar comportamiento y cuándo recuperar conocimiento externo.
7. **Routing**: modelo pequeño/grande/especializado según calidad, latencia, costo y riesgo.

Resultado: seleccionar y controlar un modelo sin atribuirle capacidades que no tiene.

## Bloque C — Conocimiento verificable

8. **Embeddings**: representación semántica, similitud, límites y filtros por metadata.
9. **RAG**: ingesta, chunking, versionado, retrieval lexical/híbrido, citas y evaluation set.
10. **Self-RAG**: decisión de recuperar, crítica de evidencia, validación de citas y evidence-fallback.
11. **Seguridad de fuentes**: prompt injection, documentos no confiables, aislamiento por tenant y PII.

Resultado: responder con evidencia, o declarar explícitamente que no existe soporte.

## Bloque D — Acciones y agentes

12. **Tools y funciones**: schemas, allowlist, validación y autorización en backend.
13. **Agentes**: planificación limitada, presupuesto, estado, reintentos e intervención humana.
14. **MCP**: recursos, herramientas y contratos entre cliente y servidores; no es una frontera de seguridad.
15. **Patrones distribuidos**: idempotencia, inbox, outbox, sagas, DLQ y circuit breaker.

Resultado: ejecutar efectos controlados y auditables, no dar una terminal al modelo.

## Bloque E — Multimodalidad y producto

16. **Imagen, audio y documentos**: OCR/ASR, extracción, procedencia, confianza y privacidad.
17. **Experiencia Angular**: estados de UI, streaming, errores recuperables, accesibilidad y contratos tipados.
18. **API Spring**: DTOs, validación, transacciones, seguridad, rate limits y trazabilidad.

Resultado: una experiencia de usuario honesta sobre estados, fuentes y límites.

## Bloque F — Calidad, operación y gobierno

19. **Evaluaciones**: goldens, rúbricas, jueces calibrados, evaluación humana y release gates.
20. **Observabilidad**: traceId, logs estructurados, métricas de latencia/tokens/costo/calidad.
21. **MLOps**: versionado de datos/modelos/prompts, registry, despliegue, drift y rollback.
22. **Gobierno**: riesgo, privacidad, sesgo, dueños, apelación e incident response.

Resultado: promover cambios con evidencia y operar el sistema después de la demo.

## Proyecto integrador

Construí un asistente para una base de conocimiento acotada con:

- Angular + Spring Boot + PostgreSQL.
- JWT real y permisos por usuario/tenant.
- RAG lexical primero; híbrido sólo si las evaluaciones demuestran una brecha.
- Self-RAG con evidencia delimitada, citas válidas y fallback.
- Una herramienta de escritura idempotente con auditoría.
- Golden set, test de prompt injection, trazas y presupuesto de costo.

El proyecto se considera completo cuando se puede demostrar un flujo correcto, un fallo seguro y un rollback o recuperación operativa.
