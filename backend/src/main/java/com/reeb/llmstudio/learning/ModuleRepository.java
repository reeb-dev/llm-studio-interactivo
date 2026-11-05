package com.reeb.llmstudio.learning;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModuleRepository extends JpaRepository<LearningModule, String> { List<LearningModule> findAllByOrderByPositionAsc(); }
