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
public class NBodySimulationK implements Runnable {
    
    
    private final Equations.EquationType equationType;
    private final Optimisers.OptimiserType optimiserType;
    
    //Runnable
    private double targetUpdatesPerSecond; //How many "Big steps" per second
    private double realUpdatesPerSecond = 0; //How many "Big steps" per were executed last time
    
    private long targetTime; //Target frame time
    private long realTime; //Real time that took the thread to run
    
    private volatile boolean isRunning = true;
    
    //Simulation
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
    
    
    public NBodySimulationK(Equations.EquationType equationType, Optimisers.OptimiserType optimiserType, Collection<Spatial> list, int initialSize, double updatesPerSecond, double secondsPerTick, int ticksPerUpdate, Date date, boolean useOpenCL) {
        this.equationType = equationType;
        this.optimiserType = optimiserType;
        
        this.targetUpdatesPerSecond = updatesPerSecond;
        targetTime = (long)(1000000000D/targetUpdatesPerSecond);
        realTime = targetTime;
        
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
    
    public boolean addObject(double x, double y, double z, double vx, double vy, double vz, double mass, double radius) {
        for (int i=0; i<this.mass.length; i++) {
            if (!isActive[i]) {
                position[i * 3] = x;
                position[i * 3 + 1] = y;
                position[i * 3 + 2] = z;
                velocity[i * 3] = vx;
                velocity[i * 3 + 1] = vy;
                velocity[i * 3 + 2] = vz;
                this.mass[i] = mass;
                this.radius[i] = radius;
                collided[i] = i;
                isActive[i] = true;
                return true;
            }
        }
        return false;
    }
    
    public boolean removeObject(int i) {
        if (i >= 0 && i < isActive.length) {
            if (!isActive[i]) {
                isActive[i] = false;
                return true;
            }
        }
        return false;
    }
    
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

    public boolean isEnabled() {
        return isEnabled;
    }
    
    public void enable() {
        isEnabled = true;
    }
    
    public void disable() {
        isEnabled = false;
    }
    
    public void toggle() {
        isEnabled = !isEnabled;
    }
    
    
    public int getTicksPerUpdate() {
        return ticksPerUpdate;
    }
    public void setTicksPerUpdate(int ticksPerUpdate) {
        this.ticksPerUpdate = ticksPerUpdate;
    }
    public double getSimulatedSecondsPerTick() {
        return secondsPerTick;
    }
    public void setSimulatedSecondsPerTick(double secondsPerTick) {
        this.secondsPerTick = secondsPerTick;
    }
    public void resetInitialSettings() {
        this.ticksPerUpdate = initialTicksPerUpdate;
        this.secondsPerTick = initialSecondsPerTick;
    }
    
    
    public void destroy() {
        isRunning = false;
    }
    
    @Override
    public void run() {
        
        long startTime;
        long endTime;
        long sleepTime;
        
        
        while (isRunning) {

            startTime = System.nanoTime();
            update();
            endTime = System.nanoTime();

            realTime = endTime-startTime;
            targetTime = (long)(1000000000D/targetUpdatesPerSecond);
            
            sleepTime = targetTime - realTime;

            if (sleepTime < 0) {
                //System.out.println("Simulation Thread Overload! Late by: " + -sleepTime/1000000 + " ms.");
            } else {

                long sleepms = Math.floorDiv(sleepTime, 1000000);
                int sleepns = (int)Math.floorMod(sleepTime, 1000000);
                try {
                    Thread.sleep(sleepms, sleepns);
                } catch (InterruptedException ex) {
                    System.out.println("Thread Error");
                }
            }
        }
    }
    
    
    public double getTargetUPS() {
        return targetUpdatesPerSecond;
    }
    
    public double getRealUPS() {
        if (realTime < targetTime) {
            return targetUpdatesPerSecond;
        }
        return 1000000000D/realTime;
    }
    
    public long getTargetTimeInMs() {
        return targetTime / 1000000;
    }
    
    public long getRealTimeInMs() {
        return realTime / 1000000;
    }
    
    public void setTargetUPS(double updatesPerSecond) {
        this.targetUpdatesPerSecond = updatesPerSecond;
    }
    
    
    
    
    
    
    
    
    
    
}
