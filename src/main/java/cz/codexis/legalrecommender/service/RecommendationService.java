package cz.codexis.legalrecommender.service;

import cz.codexis.legalrecommender.model.LegalDocument;
import cz.codexis.legalrecommender.model.RecommendationResult;
import cz.codexis.legalrecommender.util.EmbeddingUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    private final Map<String, LegalDocument> documents = new HashMap<>();

    @PostConstruct
    public void init() {
        loadDocuments();
    }

    public List<RecommendationResult> recommendSimilar(String docId, int limit) {
        LegalDocument target = documents.get(docId);
        if (target == null) return new ArrayList<>();

        return documents.values().stream()
                .filter(doc -> !doc.getId().equals(docId))
                .map(doc -> new RecommendationResult(doc.getId(), doc.getTitle(), cosineSim(target.getEmbedding(), doc.getEmbedding())))
                .sorted(Comparator.comparingDouble(RecommendationResult::getScore).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    private double cosineSim(List<Double> a, List<Double> b) {
        if (a == null || b == null || a.size() != b.size()) return 0.0;
        double dot = 0, normA = 0, normB = 0;
        for (int i = 0; i < a.size(); i++) {
            dot += a.get(i) * b.get(i);
            normA += Math.pow(a.get(i), 2);
            normB += Math.pow(b.get(i), 2);
        }
        if (normA == 0 || normB == 0) return 0.0;
        return dot / (Math.sqrt(normA) * Math.sqrt(normB));
    }

    private void loadDocuments() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream is = getClass().getClassLoader().getResourceAsStream("documents.json");
            LegalDocument[] docs = mapper.readValue(is, LegalDocument[].class);
            for (LegalDocument d : docs) {
                d.setEmbedding(EmbeddingUtil.generateEmbedding(d.getText()));
                documents.put(d.getId(), d);
            }

            cz.codexis.legalrecommender.util.EmbeddingVisualizer.visualizeDocuments(documents);

        } catch (Exception e) {
            throw new RuntimeException("Failed to load documents", e);
        }
    }

}
