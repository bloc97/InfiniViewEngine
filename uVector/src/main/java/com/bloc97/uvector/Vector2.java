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
 */
public class Vector2 implements Vector<Vector2> {
    
    private final double[] content;
    
    /**
     * Creates a new 2-vector with all its elements set to 0
     */
    public Vector2() {
        content = new double[2];
    }

    /**
     * Creates a new 2-vector with all its elements set to a value
     * @param fill Value to fill the vector
     */
    public Vector2(double fill) {
        content = new double[] {fill, fill};
    }
    
    /**
     * Creates a new 2-vector with specific values
     * @param a Value of i
     * @param b Value of j
     */
    public Vector2(double a, double b) {
        content = new double[] {a, b};
    }
    
    @Override
    public Vector2 copy() {
        return new Vector2(content[0], content[1]);
    }
    @Override
    public Vector2 shell() {
        return new Vector2();
    }
    
    @Override
    public int size() {
        return 2;
    }

    @Override
    public double[] content() {
        return content;
    }

    @Override
    public double[] get() {
        return Arrays.copyOf(content, 2);
    }
    
    @Override
    public double get(int i) {
        if (i >= 0 && i < 2) {
            return content[i];
        }
        return 0;
    }
    
    /**
     * @return Element at index 0 of the vector, same as {@link #get(0)}
     */
    public double getX() {
        return get(0);
    }
    /**
     * @return Element at index 1 of the vector, same as {@link #get(1)}
     */
    public double getY() {
        return get(1);
    }
    /**
     * Sets the element at index 0 to a value, sale as {@link #set(0, d)}
     * @param d
     * @return The same vector for method chaining
     */
    public Vector2 setX(double d) {
        return set(0, d);
    }
    /**
     * Sets the element at index 1 to a value, sale as {@link #set(1, d)}
     * @param d
     * @return The same vector for method chaining
     */
    public Vector2 setY(double d) {
        return set(1, d);
    }
    
    public Vector2 rotate(double angle) {
        double s = Math.sin(angle);
        double c = Math.cos(angle);
        return set(getX() * c + getY() * s, getX() * s + getY() * c);
    }

    @Override
    public Vector2 zero() {
        content[0] = 0;
        content[1] = 0;
        return this;
    }
    
    @Override
    public Vector2 fill(double d) {
        content[0] = d;
        content[1] = d;
        return this;
    }
    
    @Override
    public Vector2 negate() {
        content[0] = -content[0];
        content[1] = -content[1];
        return this;
    }
    
    @Override
    public Vector2 normalise() {
        double norm = norm();
        if (norm == 0) {
            content[0] = 1;
            content[1] = 1;
            norm = norm();
        }
        div(norm);
        return this;
    }

    @Override
    public Vector2 set(double... dArr) {
        if (dArr.length >= 2) {
            content[0] = dArr[0];
            content[1] = dArr[1];
        } else if (dArr.length == 1) {
            content[0] = dArr[0];
        }
        return this;
    }
    
    @Override
    public Vector2 set(Vector2 vector) {
        content[0] = vector.get(0);
        content[1] = vector.get(1);
        return this;
    }

    @Override
    public Vector2 set(int i, double d) {
        if (i >= 0 && i < 2) {
            content[i] = d;
        }
        return this;
    }
    
    @Override
    public Vector2 add(Vector2 vector) {
        content[0] += vector.get(0);
        content[1] += vector.get(1);
        return this;
    }
    @Override
    public Vector2 add(double d) {
        content[0] += d;
        content[1] += d;
        return this;
    }
    
    @Override
    public Vector2 sub(Vector2 vector) {
        content[0] -= vector.get(0);
        content[1] -= vector.get(1);
        return this;
    }
    @Override
    public Vector2 sub(double d) {
        content[0] -= d;
        content[1] -= d;
        return this;
    }
    
    @Override
    public Vector2 mulElem(Vector2 vector) {
        content[0] *= vector.get(0);
        content[1] *= vector.get(1);
        return this;
    }
    @Override
    public Vector2 mulElem(double d) {
        content[0] *= d;
        content[1] *= d;
        return this;
    }
    
    @Override
    public Vector2 div(Vector2 vector) {
        content[0] /= (vector.get(0) == 0) ? 1 : vector.get(0);
        content[1] /= (vector.get(1) == 0) ? 1 : vector.get(1);
        return this;
    }
    @Override
    public Vector2 div(double d) {
        content[0] /= (d == 0) ? 1 : d;
        content[1] /= (d == 0) ? 1 : d;
        return this;
    }

    @Override
    public double norm() { //Frobenius norm
        return Math.sqrt((content[0] * content[0]) + (content[1] * content[1]));
    }
    @Override
    public double norm(double p) { //n-norm as vector norm
        if (p == 0) {
            return Double.NaN;
        } else {
            return Math.pow(Math.pow(content[0], p) + Math.pow(content[1], p), 1/p);
        }
        
    }
    @Override
    public double normMax() { //returns max norm, where p = inf
        return Math.max(content[0], content[1]);
    }
    @Override
    public double normSqr() { //Squared norm
        return (content[0] * content[0]) + (content[1] * content[1]);
    }
    
    @Override
    public String toString() {
        return "[" + content[0] + ", " + content[1] + "]";
    }
}
