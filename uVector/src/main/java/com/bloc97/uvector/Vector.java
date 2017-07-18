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
public interface Vector<T extends Vector> {

    /**
     *
     * @return Copy of the Vector
     */
    T copy();
    
    /**
     *
     * @return Vector of same size with all elements set to 0
     */
    T shell();
    
    int size();
    double[] content();
    double[] get();
    double get(int i);
    
    T zero();
    T fill(double d);
    T negate();
    T normalise();
    T set(double... dArr);
    T set(Vector vector);
    T set(int i, double d);
    
    T add(T vector);
    T add(double d);
    T sub(T vector);
    T sub(double d);
    T mulElem(T vector);
    T mulElem(double d);
    T div(T vector);
    T div(double d);
    
    double norm(); //Frobenius norm
    double norm(int p); //n-norm as vector norm
    double normMax(); //max norm, where p = inf
    double normSqr(); //Squared norm
    
    default boolean equals(Vector vector) {
        return size() == vector.size() && Arrays.equals(content(), vector.content());
    }
    
}
