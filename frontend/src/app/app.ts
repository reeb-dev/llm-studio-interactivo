import { Component, computed, inject, signal } from '@angular/core';
import { LearningApi, LearningModule } from './learning-api';

type Challenge = { question: string; options: string[]; correct: number };

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
  protected readonly progress = computed(() => `${this.completed().size} / ${this.modules().length || 0}`);

  protected readonly challenges: Record<string, Challenge> = {
    architecture: { question: '¿Quién ejecuta una acción con efecto real?', options: ['El navegador', 'El backend validado', 'El modelo'], correct: 1 },
    rag: { question: '¿Qué pasa si no hay evidencia recuperada?', options: ['Se inventa una respuesta', 'Se usa evidence-fallback', 'Se expande el prompt'], correct: 1 },
    idempotency: { question: '¿Qué devuelve una repetición con la misma key?', options: ['Un recurso nuevo', 'El resultado guardado', 'Un 500'], correct: 1 },
    inbox: { question: '¿Qué evita duplicar el efecto de un evento?', options: ['ACK temprano', 'eventId único en inbox', 'Más consumidores'], correct: 1 },
    angular: { question: '¿Dónde se autoriza una operación?', options: ['Angular guard', 'Backend', 'Local storage'], correct: 1 }
  };

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
        this.feedback.set(result.feedback);
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
