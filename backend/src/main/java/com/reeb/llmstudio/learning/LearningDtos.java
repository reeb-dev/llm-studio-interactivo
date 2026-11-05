package com.reeb.llmstudio.learning;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public final class LearningDtos {
  private LearningDtos() { }
  public record ModuleResponse(String id, String title, String category, int position) { }
  public record ProgressResponse(String learnerId, List<String> completedModuleIds) { }
  public record CompleteModuleRequest(@NotBlank String learnerId, @NotBlank String moduleId) { }
  public record ChallengeRequest(@NotBlank String moduleId, @NotNull Integer selectedOption) { }
  public record ChallengeResponse(boolean correct, String feedback) { }
}
