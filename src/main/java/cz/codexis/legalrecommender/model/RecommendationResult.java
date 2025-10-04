package cz.codexis.legalrecommender.model;

public class RecommendationResult {
    private String id;
    private String title;
    private double score;

    public RecommendationResult() {}

    public RecommendationResult(String id, String title, double score) {
        this.id = id;
        this.title = title;
        this.score = score;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }
}
