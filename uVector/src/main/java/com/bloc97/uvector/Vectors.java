/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bloc97.uvector;

/**
 *
 * @author bowen
 */
public abstract class Vectors {
    
    public static <T extends Vector<T>> T add(T vector, T vector2) {
        if (vector.size() == vector2.size()) {
            return vector.copy().add(vector2);
        }
        throw new IllegalArgumentException("Different vector sizes.");
    }
    public static <T extends Vector<T>> T add(T vector, double d) {
        return vector.copy().add(d);
    }
    
    public static <T extends Vector<T>> T sub(T vector, T vector2) {
        if (vector.size() == vector2.size()) {
            return vector.copy().sub(vector2);
        }
        throw new IllegalArgumentException("Different vector sizes.");
    }
    public static <T extends Vector<T>> T sub(T vector, double d) {
        return vector.copy().sub(d);
    }
    
    public static <T extends Vector<T>> T mulElem(T vector, T vector2) {
        if (vector.size() == vector2.size()) {
            return vector.copy().mulElem(vector2);
        }
        throw new IllegalArgumentException("Different vector sizes.");
    }
    public static <T extends Vector<T>> T mulElem(T vector, double d) {
        return vector.copy().mulElem(d);
    }
    
    public static <T extends Vector<T>> T div(T vector, T vector2) {
        if (vector.size() == vector2.size()) {
            return vector.copy().div(vector2);
        }
        throw new IllegalArgumentException("Different vector sizes.");
    }
    public static <T extends Vector<T>> T div(T vector, double d) {
        return vector.copy().div(d);
    }
    
    public static double dot(Vector vector, Vector vector2) {
        if (vector.size() == vector2.size()) {
            double ans = 0;
            for (int i=0; i<vector.size(); i++) {
                ans += vector.get(i) * vector2.get(i);
            }
            return ans;
        }
        throw new IllegalArgumentException("Different vector sizes.");
    }
    public static double scalar(Vector vector, Vector vector2) {
        return dot(vector, vector2)/vector2.norm();
    }
    
    public static <T extends Vector<T>> T proj(T vector, T vector2) {
        double denom = Vectors.dot(vector2, vector2);
        if (denom != 0) {
            double scalar = Vectors.dot(vector, vector2)/denom;
            return Vectors.mulElem(vector, scalar);
        }
        return vector;
    }
    public static <T extends Vector<T>> T rej(T vector, T vector2) {
        double denom = Vectors.dot(vector2, vector2);
        if (denom != 0) {
            double scalar = Vectors.dot(vector, vector2)/denom;
            return Vectors.sub(vector, Vectors.mulElem(vector, scalar));
        }
        return vector;
    }
    
    public static Vector2 cross2D_AxBxB(Vector a, Vector b) {
        if (a instanceof Vector2) {
            double AxB_k = (a.get(0) * b.get(1) - a.get(1) * b.get(0));
            double AxBxB_i = -b.get(1) * AxB_k;
            double AxBxB_j = b.get(0) * AxB_k;
            return new Vector2(AxBxB_i, AxBxB_j);
        } else {
            throw new IllegalArgumentException("Vectors are not of size 2");
        }
    }
    
}
