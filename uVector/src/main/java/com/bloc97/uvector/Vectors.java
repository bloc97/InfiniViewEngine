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
    
    public static Vector3 cross(Vector3 vector, Vector3 vector2) {
        double s0 = vector.get(1)*vector2.get(2) - vector.get(2)*vector2.get(1);
        double s1 = vector.get(2)*vector2.get(0) - vector.get(0)*vector2.get(2);
        double s2 = vector.get(0)*vector2.get(1) - vector.get(1)*vector2.get(0);
        return new Vector3(s0, s1, s2);
    }
    
    public static Vector cross_AxBxB(Vector a, Vector b) {
        if (a instanceof Vector2 && b instanceof Vector2) {
            return cross_AxBxB((Vector2) a, (Vector2) b);
        } else if (a instanceof Vector3 && b instanceof Vector3) {
            return cross_AxBxB((Vector3) a, (Vector3) b);
        }
        throw new IllegalArgumentException("Cannot perform the cross product on a vector of this size");
    }
    
    public static Vector2 cross_AxBxB(Vector2 a, Vector2 b) {
        double AxB_k = (a.get(0) * b.get(1) - a.get(1) * b.get(0));
        double AxBxB_i = -b.get(1) * AxB_k;
        double AxBxB_j = b.get(0) * AxB_k;
        return new Vector2(AxBxB_i, AxBxB_j);
    }
    
    public static Vector3 cross_AxBxB(Vector3 a, Vector3 b) {
        return cross(cross(a, b), b);
    }
    
    public static Vector cross_AxBxA(Vector a, Vector b) {
        if (a instanceof Vector2 && b instanceof Vector2) {
            return cross_AxBxA((Vector2) a, (Vector2) b);
        } else if (a instanceof Vector3 && b instanceof Vector3) {
            return cross_AxBxA((Vector3) a, (Vector3) b);
        }
        throw new IllegalArgumentException("Cannot perform the cross product on a vector of this size");
    }
    
    public static Vector cross_AxBxA(Vector2 a, Vector2 b) {
        double AxB_k = (a.get(0) * b.get(1) - a.get(1) * b.get(0));
        double AxBxA_i = -a.get(1) * AxB_k;
        double AxBxA_j = a.get(0) * AxB_k;
        return new Vector2(AxBxA_i, AxBxA_j);
    }
    
    public static Vector cross_AxBxA(Vector3 a, Vector3 b) {
        return cross(cross(a, b), a);
    }
    
}
