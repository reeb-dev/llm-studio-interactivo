package com.reeb.llmstudio.learning;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class LearningController {
  private final LearningService service;
  LearningController(LearningService service) { this.service=service; }
  @GetMapping("/modules") public List<LearningDtos.ModuleResponse> modules() { return service.modules(); }
  @GetMapping("/progress/{learnerId}") public LearningDtos.ProgressResponse progress(@PathVariable String learnerId) { return service.progress(learnerId); }
  @PutMapping("/progress") @ResponseStatus(HttpStatus.OK) public LearningDtos.ProgressResponse complete(@Valid @RequestBody LearningDtos.CompleteModuleRequest request) { return service.complete(request); }
  @PostMapping("/challenges/validate") public LearningDtos.ChallengeResponse validate(@Valid @RequestBody LearningDtos.ChallengeRequest request) { return service.validate(request); }
}
