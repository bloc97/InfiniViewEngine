/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bloc97.infinisim.NBody;

import com.bloc97.infinisim.OpenCL.OpenCLIntegrators;
import com.bloc97.infinisim.Spatial;
import com.bloc97.infinisim.Simulation;
import java.util.AbstractSet;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author bowen
 */
public class NBodySimulation implements Simulation {
    
    private Set<Spatial> bodies = new LinkedHashSet<>();
    
    private final Equations.EquationType equationType;
    private final Optimisers.OptimiserType optimiserType;
    private final Integrators.IntegratorType integratorType;
    
    private volatile int ticksPerUpdate; //How many "Small steps" per update
    private volatile double secondsPerTick; //How many "Simulated seconds" per tick
    
    private final int initialTicksPerUpdate;
    private final double initialSecondsPerTick;
    
    
    private volatile long ticks = 0;
    private final Date date;
    
    private volatile boolean isEnabled = true;
    
    private volatile boolean useOpenCL;
    
    
    public NBodySimulation(Equations.EquationType equationType, Optimisers.OptimiserType optimiserType, Integrators.IntegratorType integratorType, double secondsPerTick, int ticksPerUpdate, Date date, boolean useOpenCL) {
        this.bodies = bodies = new LinkedHashSet<>();
        
        this.equationType = equationType;
        this.optimiserType = optimiserType;
        this.integratorType = integratorType;
        
        this.initialTicksPerUpdate = ticksPerUpdate;
        this.initialSecondsPerTick = secondsPerTick;
        
        this.ticksPerUpdate = ticksPerUpdate;
        this.secondsPerTick = secondsPerTick;
        
        this.date = date;
        
        this.useOpenCL = useOpenCL;
        
    }
    
    @Override
    public void update() {
        if (!isEnabled || bodies.isEmpty()) {
            return;
        }
        final double t = secondsPerTick;
        final int n = ticksPerUpdate;
        if (useOpenCL) {
            OpenCLIntegrators.integrate(integratorType, optimiserType, equationType, bodies, t, n);
        } else {
            for (int i=0; i<n; i++) {
                Integrators.integrate(integratorType, optimiserType, equationType, bodies, t);
            }
        }
        date.setTime(date.getTime() + (long)(t * n * 1000));
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
    public List getObjectsSnapshot() {
        List<Spatial> list = new LinkedList<>();
        for (Spatial body : bodies) {
            list.add(body);
        }
        return list;
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
    public void setObjects(Set set) {
        bodies = set;
    }

    @Override
    public Date getDate() {
        return date;
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
    public void resetInitialSettings() {
        this.ticksPerUpdate = initialTicksPerUpdate;
        this.secondsPerTick = initialSecondsPerTick;
    }
    
}
