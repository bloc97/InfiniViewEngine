/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Physics2D;

import MathExt.Algebra.Tensor;
import MathExt.Algebra.Vector;

/**
 *
 * @author bowen
 */
public class Vector2 extends Vector {
    private boolean isCartesian;
    public Vector2() {
        super(2);
        isCartesian = true;
    }
    public Vector2(boolean isCartesian) {
        super(2);
        this.isCartesian = isCartesian;
    }
    public Vector2(double fill) {
        super(2, fill);
        this.isCartesian = true;
    }
    public Vector2(double fill, boolean isCartesian) {
        super(2, fill);
        this.isCartesian = isCartesian;
    }
    public Vector2(double[] elementArray) {
        super(elementArray);
        this.isCartesian = true;
    }
    public Vector2(double[] elementArray, boolean isCartesian) {
        super(elementArray);
        this.isCartesian = isCartesian;
    }
    protected Vector2(Tensor tensor) {
        super(tensor);
        this.isCartesian = true;
    }
    protected Vector2(Tensor tensor, boolean isCartesian) {
        super(tensor);
        this.isCartesian = isCartesian;
    }
    public boolean isCartesian() {
        return isCartesian;
    }
    public double normSquared() {
        if (isCartesian) {
            return super.get(0) * super.get(0) + super.get(1) * super.get(1);
        } else {
            return super.get(0) * super.get(0);
        }
    }
    @Override
    public double norm() {
        if (isCartesian) {
            return super.norm();
        } else {
            return super.get(0);
        }
    }
    public double rot() {
        if (isCartesian) {
            return Math.atan2(super.get(1), super.get(0));
        } else {
            return super.get(1);
        }
    }
    public Vector2 setNorm(double c) {
        if (isCartesian) {
            double rot = rot();
            super.set(0, c*Math.cos(rot));
            super.set(1, c*Math.sin(rot));
        } else {
            super.set(0, c);
        }
        return this;
    }
    public Vector2 setRot(double rad) {
        if (isCartesian) {
            double norm = norm();
            super.set(0, norm*Math.cos(rad));
            super.set(1, norm*Math.sin(rad));
        } else {
            super.set(1, rad);
        }
        return this;
    }
    public Vector2 addNorm(double c) {
        return setNorm(norm() + c);
    }
    public Vector2 multNorm(double c) {
        return setNorm(norm() * c);
    }
    public Vector2 addRot(double c) {
        return setRot(rot() + c);
    }
    public Vector2 multRot(double c) {
        return setRot(rot() * c);
    }
    public Vector2 add(int i, double c) {
        set(i, get(i) + c);
        return this;
    }
    public Vector2 prod(int i, double c) {
        set(i, get(i) * c);
        return this;
    }
    public Vector2 add(Vector2 vector2) {
        if (isCartesian == vector2.isCartesian) {
            super.set(0, get(0) + vector2.get(0));
            super.set(1, get(1) + vector2.get(1));
        } else if (isCartesian) {
            set(0, get(0) + vector2.get(0));
            set(1, get(1) + vector2.get(1));
        } else {
            setNorm(norm() + vector2.norm());
            setRot(rot() + vector2.rot());
        }
        return this;
    }
    public Vector2 addC(double c) {
        set(0, get(0) + c);
        set(1, get(1) + c);
        return this;
    }
    public Vector2 sub(Vector2 vector2) {
        if (isCartesian == vector2.isCartesian) {
            super.set(0, get(0) - vector2.get(0));
            super.set(1, get(1) - vector2.get(1));
        } else if (isCartesian) {
            set(0, get(0) - vector2.get(0));
            set(1, get(1) - vector2.get(1));
        } else {
            setNorm(norm() - vector2.norm());
            setRot(rot() - vector2.rot());
        }
        return this;
    }
    public Vector2 subC(double c) {
        set(0, get(0) - c);
        set(1, get(1) - c);
        return this;
    }
    public Vector2 prod(Vector2 vector2) {
        if (isCartesian == vector2.isCartesian) {
            super.set(0, get(0) * vector2.get(0));
            super.set(1, get(1) * vector2.get(1));
        } else if (isCartesian) {
            set(0, get(0) * vector2.get(0));
            set(1, get(1) * vector2.get(1));
        } else {
            setNorm(norm() * vector2.norm());
            setRot(rot() * vector2.rot());
        }
        return this;
    }
    public Vector2 prodC(double c) {
        set(0, get(0) * c);
        set(1, get(1) * c);
        return this;
    }
    public Vector2 div(Vector2 vector2) {
        if (isCartesian == vector2.isCartesian) {
            double other0 = vector2.get(0);
            double other1 = vector2.get(1);
            if (other0 * other1 == 0) {
                throw new IllegalArgumentException("Division by zero.");
            }
            super.set(0, get(0) / other0);
            super.set(1, get(1) / other1);
        } else if (isCartesian) {
            double otherX = vector2.get(0);
            double otherY = vector2.get(1);
            if (otherX * otherY == 0) {
                throw new IllegalArgumentException("Division by zero.");
            }
            set(0, get(0) / otherX);
            set(1, get(1) / otherY);
        } else {
            double otherNorm = vector2.norm();
            double otherRot = vector2.rot();
            if (otherNorm * otherRot == 0) {
                throw new IllegalArgumentException("Division by zero.");
            }
            setNorm(norm() / otherNorm);
            setRot(rot() / otherRot);
        }
        return this;
    }
    public Vector2 divC(double c) {
        set(0, get(0) / c);
        set(1, get(1) / c);
        return this;
    }
    @Override
    public double get(int i) {
        if (isCartesian) {
            return super.get(i);
        } else {
            switch(i) {
                case 0:
                    return norm()*Math.cos(rot());
                case 1:
                    return norm()*Math.sin(rot());
                default:
                    throw new IndexOutOfBoundsException("Vector2 out of bounds.");
            }
        }
    }
    public double getPolar(int i) {
        switch(i) {
            case 0:
                return norm();
            case 1:
                return rot();
            default:
                throw new IndexOutOfBoundsException("Vector2 out of bounds.");
        }
    }
    @Override
    public void set(int i, double c) {
        if (isCartesian) {
            super.set(i, c);
        } else {
            double x,y;
            switch(i) {
                case 0:
                    x = c;
                    y = get(1);
                    super.set(0, Math.sqrt(x*x + y*y));
                    super.set(1, Math.atan2(y, x));
                    break;
                case 1:
                    x = get(0);
                    y = c;
                    super.set(0, Math.sqrt(x*x + y*y));
                    super.set(1, Math.atan2(y, x));
                    break;
                default:
                    throw new IndexOutOfBoundsException("Vector2 out of bounds.");
            }
        }
    }
    public void setPolar(int i, double c) {
        switch(i) {
            case 0:
                setNorm(c);
                break;
            case 1:
                setRot(c);
                break;
            default:
                throw new IndexOutOfBoundsException("Vector2 out of bounds.");
        }
    }
    @Override
    public void set(double[] elementArray) {
        if (elementArray.length != 2) {
            throw new IndexOutOfBoundsException("Vector2 out of bounds.");
        } else {
            set(0, elementArray[0]);
            set(1, elementArray[1]);
        }
    }
    public void set(Vector2 vector2) {
        if (isCartesian == vector2.isCartesian) {
            super.set(0, ((Tensor)vector2).get(0));
            super.set(1, ((Tensor)vector2).get(1));
            set(0, vector2.get(0));
            set(1, vector2.get(1));
        } else {
            set(0, vector2.get(0));
            set(1, vector2.get(1));
        }
    }
    public Vector2 proj(Vector2 vector2) {
        if (isCartesian) {
            super.proj(vector2);
        } else {
            setNorm(norm() * Math.cos(Math.abs(rot() - vector2.rot())));
            setRot(vector2.rot());
        }
        return this;
    }
    public Vector2 rej(Vector2 vector2) {
        if (isCartesian) {
            super.rej(vector2);
        } else {
            double myRot = rot();
            double otherRot = vector2.rot();
            double diffRot = Math.abs(myRot - otherRot);
            double remRot = (Math.PI/2) - diffRot;
            setNorm(norm() * Math.sin(diffRot));
            if (myRot > otherRot) {
                setRot(myRot + remRot);
            } else {
                setRot(myRot - remRot);
            }
        }
        return this;
    }
    @Override
    public Vector2 clone() {
        Vector2 newVector = new Vector2(super.clone(), isCartesian);
        return newVector;
    }
    
}
