package com.reeb.llmstudio.learning;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgressRepository extends JpaRepository<LearningProgress, Long> {
  List<LearningProgress> findAllByLearnerIdAndCompletedTrue(String learnerId);
  Optional<LearningProgress> findByLearnerIdAndModuleId(String learnerId, String moduleId);
}
