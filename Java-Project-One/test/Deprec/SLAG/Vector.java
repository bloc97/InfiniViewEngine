/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Deprec.SLAG;


/**
 *
 * @author bowen
 */
public class Vector extends Matrix {
    
    public Vector(int n) {
        super(1, n);
    }
    public Vector(int n, double s) {
        super(1, n, s);
    }
    public Vector(double[] array) {
        super(array);
    }
    public Vector(Matrix matrix) {
        super(matrix.getElements()[0]);
    }
    private Vector(Matrix matrix, boolean noCopy) {
        super(matrix, true);
    }
    
    /**
     * Duplicates a vector
     * @param vector Vector to duplicate
     * @return The cloned vector
     */
    public static Vector clone(Vector vector) {
        return new Vector(Matrix.clone(vector), true);
    }
    /**
     * Performs element-wise vector addition
     * @param args <b>u</b> = {<b>u</b><sub>1</sub>, <b>u</b><sub>2</sub>, <b>u</b><sub>3</sub>, ...}
     * @return <b>v</b> = ∑<b>u</b><sub>i</sub>
     */
    public static Vector add(Vector... args) {
        return new Vector(Matrix.add(args), true);
    }
    /**
     * Negates a vector
     * @param vector <b>u</b>
     * @return <b>v</b> = -1 ⋅ <b>u</b>
     */
    public static Vector negate(Vector vector) {
        return new Vector(Matrix.negate(vector), true);
    }
    /**
     * Performs scalar multiplication
     * @param vector <b>u</b>
     * @param scalars <b>s</b> = {s<sub>1</sub>, s<sub>2</sub>, s<sub>3</sub>, ...}
     * @return <b>v</b> = ∏<b>s</b><sub>i</sub> ⋅ <b>u</b>
     */
    public static Vector multSca(Vector vector, double... scalars) {
        return new Vector(Matrix.multSca(vector, scalars), true);
    }
    /**
     * Returns the dot product of vectors
     * @param args <b>u</b> = {<b>u</b><sub>1</sub>, <b>u</b><sub>2</sub>, <b>u</b><sub>3</sub>, ...}
     * @return <b>v</b> = <b>u</b><sub>1</sub> ⋅ <b>u</b><sub>2</sub> ⋅ <b>u</b><sub>3</sub> ⋅ ...
     */
    public static double prodSca(Vector... args) {
        Matrix matrix = args[0];
        for (int k=1; k<args.length; k++) {
            matrix = multMat(matrix, args[k].getTranspose());
        }
        return matrix.getElement(0, 0);
    }
    /**
     * Performs scalar projection
     * @param vec1 <b>u</b>
     * @param vec2 <b>v</b>
     * @return s = <i>comp</i><sub><b>v</b></sub> <b>u</b>
     */
    public static double projSca(Vector vec1, Vector vec2) {
        return Vector.prodSca(vec1, vec2)/vec2.getNorm();
    }
    /**
     * Performs vector projection
     * @param args <b>u</b>, <b>v</b>, <b>w</b>, ...
     * @return <b>x</b> = <i>proj</i><sub><b>v</b>,<b>w</b>,...</sub> <b>u</b>
     */
    public static Vector projVec(Vector... args) {
        Vector vec1 = args[0];
        for (int k=1; k<args.length; k++) {
            Vector vec2 = args[k];
            double prod1 = Vector.prodSca(vec1, vec2);
            double prod2 = Vector.prodSca(vec2, vec2);
            if (prod2 == 0) return vec1;
            vec1 = Vector.multSca(vec2, prod1/prod2);
        }
        return vec1;
    }
    /**
     * Performs vector rejection
     * @param args <b>u</b>, <b>v</b>, <b>w</b>, ...
     * @return <b>x</b> = <i>rej</i><sub><b>v</b>,<b>w</b>,...</sub> <b>u</b>
     */
    public static Vector rejVec(Vector... args) {
        Vector vec1 = args[0];
        for (int k=1; k<args.length; k++) {
            Vector vec2 = args[k];
            double prod1 = Vector.prodSca(vec1, vec2);
            double prod2 = Vector.prodSca(vec2, vec2);
            if (prod2 == 0) return vec1;
            vec1 = new Vector(Matrix.add(vec1, Matrix.negate(Matrix.multSca(vec2, prod1/prod2))));
        }
        return vec1;
    }
    /**
     * Returns the vector norm
     * @param vector <b>u</b>
     * @return r = ‖<b>u</b>‖
     */
    public static double norm(Vector vector) {
        double sum = 0;
        for (int i=0; i<vector.getColumnDimension(); i++) {
            sum += Math.pow(vector.getElement(0, i), 2);
        }
        return Math.sqrt(sum);
    }
    
    @Override
    public Vector getClone() {
        return clone(this);
    }
    public Vector getAdd(Vector... args) {
        Vector[] newargs = new Vector[args.length+1];
        newargs[0] = this;
        for (int i=0; i<args.length; i++) {
            newargs[i+1] = args[i];
        }
        return add(newargs);
    }
    @Override
    public Vector getNegate() {
        return negate(this);
    }
    @Override
    public Vector getMult(double... scalars) {
        return multSca(this, scalars);
    }
    public double getProd(Vector... args) {
        return prodSca(args);
    }
    public double getProjSca(Vector vector) {
        return projSca(this, vector);
    }
    public Vector getProjVec(Vector... args) {
        Vector[] newargs = new Vector[args.length+1];
        newargs[0] = this;
        for (int i=0; i<args.length; i++) {
            newargs[i+1] = args[i];
        }
        return projVec(newargs);
    }
    public Vector getRejVec(Vector... args) {
        Vector[] newargs = new Vector[args.length+1];
        newargs[0] = this;
        for (int i=0; i<args.length; i++) {
            newargs[i+1] = args[i];
        }
        return rejVec(newargs);
    }
    public double getNorm() {
        return norm(this);
    }
    
    public double getElement(int j) {
        return this.getElement(0, j);
    }
    public int getDimension() {
        return this.getColumnDimension();
    }
    public void setElement(int j, double s) {
        this.setElement(0, j, s);
    }
    
    
    //TODO
    //Cross product https://en.wikipedia.org/wiki/Cross_product
}
