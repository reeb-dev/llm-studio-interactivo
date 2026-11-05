package com.reeb.llmstudio.learning;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class LearningCurriculumTest {
  @Test void everyPublishedModuleHasGuidedPracticeAndSpecificChallenge() {
    assertEquals(30, LearningCurriculum.size());
    var rag = LearningCurriculum.forId("rag");
    var capstone = LearningCurriculum.forId("ai-capstone");
    assertFalse(rag.practice().isBlank());
    assertFalse(rag.acceptance().isBlank());
    assertEquals(3, rag.options().size());
    assertTrue(capstone.question().contains("proyecto IA"));
  }
}
