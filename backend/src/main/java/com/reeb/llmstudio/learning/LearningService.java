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
  private final Map<String, Integer> correctOptions = Map.of("architecture", 1, "rag", 1, "idempotency", 1, "inbox", 1, "angular", 1);
  LearningService(ModuleRepository modules, ProgressRepository progress) { this.modules=modules; this.progress=progress; }
  @PostConstruct void seed() {
    if (modules.count() > 0) return;
    List.of(new LearningModule("architecture", "Arquitectura de un sistema con LLM", "fundamentos", 1), new LearningModule("rag", "RAG: recuperar evidencia", "conocimiento", 3), new LearningModule("idempotency", "Idempotencia", "confiabilidad", 6), new LearningModule("inbox", "Inbox Pattern", "eventos", 13), new LearningModule("angular", "Angular: frontend confiable", "frontend", 17)).forEach(modules::save);
  }
  @Transactional(readOnly = true) public List<LearningDtos.ModuleResponse> modules() { return modules.findAllByOrderByPositionAsc().stream().map(m -> new LearningDtos.ModuleResponse(m.getId(),m.getTitle(),m.getCategory(),m.getPosition())).toList(); }
  @Transactional(readOnly = true) public LearningDtos.ProgressResponse progress(String learnerId) { return new LearningDtos.ProgressResponse(learnerId, progress.findAllByLearnerIdAndCompletedTrue(learnerId).stream().map(LearningProgress::getModuleId).toList()); }
  public LearningDtos.ProgressResponse complete(LearningDtos.CompleteModuleRequest request) { var item=progress.findByLearnerIdAndModuleId(request.learnerId(), request.moduleId()).orElseGet(() -> new LearningProgress(request.learnerId(),request.moduleId(),false)); item.complete(); progress.save(item); return progress(request.learnerId()); }
  @Transactional(readOnly = true) public LearningDtos.ChallengeResponse validate(LearningDtos.ChallengeRequest request) { boolean ok=correctOptions.getOrDefault(request.moduleId(), 1).equals(request.selectedOption()); return new LearningDtos.ChallengeResponse(ok, ok ? "Correcto: podés marcar el módulo." : "Todavía no: revisá la decisión de diseño y probá otra vez."); }
}
