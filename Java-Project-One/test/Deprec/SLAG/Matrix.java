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
public class Matrix {
    private double[][] elements;
    private int rows, columns;
    
    public Matrix(int m, int n) {
        if (!Utils.checkPositive(m, n)) {
            throw new IllegalArgumentException("Invalid number of rows or columns.");
        }
        
        this.rows = m;
        this.columns = n;
        this.elements = new double[m][n];
    }
    public Matrix(int n, double s) {
        if (!Utils.checkPositive(n)) {
            throw new IllegalArgumentException("Invalid number of rows or columns.");
        }
        this.rows = n;
        this.columns = n;
        this.elements = new double[n][n];
        Utils.fill(this.elements, s);
    }
    public Matrix(int m, int n, double s) {
        if (!Utils.checkPositive(m, n)) {
            throw new IllegalArgumentException("Invalid number of rows or columns.");
        }
        this.rows = m;
        this.columns = n;
        this.elements = new double[m][n];
        Utils.fill(this.elements, s);
    }
    public Matrix(int n) {
        if (!Utils.checkPositive(n)) {
            throw new IllegalArgumentException("Invalid number of rows or columns.");
        }
        this.rows = n;
        this.columns = n;
        this.elements = new double[n][n];
        
        for (int i=0; i<n; i++) {
            this.elements[i][i] = 1;
        }
    }
    public Matrix(double[]... array) {
        if (array.length < 1) { //if there are less than 1 rows
            throw new IllegalArgumentException("Invalid number of rows.");
        } else if (array[0].length < 1) { //if there are less than 1 columns
            throw new IllegalArgumentException("Invalid number of columns.");
        }
        if (!Utils.checkLength(array)) {
            throw new IllegalArgumentException("Matrix has different row lengths.");
        }
        
        this.rows = array.length;
        this.columns = array[0].length;
        this.elements = Utils.copy(array);
    }
    public Matrix(Matrix matrix, boolean noCopy) {
        
        this.rows = matrix.rows;
        this.columns = matrix.columns;
        this.elements = matrix.elements;
    }
    
