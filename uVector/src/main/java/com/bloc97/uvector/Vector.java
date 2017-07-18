/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
    
    int size();
    double[] content();
    default double[] get() {
        return Arrays.copyOf(content(), size());
    }
    
    default double get(int i) {
        if (i >= 0 && i < size()) {
            return content()[i];
        }
        return Double.NaN;
    }
    
    default T zero() {
        return fill(0);
    }
    default T fill(double d) {
        for (int i=0, n=size(); i<n; i++) {
            set(i, d);
        }
        return (T) this;
    }
    default T negate() {
        for (int i=0, n=size(); i<n; i++) {
            set(i, -get(i));
        }
        return (T) this;
    }
    default T normalise() {
        div(norm());
        return (T) this;
    }
    default T set(double... dArr) {
        for (int i=0, n=size(), m=dArr.length; i<n && i<m; i++) {
            set(i, dArr[i]);
        }
        return (T) this;
    }
    default T set(T vector) {
        return set(vector.content());
    }
    T set(int i, double d);
    
    default T add(double... dArr) {
        for (int i=0, n=size(), m=dArr.length; i<n && i<m; i++) {
            set(i, get(i) + dArr[i]);
        }
        return (T) this;
    }
    default T add(T vector) {
        return add(vector.content());
    }
    default T add(double d) {
        for (int i=0, n=size(); i<n; i++) {
            set(i, get(i) + d);
        }
        return (T) this;
    }
    default T sub(double... dArr) {
        for (int i=0, n=size(), m=dArr.length; i<n && i<m; i++) {
            set(i, get(i) - dArr[i]);
        }
        return (T) this;
    }
    default T sub(T vector) {
        return sub(vector.content());
    }
    default T sub(double d) {
        return add(-d);
    }
    default T mulElem(double... dArr) {
        for (int i=0, n=size(), m=dArr.length; i<n && i<m; i++) {
            set(i, get(i) * dArr[i]);
        }
        return (T) this;
    }
    default T mulElem(T vector) {
        return mulElem(vector.content());
    }
    default T mulElem(double d) {
        for (int i=0, n=size(); i<n; i++) {
            set(i, get(i) * d);
        }
        return (T) this;
    }
    default T div(double... dArr) {
        for (int i=0, n=size(), m=dArr.length; i<n && i<m; i++) {
            set(i, get(i) / dArr[i]);
        }
        return (T) this;
    }
    default T div(T vector) {
        return div(vector.content());
    }
    default T div(double d) {
        if (d == 0) {
            return (T) this;
        }
        return mulElem(1/d);
    }
    
    default double norm() { //Frobenius norm
        return Math.sqrt(normSqr());
    }
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
    default double normSqr() {
        double sqrSum = 0;
        for (int i=0; i<size(); i++) {
            sqrSum += get(i) * get(i);
        }
        return sqrSum;
    }
    
    default boolean equals(Vector vector) {
        return size() == vector.size() && Arrays.equals(content(), vector.content());
    }
    
}
