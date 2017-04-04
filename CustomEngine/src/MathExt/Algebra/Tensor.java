/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MathExt.Algebra;

import java.util.Arrays;

/**
 *
 * @author bowen
 */
public class Tensor implements Dimensional {
    final private int dimensions;
    final private int[] dimSizes;
    final private int[] dimLeap;
    private double[] elements;
    
    private Tensor(int[] sizeArray, int[] leapArray, double[] elementArray) {
        dimensions = sizeArray.length;
        dimSizes = sizeArray;
        dimLeap = leapArray;
        elements = elementArray;
    }
    public Tensor(int[] sizeArray) {
        dimensions = sizeArray.length;
        dimSizes = sizeArray.clone();
        dimLeap = dimLeap(sizeArray);
        elements = new double[dimSizes[0] * dimLeap[0]];
    }
    public Tensor(int[] sizeArray, double fill) {
        this(sizeArray);
        this.fill(fill);
    }
    protected Tensor(Tensor tensor) {
        dimensions = tensor.dimensions;
        dimSizes = tensor.dimSizes;
        dimLeap = tensor.dimLeap;
        elements = tensor.elements;
    }
    public Tensor(double[] elementArray) {
        dimensions = 1;
        dimSizes = new int[] {elementArray.length};
        dimLeap = dimLeap(dimSizes);
        elements = elementArray.clone();
    }
    public Tensor(double[][] elementArray2D) {
        this(new int[] {elementArray2D.length, elementArray2D[0].length});
        int n = 0;
        for (int i=0; i<elementArray2D.length; i++) {
            for (int j=0; j<elementArray2D[0].length; j++) {
                elements[n] = elementArray2D[i][j];
                n++;
            }
        }
    }
    public Tensor(double[][][] elementArray3D) {
        this(new int[] {elementArray3D.length, elementArray3D[0].length, elementArray3D[0][0].length});
        int n = 0;
        for (int i=0; i<elementArray3D.length; i++) {
            for (int j=0; j<elementArray3D[0].length; j++) {
                for (int k=0; k<elementArray3D[0][0].length; k++) {
                    elements[n] = elementArray3D[i][j][k];
                    n++;
                }
            }
        }
    }
    private static int[] dimLeap(int[] sizeArray) {
        int prod = 1;
        int[] dimLeap = new int[sizeArray.length];
        for (int i=sizeArray.length-1; i>=0; i--) {
            dimLeap[i] = prod;
            prod *= sizeArray[i];
        }
        return dimLeap;
    }
    public int dimensions() {
        return dimensions;
    }
    public int length() {
        return elements.length;
    }
    public int[] size() {
        return dimSizes.clone();
    }
    
