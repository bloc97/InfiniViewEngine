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
    
    protected long ticks = 0;
    
    private boolean isEnabled = true;
    
    
    public NBodySimulation(Equations.EquationType equationType, Optimisers.OptimiserType optimiserType, Integrators.IntegratorType integratorType) {
        this.bodies = bodies = new LinkedHashSet<>();
        
        this.equationType = equationType;
        this.optimiserType = optimiserType;
        this.integratorType = integratorType;
    }
    
    @Override
    public void step(double seconds) {
        step(1, seconds);
    }
    
    @Override
    public void step(int ticks, double secondsPerTick) {
        if (!isEnabled || bodies.isEmpty()) {
            return;
        }
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

}
