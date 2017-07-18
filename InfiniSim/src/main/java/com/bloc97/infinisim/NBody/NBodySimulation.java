/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bloc97.infinisim.NBody;

import com.bloc97.infinisim.Spatial;
import com.bloc97.infinisim.Simulation;
import java.util.AbstractSet;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 *
 * @author bowen
 */
public class NBodySimulation implements Simulation {
    
    protected Set<Spatial> bodies = new LinkedHashSet<>();
    
    protected Equations.EquationType equationType;
    protected Optimisers.OptimiserType optimiserType;
    protected Integrators.IntegratorType integratorType;
    
    protected double updatesPerSecond; //How many "Big steps" per second
    protected int ticksPerUpdate; //How many "Small steps" per update
    protected double secondsPerTick; //How many "Simulated seconds" per tick
    
    protected final double initialUpdatesPerSecond;
    protected final int initialTicksPerUpdate;
    protected final double initialSecondsPerTick;
    
    protected long ticks = 0;
    protected Date date;
    
    private boolean isEnabled = true;

    public NBodySimulation(Equations.EquationType equationType, Optimisers.OptimiserType optimiserType, Integrators.IntegratorType integratorType) {
        this(equationType, optimiserType, integratorType, 0, 0, 0, new Date(0));
    }
    
    public NBodySimulation(Equations.EquationType equationType, Optimisers.OptimiserType optimiserType, Integrators.IntegratorType integratorType, double updatesPerSecond, int ticksPerUpdate, double secondsPerTick, Date date) {
        this.bodies = bodies = new LinkedHashSet<>();
        
        this.equationType = equationType;
        this.optimiserType = optimiserType;
        this.integratorType = integratorType;
        
        this.initialUpdatesPerSecond = updatesPerSecond;
        this.initialTicksPerUpdate = ticksPerUpdate;
        this.initialSecondsPerTick = secondsPerTick;
        
        this.updatesPerSecond = this.initialUpdatesPerSecond;
        this.ticksPerUpdate = this.initialTicksPerUpdate;
        this.secondsPerTick = this.initialSecondsPerTick;
        
        this.date = date;
    }
    
    @Override
    public void step() {
        step(1);
    }
    
    @Override
    public void step(int ticks) {
        if (!isEnabled || bodies.isEmpty()) {
            return;
        }
        this.ticks += ticks;
        long newTime = date.getTime() + (long)(1000 * ticks * secondsPerTick);
        date = new Date(newTime);
        
        for (int i=0; i<ticks; i++) {
            Integrators.integrate(integratorType, optimiserType, equationType, bodies, secondsPerTick);
        }
        
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
        bodies = set;
        
        
        return oldSet;
    }
    
    
    @Override
    public int getObjectsNumber() {
        return bodies.size();
    }

    @Override
    public long getTicks() {
        return ticks;
    }


    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public void setDate(Date date) {
        this.date = new Date(date.getTime());
    }
    
    @Override
    public double getUpdatesPerSecond() {
        return updatesPerSecond;
    }

    @Override
    public void setUpdatesPerSecond(double updatesPerSecond) {
        this.updatesPerSecond = updatesPerSecond;
    }

    @Override
    public int getTicksPerUpdate() {
        return ticksPerUpdate;
    }

    @Override
    public void setTicksPerUpdate(int ticksPerUpdate) {
        this.ticksPerUpdate = ticksPerUpdate;
    }

    @Override
    public double getSimulatedSecondsPerTick() {
        return secondsPerTick;
    }

    @Override
    public void setSimulatedSecondsPerTick(double secondsPerTick) {
        this.secondsPerTick = secondsPerTick;
    }

    @Override
    public void addObject(Object object) {
        if (object instanceof Spatial) {
            this.bodies.add((Spatial) object);
        }
    }

    @Override
    public boolean removeObject(Object object) {
        return this.bodies.remove(object);
    }

    @Override
    public void clearObjects() {
        this.bodies.clear();
    }

    

}
