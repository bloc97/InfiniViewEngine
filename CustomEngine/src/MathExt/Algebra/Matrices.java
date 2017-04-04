/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MathExt.Algebra;

/**
 *
 * @author bowen
 */
public abstract class Matrices {
    final public static Matrix shell(Matrix matrix) {
        return new Matrix(matrix.getDimensionSize(2), matrix.getDimensionSize(1));
    }
    final public static Matrix neg(Matrix matrix) {
        return new Matrix(Tensors.neg(matrix));
    }
    final public static Matrix transpose(Matrix matrix) {
        int rows = matrix.getDimensionSize(1); //swap dimension 1 and 2
        int columns = matrix.getDimensionSize(2);
        Matrix newMatrix = new Matrix(rows, columns);
        
        for (int i=0; i<rows; i++) {
            for (int j=0; j<columns; j++) {
                newMatrix.set(matrix.get(j, i), i, j);
            }
        }
        return newMatrix;
    }
    final public static Matrix add(Matrix... matrixArray) {
        return new Matrix(Tensors.add(matrixArray));
    }
    final public static Matrix add(Matrix matrix, double c) {
        return new Matrix(Tensors.add(matrix, c));
    }
    final public static Matrix sub(Matrix... matrixArray) {
        return new Matrix(Tensors.sub(matrixArray));
    }
    final public static Matrix sub(Matrix matrix, double c) {
        return new Matrix(Tensors.sub(matrix, c));
    }
    final public static Matrix prod(Matrix... matrixArray) {
        return new Matrix(Tensors.prod(matrixArray));
    }
    final public static Matrix prod(Matrix matrix, double c) {
        return new Matrix(Tensors.prod(matrix, c));
    }
    final public static Matrix div(Matrix... matrixArray) {
        return new Matrix(Tensors.div(matrixArray));
    }
    final public static Matrix div(Matrix matrix, double c) {
        return new Matrix(Tensors.div(matrix, c));
    }
    final public static Matrix mult(Matrix... matrixArray) {
        Matrix multMatrix = matrixArray[0].clone(); //save the leftmost matrix
        int multMatrixRows = multMatrix.getDimensionSize(2);
        int multMatrixColumns = multMatrix.getDimensionSize(1);
        for (int k=1; k<matrixArray.length; k++) { //multiply the leftmost matrix with each matrix on the right of it
            
            int nextMatrixRows = matrixArray[k].getDimensionSize(2);
            int nextMatrixColumns = matrixArray[k].getDimensionSize(1);
            
            if (multMatrixColumns == nextMatrixRows) { //if the two matrices can be multiplied
                Matrix newMatrix = new Matrix(multMatrixRows, nextMatrixColumns); //create the shell of the new matrix
                int n = multMatrixColumns;
                
                int newMatrixRows = newMatrix.getDimensionSize(2);
                int newMatrixColumns = newMatrix.getDimensionSize(1);

                for (int i=0; i<newMatrixRows; i++) { //matrix multiplication
                    for (int j=0; j<newMatrixColumns; j++) {
                        double component = 0;
                        for (int r=0; r<n; r++) {
                            component += multMatrix.get(i, r) * matrixArray[k].get(r, j);
                        }
                        newMatrix.set(i, j, component);
                    }
                }
                multMatrix = newMatrix; //save the new matrix as the leftmost matrix, and repeat
            } else {
                throw new IllegalArgumentException("Multiplication of different-sized matrices.");
            }
        }
        return multMatrix;
    }
    final public static boolean isSameDimensions(Matrix... matrixArray) {
        int rows = matrixArray[0].getDimensionSize(2);
        int columns = matrixArray[0].getDimensionSize(1);
        
        for (int n=1; n<matrixArray.length; n++) {
            if (matrixArray[n].getDimensionSize(2) != rows || matrixArray[n].getDimensionSize(1) != columns) {
                return false;
            }
        }
        return true;
    }
}
