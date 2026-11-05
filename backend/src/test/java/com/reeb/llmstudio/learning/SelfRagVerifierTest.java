package com.reeb.llmstudio.learning;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class SelfRagVerifierTest {
  private final SelfRagVerifier verifier = new SelfRagVerifier();

  @Test
  void fallsBackWhenThereAreNoCitations() {
    var result = verifier.review(new LearningDtos.SelfRagReviewRequest(
        "Los reintentos usan backoff.",
        List.of(new LearningDtos.Evidence("retry-policy", "Usar backoff exponencial con jitter.")),
        List.of()));

    assertThat(result.grounded()).isFalse();
    assertThat(result.answer()).contains("evidencia suficiente");
  }

  @Test
  void acceptsCitationsInsideRetrievedEvidence() {
    var result = verifier.review(new LearningDtos.SelfRagReviewRequest(
        "Los reintentos usan backoff [retry-policy].",
        List.of(new LearningDtos.Evidence("retry-policy", "Usar backoff exponencial con jitter.")),
        List.of("retry-policy")));

    assertThat(result.grounded()).isTrue();
    assertThat(result.validCitations()).containsExactly("retry-policy");
  }

  @Test
  void fallsBackWhenCitationWasNotRetrieved() {
    var result = verifier.review(new LearningDtos.SelfRagReviewRequest(
        "Respuesta [inventada].",
        List.of(new LearningDtos.Evidence("retry-policy", "Usar backoff exponencial con jitter.")),
        List.of("inventada")));

    assertThat(result.grounded()).isFalse();
    assertThat(result.reasons().getFirst()).contains("fuera del conjunto recuperado");
  }
}
