package com.reeb.llmstudio.learning;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * Verificador determinista del paso de crítica de Self-RAG.
 *
 * No decide si una afirmación es semánticamente verdadera: comprueba lo que el
 * sistema sí puede exigir de forma reproducible antes de mostrar una respuesta:
 * evidencia recuperada, citas no vacías y citas que existan dentro del corpus
 * recuperado. Si no se cumple, la respuesta segura es evidence-fallback.
 */
@Service
public class SelfRagVerifier {
  public LearningDtos.SelfRagReviewResponse review(LearningDtos.SelfRagReviewRequest request) {
    Set<String> available = request.evidence().stream()
        .map(LearningDtos.Evidence::id)
        .collect(Collectors.toUnmodifiableSet());
    List<String> citations = request.citations() == null ? List.of() : request.citations();
    List<String> unknown = citations.stream().filter(id -> !available.contains(id)).toList();

    if (available.isEmpty()) return fallback(List.of("No hay evidencia recuperada para verificar."));
    if (citations.isEmpty()) return fallback(List.of("La respuesta no incluye citas verificables."));
    if (!unknown.isEmpty()) return fallback(List.of("Citas fuera del conjunto recuperado: " + String.join(", ", unknown)));

    return new LearningDtos.SelfRagReviewResponse(
        true,
        request.answer(),
        List.copyOf(citations),
        List.of("Citas estructuralmente verificadas. Aún requiere evaluación semántica y humana según el riesgo."));
  }

  private LearningDtos.SelfRagReviewResponse fallback(List<String> reasons) {
    return new LearningDtos.SelfRagReviewResponse(
        false,
        "No encontré evidencia suficiente para responder con confianza.",
        List.of(),
        reasons);
  }
}
