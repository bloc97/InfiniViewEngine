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
public abstract class Tensors {
    final public static Tensor join(Tensor... tensorArray) {
        if (isSameDimensions(tensorArray)) {
            int[] newDimSizes = new int[tensorArray[0].dimensions() + 1];
            newDimSizes[0] = tensorArray.length;
            for (int i=0; i<tensorArray[0].dimensions(); i++) {
                newDimSizes[i+1] = tensorArray[0].getDimSize(i);
            }
            Tensor newTensor = new Tensor(newDimSizes);
            
            int n = 0;
            for (int i=0; i<tensorArray.length; i++) {
                for (int j=0; j<tensorArray[0].length(); j++) {
                    newTensor.set(n, tensorArray[i].get(j));
                    n++;
                }
            }
            
            return newTensor;
        } else {
            throw new IndexOutOfBoundsException("Tensor of different sizes.");
        }
    }
    final public static Tensor[] split(Tensor tensor) {
        Tensor[] tensorArray = new Tensor[tensor.getDimSize(0)];
        int[] newDimSizes = new int[tensor.dimensions() - 1];
        for (int i=0; i<newDimSizes.length; i++) {
            newDimSizes[i] = tensor.getDimSize(i+1);
        }
        
        Tensor blankTensor = new Tensor(newDimSizes);
        double[] tensorElements = tensor.getElements();
        int leap = tensor.getDimLeap(0);
        
        for (int n=0; n<tensorArray.length; n++) {
            tensorArray[n] = blankTensor.clone();
            tensorArray[n].set( Arrays.copyOfRange(tensorElements, n*leap, (n+1)*leap) );
        }
        
        return tensorArray;
    }
    final public static Tensor neg(Tensor tensor) {
        Tensor newTensor = tensor.clone();
        newTensor.negate();
        return newTensor;
    }
    final public static Tensor add(Tensor... tensorArray) {
        Tensor newTensor = tensorArray[0].clone();
        for (int n=1; n<tensorArray.length; n++) {
            newTensor.add(tensorArray[n]);
        }
        return newTensor;
    }
    final public static Tensor add(Tensor tensor, double c) {
        Tensor newTensor = tensor.clone();
        newTensor.add(c);
        return newTensor;
    }
    final public static Tensor sub(Tensor... tensorArray) {
        Tensor newTensor = tensorArray[0].clone();
        for (int n=1; n<tensorArray.length; n++) {
            newTensor.sub(tensorArray[n]);
        }
        return newTensor;
    }
    final public static Tensor sub(Tensor tensor, double c) {
        Tensor newTensor = tensor.clone();
        newTensor.sub(c);
        return newTensor;
    }
    final public static Tensor prod(Tensor... tensorArray) {
        Tensor newTensor = tensorArray[0].clone();
        for (int n=1; n<tensorArray.length; n++) {
            newTensor.prod(tensorArray[n]);
        }
        return newTensor;
    }
    final public static Tensor prod(Tensor tensor, double c) {
        Tensor newTensor = tensor.clone();
        newTensor.prod(c);
        return newTensor;
    }
    final public static Tensor div(Tensor... tensorArray) {
        Tensor newTensor = tensorArray[0].clone();
        for (int n=1; n<tensorArray.length; n++) {
            newTensor.div(tensorArray[n]);
        }
        return newTensor;
    }
    final public static Tensor div(Tensor tensor, double c) {
        Tensor newTensor = tensor.clone();
        newTensor.div(c);
        return newTensor;
    }
    final public static boolean isSameDimensions(Tensor... tensorArray) {
        int[] firstDimSizes = tensorArray[0].size();
        
        for (int n=1; n<tensorArray.length; n++) {
            if (!Arrays.equals(firstDimSizes, tensorArray[n].size())) {
                return false;
            }
        }
        return true;
    }
    final public static boolean isSameSubDimensions(Tensor... tensorArray) {
        int[] firstDimSizes = tensorArray[0].size();
        int firstDimLength = tensorArray[0].dimensions();
        
        
        for (int n=1; n<tensorArray.length; n++) {
            int[] subDimSizes = tensorArray[n].size();
            int subDimLength = tensorArray[n].dimensions();
            
            if (firstDimLength < subDimLength) {
                return false;
            }
            for (int i=0; i<subDimLength; i++) {
                if (firstDimSizes[firstDimLength-1-i] != subDimSizes[subDimLength-1-i]) {
                    return false;
                }
            }
        }
        return true;
    }
    public static Matrix toMatrix(Tensor tensor2) {
        if (tensor2.dimensions() > 2) {
            throw new IndexOutOfBoundsException("Cannot convert 3rd or higher order tensor into matrix.");
        }
        return new Matrix(tensor2);
    }
    public static Matrix toRowMatrix(Tensor tensor1) {
        if (tensor1.dimensions() > 1) {
            throw new IndexOutOfBoundsException("Tensor is not of 1st order.");
        }
        return new Matrix(Tensors.join(tensor1));
    }
    public static Matrix toColumnMatrix(Tensor tensor1) {
        if (tensor1.dimensions() > 1) {
            throw new IndexOutOfBoundsException("Tensor is not of 1st order.");
        }
        return Matrices.transpose(toMatrix(Tensors.join(tensor1)));
    }
    public static Vector toVector(Tensor tensor1) {
        if (tensor1.dimensions() > 1) {
            throw new IndexOutOfBoundsException("Cannot convert 2rd or higher order tensor into vector.");
        }
        return new Vector(tensor1);
    }
}
