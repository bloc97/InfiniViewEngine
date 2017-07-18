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
    
    public Vector2() {
        content = new double[2];
    }
    public Vector2(double fill) {
        content = new double[] {fill, fill};
    }
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
    public boolean equals(Object object) {
        if (object instanceof Vector) {
            Vector vector = (Vector) object;
            return vector.size() == size() && Arrays.equals(vector.content(), content);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 13 * hash + Arrays.hashCode(this.content);
        return hash;
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
        return Double.NaN;
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
    public Vector2 set(Vector vector) {
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
    public double norm() { //Frobenius norm
        return Math.sqrt((content[0] * content[0]) + (content[1] * content[1]));
    }
    @Override
    public double norm(int p) { //n-norm as vector norm
        if (p < 1) {
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
    public String toString() {
        return "[" + content[0] + ", " + content[1] + "]";
    }
}
