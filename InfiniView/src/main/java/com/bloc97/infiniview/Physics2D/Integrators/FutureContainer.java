/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bloc97.infiniview.Physics2D.Integrators;

import com.bloc97.infiniview.MathExt.MathHelper;
import com.bloc97.uvector.Vector2;
import com.bloc97.uvector.Vectors;
import java.util.Date;

/**
 *
 * @author bowen
 */
public class FutureContainer implements FuturePath {
    
    private Vector2[] paths;
    private Vector2[] vels;
    private long[] timeStamps;
    
    
    public FutureContainer(Vector2[] paths, Vector2[] vels, long[] timeStamps) {
        setOrbitPath(paths, vels, timeStamps);
    }
    
    public Vector2 getPos(int i) {
        if (i >= paths.length || i < 0) {;
            throw new IllegalArgumentException("Path index out of bounds.");
        }
        return paths[i];
    }
    public Vector2 getPos(Date date) {
        long timeMs = date.getTime();
        return getPos(timeMs);
    }
    public Vector2 getPos(long timeMs) {
        int i = 0;
        while (i < timeStamps.length) {
            if (timeMs == timeStamps[i]) {
                return paths[i];
            } else if (timeMs < timeStamps[i]) {
                if (i > 0) {
                    double ratio = (timeMs-timeStamps[i-1]) / (timeStamps[i]-timeStamps[i-1]);
                    return MathHelper.lerp(paths[i-1], paths[i], ratio);
                    
                } else {
                    throw new IllegalStateException("PathContainer time below range.");
                }
            }
            i++;
            
        }
        throw new IllegalStateException("PathContainer time over range.");
    }
    
    public Vector2 getPos(int i, FutureContainer reference) {
        long timeMs = getTime(i);
        return getPos(timeMs, reference);
    }
    public Vector2 getPos(Date date, FutureContainer reference) {
        long timeMs = date.getTime();
        return getPos(timeMs, reference);
    }
    public Vector2 getPos(long timeMs, FutureContainer reference) {
        return Vectors.sub(getPos(timeMs), reference.getPos(timeMs));
    }
    
    
    public Vector2 getVel(int i) {
        if (i >= vels.length || i < 0) {
            throw new IllegalArgumentException("Velocity index out of bounds.");
        }
        return vels[i];
    }
    public long getTime(int i) {
        if (i >= timeStamps.length || i < 0) {
            throw new IllegalArgumentException("TimeStamp index out of bounds.");
        }
        return timeStamps[i];
    }
    
    @Override
    public final void setOrbitPath(Vector2[] paths, Vector2[] vels, long[] timeStamps) {
        if (paths.length == timeStamps.length && vels.length == timeStamps.length) {
            this.paths = paths;
            this.vels = vels;
            this.timeStamps = timeStamps;
        } else {
            throw new IllegalArgumentException("Path and time arrays different length.");
        }
    }
    
    public int length() {
        return paths.length;
    }
    
    public int length(FutureContainer reference) {
        long referenceMaxTime = reference.getTime(reference.length()-1);
        int maxIndex = 0;
        for (int i=0; i<paths.length; i++) {
            if (timeStamps[i] > referenceMaxTime) {
                break;
            }
            maxIndex = i;
        }
        return maxIndex;
    }

}
