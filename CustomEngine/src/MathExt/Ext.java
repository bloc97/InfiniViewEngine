/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MathExt;

import jdk.nashorn.internal.objects.Global;

/**
 *
 * @author bowen
 */
public class Ext {
    private static double g = 7;
    private static double[] C = {0.99999999999980993, 676.5203681218851, -1259.1392167224028,771.32342877765313, -176.61502916214059, 12.507343278686905, -0.13857109526572012, 9.9843695780195716e-6, 1.5056327351493116e-7};
    
    private static final double PIO180 = 0.01745329251994329e0; //PI divided by 180
    private static final double PIU180 = 57.2957795130823208e0; //180 divided by PI
    
    public static long factorial(long n) {
        if (n < 0) {
            throw new IllegalArgumentException("Factorial out of bounds, must be positive.");
        } else if (n > 20) {
            throw new IllegalArgumentException("Factorial out of bounds, must be smaller than 21");
        }
        long fact = 1; // this  will be the result
        for (long i = 1; i <= n; i++) {
            fact *= i;
        }
        return fact;
    }
    public static int factorial(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Factorial out of bounds, must be positive.");
        } else if (n > 12) {
            throw new IllegalArgumentException("Factorial out of bounds, must be smaller than 13");
        }
        int fact = 1; // this  will be the result
        for (int i = 1; i <= n; i++) {
            fact *= i;
        }
        return fact;
    }
    public static double factorial(double n) {
        if (n < 0) {
            throw new IllegalArgumentException("Factorial out of bounds, must be positive.");
        }
        return gamma(n + 1);
    }
    public static String hugeFactorial(double n) {
        if (n < 142) {
            return factorial(n) + "";
        }
        final double expb10 = log10Factorial(n);
        final double significand = (n >= 0) ? Math.pow(10, expb10%1) : Math.pow(10, expb10%1+1);
        final long base10 = (long)Math.floor(expb10);
        return significand + ((base10 != 0) ? "E" + base10 : "");
    }
    public static double logFactorial(double n) {
        if (n < 0) {
            throw new IllegalArgumentException("Factorial out of bounds, must be positive.");
        } 
        return (Math.log(n) - 1) * n + Math.log(Math.sqrt(2*Math.PI*n));
    }
    public static double log10Factorial(double n) {
        if (n < 0) {
            throw new IllegalArgumentException("Factorial out of bounds, must be positive.");
        } 
        double base = n/Math.E;
        return Math.log10(base) * n + Math.log10(Math.sqrt(2*Math.PI*n));
    }
    private static double gamma(double z) {
        if (z > 150) {
            return Global.Infinity;
        }
        
        if (z < 0.5) { 
            return Math.PI / (Math.sin(Math.PI * z) * gamma(1 - z));
        } else {
            z -= 1;

            double x = C[0];
            for (int i = 1; i < g + 2; i++)
            x += C[i] / (z + i);

            double t = z + g + 0.5;
            return Math.sqrt(2 * Math.PI) * Math.pow(t, (z + 0.5)) * Math.exp(-t) * x;
        }
    }
    public static String hugePow(double base, double exp) {
        final double expb10 = Math.log10(Math.abs(base)) * exp;
        final double significand = (exp >= 0) ? Math.pow(10, expb10%1) : Math.pow(10, expb10%1+1);
        final long base10 = (long)Math.floor(expb10);
        String sign = "";
        if (base < 0) {
            if (Math.floor(exp) == exp) {
                sign = (exp%2 == 0) ? "" : "-" ;
            } else {
                throw new IllegalArgumentException("Result of power is a complex number.");
                //return "-" + significand + "E" + base10 + " i";
            }
        }
        return sign + significand + ((base10 != 0) ? "E" + base10 : "");
    }
    
    public static final double toDegrees(double radians) {
        return radians * PIU180;
    }
    public static final double toRadians(double degrees) {
        return degrees * PIO180;
    }
    public static final double log(double d, double b) {
        return Math.log(d)/Math.log(b);
    }
    public static final double log(double d, int b) {
        return Math.log(d)/Math.log(b);
    }
}
