/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MathExt;

/**
 *
 * @author bowen
 */
public abstract class Statistics {
    public static double norm(double... doubleArray) {
        double sum = 0;
        for (int i=0; i<doubleArray.length; i++) {
            sum += doubleArray[i] * doubleArray[i];
        }
        return Math.sqrt(sum);
    }
    public static double mean(double... doubleArray) {
        int n = doubleArray.length;
        double sum = 0;
        for (int i=0; i<n; i++) {
            sum += doubleArray[i];
        }
        return sum/n;
    }
    public static double meanAbsoluteError(double[] predictionArray, double[] truthArray) {
        if (predictionArray.length != truthArray.length) {
            throw new IllegalArgumentException("Different data sizes.");
        } else {
            int n = predictionArray.length;
            double sum = 0;
            for (int i=0; i<n; i++) {
                sum += Math.abs(predictionArray[i] - truthArray[i]);
            }
            return sum/n;
        }
    }
    public static double meanSquaredError(double[] predictionArray, double[] truthArray) {
        if (predictionArray.length != truthArray.length) {
            throw new IllegalArgumentException("Different data sizes.");
        } else {
            int n = predictionArray.length;
            double sum = 0;
            for (int i=0; i<n; i++) {
                double diff = (predictionArray[i] - truthArray[i]);
                sum += diff * diff;
            }
            return sum/n;
        }
    }
}
