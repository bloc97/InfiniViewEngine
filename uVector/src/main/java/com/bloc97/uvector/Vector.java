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

import java.util.Arrays;

/**
 *
 * @author bowen
 * @param <T>
 */
public interface Vector<T extends Vector<T>> {
    
    
    /**
     *
     * @return Copy of the Vector
     */
    T copy();
    
    /**
     *
     * @return Vector of same size with all elements set to 0
     */
    default T shell() {
        return copy().zero();
    }
    
    /**
     *
     * @return Size of the vector
     */
    int size();
    
    /**
     *
     * @return Content array of the vector
     */
    double[] content();
    
    /**
     *
     * @return Copy of the vector as a double array
     */
    default double[] get() {
        return Arrays.copyOf(content(), size());
    }
    
    /**
     *
     * @param i Element index
     * @return i-th element of the vector
     */
    default double get(int i) {
        if (i >= 0 && i < size()) {
            return content()[i];
        }
        return 0;
    }
    
    /**
     * Sets all the elements of the vector to 0
     * @return The same vector for method chaining
     */
    default T zero() {
        return fill(0);
    }
    
    /**
     * Sets all the elements of the vector to a value
     * @param d New value
     * @return The same vector for method chaining
     */
    default T fill(double d) {
        for (int i=0, n=size(); i<n; i++) {
            set(i, d);
        }
        return (T) this;
    }
    
    /**
     * Negates each element of the vector
     * @return The same vector for method chaining
     */
    default T negate() {
        for (int i=0, n=size(); i<n; i++) {
            set(i, -get(i));
        }
        return (T) this;
    }
    
    /**
     * Normalises the vector such as its norm is equal to 1
     * @return The same vector for method chaining
     */
    default T normalise() {
        div(norm());
        return (T) this;
    }
    
    /**
     * Sets elements of the vector to a value
     * @param dArr New values for the vector
     * @return The same vector for method chaining
     */
    default T set(double... dArr) {
        for (int i=0, n=size(), m=dArr.length; i<n && i<m; i++) {
            set(i, dArr[i]);
        }
        return (T) this;
    }
    
    /**
     * Copies another vector's elements into this vector
     * @param vector
     * @return The same vector for method chaining
     */
    default T set(T vector) {
        return set(vector.content());
    }
    
    /**
     * Sets the i-th element of the vector to a value
     * @param i Element index
     * @param d New value
     * @return The same vector for method chaining
     */
    T set(int i, double d);
    
    /**
     * Performs element-wise addition on this vector
     * @param dArr Array of elements
     * @return The same vector for method chaining
     */
    default T add(double... dArr) {
        for (int i=0, n=size(), m=dArr.length; i<n && i<m; i++) {
            set(i, get(i) + dArr[i]);
        }
        return (T) this;
    }
    
    /**
     * Performs element-wise addition on this vector
     * @param vector
     * @return The same vector for method chaining
     */
    default T add(T vector) {
        return add(vector.content());
    }
    
    /**
     * Performs element-wise addition on this vector
     * @param d Value to add
     * @return The same vector for method chaining
     */
    default T add(double d) {
        for (int i=0, n=size(); i<n; i++) {
            set(i, get(i) + d);
        }
        return (T) this;
    }
    
    /**
     * Performs element-wise subtraction on this vector
     * @param dArr Array of elements
     * @return The same vector for method chaining
     */
    default T sub(double... dArr) {
        for (int i=0, n=size(), m=dArr.length; i<n && i<m; i++) {
            set(i, get(i) - dArr[i]);
        }
        return (T) this;
    }
    /**
     * Performs element-wise subtraction on this vector
     * @param vector
     * @return The same vector for method chaining
     */
    default T sub(T vector) {
        return sub(vector.content());
    }
    
    /**
     * Performs element-wise subtraction on this vector
     * @param d Value to subtract
     * @return The same vector for method chaining
     */
    default T sub(double d) {
        return add(-d);
    }
    
    /**
     * Performs element-wise multiplication on this vector
     * @param dArr Array of elements
     * @return The same vector for method chaining
     */
    default T mulElem(double... dArr) {
        for (int i=0, n=size(), m=dArr.length; i<n && i<m; i++) {
            set(i, get(i) * dArr[i]);
        }
        return (T) this;
    }
    
    /**
     * Performs element-wise multiplication on this vector
     * @param vector
     * @return The same vector for method chaining
     */
    default T mulElem(T vector) {
        return mulElem(vector.content());
    }
    
    /**
     * Performs element-wise multiplication on this vector
     * @param d Value to multiply
     * @return The same vector for method chaining
     */
    default T mulElem(double d) {
        for (int i=0, n=size(); i<n; i++) {
            set(i, get(i) * d);
        }
        return (T) this;
    }
    
    /**
     * Performs element-wise division on this vector
     * @param dArr Array of elements
     * @return The same vector for method chaining
     */
    default T div(double... dArr) {
        for (int i=0, n=size(), m=dArr.length; i<n && i<m; i++) {
            set(i, get(i) / dArr[i]);
        }
        return (T) this;
    }
    
    /**
     * Performs element-wise division on this vector
     * @param vector
     * @return The same vector for method chaining
     */
    default T div(T vector) {
        return div(vector.content());
    }
    
    /**
     * Performs element-wise division on this vector
     * @param d Value to divide
     * @return The same vector for method chaining
     */
    default T div(double d) {
        if (d == 0) {
            return (T) this;
        }
        return mulElem(1/d);
    }
    
    /**
     * @return Norm of this vector
     */
    default double norm() { //Frobenius norm
        return Math.sqrt(normSqr());
    }
    
    /**
     * @return N-norm of this vector
     */
    default double norm(double p) { //n-norm as vector norm
        if (p == 0) {
            return Double.NaN;
        }
        double sum = 0;
        for (int i=0; i<size(); i++) {
            sum += Math.pow(get(i), p);
        }
        return Math.pow(sum, 1/p);
    }
    
    /**
     * @return Infinite-norm of this vector, or the maximum value of the elements
     */
    default double normMax() { //max norm, where p = inf
        if (size() < 1) {
            return Double.NaN;
        }
        double max = get(0);
        for (int i=1; i<size(); i++) {
            if (get(i) > max) {
                max = get(i);
            }
        }
        return max;
    }
    
    /**
     * @return Square norm of this vector
     */
    default double normSqr() {
        double sqrSum = 0;
        for (int i=0; i<size(); i++) {
            sqrSum += get(i) * get(i);
        }
        return sqrSum;
    }
    
    /**
     * @param vector Vector to compare
     * @return True if both vectors' elements are equal
     */
    default boolean equals(Vector vector) {
        return size() == vector.size() && Arrays.equals(content(), vector.content());
    }
    
}