    /**
     * Checks if matrices have the same dimensions
     * @param args List of matrices
     * @return boolean
     */
    public static boolean checkSameSize(Matrix... args) {
        for (int k=1; k<args.length; k++) {
            if (args[0].rows != args[k].rows || args[0].columns != args[k].columns) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Returns an empty matrix of the same dimensions
     * @param matrix <b>A</b>
     * @return <b>B</b> = 0 ⋅ <b>A</b>
     */
    public static Matrix shell(Matrix matrix) {
        int columns = matrix.columns;
        int rows = matrix.rows;
        return new Matrix(rows, columns);
    }
    /**
     * Duplicates a matrix
     * @param matrix <b>A</b>
     * @return <b>B</b> = <b>A</b>
     */
    public static Matrix clone(Matrix matrix) {
        return new Matrix(matrix.elements);
    }
    /**
     * Performs element-wise matrix addition
     * @param args <b>A</b> = {<b>A</b><sub>1</sub>, <b>A</b><sub>2</sub>, <b>A</b><sub>3</sub>, ...}
     * @return <b>B</b> = ∑<b>A</b><sub>i</sub>
     */
    public static Matrix add(Matrix... args) { //instead of using (Matrix... args), use (Matrix matrix, Matrix... args)
        if (!checkSameSize(args)) {
            throw new IllegalArgumentException("Addition of different-sized matrices.");
        }
            
        Matrix newmatrix = args[0].getShell();

        for (int k=0; k<args.length; k++) {
            for (int j=0; j<args[k].columns; j++) {
                for (int i=0; i<args[k].rows; i++) {
                    newmatrix.elements[i][j] += args[k].elements[i][j];
                }
            }
        }
        return newmatrix;

    }
    /**
     * Negates a matrix
     * @param matrix <b>A</b>
     * @return <b>B</b> = -1 ⋅ <b>A</b>
     */
    public static Matrix negate(Matrix matrix) {
        Matrix newmatrix = matrix.getShell();
        
        for (int j=0; j<matrix.columns; j++) {
            for (int i=0; i<matrix.rows; i++) {
                newmatrix.elements[i][j] = -matrix.elements[i][j];
            }
        }
        return newmatrix;
    }
    /**
     * Performs scalar multiplication
     * @param matrix <b>A</b>
     * @param scalars <b>s</b> = {s<sub>1</sub>, s<sub>2</sub>, s<sub>3</sub>, ...}
     * @return <b>B</b> = ∏<b>s</b><sub>i</sub> ⋅ <b>A</b>
     */
    public static Matrix multSca(Matrix matrix, double... scalars) {
        double c = 1;
        for (int i=0; i<scalars.length; i++) {
            c *= scalars[i];
        }
        
        Matrix newmatrix = matrix.getShell();
        
        if (c == 0) { //if scalar is 0, return the new 0 matrix to save time
            return newmatrix;
        } else if (c == 1) { //if scalar is 1, return the old matrix to save time
            return matrix;
        }
        
        for (int j=0; j<matrix.columns; j++) {
            for (int i=0; i<matrix.rows; i++) {
                newmatrix.elements[i][j] = matrix.elements[i][j] * c;
            }
        }
        return newmatrix;
    }
    /**
     * Performs matrix transposition
     * @param matrix <b>A</b>
     * @return <b>B</b> = <b>A</b><sup>T</sup>
     */
    public static Matrix transpose(Matrix matrix) {
        Matrix newmatrix = new Matrix(matrix.columns, matrix.rows);
        
        for (int j=0; j<matrix.columns; j++) {
            for (int i=0; i<matrix.rows; i++) {
                newmatrix.elements[j][i] = matrix.elements[i][j];
            }
        }
        return newmatrix;
    }
    /**
     * Performs matrix multiplication
     * @param args <b>A</b> = {<b>A</b><sub>1</sub>, <b>A</b><sub>2</sub>, <b>A</b><sub>3</sub>, ...}
     * @return <b>B</b> = <b>A</b><sub>1</sub> × <b>A</b><sub>2</sub> × <b>A</b><sub>3</sub> × ...
     */
    public static Matrix multMat(Matrix... args) {
        Matrix multmatrix = args[0].getClone(); //save the leftmost matrix
        for (int k=1; k<args.length; k++) { //multiply the leftmost matrix with each matrix on the right of it
            
            if (multmatrix.columns == args[k].rows) { //if the two matrices can be multiplied
                Matrix newmatrix = new Matrix(multmatrix.rows, args[k].columns); //create the shell of the new matrix
                int n = multmatrix.columns;

                for (int i=0; i<newmatrix.rows; i++) { //matrix multiplication
                    for (int j=0; j<newmatrix.columns; j++) {
                        double component = 0;
                        for (int r=0; r<n; r++) {
                            component += multmatrix.elements[i][r] * args[k].elements[r][j];
                        }
                        newmatrix.elements[i][j] = component;
                    }
                }
                multmatrix = newmatrix; //save the new matrix as the leftmost matrix, and repeat
            } else {
                throw new IllegalArgumentException("Multiplication of different-sized matrices.");
            }
        }
        return multmatrix;
    }
    
    /**
     * Returns an empty matrix of the same dimensions
     * @param this <b>A</b>
     * @return <b>B</b> = 0 ⋅ <b>A</b>
     */
    public Matrix getShell() {
        return shell(this);
    }
    public Matrix getClone() {
        return clone(this);
    }
    public Matrix getAdd(Matrix... args) {
        Matrix[] newargs = new Matrix[args.length+1];
        newargs[0] = this;
        for (int i=0; i<args.length; i++) {
            newargs[i+1] = args[i];
        }
        return add(newargs);
    }
    public Matrix getNegate() {
        return negate(this);
    }
    public Matrix getMult(double... scalars) {
        return multSca(this, scalars);
    }
    public Matrix getMult(Matrix... args) {
        Matrix[] newargs = new Matrix[args.length+1];
        newargs[0] = this;
        for (int i=0; i<args.length; i++) {
            newargs[i+1] = args[i];
        }
        return multMat(newargs);
    }
    public Matrix getTranspose() {
        return transpose(this);
    }
    
    public double[][] getElements() {
        return elements;
    }
    public double[][] getElementsClone() {
        return Utils.copy(elements);
    }
    public double getElement(int i, int j) {
        return elements[i][j];
    }
    
    public boolean getIsSquare() {
        return this.columns == this.rows;
    }
    public int getColumnDimension() {
        return columns;
    }
    public int getRowDimension() {
        return rows;
    }
    
    public void setElement(int i, int j, double s) {
        this.elements[i][j] = s;
    }
    public void setElements(Matrix matrix) {
        this.rows = matrix.rows;
        this.columns = matrix.columns;
        this.elements = Utils.copy(matrix.elements);
    }
    public void updateElements(Matrix matrix) {
        this.rows = matrix.rows;
        this.columns = matrix.columns;
        this.elements = matrix.elements;
    }
    
    public void setShell() {
        this.updateElements(this.getShell());
    }
    public void setAdd(Matrix... args) {
        this.updateElements(this.getAdd(args));
    }
    public void setNegate() {
        this.updateElements(this.getNegate());
    }
    public void setMult(double... scalars) {
        this.updateElements(this.getMult(scalars));
    }
    public void setMult(Matrix... args) {
        this.updateElements(this.getMult(args));
    }
    public void setTranspose() {
        this.updateElements(this.getTranspose());
    }
    //TODO
    //Add Row Operators (Row addition, multiplication, switching)
    //Add Submatrix Operation (Get submatrix from matrix and n,m) https://en.wikipedia.org/wiki/Matrix_(mathematics)#Submatrix
    //Add getDeterminant
    //Add getTrace
    //Add getMinor and getCofactor
    //Add Linear Equation solving
    //Add Linear Transformation Matrices https://en.wikipedia.org/wiki/Linear_map#Examples_of_linear_transformation_matrices
    //Add Diagonalisation
}
