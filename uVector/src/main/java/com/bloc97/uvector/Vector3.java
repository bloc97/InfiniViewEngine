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
public class Vector3 implements Vector<Vector3> {

    private final double[] content;
    
    public Vector3() {
        content = new double[3];
    }
    public Vector3(double fill) {
        content = new double[] {fill, fill, fill};
    }
    public Vector3(double a, double b, double c) {
        content = new double[] {a, b, c};
    }
    
    @Override
    public Vector3 copy() {
        return new Vector3(content[0], content[1], content[2]);
    }

    @Override
    public Vector3 shell() {
        return new Vector3();
    }

    @Override
    public int size() {
        return 3;
    }

    @Override
    public double[] content() {
        return content;
    }

    @Override
    public double[] get() {
        return Arrays.copyOf(content, 3);
    }

    @Override
    public double get(int i) {
        if (i >= 0 && i < 3) {
            return content[i];
        }
        return Double.NaN;
    }

    @Override
    public Vector3 zero() {
        content[0] = 0;
        content[1] = 0;
        content[2] = 0;
        return this;
    }

    @Override
    public Vector3 fill(double d) {
        content[0] = d;
        content[1] = d;
        content[2] = d;
        return this;
    }

    @Override
    public Vector3 negate() {
        content[0] = -content[0];
        content[1] = -content[1];
        content[2] = -content[2];
        return this;
    }

    @Override
    public Vector3 normalise() {
        double norm = norm();
        if (norm == 0) {
            content[0] = 1;
            content[1] = 1;
            content[2] = 1;
            norm = norm();
        }
        div(norm);
        return this;
    }

    @Override
    public Vector3 set(double... dArr) {
        if (dArr.length >= 3) {
            content[0] = dArr[0];
            content[1] = dArr[1];
            content[2] = dArr[2];
        } else if (dArr.length >= 2) {
            content[0] = dArr[0];
            content[1] = dArr[1];
        } else if (dArr.length == 1) {
            content[0] = dArr[0];
        }
        return this;
    }

    @Override
    public Vector3 set(Vector3 vector) {
        content[0] = vector.get(0);
        content[1] = vector.get(1);
        content[2] = vector.get(2);
        return this;
    }

    @Override
    public Vector3 set(int i, double d) {
        if (i >= 0 && i < 3) {
            content[i] = d;
        }
        return this;
    }
    
    
    public double getX() {
        return get(0);
    }
    public double getY() {
        return get(1);
    }
    public double getZ() {
        return get(2);
    }
    public Vector3 setX(double d) {
        return set(0, d);
    }
    public Vector3 setY(double d) {
        return set(1, d);
    }
    public Vector3 setZ(double d) {
        return set(2, d);
    }

    @Override
    public Vector3 add(Vector3 vector) {
        content[0] += vector.get(0);
        content[1] += vector.get(1);
        content[2] += vector.get(2);
        return this;
    }

    @Override
    public Vector3 add(double d) {
        content[0] += d;
        content[1] += d;
        content[2] += d;
        return this;
    }

    @Override
    public Vector3 sub(Vector3 vector) {
        content[0] -= vector.get(0);
        content[1] -= vector.get(1);
        content[2] -= vector.get(2);
        return this;
    }

    @Override
    public Vector3 sub(double d) {
        content[0] -= d;
        content[1] -= d;
        content[2] -= d;
        return this;
    }

    @Override
    public Vector3 mulElem(Vector3 vector) {
        content[0] *= vector.get(0);
        content[1] *= vector.get(1);
        content[2] *= vector.get(2);
        return this;
    }

    @Override
    public Vector3 mulElem(double d) {
        content[0] *= d;
        content[1] *= d;
        content[2] *= d;
        return this;
    }

    @Override
    public Vector3 div(Vector3 vector) {
        content[0] /= (vector.get(0) == 0) ? 1 : vector.get(0);
        content[1] /= (vector.get(1) == 0) ? 1 : vector.get(1);
        content[2] /= (vector.get(2) == 0) ? 1 : vector.get(2);
        return this;
    }

    @Override
    public Vector3 div(double d) {
        content[0] /= (d == 0) ? 1 : d;
        content[1] /= (d == 0) ? 1 : d;
        content[2] /= (d == 0) ? 1 : d;
        return this;
    }

    @Override
    public double norm() { //Frobenius norm
        return Math.sqrt((content[0] * content[0]) + (content[1] * content[1]) + (content[2] * content[2]));
    }
    @Override
    public double norm(double p) { //n-norm as vector norm
        if (p == 0) {
            return Double.NaN;
        } else {
            return Math.pow(Math.pow(content[0], p) + Math.pow(content[1], p) + Math.pow(content[2], p), 1/p);
        }
        
    }
    @Override
    public double normMax() { //returns max norm, where p = inf
        return Math.max(content[0], Math.max(content[1], content[2]));
    }
    @Override
    public double normSqr() { //Squared norm
        return (content[0] * content[0]) + (content[1] * content[1]) + (content[2] * content[2]);
    }
    
    @Override
    public String toString() {
        return "[" + content[0] + ", " + content[1] + ", " + content[2] + "]";
    }
    
}
