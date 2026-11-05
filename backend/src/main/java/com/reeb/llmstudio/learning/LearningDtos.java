package com.reeb.llmstudio.learning;

import jakarta.validation.Valid;
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
  public record Evidence(@NotBlank String id, @NotBlank String content) { }
  public record SelfRagReviewRequest(
      @NotBlank String answer,
      @NotNull List<@Valid Evidence> evidence,
      List<@NotBlank String> citations) { }
  public record SelfRagReviewResponse(boolean grounded, String answer, List<String> validCitations, List<String> reasons) { }
}
