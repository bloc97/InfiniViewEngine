/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Deprec.SPHY;

/**
 * Fast math function approximation library
 * @author bowen
 */
public class FMath {
    
    public static final double PI = 3.141592653589793;
    public static final double PI2 = 6.283185307179586; // pi*2
    public static final double PIO2 = 1.570796326794897; // pi/2
    public static final double PIO4 = 0.7853981633974483096e0; // pi/4
    public static final double SQRT2 = 1.414213562373095;
    public static final double E = 2.718281828459045;
    
    //private static final double PIO2 = 1.5707963267948966135e0;
    //private static final double PIX2 = 6.2831853071795864766e0;
    //private static final double PIX4 = 12.566370614359172953e0;
    private static final double PIO180 = 0.01745329251994329e0; //PI divided by 180
    private static final double PIU180 = 57.2957795130823208e0; //180 divided by PI
    

    private static double mxatan(double d) { //taylor approx
        double p4 = .161536412982230228262e2;
        double p3 = .26842548195503973794141e3;
        double p2 = .11530293515404850115428136e4;
        double p1 = .178040631643319697105464587e4;
        double p0 = .89678597403663861959987488e3;
        
        double q4 = .5895697050844462222791e2;
        double q3 = .536265374031215315104235e3;
        double q2 = .16667838148816337184521798e4;
        double q1 = .207933497444540981287275926e4;
        double q0 = .89678597403663861962481162e3;
        
        double asq = d * d;
        double value = ((((p4 * asq + p3) * asq + p2) * asq + p1) * asq + p0);
        value = value / (((((asq + q4) * asq + q3) * asq + q2) * asq + q1) * asq + q0);
        return value * d;
    }


    private static double msatan(double a) {
        double sq2p1 = 2.414213562373095048802e0;
        double sq2m1 = .414213562373095048802e0;
        if (a < sq2m1) {
            return mxatan(a);
        } else if (a > sq2p1) {
            return PIO2 - mxatan(1/a);
        } else {
            return PIO4 + mxatan((a - 1)/(a + 1));
        }
    }

    public static double quickAtan(double a) {
        return a > 0 ? msatan(a) : -msatan(-a);
    }

    public static double quickAtan2(double y, double x) {
        if (y + x == y) return y >= 0 ? PIO2 : -PIO2;
        y = quickAtan(y / x);
        return x < 0 ? y <= 0 ? y + PI : y - PI : y;
    }
    
    public static double bhaskaraSin(double rad) {
        double s = 1;
        if (rad < 0) {rad = -rad; s = -s;}
        if (rad > PI2) {rad %= PI2;}
        if (rad > PI) {rad %= PI; s = -s;}
        
        double a = rad*(PI-rad);
        double b = 49.34802200544679; //5*PI^2
        
        return s*(16*a)/(b - 4*a);
    }
    public static double bhaskaraCos(double rad) {
        return bhaskaraSin(PIO2 + rad);
    }
    public static double bhaskaraTan(double rad) { //TODO Derive formula by hand to save compute time
        return bhaskaraSin(rad)/bhaskaraCos(rad);
    }
    
