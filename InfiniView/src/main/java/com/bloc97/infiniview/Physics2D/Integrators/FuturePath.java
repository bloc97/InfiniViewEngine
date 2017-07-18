/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bloc97.infiniview.Physics2D.Integrators;

import com.bloc97.uvector.Vector2;

/**
 *
 * @author bowen
 */
public interface FuturePath {
    public void setOrbitPath(Vector2[] paths, Vector2[] vels, long[] timeStamps);
    
    //public void setOrbitReferencePath(Vector2[] paths);
    //public void setCurrentDate(Date date);
}
