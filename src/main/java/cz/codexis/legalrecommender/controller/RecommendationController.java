package cz.codexis.legalrecommender.controller;

import cz.codexis.legalrecommender.model.RecommendationResult;
import cz.codexis.legalrecommender.service.RecommendationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/recommend/{docId}")
    public List<RecommendationResult> recommend(@PathVariable("docId") String docId) {
        return recommendationService.recommendSimilar(docId, 5);
    }
}