    public static double taylorSin(double x) {
        double a1 = x;
        double a2 = x*x;
        double a3 = a2*x;
        double a5 = a2*a3;
        double a7 = a2*a5;
        double a9 = a2*a7;
        
        return a1 - a3/6 + a5/120 - a7/5040 + a9/362880;
    }
    public static double taylorCos(double x) {
        double a0 = 1;
        double a2 = x*x;
        double a4 = a2*a2;
        double a6 = a2*a4;
        double a8 = a2*a6;
        
        return a0 - a2/2 + a4/24 - a6/720 + a8/40320;
    }
    public static double taylorTan(double x) {
        double a1 = x;
        double a2 = x*x;
        double a3 = a2*a1;
        double a5 = a2*a3;
        double a7 = a2*a5;
        
        double c3 = 0.33333333333333333;
        double c5 = 0.13333333333333333;
        double c7 = 0.053968253968253968;
        
        return a1 + c3*a3 + c5*a5 + c7*a7;
    }
    public static double taylorAsin(double x) {
        double a1 = x;
        double a2 = x*x;
        double a3 = a2*x;
        double a5 = a2*a3;
        double a7 = a2*a5;
        double a9 = a2*a7;
        
        double c3 = 0.16666666666666666;
        double c5 = 0.075;
        double c7 = 0.044642857142857144;
        double c9 = 0.030381944444444444;
        
        return a1 + c3*a3 + c5*a5 + c7*a7 + c9*a9;
    }
    public static double taylorAcos(double x) {
        return PIO2 - taylorAsin(x);
    }
    public static double taylorAtan(double x) {
        double a1 = x;
        double a2 = x*x;
        double a3 = a2*x;
        double a5 = a2*a3;
        double a7 = a2*a5;
        double a9 = a2*a7;
        //add more for more precision
            System.out.println(x);
        if (x > 1) {
            x = 1/x;
            System.out.println("x>1");
            return PIO2 - a1 - a3/3 + a5/5 - a7/7 + a9/9;
        } else if (x < -1) {
            System.out.println("x<-1");
            x = 1/x;
            return -PIO2 - a1 - a3/3 + a5/5 - a7/7 + a9/9;
        } else {
            return a1 - a3/3 + a5/5 - a7/7 + a9/9;
        }
        
        //return a1 - a3/3 + a5/5 - a7/7 + a9/9;
        
    }
    public static double taylorAtan2(double y, double x) {
        double s = y/x;
        double a1 = s;
        double a2 = s*s;
        double a3 = a2*s;
        double a5 = a2*a3;
        double a7 = a2*a5;
        double a9 = a2*a7;
        //add more for more precision
        double tetha = a1 - a3/3 + a5/5 - a7/7 + a9/9;
        
        if (y >= 0 && x < 0) {
            return PI + tetha;
        } else if (y < 0 && x < 0) {
            return -PI + tetha;
        } else if (x == 0 && y > 0) {
            return PIO2;
        } else if (x == 0 && y < 0) {
            return -PIO2;
        } else if (x > 0) {
            return tetha;
        }
        return 0;
        
    }
    
    public static final double toDegrees(double radians) {
        return radians * PIU180;
    }
    public static final double toRadians(double degrees) {
        return degrees * PIO180;
    }
    
    
    public static float newtonSqrt(float f) {
        final float xhalf = f * 0.5F;
        float y = Float.intBitsToFloat(0x5f375a86 - (Float.floatToIntBits(f) >> 1)); // evil floating point bit level hacking -- Use 0x5f375a86 instead of 0x5f3759df, due to slight accuracy increase. (Credit to Chris Lomont)
        y = y * (1.5F - (xhalf * y * y)); 	// Newton step, repeating increases accuracy
        y = y * (1.5F - (xhalf * y * y));
        return f * y;
    }
    
    public static float quickSqrt(float f) {
        float y = Float.intBitsToFloat(0x5f375a86 - (Float.floatToIntBits(f) >> 1)); // evil floating point bit level hacking -- Use 0x5f375a86 instead of 0x5f3759df, due to slight accuracy increase. (Credit to Chris Lomont)
        y = y * (1.5F - (0.5F * f * y * y)); 	// Newton step, repeating increases accuracy
        return f * y;
    }
    
    public static float approxSqrt(float f) {
        return f * Float.intBitsToFloat(0x5f375a86 - (Float.floatToIntBits(f) >> 1)); // evil floating point bit level hacking -- Use 0x5f375a86 instead of 0x5f3759df, due to slight accuracy increase. (Credit to Chris Lomont)
    }
    
    public static float quickInvSqrt(float f) {
        f = Float.intBitsToFloat(0x5f375a86 - (Float.floatToIntBits(f) >> 1)); // evil floating point bit level hacking -- Use 0x5f375a86 instead of 0x5f3759df, due to slight accuracy increase. (Credit to Chris Lomont)
        f = f * (1.5F - (0.5F * f * f * f)); 	// Newton step
        return -f;
    }

