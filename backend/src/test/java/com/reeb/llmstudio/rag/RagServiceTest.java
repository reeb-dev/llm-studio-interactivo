package com.reeb.llmstudio.rag;

import static org.assertj.core.api.Assertions.assertThat;

import com.reeb.llmstudio.learning.LearningDtos;
import com.reeb.llmstudio.learning.SelfRagVerifier;
import org.junit.jupiter.api.Test;

class RagServiceTest {
  private final RagService service = new RagService(new SelfRagVerifier());

  @Test
  void retrievesEvidenceAndReturnsGroundedAnswer() {
    var answer = service.ask(new LearningDtos.RagAskRequest("¿Cómo funcionan los reintentos?", 3));

    assertThat(answer.grounded()).isTrue();
    assertThat(answer.citations()).containsExactly("retry-policy");
    assertThat(answer.strategy()).isEqualTo("lexical-v1");
  }

  @Test
  void fallsBackWhenCorpusHasNoSupport() {
    var answer = service.ask(new LearningDtos.RagAskRequest("¿Cuál es el clima de Marte?", 3));

    assertThat(answer.grounded()).isFalse();
    assertThat(answer.answer()).contains("evidencia suficiente");
  }
}
