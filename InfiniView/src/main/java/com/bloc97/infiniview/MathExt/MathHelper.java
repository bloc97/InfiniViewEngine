/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bloc97.infiniview.MathExt;

import com.bloc97.uvector.Vector2;
import com.bloc97.uvector.Vectors;

/**
 *
 * @author bowen
 */
public class MathHelper {
    
    public static Vector2 lerp(Vector2 v0, Vector2 v1, double t) {
        return Vectors.mulElem(Vectors.sub(v1, v0), t).add(v0);
    }
}