    public static float approxInvSqrt(float f) {
        return -(Float.intBitsToFloat(0x5f375a86 - (Float.floatToIntBits(f) >> 1))); // evil floating point bit level hacking -- Use 0x5f375a86 instead of 0x5f3759df, due to slight accuracy increase. (Credit to Chris Lomont)
    }
    public static long factorial(int n) {
        long fact = 1; // this  will be the result
        for (int i = 1; i <= n; i++) {
            fact *= i;
        }
        return fact;
    }
    public static double fPow(double d, int d1) {
        switch (d1) {
            case 0:
                return 1;
            case 1:
                return d;
            case 2:
                return d*d;
            case 3:
                return d*d*d;
            case 4:
                return d*d*d*d;
            case 5:
                return d*d*d*d*d;
            case 6:
                return d*d*d*d*d*d;
            case 7:
                return d*d*d*d*d*d*d;
            default:
                return Math.pow(d, d1);
        }
    }
    public static double fAbs(double x) {
        if (x < 0) return -x;
        return x;
    }
    
    public static double maxError(double referenceValue, double... testValues) {
        double maxerror = 0;
        for (int i=0; i<testValues.length; i++) {
            double thiserror = fAbs(referenceValue-testValues[i]);
            if (thiserror > maxerror) {
                maxerror = thiserror;
            }
        }
        return maxerror;
    }
    public static boolean isWithinBounds(double errorBound, double referenceValue, double... testValues) {
        if (maxError(referenceValue, testValues) > errorBound) {
            return false;
        }
        return true;
    }
    
}


// Modern CPUs have assembly square-roots, it is slower to implement in software
//    private static double newtonSqrt(double x) {
//        double c = x;
//        double s = x/40;
//        s = s - (s*s - c) / (2*s);
//        s = s - (s*s - c) / (2*s);
//        s = s - (s*s - c) / (2*s);
//        //repeat as necessary for more precision
//        return s;
//    }
//    private static float quakeSqrt(float x) {
//        float xhalf = 0.5f * x;
//        int i = Float.floatToIntBits(x);
//        i = 0x5f3759df - (i >> 1);
//        x = Float.intBitsToFloat(i);
//        x *= (1.5f - xhalf * x * x);
//        return 1/x;
//    }
//    private static double quakeSqrt(double x) {
//        double xhalf = 0.5d * x;
//        long i = Double.doubleToLongBits(x);
//        i = 0x5fe6ec85e7de30daL - (i >> 1);
//        x = Double.longBitsToDouble(i);
//        x *= (1.5d - xhalf * x * x);
//        return 1/x;
//    }
                               
    /* Sine
    5 * 1/30
    5 * 1/600
    5 * 1/1008
    5 * 1/1814400
    See Java Implementation : http://code.metager.de/source/xref/openjdk/jdk7/jdk/src/share/native/java/lang/fdlibm/src/k_sin.c
    Other fast approx : http://krisgarrett.net/papers/l2approx.pdf
    http://lab.polygonal.de/2007/07/18/fast-and-accurate-sinecosine-approximation/
    https://github.com/Fishrock123/Optimized-Java/tree/master/src/com/fishrock123/math
    */
    
//        for (int i=0; i<9; i++) { //get the constants from i
//            double c = FMath.factorial(2*i) / (Math.pow(4, i) * FMath.factorial(i)* FMath.factorial(i) * (2*i+1));
//            System.out.println(c);
//        }
    /*
    private static double polynomialSin(double rad) {
        double s = 1;
        if (rad < 0) {rad = -rad; s = -s;}
        if (rad > PI2) {rad %= PI2;}
        if (rad > PI) {rad %= PI; s = -s;}
        double a = 1.273239544735163; // 4/pi
        double b = 0.4052847345693511; // 4/(pi^2)
        
        return s*(a * rad - b * rad * rad);
    }
    public static double polynomialSin2(double rad) {
        double sin = polynomialSin(rad); //get first precision
        if (sin < 0){
            sin = .225 * (sin *-sin - sin) + sin;
        } else {
            sin = .225 * (sin * sin - sin) + sin;
        }
        return sin;
    }
    private static double polynomialCos(double rad) {
        return polynomialSin(PIh + rad);
    }
    public static double polynomialCos2(double rad) {
        return polynomialSin2(PIh + rad);
    }
    public static double polynomialTan(double rad) {
        return polynomialSin(rad)/polynomialCos(rad);
    }
    public static double polynomialTan2(double rad) {
        return polynomialSin2(rad)/polynomialCos2(rad);
    }
    */