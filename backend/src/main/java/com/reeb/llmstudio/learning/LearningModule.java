package com.reeb.llmstudio.learning;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class LearningModule {
  @Id private String id;
  private String title;
  private String category;
  private int position;
  protected LearningModule() { }
  public LearningModule(String id, String title, String category, int position) { this.id=id; this.title=title; this.category=category; this.position=position; }
  public String getId() { return id; }
  public String getTitle() { return title; }
  public String getCategory() { return category; }
  public int getPosition() { return position; }
}
