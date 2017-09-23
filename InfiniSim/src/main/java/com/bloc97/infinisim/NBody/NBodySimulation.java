/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bloc97.infinisim.NBody;

import com.bloc97.infinisim.Spatial;
import com.bloc97.uvector.Vector3;
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author bowen
 */
public class NBodySimulation implements Runnable {
    
    
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
    
    private volatile boolean isEnabled = true;
    
    private volatile boolean useOpenCL;
    
    
    private final NBodyKernel kernel;

    private NBodyWorld simulatedWorld;
    
    public NBodySimulation(Equations.EquationType equationType, Optimisers.OptimiserType optimiserType, int initialSize, double updatesPerSecond, double secondsPerTick, int ticksPerUpdate, boolean useOpenCL) {
        this.equationType = equationType;
        this.optimiserType = optimiserType;
        
        this.targetUpdatesPerSecond = updatesPerSecond;
        targetTime = (long)(1000000000D/targetUpdatesPerSecond);
        realTime = targetTime;
        
        this.initialTicksPerUpdate = ticksPerUpdate;
        this.initialSecondsPerTick = secondsPerTick;
        
        this.ticksPerUpdate = ticksPerUpdate;
        this.secondsPerTick = secondsPerTick;
        
        this.useOpenCL = useOpenCL;
        
        this.kernel = new NBodyKernel();
    }
    
    public void setSimulatedWorld(NBodyWorld world) {
        this.simulatedWorld = world;
        if (world != null) {
            kernel.setArrays(world.getPosition(), world.getVelocity(), world.getMass(), world.getRadius(), world.getCollided(), world.getIsActive());
        }
    }

    public NBodyWorld getSimulatedWorld() {
        return simulatedWorld;
    }

    public NBodyKernel getKernel() {
        return kernel;
    }
    
    public void update() {
        if (!isEnabled || simulatedWorld == null) {
            return;
        }
        final double t = secondsPerTick;
        final int n = ticksPerUpdate;
        
        kernel.setTime(secondsPerTick);
        kernel.setTypes(optimiserType, equationType);
        
        for (int i=0; i<ticksPerUpdate; i++) {
            kernel.integrate();
            //kernel.resolveCollisions();
        }
        kernel.resolveCollisions();
        //kernel.refreshArrays();
        
        //Integrators.integrate(integratorType, optimiserType, equationType, bodies, t, n, useOpenCL);
        simulatedWorld.getDate().setTime(simulatedWorld.getDate().getTime() + (long)(t * n * 1000));
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
    public double getSimulatedSecondsPerUpdate() {
        return getTicksPerUpdate() * getSimulatedSecondsPerTick();
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
        kernel.dispose();
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
