/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bloc97.infinisim.Collision;

import com.bloc97.infinisim.Spatial;
import com.bloc97.infinisim.Simulation;
import com.bloc97.uvector.Vector;
import com.bloc97.uvector.Vectors;
import java.util.AbstractMap;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author bowen
 */
public class CollisionSimulation implements Simulation {
    
    private static final double THRESHOLD = 1d;
    
    private boolean isEnabled = false;
    private Set<Spatial> bodies = new LinkedHashSet<>();
    
    private double secondsPerTick;
    
    private long ticks = 0;
    
    @Override
    public long getTicks() {
        return ticks;
    }

    @Override
    public void step() {
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
                
                if (doCollide(thisBody, otherBody, true)) { //If both particles are touching
                    
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
    
    public boolean doCollide(Spatial obj, Spatial obj2, boolean fastMode) {
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
        
        double maxDistanceTravelSqr = Vectors.mulElem(relativeVel, getSimulatedSecondsPerTick()).normSqr();
        
        if (maxDistanceTravelSqr + effectiveRadiusSqr < currentDistanceSqr) {
            return false;
        }
        
        double timeStep = effectiveRadiusSqr / relativeVel.normSqr();
        
        if (timeStep < 100) timeStep = 100;
        
        double time = timeStep;
        while (time < getSimulatedSecondsPerTick()) {
            if (Vectors.mulElem(relativeVel, time).add(relativePos).normSqr() <= effectiveRadiusSqr) {
                return true;
            }
            time += timeStep;
        }
        return false;
        
    }

    @Override
    public void step(int ticks) {
        step();
    }

    @Override
    public Date getDate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setDate(Date date) {
        
    }

    @Override
    public double getUpdatesPerSecond() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setUpdatesPerSecond(double ups) {
        
    }

    @Override
    public int getTicksPerUpdate() {
        return 1;
    }

    @Override
    public void setTicksPerUpdate(int ticksPerUpdate) {
        
    }

    @Override
    public double getSimulatedSecondsPerTick() {
        return secondsPerTick;
    }

    @Override
    public void setSimulatedSecondsPerTick(double simulatedSecondsPerTick) {
        this.secondsPerTick = simulatedSecondsPerTick;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
    
    @Override
    public void enable() {
        isEnabled = true;
    }

    @Override
    public void disable() {
        isEnabled = false;
    }

    @Override
    public void toggle() {
        isEnabled = !isEnabled;
    }

    @Override
    public Set getObjects() {
        return bodies;
    }

    @Override
    public Set setObjects(Set set) {
        Set oldSet = bodies;
        this.bodies = set;
        return oldSet;
    }

    @Override
    public int getObjectsNumber() {
        return bodies.size();
    }

    @Override
    public void clearObjects() {
        this.bodies.clear();
    }

    @Override
    public void addObject(Object object) {
        if (object instanceof Spatial) {
            this.bodies.add((Spatial) object);
        }
    }

    @Override
    public boolean removeObject(Object object) {
        if (object instanceof Spatial) {
            return this.bodies.remove((Spatial) object);
        } else {
            return false;
        }
    }
    
}
