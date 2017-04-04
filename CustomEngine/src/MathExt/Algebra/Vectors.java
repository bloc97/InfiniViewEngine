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
public abstract class Vectors {
    public static Vector shell(Vector vector) {
        return new Vector(vector.getDimensionSize(1));
    }
    public static Vector neg(Vector vector) {
        return new Vector(Tensors.neg(vector));
    }
    public static Vector add(Vector... vectorArray) {
        return new Vector(Tensors.add(vectorArray));
    }
    public static Vector add(Vector vector, double c) {
        return new Vector(Tensors.add(vector, c));
    }
    public static Vector sub(Vector... vectorArray) {
        return new Vector(Tensors.sub(vectorArray));
    }
    public static Vector sub(Vector vector, double c) {
        return new Vector(Tensors.sub(vector, c));
    }
    public static Vector prod(Vector... vectorArray) {
        return new Vector(Tensors.prod(vectorArray));
    }
    public static Vector prod(Vector vector, double c) {
        return new Vector(Tensors.prod(vector, c));
    }
    public static Vector div(Vector... vectorArray) {
        return new Vector(Tensors.div(vectorArray));
    }
    public static Vector div(Vector vector, double c) {
        return new Vector(Tensors.div(vector, c));
    }
    public static Vector mult(Matrix matrix, Vector vector) {
        return Tensors.toVector(Matrices.mult(matrix, Vectors.toColumnMatrix(vector)));
    }
    public static Vector transform(Vector vector, Matrix... matrixArray) {
        Vector newVector = vector.clone();
        for (int n=0; n<matrixArray.length; n++) {
            newVector.transform(matrixArray[n]);
        }
        return newVector;
    }
    public static double mult(Vector vector, Vector vector2) {
        if (vector.length() != vector2.length()) {
            throw new IllegalArgumentException("Cannot multiply vectors of different sizes.");
        }
        double sum = 0;
        for (int i=0, length=vector.length(); i<length; i++) {
            sum += vector.get(i) * vector2.get(i);
        }
        return sum;
        //return Matrices.mult(Vectors.toRowMatrix(vector), Vectors.toColumnMatrix(vector2)).get(0, 0); //TODO Implement faster algorithm
    }
    public static Vector cross(Vector... vectorArray) {
        Vector newVector = vectorArray[0].clone();
        for (int n=1; n<vectorArray.length; n++) {
            newVector.cross(vectorArray[n]);
        }
        return newVector;
    }
    public static double scalar(Vector vector, Vector vector2) {
        return mult(vector, vector2)/vector2.norm();
    }
    public static Vector proj(Vector... vectorArray) {
        Vector newVector = vectorArray[0].clone();
        for (int n=1; n<vectorArray.length; n++) {
            newVector.proj(vectorArray[n]);
        }
        return newVector;
    }
    public static Vector rej(Vector... vectorArray) {
        Vector newVector = vectorArray[0].clone();
        for (int n=1; n<vectorArray.length; n++) {
            newVector.rej(vectorArray[n]);
        }
        return newVector;
    }
    public static Matrix toRowMatrix(Vector vector) {
        return new Matrix(Tensors.join(vector));
    }
    public static Matrix toColumnMatrix(Vector vector) {
        return Matrices.transpose(Tensors.toMatrix(Tensors.join(vector)));
    }
}
