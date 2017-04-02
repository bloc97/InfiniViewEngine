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
public class Matrix extends Tensor {
    public Matrix(int s) {
        super(new int[] {s,s});
    }
    public Matrix(int s, double fill) {
        super(new int[] {s,s}, fill);
    }
    public Matrix(int m, int n) {
        super(new int[] {m,n});
    }
    public Matrix(int m, int n, double fill) {
        super(new int[] {m,n}, fill);
    }
    public Matrix(double[][] elementArray2D) {
        super(elementArray2D);
    }
    protected Matrix(Tensor tensor) {
        super(tensor);
    }
    
    public double norm(int p, int q) { //Implement PQ norm
        return 0;
    }
    public double get(int m, int n) {
        if (isWithinBounds(m, n)) {
            int index = m * getDimensionSize(1) + n;
            return get(index);
        } else {
            throw new IndexOutOfBoundsException("Invalid matrix index.");
        }
    }
    public void set(int m, int n, double c) {
        if (isWithinBounds(m, n)) {
            int index = m * getDimensionSize(1) + n;
            set(index, c);
        } else {
            throw new IndexOutOfBoundsException("Invalid matrix index.");
        }
    }
    private boolean isWithinBounds(int m, int n) {
        if (m >= 0 && m < getDimensionSize(2) && n >= 0 && n < getDimensionSize(1)) {
            return true;
        } else {
            return false;
        }
    }
    @Override
    final public Matrix clone() {
        Matrix newMatrix = new Matrix(super.clone());
        return newMatrix;
    }
}
