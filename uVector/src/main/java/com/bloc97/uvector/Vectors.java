/*
 * The MIT License
 *
 * Copyright 2017 Bowen Peng.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.bloc97.uvector;

/**
 *
 * @author bowen
 */
public abstract class Vectors {
    
    /**
     * Performs element-wise addition of two vectors of same size
     * @param <T> Type of vector
     * @param vector First vector
     * @param vector2 Second vector
     * @return A new vector with its elements equal to vector + vector2
     */
    public static <T extends Vector<T>> T add(T vector, T vector2) {
        if (vector.size() == vector2.size()) {
            return vector.copy().add(vector2);
        }
        throw new IllegalArgumentException("Different vector sizes.");
    }
    /**
     * Performs element-wise addition of a vector with a value
     * @param <T> Type of vector
     * @param vector Vector
     * @param d Value
     * @return A new vector with its elements equal to vector + d
     */
    public static <T extends Vector<T>> T add(T vector, double d) {
        return vector.copy().add(d);
    }
    
    /**
     * Performs element-wise subtraction of two vectors of same size
     * @param <T> Type of vector
     * @param vector First vector
     * @param vector2 Second vector
     * @return A new vector with its elements equal to vector - vector2
     */
    public static <T extends Vector<T>> T sub(T vector, T vector2) {
        if (vector.size() == vector2.size()) {
            return vector.copy().sub(vector2);
        }
        throw new IllegalArgumentException("Different vector sizes.");
    }
    public static <T extends Vector<T>> T sub(T vector, double d) {
        return vector.copy().sub(d);
    }
    
    /**
     * Performs element-wise multiplication of two vectors of same size
     * @param <T> Type of vector
     * @param vector First vector
     * @param vector2 Second vector
     * @return A new vector with its elements equal to vector * vector2
     */
    public static <T extends Vector<T>> T mulElem(T vector, T vector2) {
        if (vector.size() == vector2.size()) {
            return vector.copy().mulElem(vector2);
        }
        throw new IllegalArgumentException("Different vector sizes.");
    }
    public static <T extends Vector<T>> T mulElem(T vector, double d) {
        return vector.copy().mulElem(d);
    }
    
    /**
     * Performs element-wise division of two vectors of same size
     * @param <T> Type of vector
     * @param vector First vector
     * @param vector2 Second vector
     * @return A new vector with its elements equal to vector / vector2
     */
    public static <T extends Vector<T>> T div(T vector, T vector2) {
        if (vector.size() == vector2.size()) {
            return vector.copy().div(vector2);
        }
        throw new IllegalArgumentException("Different vector sizes.");
    }
    public static <T extends Vector<T>> T div(T vector, double d) {
        return vector.copy().div(d);
    }
    
    /**
     * Performs the dot-product of two vectors of same size
     * @param <T> Type of vector
     * @param vector First vector
     * @param vector2 Second vector
     * @return A new vector with its elements equal to vector (dot) vector2
     */
    public static <T extends Vector<T>> double dot(T vector, T vector2) {
        if (vector.size() == vector2.size()) {
            double ans = 0;
            for (int i=0; i<vector.size(); i++) {
                ans += vector.get(i) * vector2.get(i);
            }
            return ans;
        }
        throw new IllegalArgumentException("Different vector sizes.");
    }
    
    /**
     * Performs the scalar-product of two vectors of same size
     * @param <T> Type of vector
     * @param vector First vector
     * @param vector2 Second vector
     * @return A new vector with its elements equal to ( vector (dot) vector2 ) / norm of vector2
     */
    public static <T extends Vector<T>> double scalar(T vector, T vector2) {
        return dot(vector, vector2)/vector2.norm();
    }
    
    /**
     * Performs the projection of a vector into another vector of same size
     * @param <T> Type of vector
     * @param vector First vector
     * @param vector2 Second vector
     * @return A new vector with its elements equal to vector (proj) vector2
     */
    public static <T extends Vector<T>> T proj(T vector, T vector2) {
        double denom = Vectors.dot(vector2, vector2);
        if (denom != 0) {
            double scalar = Vectors.dot(vector, vector2)/denom;
            return Vectors.mulElem(vector, scalar);
        }
        return vector;
    }
    
