package cz.codexis.legalrecommender.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class EmbeddingUtil {

    private static final String API_KEY = "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";
    private static final String MODEL = "text-embedding-3-small";

    public static List<Double> generateEmbedding(String text) {
        try {
            String safeText = text.replace("\"", "\\\"");

            String requestBody = """
                {
                  "input": "%s",
                  "model": "%s"
                }
            """.formatted(safeText, MODEL);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.openai.com/v1/embeddings"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + API_KEY)
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("HTTP status code: " + response.statusCode());
            System.out.println("Raw API response: " + response.body());

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.body());

            List<Double> embedding = new ArrayList<>();
            JsonNode embeddingNode = root.path("data").get(0).path("embedding");

            if (embeddingNode.isArray()) {
                for (JsonNode value : embeddingNode) {
                    embedding.add(value.asDouble());
                }
            } else {
                System.err.println("API nevrátil žádný embedding. Response: " + response.body());
            }

            return embedding;

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
