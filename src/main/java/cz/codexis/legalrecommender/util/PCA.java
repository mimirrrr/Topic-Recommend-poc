package cz.codexis.legalrecommender.util;

import org.ejml.simple.SimpleMatrix;


public class PCA {

    public static double[][] reduce(double[][] data, int targetDims) {
        if (data.length == 0) return new double[0][targetDims];

        SimpleMatrix matrix = new SimpleMatrix(data);

        SimpleMatrix cov = matrix.transpose().mult(matrix);

        var svd = cov.svd();
        SimpleMatrix V = svd.getV();

        SimpleMatrix projection = matrix.mult(V.extractMatrix(0, V.numRows(), 0, targetDims));

        double[][] result = new double[projection.numRows()][projection.numCols()];
        for (int i = 0; i < projection.numRows(); i++) {
            for (int j = 0; j < projection.numCols(); j++) {
                result[i][j] = projection.get(i, j);
            }
        }
        return result;
    }
}
