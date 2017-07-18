/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bloc97.uvector;

/**
 *
 * @author bowen
 */
public class Test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Vector2 a = new Vector2(18, 24);
        Vector2 b = new Vector2(1, 0);
        
        System.out.println(Vectors.cross2D_AxBxB(a, b));
    }
    
}
