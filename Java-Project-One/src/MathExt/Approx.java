/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MathExt;


/**
 *
 * @author bowen
 */
public abstract class Approx {
    
    public static final double E =     2.718281828459045;
    public static final double SQRT2 = 1.414213562373095;
    
    public static final double PI =     3.141592653589793;
    public static final double PI2 = PI*2; // pi*2
    public static final double PIO2 = PI/2; // pi/2
    
    public static double sin(double d) {
        return cos(PIO2-d);
    }
    public static double cos(double d) {
        int quad; // what quadrant are we in?
        if (d < 0) d = -d;
        if (d > PI2) d %= PI2;
        
        quad=(int)(d/PIO2); // Get quadrant # (0 to 3) 
        switch (quad){
            case 0: return mxcos(d);
            case 1: return -mxcos(PI-d);
            case 2: return -mxcos(d-PI);
            case 3: return mxcos(PI2-d);
            default: return 0;
        }
        
    }
    private static double mxcos(double x) {
        final double c1 = 0.99940307;
        final double c2 =-0.49558072;
        final double c3 = 0.03679168;
        final double x2 = x * x;
        return (c1 + x2*(c2 + c3 * x2));
    }
    
    public static double sqrt(double d) {
        final double xhalf = d * 0.5F;
        double y = Double.longBitsToDouble(0x5fe6ec85e7de30daL - (Double.doubleToLongBits(d) >> 1));
        return d * y;
    }
    public static float sqrt(float f) {
        final float xhalf = f * 0.5F;
        float y = Float.intBitsToFloat(0x5f375a86 - (Float.floatToIntBits(f) >> 1));
        return f * y;
    }
    
    public static double invSqrt(double d) {
        double xhalf = d * 0.5D;
        long i = Double.doubleToLongBits(d);
        i = 0x5fe6ec85e7de30daL - (i >> 1);
        d = Double.longBitsToDouble(i);
        return d;
    }
    public static float invSqrt(float f) {
        f = Float.intBitsToFloat(0x5f375a86 - (Float.floatToIntBits(f) >> 1)); 
        return -f;
    }
}
