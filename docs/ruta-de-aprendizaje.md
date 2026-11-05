# Ruta de aprendizaje: patrones para un sistema LLM real

## Cómo usar este laboratorio

Para cada módulo, seguí este ciclo:

1. Nombrá el problema de negocio o de operación.
2. Leé la decisión de diseño y el pseudocódigo.
3. Ejecutá el playground y observá el evento producido.
4. Intentá el fallo indicado.
5. Verificá el criterio de aceptación antes de avanzar.

## Patrones de consistencia y eventos

| Patrón | ¿Dónde se aplica mejor? | Complejidad | Infraestructura necesaria |
| --- | --- | --- | --- |
| Inbox Pattern | Consumidores de Kafka/RabbitMQ con entrega al menos una vez | Media | Tabla SQL `inbox` con clave única por evento y consumidor |
| Optimistic Locking | Entidades compartidas en CRUD/JPA con conflictos poco frecuentes | Baja | Columna de versión; Hibernate/JPA ofrece `@Version` |
| UPSERT / clave natural | Catálogos, importaciones y sincronizaciones masivas | Muy baja | Base compatible y un índice único por `external_id` o clave de negocio |
| Saga Pattern | Proceso de negocio que cruza múltiples servicios | Alta | Estado durable; Temporal o Camunda ayudan con timers, reintentos y compensaciones |
| Idempotencia | POST de pagos, altas, webhooks y reintentos cliente-servidor | Media | Tabla/almacenamiento de claves y resultados por TTL |
| Transactional Outbox | Escritura SQL que debe producir un evento externo | Media | Tabla outbox y relay/worker publicador |

### Inbox Pattern

El broker puede reentregar un mensaje. Antes de aplicar el efecto de negocio, insertá `event_id` en una tabla con restricción única, junto con el nombre del consumidor. Si ya existe, devolvé éxito sin repetir el efecto. Confirmá el mensaje sólo después del commit.

### Optimistic Locking

Al leer una entidad entregá su `version`. El update compara `id` y `version`; si no actualiza filas, otra persona cambió el registro. Devolvé un conflicto entendible para que la UI recargue y permita resolverlo.

### UPSERT y clave natural

La identidad para sincronizar no debe ser el nombre visible. Usá un `external_id` estable o una combinación de negocio respaldada por un índice único. Repetir la importación debe dejar el mismo estado final.

### Saga Pattern

Una saga no brinda rollback distribuido. Define pasos locales y sus compensaciones: reservar stock, cobrar, crear envío; si falla el cobro, liberar stock. Cada comando y compensación también debe ser idempotente.

## RAG bien delimitado

Un RAG de producción tiene cuatro etapas: ingesta/versionado, chunking con metadatos, retrieval y respuesta grounded. Los documentos recuperados son datos no confiables: delimitálos y no permitas que reescriban las instrucciones del sistema.

Empezá por búsqueda lexical con preguntas doradas. Introducí retrieval híbrido sólo cuando haya brechas semánticas demostrables. Medí `hit@k`, MRR, validez de citas y la tasa de `evidence-fallback`.

## Self-RAG: recuperación con crítica antes de responder

Self-RAG no es “RAG con otro prompt”. Incorpora decisiones y controles explícitos entre recuperar y publicar la respuesta:

1. Decidir si la consulta requiere evidencia externa o del corpus.
2. Recuperar fragmentos con `id`, versión y metadatos de aislamiento (tenant, permisos, fecha).
3. Delimitar cada fragmento como dato no confiable; nunca como instrucción.
4. Generar un borrador con citas estructuradas.
5. Criticar el borrador: ¿hay evidencia?, ¿cada cita pertenece al retrieval?, ¿falta soporte?, ¿hay contradicción?
6. Publicar sólo si pasa los controles; de lo contrario usar `evidence-fallback` y registrar el motivo.

El endpoint `POST /api/self-rag/review` del proyecto implementa un verificador **estructural**: rechaza falta de evidencia, falta de citas y citas que no llegaron en el retrieval. No afirma demostrar verdad semántica; para eso hacen falta una rúbrica, casos dorados y revisión humana proporcional al riesgo.

### Pruebas mínimas para Self-RAG

- Sin evidencia → `evidence-fallback`.
- Con evidencia pero sin citas → `evidence-fallback`.
- Con cita fuera del conjunto recuperado → `evidence-fallback`.
- Con cita presente en el conjunto recuperado → respuesta estructuralmente grounded.
- Con una instrucción maliciosa dentro de un fragmento → se trata como texto, no modifica las políticas del sistema.

Medí cobertura de citas, tasa de fallback, precisión de la crítica contra un set dorado y falsos positivos/falsos negativos del crítico. Versioná prompt, corpus, retrieval y rúbrica para comparar resultados.

## Frontend Angular

Angular consume contratos, no aplica seguridad de negocio. Modelá en la UI los estados `idle`, `loading`, `success`, `empty` y `error`; usá formularios reactivos, servicios HTTP tipados y un interceptor para el token. Un guard mejora UX, pero el backend sigue siendo el único que autoriza.

## Criterios de salida de un proyecto real

- Cada endpoint crítico define autenticación, autorización, timeout, idempotencia y trazabilidad.
- Cada mensaje tiene ID, contrato versionado, consumidor idempotente y estrategia de reintento/DLQ.
- RAG y Self-RAG limitan el corpus, delimitan fuentes no confiables, validan citas y tienen evaluación repetible.
- Prompt, modelo, corpus y herramientas se versionan y se miden en producción.
- El frontend expone errores recuperables y no almacena secretos.
