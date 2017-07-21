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
import java.util.Collection;
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
public class NBodySimulationK implements Simulation {
    
    
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
    
    //General Information
    double[] position, velocity, acceleration; //Stored as i,j,k
    double[] mass, radius;
    int[] collided;
    boolean[] isActive;
    
    int currentSize;
    
    private final NBodyKernel kernel;
    
    public NBodySimulationK(Equations.EquationType equationType, Optimisers.OptimiserType optimiserType, Integrators.IntegratorType integratorType, Collection<Spatial> list, int initialSize, double secondsPerTick, int ticksPerUpdate, Date date, boolean useOpenCL) {
        this.equationType = equationType;
        this.optimiserType = optimiserType;
        this.integratorType = integratorType;
        
        this.initialTicksPerUpdate = ticksPerUpdate;
        this.initialSecondsPerTick = secondsPerTick;
        
        this.ticksPerUpdate = ticksPerUpdate;
        this.secondsPerTick = secondsPerTick;
        
        this.date = date;
        
        this.useOpenCL = useOpenCL;
        
        initArrays(list, initialSize);
        
        this.kernel = new NBodyKernel();
        kernel.setArrays(position, velocity, mass, radius, collided, isActive);
    }
    
    public final void initArrays(Collection<Spatial> list, int initialSize) {
        
        int length = Math.max(list.size(), initialSize);

        position = new double[length * 3];
        velocity = new double[length * 3];
        acceleration = new double[length * 3];

        mass = new double[length];
        radius = new double[length];

        collided = new int[length];
        isActive = new boolean[length];

        int i=0;
        for (Spatial body : list) {

            position[i * 3] = body.position().get(0);
            position[i * 3 + 1] = body.position().get(1);
            position[i * 3 + 2] = body.position().get(2);

            velocity[i * 3] = body.velocity().get(0);
            velocity[i * 3 + 1] = body.velocity().get(1);
            velocity[i * 3 + 2] = body.velocity().get(2);

            mass[i] = body.getMass();
            radius[i] = body.getRadius();
            collided[i] = i;
            isActive[i] = true;

            i++;
        }
        currentSize = i;
    }
    
    @Override
    public void update() {
        if (!isEnabled || isActive.length <= 0) {
            return;
        }
        final double t = secondsPerTick;
        final int n = ticksPerUpdate;
        
        kernel.setTime(secondsPerTick);
        kernel.setTypes(optimiserType, equationType);
        
        for (int i=0; i<ticksPerUpdate; i++) {
            kernel.integrate();
            kernel.resolveCollisions();
        }
        kernel.refreshArrays();
        
        //Integrators.integrate(integratorType, optimiserType, equationType, bodies, t, n, useOpenCL);
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
