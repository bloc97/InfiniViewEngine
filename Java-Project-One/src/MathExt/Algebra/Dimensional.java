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
public interface Dimensional {
    //Strict Getters
    int dimensions(); //Returns num of dimensions
    int getDimensionSize(int i); //Returns the size of dimension (num of elements)
    double norm();
    
    //Returns
    /*Geometric getShell();
    Geometric getFill();
    Geometric getNegation();
    Geometric getClone();
    */
    //Modifies
    
}
