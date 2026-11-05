import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';

export interface LearningModule { id: string; title: string; category: string; position: number; }
export interface Progress { learnerId: string; completedModuleIds: string[]; }
export interface ChallengeResult { correct: boolean; feedback: string; }
export interface SelfRagReview { grounded: boolean; answer: string; validCitations: string[]; reasons: string[]; }

@Injectable({ providedIn: 'root' })
export class LearningApi {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = 'http://localhost:8080/api';
  modules() { return this.http.get<LearningModule[]>(`${this.baseUrl}/modules`); }
  progress(learnerId: string) { return this.http.get<Progress>(`${this.baseUrl}/progress/${learnerId}`); }
  complete(learnerId: string, moduleId: string) { return this.http.put<Progress>(`${this.baseUrl}/progress`, { learnerId, moduleId }); }
  validate(moduleId: string, selectedOption: number) { return this.http.post<ChallengeResult>(`${this.baseUrl}/challenges/validate`, { moduleId, selectedOption }); }
  reviewSelfRag(answer: string, citations: string[]) {
    return this.http.post<SelfRagReview>(`${this.baseUrl}/self-rag/review`, {
      answer,
      citations,
      evidence: [{ id: 'retry-policy', content: 'Usar backoff exponencial con jitter para fallos transitorios.' }]
    });
  }
}
