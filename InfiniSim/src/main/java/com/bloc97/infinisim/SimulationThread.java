/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bloc97.infinisim;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author bowen
 */
public class SimulationThread implements Runnable {
    private Thread thread;
    
    private ExecutorService executor = Executors.newCachedThreadPool();
    
    private List<Simulation> simulationList;
    private Set objects = new LinkedHashSet();
    
    private double updatesPerSecond; //How many "Big steps" per second
    private int ticksPerUpdate; //How many "Small steps" per update
    private double secondsPerTick; //How many "Simulated seconds" per tick
    
    private final double initialUpdatesPerSecond;
    private final int initialTicksPerUpdate;
    private final double initialSecondsPerTick;
    
    private final Date date;
    
    private long totalTime = 0;
    private long targetTime = 0;
    
    private boolean isEnabled = false;

    public SimulationThread(List<Simulation> simulationList, double updatesPerSecond, int ticksPerUpdate, double secondsPerTick, Date date) {
        this.thread = new Thread(this);
        this.simulationList = new LinkedList<>(simulationList);
        
        this.initialUpdatesPerSecond = updatesPerSecond;
        this.initialTicksPerUpdate = ticksPerUpdate;
        this.initialSecondsPerTick = secondsPerTick;
        
        this.updatesPerSecond = this.initialUpdatesPerSecond;
        this.ticksPerUpdate = this.initialTicksPerUpdate;
        this.secondsPerTick = this.initialSecondsPerTick;
        
        this.date = date;
        
    }
    
    public Date getDate() {
        return date;
    }
    
    public void step() {
        step(1);
    }
    
    public void step(int ticks) {
        long initialTime = date.getTime();
        
        for (int i=0; i<ticks; i++) {
            for (Simulation simulation : simulationList) {
                simulation.step(secondsPerTick);
            }
            date.setTime(date.getTime() + (long)(1000 * secondsPerTick)); //Updates the time each tick for fluidness
        }
        date.setTime(initialTime + (long)(1000 * ticks * secondsPerTick)); //Update the time to be most precise
    }
    
    public List<Simulation> getSimulations() {
        return new ArrayList<>(simulationList);
    }
    
    public boolean isEnabled() {
        return isEnabled;
    }
    
    public void enable() {
        isEnabled = true;
        if (!this.thread.isAlive() && !this.thread.isInterrupted()) {
            this.thread.start();
        }
    }
    
    public void disable() {
        isEnabled = false;
    }
    
    public void toggle() {
        isEnabled = !isEnabled;
    }
    
    @Override
    public void run() {
        
        double desiredSleepms = 1000D/updatesPerSecond; //Desired sleep time in miliseconds
        double desiredSleepns = 1000000000D/updatesPerSecond;
        
        targetTime = (long)(desiredSleepms*1000000);
        
        long startTime;
        long endTime;
        long sleepTime;
        
        
        double realLagRatio;
        
        while (true) {
                startTime = System.nanoTime();
                
                if (isEnabled) {
                    step(ticksPerUpdate);
                }
                endTime = System.nanoTime();
                
                totalTime = endTime-startTime;
                sleepTime = targetTime - totalTime;
                
                //realLagRatio = desiredSleepns/(endTime-startTime)*ratio;
                //realLagRatio = 0;
                //updateInterpolationSimulationTime((realLagRatio > ratio) ? ratio : realLagRatio);
                
                if (sleepTime < 0) {
                    //sleepTime = 0;
                    System.out.println("Simulation Thread Overload! Late by: " + -sleepTime/1000000 + " ms.");
                    //speedDown();
                } else {
                  
                    long sleepms = Math.floorDiv(sleepTime, 1000000);
                    int sleepns = (int)Math.floorMod(sleepTime, 1000000);

                    try {
                        Thread.sleep(sleepms, sleepns);
                    } catch (InterruptedException ex) {
                        System.out.println("Thread Error");
                    }
                    //updateInterpolationSimulationTime(0);
                }
        }
    }
    
    
    public double getUpdatesPerSecond() {
        return updatesPerSecond;
    }
    
    public void setUpdatesPerSecond(double updatesPerSecond) {
        this.updatesPerSecond = updatesPerSecond;
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

    public double getTicksPerSecond() {
        return getUpdatesPerSecond() * getTicksPerUpdate();
    }
    public double getSimulatedSecondsPerSecond() {
        return getTicksPerSecond() * getSimulatedSecondsPerTick();
    }
    public double getSimulatedSecondsPerUpdate() {
        return getTicksPerUpdate() * getSimulatedSecondsPerTick();
    }

}