    public double norm() { //Frobenius norm, generalised as vector norm for higher dimensions.
        if (dimensions < 1) {
            return 0;
        } else {
            double sqrsum = 0;
            for (int i=0; i<elements.length; i++) {
                double elem = elements[i];
                sqrsum += elem * elem;
            }
            return Math.sqrt(sqrsum);
        }
    }
    public double norm(int p) { //n-norm as vector norm
        if (p < 1) {
            throw new IllegalArgumentException("Invalid p parameter, must be greater or equal than 1.");
        } else if (dimensions < 1) {
            return 0;
        } else {
            double sqrsum = 0;
            for (int i=0; i<elements.length; i++) {
                double elem = elements[i];
                sqrsum += Math.pow(elem, p);
            }
            return Math.pow(sqrsum, 1/p);
        }
        
    }
    public double maxNorm() { //returns max norm, where p = inf
        double max = elements[0];
        for (int i=1; i<elements.length; i++) {
            double elem = elements[i];
            if (max < elem) {
                max = elem;
            }
        }
        return max;
    }
    /*private int getDimLeap(int k) { //Get the leap at the nth dimension
        if (k > dimensions) {
            throw new IndexOutOfBoundsException("Invalid tensor dimension index.");
        }
        
        int prod = 1;
        for (int i=(dimensions - k + 1); i<dimensions; i++) {
            prod *= dimSizes[i];
        }
        return prod;
    }*/
    private int getElemIndex(int[] n) {
        if (n.length < dimensions) {
            throw new IndexOutOfBoundsException("Invalid arguments, the number of index arguments must be equal to the number of dimensions.");
        }
        int leap = 0;
        for (int i=0; i<n.length; i++) {
            leap += dimLeap[i]*n[i];
        }
        return leap;
    }
    private boolean isWithinBounds(int[] n) {
        if (n.length <= dimensions) {
            for (int i=0; i<n.length; i++) {
                if (n[i] >= dimSizes[i] || n[i] < 0) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }
    protected int getDimSize(int i) {
        return dimSizes[i];
    }
    protected int getDimLeap(int i) {
        return dimLeap[i];
    }
    protected double[] getElements() {
        return elements.clone();
    }
    public int getDimensionSize(int d) {
        if (d > 0 && d <= dimensions) {
            return dimSizes[dimensions-d];
        } else {
            throw new IndexOutOfBoundsException("Invalid tensor dimension index.");
        }
    }
    public double get(int i) {
        if (i >= 0 && i < elements.length) {
            return elements[i];
        } else {
            throw new IndexOutOfBoundsException("Invalid tensor index.");
        }
    }
    public double get(int... n) {
        if (isWithinBounds(n)) {
            int index = getElemIndex(n);
            return elements[index];
        } else {
            throw new IndexOutOfBoundsException("Invalid tensor index.");
        }
    }
    public void set(int i, double c) {
        if (i >= 0 && i < elements.length) {
            elements[i] = c;
        } else {
            throw new IndexOutOfBoundsException("Invalid tensor index.");
        }
    }
    public void set(int[] n, double c) {
        if (isWithinBounds(n)) {
            int index = getElemIndex(n);
            elements[index] = c;
        } else {
            throw new IndexOutOfBoundsException("Invalid tensor index.");
        }
    }
    public void set(double c, int... n) {
        set(n, c);
    }
    protected void set(double[] elementArray) {
        if (elements.length == elementArray.length) {
            elements = elementArray;
        } else {
            throw new IndexOutOfBoundsException("Different tensor sizes.");
        }
    }
    public void set(Tensor tensor) {
        if (Tensors.isSameDimensions(this, tensor)) {
            elements = tensor.elements;
        } else {
            throw new IndexOutOfBoundsException("Different tensor sizes.");
        }
    }
    protected void copy(double[] elementArray) {
        set(elementArray.clone());
    }
    public void copy(Tensor tensor) {
        set(tensor.clone());
    }
    final public void fill(double c) {
        for (int i=0; i<elements.length; i++) {
            elements[i] = c;
        }
    }
    final public void zero() {
        fill(0);
    }
    final public void negate() {
        for (int i=0; i<elements.length; i++) {
            elements[i] = -elements[i];
        }
    }
    final public void add(Tensor tensor) {
        if (Tensors.isSameSubDimensions(new Tensor[] {this, tensor})) {
            int subLength = tensor.elements.length;
            for (int i=0; i<elements.length; i++) {
                elements[i] += tensor.elements[i%subLength];
            }
        } else {
            throw new IndexOutOfBoundsException("Incompatible tensor sizes.");
        }
    }
    final public void add(double c) {
        for (int i=0; i<elements.length; i++) {
            elements[i] += c;
        }
    }
    final public void sub(Tensor tensor) {
        if (Tensors.isSameSubDimensions(new Tensor[] {this, tensor})) {
            int subLength = tensor.elements.length;
            for (int i=0; i<elements.length; i++) {
                elements[i] -= tensor.elements[i%subLength];
            }

        } else {
            throw new IndexOutOfBoundsException("Incompatible tensor sizes.");
        }
    }
    final public void sub(double c) {
        for (int i=0; i<elements.length; i++) {
            elements[i] -= c;
        }
    }
    final public void prod(Tensor tensor) {
        if (Tensors.isSameSubDimensions(new Tensor[] {this, tensor})) {
            int subLength = tensor.elements.length;
            for (int i=0; i<elements.length; i++) {
                elements[i] *= tensor.elements[i%subLength];
            }

        } else {
            throw new IndexOutOfBoundsException("Incompatible tensor sizes.");
        }
    }
    final public void prod(double c) {
        for (int i=0; i<elements.length; i++) {
            elements[i] *= c;
        }
    }
    final public void div(Tensor tensor) {
        if (Tensors.isSameSubDimensions(new Tensor[] {this, tensor})) {
            int subLength = tensor.elements.length;
            for (int i=0; i<elements.length; i++) {
                double divisor = tensor.elements[i%subLength];
                if (divisor != 0) {
                    elements[i] /= divisor;
                } else {
                    throw new IllegalArgumentException("Divide by zero.");
                }
            }

        } else {
            throw new IndexOutOfBoundsException("Incompatible tensor sizes.");
        }
    }
    final public void div(double c) {
        for (int i=0; i<elements.length; i++) {             
            if (c != 0) {
                elements[i] /= c;
            } else {
                throw new IllegalArgumentException("Divide by zero.");
            }
        }
    }
    @Override
    public Tensor clone() {
        Tensor newTensor = new Tensor(dimSizes.clone(), dimLeap.clone(), elements.clone());
        return newTensor;
    }
    @Override
    public String toString() {
        if (dimensions == 0) {
            return "[]\nTensor of dimension 0";
        } else if (dimensions == 1) {
            String string = new String();
            for (int i=0; i<elements.length; i++) { //
                string += elements[i] + " "; //Separate columns by a space
            }
            return "_\n" + string + "\n_\nTensor of dimension 1";
        } else if (dimensions == 2) {
            String string = new String();
            string += elements[0] + " ";
            for (int i=1; i<elements.length; i++) { //For all elements
                if (i%dimSizes[1] == 0) {
                    string += "\n"; //Separate rows by a newline
                }
                string += elements[i] + " "; //Separate columns by a space
            }
            
            return "_\n" + string + "\n_\nTensor of dimension 2";
        } else {
            return "_\n[...]\n_\nTensor of dimension " + dimensions;
        }
    }
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!Tensor.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        final Tensor other = (Tensor) obj;
        if ((this.elements == null) ? (other.elements != null) : !Arrays.equals(this.elements, other.elements)) {
            return false;
        }
        if ((this.dimSizes == null) ? (other.dimSizes != null) : !Arrays.equals(this.dimSizes, other.dimSizes)) {
            return false;
        }
        return true;
    }
}
