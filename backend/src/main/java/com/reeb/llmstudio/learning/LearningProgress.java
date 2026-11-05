package com.reeb.llmstudio.learning;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "learning_progress", uniqueConstraints = @UniqueConstraint(columnNames = {"learner_id", "module_id"}))
public class LearningProgress {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
  private String learnerId;
  private String moduleId;
  private boolean completed;
  protected LearningProgress() { }
  public LearningProgress(String learnerId, String moduleId, boolean completed) { this.learnerId=learnerId; this.moduleId=moduleId; this.completed=completed; }
  public Long getId() { return id; }
  public String getLearnerId() { return learnerId; }
  public String getModuleId() { return moduleId; }
  public boolean isCompleted() { return completed; }
  public void complete() { this.completed = true; }
}
