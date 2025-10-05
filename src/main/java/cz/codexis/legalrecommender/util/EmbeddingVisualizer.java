package cz.codexis.legalrecommender.util;

import cz.codexis.legalrecommender.model.LegalDocument;
import org.knowm.xchart.*;
import org.knowm.xchart.style.markers.SeriesMarkers;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EmbeddingVisualizer {

    public static void visualizeDocuments(Map<String, LegalDocument> documents) {
        if (documents.isEmpty()) {
            System.out.println("No documents to visualize.");
            return;
        }

        // Prepare matrix of embeddings
        double[][] matrix = documents.values().stream()
                .map(doc -> doc.getEmbedding().stream().mapToDouble(Double::doubleValue).toArray())
                .toArray(double[][]::new);

        // Reduce to 2D
        double[][] reduced = PCA.reduce(matrix, 2);

        List<String> titles = documents.values().stream()
                .map(LegalDocument::getTitle)
                .collect(Collectors.toList());

        double[] xData = new double[reduced.length];
        double[] yData = new double[reduced.length];
        for (int i = 0; i < reduced.length; i++) {
            xData[i] = reduced[i][0];
            yData[i] = reduced[i][1];
        }

        XYChart chart = new XYChartBuilder()
                .width(1000)
                .height(800)
                .title("Document Embeddings (PCA Projection)")
                .xAxisTitle("PC 1")
                .yAxisTitle("PC 2")
                .build();

        XYSeries series = chart.addSeries("Documents", xData, yData);
        series.setXYSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);
        series.setMarker(SeriesMarkers.CIRCLE);

        // Add titles as annotations near each point
        for (int i = 0; i < titles.size(); i++) {
            chart.addSeries(titles.get(i),
                    new double[]{xData[i] + 0.01},
                    new double[]{yData[i] + 0.01});
        }

        try {
            String outputPath = Paths.get("embeddings_chart.png").toAbsolutePath().toString();
            BitmapEncoder.saveBitmap(chart, outputPath, BitmapEncoder.BitmapFormat.PNG);
            System.out.println("✅ Embeddings chart saved at: " + outputPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
