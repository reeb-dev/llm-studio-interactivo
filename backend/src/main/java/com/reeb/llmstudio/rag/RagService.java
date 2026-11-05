package com.reeb.llmstudio.rag;

import com.reeb.llmstudio.learning.LearningDtos;
import com.reeb.llmstudio.learning.SelfRagVerifier;
import java.text.Normalizer;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * Retrieval lexical deliberadamente simple y ejecutable para el laboratorio.
 * El siguiente nivel reemplaza el ranking por híbrido/vectorial sin cambiar el
 * contrato de evidencia ni la verificación Self-RAG.
 */
@Service
public class RagService {
  private final SelfRagVerifier verifier;
  private final List<Document> corpus = List.of(
      new Document("retry-policy", "Reintentos", "Para fallos transitorios usar timeout, backoff exponencial con jitter e idempotencia."),
      new Document("idempotency", "Idempotencia", "Una idempotency key liga una intención a un resultado y permite reintentos sin duplicar efectos."),
      new Document("inbox-pattern", "Inbox", "El consumidor registra eventId con índice único antes de aplicar efectos y hace ACK después del commit."),
      new Document("self-rag", "Self-RAG", "Self-RAG recupera evidencia, genera un borrador, valida citas y usa evidence-fallback si la evidencia no alcanza."));

  public RagService(SelfRagVerifier verifier) { this.verifier = verifier; }

  public LearningDtos.RagAnswerResponse ask(LearningDtos.RagAskRequest request) {
    Set<String> terms = tokens(request.question());
    List<Document> hits = corpus.stream()
        .map(document -> new ScoredDocument(document, score(terms, document)))
        .filter(scored -> scored.score() > 0)
        .sorted(Comparator.comparingInt(ScoredDocument::score).reversed())
        .limit(Math.min(request.topKOrDefault(), 3))
        .map(ScoredDocument::document)
        .toList();

    List<LearningDtos.Evidence> evidence = hits.stream()
        .map(hit -> new LearningDtos.Evidence(hit.id(), hit.content()))
        .toList();
    String answer = hits.isEmpty()
        ? "No encontré una fuente del corpus que responda esa consulta."
        : hits.getFirst().content() + " [" + hits.getFirst().id() + "]";
    List<String> citations = hits.isEmpty() ? List.of() : List.of(hits.getFirst().id());
    var review = verifier.review(new LearningDtos.SelfRagReviewRequest(answer, evidence, citations));
    return new LearningDtos.RagAnswerResponse(
        review.grounded() ? review.answer() : review.answer(),
        review.grounded(),
        evidence,
        review.validCitations(),
        review.reasons(),
        "lexical-v1");
  }

  private int score(Set<String> queryTerms, Document document) {
    Set<String> sourceTerms = tokens(document.title() + " " + document.content());
    return (int) queryTerms.stream().filter(sourceTerms::contains).count();
  }

  private Set<String> tokens(String value) {
    return java.util.Arrays.stream(Normalizer.normalize(value, Normalizer.Form.NFD)
            .replaceAll("\\p{M}", "").toLowerCase(Locale.ROOT).split("[^a-z0-9]+"))
        .filter(token -> token.length() > 2)
        .collect(Collectors.toUnmodifiableSet());
  }

  private record Document(String id, String title, String content) { }
  private record ScoredDocument(Document document, int score) { }
}