    /**
     * Performs the rejection of a vector into another vector of same size
     * @param <T> Type of vector
     * @param vector First vector
     * @param vector2 Second vector
     * @return A new vector with its elements equal to vector (rej) vector2
     */
    public static <T extends Vector<T>> T rej(T vector, T vector2) {
        double denom = Vectors.dot(vector2, vector2);
        if (denom != 0) {
            double scalar = Vectors.dot(vector, vector2)/denom;
            return Vectors.sub(vector, Vectors.mulElem(vector, scalar));
        }
        return vector;
    }
    
    /**
     * Performs the cross product of two 3-vectors
     * @param vector First vector
     * @param vector2 Second vector
     * @return A new 3-vector with its elements equal to vector (cross) vector2
     */
    public static Vector3 cross(Vector3 vector, Vector3 vector2) {
        double s0 = vector.get(1)*vector2.get(2) - vector.get(2)*vector2.get(1);
        double s1 = vector.get(2)*vector2.get(0) - vector.get(0)*vector2.get(2);
        double s2 = vector.get(0)*vector2.get(1) - vector.get(1)*vector2.get(0);
        return new Vector3(s0, s1, s2);
    }
    
    /**
     * Performs a double cross product of two vectors
     * The vectors must be of size 2 or 3
     * @param a First vector
     * @param b Second vector
     * @return A new vector with its elements equal to a (cross) b (cross) b
     */
    public static Vector cross_AxBxB(Vector a, Vector b) {
        if (a instanceof Vector2 && b instanceof Vector2) {
            return cross_AxBxB((Vector2) a, (Vector2) b);
        } else if (a instanceof Vector3 && b instanceof Vector3) {
            return cross_AxBxB((Vector3) a, (Vector3) b);
        }
        throw new IllegalArgumentException("Cannot perform the cross product on a vector of this size");
    }
    
    /**
     * Performs a double cross product of two 2-vectors
     * @param a First vector
     * @param b Second vector
     * @return A new 2-vector with its elements equal to a (cross) b (cross) b
     */
    public static Vector2 cross_AxBxB(Vector2 a, Vector2 b) {
        double AxB_k = (a.get(0) * b.get(1) - a.get(1) * b.get(0));
        double AxBxB_i = -b.get(1) * AxB_k;
        double AxBxB_j = b.get(0) * AxB_k;
        return new Vector2(AxBxB_i, AxBxB_j);
    }
    
    /**
     * Performs a double cross product of two 3-vectors
     * @param a First vector
     * @param b Second vector
     * @return A new 3-vector with its elements equal to a (cross) b (cross) b
     */
    public static Vector3 cross_AxBxB(Vector3 a, Vector3 b) {
        return cross(cross(a, b), b);
    }
    
    /**
     * Performs a double cross product of two vectors
     * The vectors must be of size 2 or 3
     * @param a First vector
     * @param b Second vector
     * @return A new vector with its elements equal to a (cross) b (cross) a
     */
    public static Vector cross_AxBxA(Vector a, Vector b) {
        if (a instanceof Vector2 && b instanceof Vector2) {
            return cross_AxBxA((Vector2) a, (Vector2) b);
        } else if (a instanceof Vector3 && b instanceof Vector3) {
            return cross_AxBxA((Vector3) a, (Vector3) b);
        }
        throw new IllegalArgumentException("Cannot perform the cross product on a vector of this size");
    }
    
    /**
     * Performs a double cross product of two 2-vectors
     * @param a First vector
     * @param b Second vector
     * @return A new 2-vector with its elements equal to a (cross) b (cross) a
     */
    public static Vector2 cross_AxBxA(Vector2 a, Vector2 b) {
        double AxB_k = (a.get(0) * b.get(1) - a.get(1) * b.get(0));
        double AxBxA_i = -a.get(1) * AxB_k;
        double AxBxA_j = a.get(0) * AxB_k;
        return new Vector2(AxBxA_i, AxBxA_j);
    }
    
    /**
     * Performs a double cross product of two 3-vectors
     * @param a First vector
     * @param b Second vector
     * @return A new 3-vector with its elements equal to a (cross) b (cross) a
     */
    public static Vector3 cross_AxBxA(Vector3 a, Vector3 b) {
        return cross(cross(a, b), a);
    }
    
}
