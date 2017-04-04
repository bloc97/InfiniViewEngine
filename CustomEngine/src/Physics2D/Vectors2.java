/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Physics2D;

import MathExt.Algebra.Tensor;
import MathExt.Algebra.Tensors;

/**
 *
 * @author bowen
 */
public class Vectors2 {
    
    public static Vector2 tensorToVector2(Tensor tensor1) {
        return tensorToVector2(tensor1, true);
    }
    public static Vector2 tensorToVector2(Tensor tensor1, boolean isCartesian) {
        if (tensor1.dimensions() > 1) {
            throw new IndexOutOfBoundsException("Cannot convert 2rd or higher order tensor into vector2.");
        } else if (tensor1.length() != 2) {
            throw new IndexOutOfBoundsException("Cannot convert non-2D vector into vector2.");
        }
        return new Vector2(tensor1, isCartesian);
    }
    public static Vector2 shell(Vector2 vector2) {
        return new Vector2(vector2.getDimensionSize(1), vector2.isCartesian());
    }
    public static Vector2 neg(Vector2 vector2) {
        return new Vector2(Tensors.neg(vector2), vector2.isCartesian());
    }
    public static Vector2 add(Vector2... vector2Array) {
        return new Vector2(Tensors.add(vector2Array), vector2Array[0].isCartesian());
    }
    public static Vector2 add(Vector2 vector2, double c) {
        return new Vector2(Tensors.add(vector2, c), vector2.isCartesian());
    }
    public static Vector2 sub(Vector2... vector2Array) {
        return new Vector2(Tensors.sub(vector2Array), vector2Array[0].isCartesian());
    }
    public static Vector2 sub(Vector2 vector2, double c) {
        return new Vector2(Tensors.sub(vector2, c), vector2.isCartesian());
    }
    public static Vector2 prod(Vector2... vector2Array) {
        return new Vector2(Tensors.prod(vector2Array), vector2Array[0].isCartesian());
    }
    public static Vector2 prod(Vector2 vector2, double c) {
        return new Vector2(Tensors.prod(vector2, c), vector2.isCartesian());
    }
    public static Vector2 div(Vector2... vector2Array) {
        return new Vector2(Tensors.div(vector2Array), vector2Array[0].isCartesian());
    }
    public static Vector2 div(Vector2 vector2, double c) {
        return new Vector2(Tensors.div(vector2, c), vector2.isCartesian());
    }
    public static Vector2 proj(Vector2... vector2Array) {
        Vector2 newVector2 = vector2Array[0].clone();
        for (int n=1; n<vector2Array.length; n++) {
            newVector2.proj(vector2Array[n]);
        }
        return newVector2;
    }
    public static Vector2 rej(Vector2... vector2Array) {
        Vector2 newVector2 = vector2Array[0].clone();
        for (int n=1; n<vector2Array.length; n++) {
            newVector2.rej(vector2Array[n]);
        }
        return newVector2;
    }
    public static Vector2 zeroVector2() {
        return new Vector2(0);
    }
    public static Vector2 lerp(Vector2 v0, Vector2 v1, double t) {
        return prod(sub(v1, v0), t).add(v0);
    }
}
