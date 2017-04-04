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
public class Vector extends Tensor {
    public Vector(int n) {
        super(new int[] {n});
    }
    public Vector(int n, double fill) {
        super(new int[] {n}, fill);
    }
    public Vector(double[] elementArray) {
        super(elementArray);
    }
    protected Vector(Tensor tensor) {
        super(tensor);
    }
    public void transform(Matrix matrix) {
        set(Matrices.mult(matrix, Vectors.toColumnMatrix(this)));
    }
    public void cross(Vector vector) {
        if (length() != 3 || vector.length() != 3) {
            throw new IllegalArgumentException("Cannot perform cross product on vectors of size other than 3.");
        } else {
            double s0 = get(1)*vector.get(2) - get(2)*vector.get(1);
            double s1 = get(2)*vector.get(0) - get(0)*vector.get(2);
            double s2 = get(0)*vector.get(1) - get(1)*vector.get(0);
            set(new double[] {s0, s1, s2});
        }
    }
    public void proj(Vector vector) {
        double denom = Vectors.mult(vector, vector);
        if (denom != 0) {
            double scalar = Vectors.mult(this, vector)/denom;
            set(Vectors.prod(vector, scalar));
        }
    }
    public void rej(Vector vector) {
        double denom = Vectors.mult(vector, vector);
        if (denom != 0) {
            double scalar = Vectors.mult(this, vector)/denom;
            sub(Vectors.prod(vector, scalar));
        }
    }
    @Override
    public Vector clone() {
        Vector newVector = new Vector(super.clone());
        return newVector;
    }
}
