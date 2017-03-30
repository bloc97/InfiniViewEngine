/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Deprec.SPHY;
/**
 *
 * @author bowen
 */
public class Vector2 {
    private double[] elements = new double[2];
    public static boolean useFastMath = false;
    
    public Vector2() {
    }
    public Vector2(double scalar) {
        this.elements = new double[]{scalar,scalar};
    }
    public Vector2(double[] array) {
        this.elements[0] = array[0];
        this.elements[1] = array[1];
    }
    public Vector2(double x, double y) {
        this.elements = new double[]{x,y};
    }
    public Vector2(double r, double phi, boolean isPolar) {
        this.elements[0] = r*cos(phi);
        this.elements[1] = r*sin(phi);
    }
    
    private static double sin(double phi) {
        if (useFastMath) {
            //return FMath.bhaskaraSin(phi);
        }
        return Math.sin(phi);
    }
    private static double cos(double phi) {
        if (useFastMath) {
            //return FMath.bhaskaraCos(phi);
        }
        return Math.cos(phi);
    }
    private static double atan2(double y, double x) {
        if (useFastMath) {
            return FMath.quickAtan2(y, x);
        } else {
            return Math.atan2(y, x);
        }
    }
    private static double sqrt(double x) {
        if (useFastMath) {
            //return FMath.quickSqrt((float)x);
        }
        return Math.sqrt(x);
    }
    
    public double getElement(int i) {
        return this.elements[i];
    }
    public double getNorm() {
        double a = this.elements[0]*this.elements[0];
        double b = this.elements[1]*this.elements[1];
        return sqrt(a + b);
    }
    public double getRot() {
        return atan2(this.elements[1], this.elements[0]);
    }
    public Vector2 getFill(double scalar) {
        return new Vector2(scalar, scalar);
    }
    public Vector2 getAdd(Vector2 vector) {
        return new Vector2(this.elements[0] + vector.elements[0], this.elements[1] + vector.elements[1]);
    }
    public Vector2 getAdd(double scalar) {
        return new Vector2(this.elements[0] + scalar, this.elements[1] + scalar);
    }
    public Vector2 getSub(Vector2 vector) {
        return new Vector2(this.elements[0] - vector.elements[0], this.elements[1] - vector.elements[1]);
    }
    public Vector2 getNeg() {
        return new Vector2(-this.elements[0], -this.elements[1]);
    }
    public Vector2 getMult(double scalar) {
        return new Vector2(this.elements[0] * scalar, this.elements[1] * scalar);
    }
    public double getMult(Vector2 vector) {
        return this.elements[0] * vector.elements[0] + this.elements[1] * vector.elements[1];
    }
    public double getProjSca(Vector2 vector) {
        return this.getMult(vector)/vector.getNorm();
    }
    public Vector2 getProjVec(Vector2 vector) {
        double scalar = this.getProjSca(vector);
        return new Vector2(scalar, vector.getRot(), false);
    }
    public Vector2 getRejVec(Vector2 vector) {
        return this.getSub(this.getProjVec(vector));
    }
    
    public void add(Vector2 vector) {
        this.elements[0] += vector.elements[0];
        this.elements[1] += vector.elements[1];
    }
    public void add(double scalar) {
        this.elements[0] += scalar;
        this.elements[1] += scalar;
    }
    public void sub(Vector2 vector) {
        this.elements[0] -= vector.elements[0];
        this.elements[1] -= vector.elements[1];
    }
    public void neg() {
        this.elements[0] = -this.elements[0];
        this.elements[1] = -this.elements[1];
    }
    public void mult(double scalar) {
        this.elements[0] *= scalar;
        this.elements[1] *= scalar;
        
    }
    public void proj(Vector2 vector) {
        double r = this.getProjSca(vector);
        double phi = vector.getRot();
        this.elements[0] = r*cos(phi);
        this.elements[1] = r*sin(phi);
    }
    public void rej(Vector2 vector) {
        double r = this.getProjSca(vector);
        double phi = vector.getRot();
        this.elements[0] -= r*cos(phi);
        this.elements[1] -= r*sin(phi);
    }
    public void fill(double scalar) {
        this.elements[0] = scalar;
        this.elements[1] = scalar;
    }
    public void setElement(int i, double scalar) {
        this.elements[i] = scalar;
    }
    public void setElements(Vector2 vector) {
        this.elements[0] = vector.elements[0];
        this.elements[1] = vector.elements[1];
    }
    public void setElements(double[] array) {
        this.elements[0] = array[0];
        this.elements[1] = array[1];
    }
    public void addToElement(int i, double scalar) {
        this.elements[i] += scalar;
    }
    public void multToElement(int i, double scalar) {
        this.elements[i] *= scalar;
    }
    public void setNorm(double r) {
        double phi = this.getRot();
        this.elements[0] = r*cos(phi);
        this.elements[1] = r*sin(phi);
    }
    public void setRot(double phi) {
        double r = this.getNorm();
        this.elements[0] = r*cos(phi);
        this.elements[1] = r*sin(phi);
    }
    public void addToNorm(double d) {
        double phi = this.getRot();
        double r = this.getNorm() + d;
        this.elements[0] = r*cos(phi);
        this.elements[1] = r*sin(phi);
    }
    public void multToNorm(double scalar) {
        double phi = this.getRot();
        double r = this.getNorm() * scalar;
        this.elements[0] = r*cos(phi);
        this.elements[1] = r*sin(phi);
    }
    public void addToRot(double tetha) {
        double phi = this.getRot() + tetha;
        double r = this.getNorm();
        this.elements[0] = r*cos(phi);
        this.elements[1] = r*sin(phi);
    }
    public void multToRot(double scalar) {
        double phi = this.getRot() * scalar;
        double r = this.getNorm();
        this.elements[0] = r*cos(phi);
        this.elements[1] = r*sin(phi);
    }
    
    
}
