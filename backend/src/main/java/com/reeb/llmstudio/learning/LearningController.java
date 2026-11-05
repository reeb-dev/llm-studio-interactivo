package com.reeb.llmstudio.learning;

import jakarta.validation.Valid;
import java.util.List;
import com.reeb.llmstudio.rag.RagService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class LearningController {
  private final LearningService service;
  private final RagService rag;
  LearningController(LearningService service, RagService rag) { this.service=service; this.rag=rag; }
  @GetMapping("/modules") public List<LearningDtos.ModuleResponse> modules() { return service.modules(); }
  @GetMapping("/progress/{learnerId}") public LearningDtos.ProgressResponse progress(@PathVariable String learnerId) { return service.progress(learnerId); }
  @PutMapping("/progress") @ResponseStatus(HttpStatus.OK) public LearningDtos.ProgressResponse complete(@Valid @RequestBody LearningDtos.CompleteModuleRequest request) { return service.complete(request); }
  @PostMapping("/challenges/validate") public LearningDtos.ChallengeResponse validate(@Valid @RequestBody LearningDtos.ChallengeRequest request) { return service.validate(request); }
  @PostMapping("/self-rag/review") public LearningDtos.SelfRagReviewResponse reviewSelfRag(@Valid @RequestBody LearningDtos.SelfRagReviewRequest request) { return service.reviewSelfRag(request); }
  @PostMapping("/rag/ask") public LearningDtos.RagAnswerResponse askRag(@Valid @RequestBody LearningDtos.RagAskRequest request) { return rag.ask(request); }
}
