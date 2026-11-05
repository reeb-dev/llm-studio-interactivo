package com.reeb.llmstudio.learning;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LearningService {
  private final ModuleRepository modules;
  private final ProgressRepository progress;
  private final SelfRagVerifier selfRagVerifier;
  private final Map<String, Integer> correctOptions = Map.ofEntries(
      Map.entry("architecture", 1), Map.entry("context", 1), Map.entry("rag", 1),
      Map.entry("tools", 1), Map.entry("jwt", 1), Map.entry("idempotency", 1),
      Map.entry("outbox", 0), Map.entry("resilience", 1), Map.entry("observability", 0),
      Map.entry("evals", 1), Map.entry("security", 1), Map.entry("performance", 1),
      Map.entry("inbox", 1), Map.entry("optimistic", 0), Map.entry("upsert", 1),
      Map.entry("saga", 1), Map.entry("angular", 1), Map.entry("self-rag", 1));
  LearningService(ModuleRepository modules, ProgressRepository progress, SelfRagVerifier selfRagVerifier) { this.modules=modules; this.progress=progress; this.selfRagVerifier=selfRagVerifier; }
  @PostConstruct void seed() {
    if (modules.count() > 0) return;
    List.of(
        new LearningModule("architecture", "Arquitectura de un sistema con LLM", "fundamentos", 1),
        new LearningModule("context", "Contexto, prompts y memoria", "orquestación", 2),
        new LearningModule("rag", "RAG: recuperar evidencia", "conocimiento", 3),
        new LearningModule("tools", "Funciones y herramientas seguras", "acciones", 4),
        new LearningModule("jwt", "JWT, sesiones y autorización", "seguridad", 5),
        new LearningModule("idempotency", "Idempotencia", "confiabilidad", 6),
        new LearningModule("outbox", "Outbox, eventos y sagas", "consistencia", 7),
        new LearningModule("resilience", "Timeouts, reintentos y DLQ", "resiliencia", 8),
        new LearningModule("observability", "Trazas, logs, métricas y costos", "operación", 9),
        new LearningModule("evals", "Evaluaciones y regresiones", "calidad", 10),
        new LearningModule("security", "Guardrails, secretos y privacidad", "seguridad", 11),
        new LearningModule("performance", "Cache, cuotas y costos", "escala", 12),
        new LearningModule("inbox", "Inbox Pattern", "eventos", 13),
        new LearningModule("optimistic", "Optimistic Locking", "concurrencia", 14),
        new LearningModule("upsert", "UPSERT y claves naturales", "datos", 15),
        new LearningModule("saga", "Saga Pattern", "consistencia", 16),
        new LearningModule("angular", "Angular: frontend confiable", "frontend", 17),
        new LearningModule("self-rag", "Self-RAG: recuperar, criticar y responder", "conocimiento", 18),
        new LearningModule("ai-foundations", "Fundamentos de IA y probabilidad", "ia", 19),
        new LearningModule("supervised-learning", "Machine Learning supervisado", "ml", 20),
        new LearningModule("neural-networks", "Redes neuronales y entrenamiento", "ml", 21),
        new LearningModule("transformers", "Transformers y atención", "llm", 22),
        new LearningModule("embeddings", "Embeddings y bases vectoriales", "conocimiento", 23),
        new LearningModule("agents-mcp", "Agentes, tools y MCP", "agentes", 24),
        new LearningModule("multimodal", "IA multimodal", "llm", 25),
        new LearningModule("fine-tuning", "Fine-tuning, adapters y routing", "mlops", 26),
        new LearningModule("mlops", "MLOps, datos y despliegue", "operación", 27),
        new LearningModule("ai-governance", "Seguridad, ética y gobierno de IA", "gobierno", 28),
        new LearningModule("ai-evals", "Evaluaciones de modelos y productos IA", "calidad", 29),
        new LearningModule("ai-capstone", "Proyecto integrador de IA", "proyecto", 30)
    ).forEach(modules::save);
  }
  @Transactional(readOnly = true) public List<LearningDtos.ModuleResponse> modules() { return modules.findAllByOrderByPositionAsc().stream().map(m -> new LearningDtos.ModuleResponse(m.getId(),m.getTitle(),m.getCategory(),m.getPosition())).toList(); }
  @Transactional(readOnly = true) public LearningDtos.ProgressResponse progress(String learnerId) { return new LearningDtos.ProgressResponse(learnerId, progress.findAllByLearnerIdAndCompletedTrue(learnerId).stream().map(LearningProgress::getModuleId).toList()); }
  public LearningDtos.ProgressResponse complete(LearningDtos.CompleteModuleRequest request) { var item=progress.findByLearnerIdAndModuleId(request.learnerId(), request.moduleId()).orElseGet(() -> new LearningProgress(request.learnerId(),request.moduleId(),false)); item.complete(); progress.save(item); return progress(request.learnerId()); }
  @Transactional(readOnly = true) public LearningDtos.ChallengeResponse validate(LearningDtos.ChallengeRequest request) { boolean ok=correctOptions.getOrDefault(request.moduleId(), 1).equals(request.selectedOption()); return new LearningDtos.ChallengeResponse(ok, ok ? "Correcto: podés marcar el módulo." : "Todavía no: revisá la decisión de diseño y probá otra vez."); }
  @Transactional(readOnly = true) public LearningDtos.SelfRagReviewResponse reviewSelfRag(LearningDtos.SelfRagReviewRequest request) { return selfRagVerifier.review(request); }
}
