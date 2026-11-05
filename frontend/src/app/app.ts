import { Component, computed, inject, signal } from '@angular/core';
import { LearningApi, LearningModule } from './learning-api';

@Component({
  selector: 'app-root',
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  private readonly api = inject(LearningApi);
  protected readonly learnerId = this.getLearnerId();
  protected readonly modules = signal<LearningModule[]>([]);
  protected readonly completed = signal<Set<string>>(new Set());
  protected readonly selected = signal<LearningModule | null>(null);
  protected readonly loading = signal(true);
  protected readonly feedback = signal('Elegí una respuesta para validar el reto contra la API.');
  protected readonly status = signal('Conectando con Spring Boot…');
  protected readonly ragQuestion = signal('¿Cómo funcionan los reintentos?');
  protected readonly ragResult = signal('Consultá el corpus del laboratorio para ver evidencia y citas.');
  protected readonly progress = computed(() => `${this.completed().size} / ${this.modules().length || 0}`);

  constructor() { this.load(); }

  protected select(module: LearningModule): void {
    this.selected.set(module);
    this.feedback.set('Reto listo. Elegí una alternativa y observá la respuesta del backend.');
  }

  protected answer(index: number): void {
    const module = this.selected();
    if (!module) return;
    this.api.validate(module.id, index).subscribe({
      next: result => {
        this.feedback.set(`${result.feedback} ${result.explanation} Siguiente paso: ${result.nextStep}`);
        if (result.correct) this.complete(module.id);
      },
      error: () => this.feedback.set('No se pudo validar el reto. Revisá que Spring Boot esté activo en :8080.')
    });
  }

  protected complete(moduleId: string): void {
    this.api.complete(this.learnerId, moduleId).subscribe({
      next: progress => this.completed.set(new Set(progress.completedModuleIds)),
      error: () => this.feedback.set('El reto fue correcto, pero no se pudo guardar el progreso.')
    });
  }

  protected reviewSelfRag(includeCitation: boolean): void {
    this.api.reviewSelfRag('Los reintentos usan backoff [retry-policy].', includeCitation ? ['retry-policy'] : []).subscribe({
      next: review => this.feedback.set(`${review.grounded ? 'Grounded' : 'Evidence fallback'}: ${review.answer} · ${review.reasons.join(' ')}`),
      error: () => this.feedback.set('No se pudo revisar Self-RAG. Verificá que Spring Boot esté activo en :8080.')
    });
  }

  protected setRagQuestion(question: string): void { this.ragQuestion.set(question); }

  protected askRag(): void {
    this.ragResult.set('Buscando evidencia en el corpus…');
    this.api.askRag(this.ragQuestion()).subscribe({
      next: answer => this.ragResult.set(
        (answer.grounded ? 'Grounded' : 'Evidence fallback') + ' · ' + answer.answer + '\n' +
        'Citas: ' + (answer.citations.join(', ') || 'ninguna') + ' · estrategia: ' + answer.strategy + '\n' +
        'Motivo: ' + answer.reasons.join(' ')),
      error: () => this.ragResult.set('No se pudo consultar RAG. Verificá Spring Boot en :8080.')
    });
  }

  private load(): void {
    this.api.modules().subscribe({
      next: modules => {
        this.modules.set(modules);
        this.selected.set(modules[0] ?? null);
        this.api.progress(this.learnerId).subscribe({
          next: progress => this.completed.set(new Set(progress.completedModuleIds)),
          error: () => this.status.set('API disponible; progreso nuevo para este learner.')
        });
        this.status.set('API Spring conectada · H2 en memoria · retos validados en servidor');
        this.loading.set(false);
      },
      error: () => { this.status.set('API no disponible. Iniciá Spring Boot en localhost:8080.'); this.loading.set(false); }
    });
  }

  private getLearnerId(): string {
    const key = 'llm-studio-learner-id';
    const current = localStorage.getItem(key);
    if (current) return current;
    const id = crypto.randomUUID();
    localStorage.setItem(key, id);
    return id;
  }
}
