/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bloc97.infinisim.Collision;

import com.bloc97.infinisim.Spatial;
import com.bloc97.uvector.Vector;
import com.bloc97.uvector.Vectors;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 *
 * @author bowen
 */
public class CollisionSimulation {
    
    private static final double THRESHOLD = 1d;
    
    private boolean isEnabled = false;
    private Set<Spatial> bodies = new LinkedHashSet<>();
    
    private double secondsPerTick;
    
    private long ticks = 0;
    
    public void step(double secondsPerTick) {
        if (!isEnabled) {
            return;
        }
        
        LinkedList<Spatial> toRemove = new LinkedList<>();
        
        int n = 0;
        for (Spatial thisBody : bodies) {
            if (toRemove.contains(thisBody)) { //Skip bodies that will be deleted
                n++;
                continue;
            }
            
            int i=0;
            for (Spatial otherBody : bodies) {
                if (i <= n || thisBody == otherBody) { //Skip bodies that were already checked for collision
                    i++;
                    continue;
                }
                
                if (doCollide(thisBody, otherBody, secondsPerTick, true)) { //If both particles are touching
                    
                    toRemove.add(otherBody);
                    Helpers.merge(thisBody, otherBody);
                    
                }
                
                i++;
            }
            
            
            n++;
        }
        
        for (Spatial removeBody : toRemove) {
            bodies.remove(removeBody);
        }
        
        
        ticks++;
    }
    
    public boolean doCollide(Spatial obj, Spatial obj2, double secondsPerTick, boolean fastMode) {
        Vector relativePos = Vectors.sub(obj.position(), obj2.position());
        double currentDistanceSqr = relativePos.normSqr();
        
        double effectiveRadius = obj.getRadius() + obj2.getRadius();
        effectiveRadius *= THRESHOLD;
        double effectiveRadiusSqr = effectiveRadius * effectiveRadius;
        
        if (currentDistanceSqr <= effectiveRadiusSqr) {
            return true;
        }
        if (fastMode) {
            return false;
        }
        
        Vector relativeVel = Vectors.sub(obj.velocity(), obj2.velocity());
        
        Vector newPosDt = Vectors.add(relativePos, relativeVel.copy().normalise());
        double newDistanceSqr = newPosDt.normSqr();
        
        if (newDistanceSqr > currentDistanceSqr) {
            return false;
        }
        
        double maxDistanceTravelSqr = Vectors.mulElem(relativeVel, secondsPerTick).normSqr();
        
        if (maxDistanceTravelSqr + effectiveRadiusSqr < currentDistanceSqr) {
            return false;
        }
        
        double timeStep = effectiveRadiusSqr / relativeVel.normSqr();
        
        if (timeStep < 100) timeStep = 100;
        
        double time = timeStep;
        while (time < secondsPerTick) {
            if (Vectors.mulElem(relativeVel, time).add(relativePos).normSqr() <= effectiveRadiusSqr) {
                return true;
            }
            time += timeStep;
        }
        return false;
        
    }
}
