/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MathExt.Algebra;

/**
 *
 * @author bowen
 */
public abstract class MatrixUtils {
    public Matrix createIdentity(int n) {
        return createDiagonal(n, 1);
    }
    public Matrix createDiagonal(int n, int fill) {
        Matrix newMatrix = new Matrix(n);
        for (int i=0; i<n; i++) {
            newMatrix.set(i, i, fill);
        }
        return newMatrix;
    }
}
