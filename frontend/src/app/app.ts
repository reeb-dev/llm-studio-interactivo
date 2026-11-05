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
  protected readonly ragQuestion = signal('¿Cómo funcionan los reintentos?');
  protected readonly ragResult = signal('Consultá el corpus del laboratorio para ver evidencia y citas.');
  protected readonly progress = computed(() => `${this.completed().size} / ${this.modules().length || 0}`);

  protected readonly challenges: Record<string, Challenge> = {
    architecture: { question: '¿Quién ejecuta una acción con efecto real?', options: ['El navegador', 'El backend validado', 'El modelo'], correct: 1 },
    context: { question: '¿Cuál es una memoria segura por defecto?', options: ['Historial infinito', 'Resumen con TTL y consentimiento', 'Guardar secretos en el prompt'], correct: 1 },
    rag: { question: '¿Qué pasa si no hay evidencia recuperada?', options: ['Se inventa una respuesta', 'Se usa evidence-fallback', 'Se expande el prompt'], correct: 1 },
    tools: { question: '¿Qué valida los argumentos de una herramienta?', options: ['El modelo', 'El schema y el backend', 'El texto del usuario'], correct: 1 },
    jwt: { question: '¿Qué demuestra la firma de un JWT?', options: ['Que el payload es secreto', 'Que no fue alterado', 'Que tiene todos los permisos'], correct: 1 },
    idempotency: { question: '¿Qué devuelve una repetición con la misma key?', options: ['Un recurso nuevo', 'El resultado guardado', 'Un 500'], correct: 1 },
    outbox: { question: '¿Cuándo se registra un evento de outbox?', options: ['En la transacción de la entidad', 'Después de publicar', 'Sólo si falla el relay'], correct: 0 },
    resilience: { question: '¿Cuándo es seguro reintentar?', options: ['Siempre', 'Ante un fallo transitorio y operación segura', 'Nunca'], correct: 1 },
    observability: { question: '¿Qué permite reconstruir una respuesta?', options: ['Un traceId entre capas', 'Sólo un error log', 'El navegador'], correct: 0 },
    evals: { question: '¿Cómo aislás un problema de retrieval?', options: ['Evaluando todo junto', 'Midiendo retrieval y respuesta por separado', 'Cambiando el modelo'], correct: 1 },
    security: { question: '¿Cómo se trata un fragmento recuperado?', options: ['Como instrucción', 'Como dato no confiable delimitado', 'Como secreto'], correct: 1 },
    performance: { question: '¿Qué debe incluir una cache key?', options: ['Sólo la pregunta', 'Tenant, permisos y versiones', 'Sólo el modelo'], correct: 1 },
    inbox: { question: '¿Qué evita duplicar el efecto de un evento?', options: ['ACK temprano', 'eventId único en inbox', 'Más consumidores'], correct: 1 },
    optimistic: { question: '¿Qué señala un conflicto optimista?', options: ['0 filas actualizadas por versión', 'Servidor lento', 'Falta una cola'], correct: 0 },
    upsert: { question: '¿Qué identidad conviene para sincronizar un catálogo?', options: ['Nombre visible', 'externalId estable con índice único', 'Timestamp'], correct: 1 },
    saga: { question: '¿Qué hace una compensación?', options: ['Rollback distribuido', 'Revierte un paso de negocio', 'Borra logs'], correct: 1 },
    angular: { question: '¿Dónde se autoriza una operación?', options: ['Angular guard', 'Backend', 'Local storage'], correct: 1 },
    'self-rag': { question: 'Si las citas no pertenecen a la evidencia recuperada, ¿qué debe hacer el sistema?', options: ['Responder igual', 'Usar evidence-fallback', 'Inventar una cita válida'], correct: 1 }
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

  protected challengeFor(moduleId: string): Challenge {
    return this.challenges[moduleId] ?? {
      question: '¿Qué práctica vuelve este componente verificable y mantenible?',
      options: ['Confiar en resultados visuales', 'Definir contrato, prueba y métrica', 'Ocultar los errores'],
      correct: 1
    };
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
