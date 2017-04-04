/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MathExt;
//SEE APPROX PDF
//http://nnp.ucsd.edu/phy120b/student_dirs/shared/gnarly/misc_docs/trig_approximations.pdf
/**
 *
 * @author bowen
 */
public abstract class Fast {
    
    public static final double E =     2.71828182845904523536028747135266249775724709369995957496696763;
    public static final double SQRT2 = 1.41421356237309504880168872420969807856967187537694807317667974;
    public static final double SQRT3 = 1.73205080756887729352744634150587236694280525381038062805580698;
    public static final double SQRT5 = 2.23606797749978969640917366873127623544061835961152572427089725;
    
    public static final double PI =    3.14159265358979323846264338327950288419716939937510582097494459;
    public static final double PI2 = PI*2; // pi*2
    public static final double PI4 = PI*4; // pi*4
    public static final double PIO2 = PI/2; // pi/2
    public static final double PIO4 = PI/4; // pi/4
    public static final double PI32 = (3*PI/2); // 3/2 pi
    
    //1.27323954  = 4/pi
    //0.405284735 = 4/(pi^2)
    public static final double PIU4 = 4/PI;
    public static final double PIS2 = PI * PI;
    public static final double PIS2U4 = 4/PIS2;
    
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
        final double c1 = 0.999999953464;
        final double c2 =-0.4999999053455;
        final double c3 = 0.0416635846769;
        final double c4 =-0.0013853704264;
        final double c5 = 0.00002315393167;
        final double x2 = x * x;
        return (c1 + x2*(c2 + x2*(c3 + x2*(c4 + c5*x2))));
    }

    
    
    public static double tan(double d){
        int octant; // what octant are we in?
        if (d < 0) d = d + (-2*d);
        if (d > PI2) d %= PI2;
        octant=(int)(d/PIO4); // Get octant # (0 to 7)
        switch (octant){
            case 0: return mxtan(d *PIU4);
            case 1: return 1.0/mxtan((PIO2-d) *PIU4);
            case 2: return -1.0/mxtan((d-PIO2) *PIU4);
            case 3: return - mxtan((PI-d) *PIU4);
            case 4: return mxtan((d-PI) *PIU4);
            case 5: return 1.0/mxtan((PI32-d)*PIU4);
            case 6: return -1.0/mxtan((d-PI32)*PIU4);
            case 7: return - mxtan((PI2-d) *PIU4);
            default: return 0;
        }
    }
    private static double mxtan(double x) {
        final double c1= 211.849369664121;
        final double c2=- 12.5288887278448 ;
        final double c3= 269.7350131214121;
        final double c4=- 71.4145309347748;
        double x2; // The input argument squared
        x2=x * x;
        return (x*(c1 + c2 * x2)/(c3 + x2*(c4 + x2)));
    }
    
    private static double mxatan(double d) { //taylor approx
        final double p4 = .161536412982230228262e2;
        final double p3 = .26842548195503973794141e3;
        final double p2 = .11530293515404850115428136e4;
        final double p1 = .178040631643319697105464587e4;
        final double p0 = .89678597403663861959987488e3;
        
        final double q4 = .5895697050844462222791e2;
        final double q3 = .536265374031215315104235e3;
        final double q2 = .16667838148816337184521798e4;
        final double q1 = .207933497444540981287275926e4;
        final double q0 = .89678597403663861962481162e3;
        
        final double asq = d * d;
        double value = ((((p4 * asq + p3) * asq + p2) * asq + p1) * asq + p0);
        value = value / (((((asq + q4) * asq + q3) * asq + q2) * asq + q1) * asq + q0);
        return value * d;
    }
    private static double msatan(double a) {
        final double sq2p1 = 2.414213562373095048802e0;
        final double sq2m1 = .414213562373095048802e0;
        if (a < sq2m1) {
            return mxatan(a);
        } else if (a > sq2p1) {
            return PIO2 - mxatan(1/a);
        } else {
            return PIO4 + mxatan((a - 1)/(a + 1));
        }
    }
    
    public static double atan(double d) {
        return d > 0 ? msatan(d) : -msatan(-d);
    }
    public static double atan2(double y, double x) {
        if (y + x == y) return y >= 0 ? PIO2 : -PIO2;
        y = atan(y / x);
        return x < 0 ? y <= 0 ? y + PI : y - PI : y;
    }
    
    public static int pow(int base, int exp) {
        int result = 1;
        while (exp != 0)
        {
            if ((exp & 1) == 1)
                result *= base;
            exp >>= 1;
            base *= base;
        }

        return result;
    }
    public static long pow(long base, int exp) {
        long result = 1;
        while (exp != 0)
        {
            if ((exp & 1) == 1)
                result *= base;
            exp >>= 1;
            base *= base;
        }

        return result;
    }
    public static long pow(long base, long exp) {
        long result = 1;
        while (exp != 0)
        {
            if ((exp & 1) == 1)
                result *= base;
            exp >>= 1;
            base *= base;
        }

        return result;
    }
    
    public static double sqrt(double d) {
        final double xhalf = d * 0.5F;
        double y = Double.longBitsToDouble(0x5fe6ec85e7de30daL - (Double.doubleToLongBits(d) >> 1)); // evil floating point bit level hacking -- Use 0x5f375a86 instead of 0x5f3759df, due to slight accuracy increase. (Credit to Chris Lomont)
        y = y * (1.5F - (xhalf * y * y)); 	// Newton step, repeating increases accuracy
        y = y * (1.5F - (xhalf * y * y));
        return d * y;
    }
    public static float sqrt(float f) {
        final float xhalf = f * 0.5F;
        float y = Float.intBitsToFloat(0x5f375a86 - (Float.floatToIntBits(f) >> 1)); // evil floating point bit level hacking -- Use 0x5f375a86 instead of 0x5f3759df, due to slight accuracy increase. (Credit to Chris Lomont)
        y = y * (1.5F - (xhalf * y * y)); 	// Newton step, repeating increases accuracy
        y = y * (1.5F - (xhalf * y * y));
        return f * y;
    }
    
    public static double invSqrt(double d) {
        double xhalf = d * 0.5D;
        long i = Double.doubleToLongBits(d);
        i = 0x5fe6ec85e7de30daL - (i >> 1);
        d = Double.longBitsToDouble(i);
        d *= (1.5d - xhalf * d * d);
        return d;
    }
    public static float invSqrt(float f) {
        f = Float.intBitsToFloat(0x5f375a86 - (Float.floatToIntBits(f) >> 1)); // evil floating point bit level hacking -- Use 0x5f375a86 instead of 0x5f3759df, due to slight accuracy increase. (Credit to Chris Lomont)
        f = f * (1.5F - (0.5F * f * f * f)); 	// Newton step
        return -f;
    }
    
    private static double maxError(double referenceValue, double... testValues) {
        double maxerror = 0;
        for (int i=0; i<testValues.length; i++) {
            double thiserror = Math.abs(referenceValue-testValues[i]);
            if (thiserror > maxerror) {
                maxerror = thiserror;
            }
        }
        return maxerror;
    }
    private static boolean isWithinBounds(double errorBound, double referenceValue, double... testValues) {
        if (maxError(referenceValue, testValues) > errorBound) {
            return false;
        }
        return true;
    }
    

    
}
