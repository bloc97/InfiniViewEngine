/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bloc97.infinisim.Aparapi;

/**
 *
 * @author bowen
 */
public class FP64Emulation {
   public static final long[] POW_10_LONG = new long[100];
   public static volatile boolean isInit = false;

   public static void generatePow10Long() {
      POW_10_LONG[0] = 1;
      for (int i = 1; i < 100; i++) {
         if (i < 19) {
            POW_10_LONG[i] = 10 * POW_10_LONG[i - 1];
         } else {
            POW_10_LONG[i] = POW_10_LONG[18];
         }
      }
      isInit = true;
   }
   /**
    * Two digits are reserved for exponent [0..99] 
    */
   private static final int MIN_EXP = -49;

   /**
    * Used to split the double value to mantissa and exponent. 
    * The mantissa scaled to use maximal 17 digits. These are 
    * MANTISSA_DIGITS plus 1 for overflows in add operation
    */
   private static final long SPLIT_EXP = 100000000000000000L;

   /**
    * Used to split the mantissa to a higher and lower integer. 
    */
   private static final long SPLIT_INT = 100000000L;

   public static long multiplyPacked(long multiplicand, long multiplier) {

      final long md_mantissa = multiplicand - multiplicand / SPLIT_EXP * SPLIT_EXP;
      final long md_hi = md_mantissa / SPLIT_INT;
      final long md_lo = md_mantissa % SPLIT_INT;

      final long mr_mantissa = multiplier - multiplier / SPLIT_EXP * SPLIT_EXP;
      final long mr_hi = mr_mantissa / SPLIT_INT;
      final long mr_lo = mr_mantissa % SPLIT_INT;

      final long product_mantissa = md_hi * mr_hi + md_lo * mr_hi / SPLIT_INT + md_hi * mr_lo / SPLIT_INT;
      final long product_exponent = (multiplicand >> 63 | -multiplicand >>> 63) * (multiplicand / SPLIT_EXP)
            + (multiplier >> 63 | -multiplier >>> 63) * (multiplier / SPLIT_EXP) + 2 * MIN_EXP + 1;

      return (product_exponent - MIN_EXP) * SPLIT_EXP * (product_mantissa >> 63 | -product_mantissa >>> 63)
            + product_mantissa;
   }

   public static long addPacked(long augend, long addend) {
       
      if (!isInit) {
          generatePow10Long();
      }

      long augend_exponent = ((augend >> 63 | -augend >>> 63) * (augend / SPLIT_EXP) + MIN_EXP);
      long addend_exponent = ((addend >> 63 | -addend >>> 63) * (addend / SPLIT_EXP) + MIN_EXP);

      if (augend_exponent < addend_exponent) {

         // Swap values
         augend = augend ^ addend;
         addend = addend ^ augend;
         augend = augend ^ addend;

         final long value = augend / SPLIT_EXP;
         augend_exponent = (value * (value >> 63 | -value >>> 63)) + MIN_EXP;
         final long value1 = addend / SPLIT_EXP;
         addend_exponent = (value1 * (value1 >> 63 | -value1 >>> 63)) + MIN_EXP;

         final long addend_mantissa = addend - addend / SPLIT_EXP * SPLIT_EXP;
         final long augend_mantissa = augend - augend / SPLIT_EXP * SPLIT_EXP;
         final long sum_mantissa = augend_mantissa + addend_mantissa
                  / POW_10_LONG[(int) (augend_exponent - addend_exponent)];
         return (augend_exponent - MIN_EXP) * SPLIT_EXP * (sum_mantissa >> 63 | -sum_mantissa >>> 63) 
                  + sum_mantissa;
      } else {
         final long addend_mantissa = addend - addend / SPLIT_EXP * SPLIT_EXP;
         final long augend_mantissa = augend - augend / SPLIT_EXP * SPLIT_EXP;
         final long sum_mantissa = augend_mantissa + addend_mantissa
                  / POW_10_LONG[(int) (augend_exponent - addend_exponent)];
         return (augend_exponent - MIN_EXP) * SPLIT_EXP * (sum_mantissa >> 63 | -sum_mantissa >>> 63) 
                 + sum_mantissa;
      }
   }
}
