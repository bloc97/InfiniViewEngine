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

/**
 *
 * @author bowen
 */
public class SimulationThread implements Runnable, Simulation {
    private Thread thread;
    
    protected List<Simulation> simulationList;
    protected Set objects = new LinkedHashSet();
    
    protected double updatesPerSecond; //How many "Big steps" per second
    protected int ticksPerUpdate; //How many "Small steps" per update
    protected double secondsPerTick; //How many "Simulated seconds" per tick
    
    protected final double initialUpdatesPerSecond;
    protected final int initialTicksPerUpdate;
    protected final double initialSecondsPerTick;
    
    protected long ticks = 0;
    protected Date date;
    
    private long totalTime = 0;
    
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
        passSettings();
    }
    
    private void passSettings() {
        for (Simulation simulation : simulationList) {
            simulation.setSimulatedSecondsPerTick(secondsPerTick);
            simulation.setTicksPerUpdate(ticksPerUpdate);
            simulation.setUpdatesPerSecond(updatesPerSecond);
            simulation.setObjects(objects);
        }
    }
    
    @Override
    public void step() {
        step(1);
    }
    
    @Override
    public void step(int ticks) {
        for (Simulation simulation : simulationList) {
            simulation.step(ticks);
        }
    }
    
    public List<Simulation> getSimulations() {
        return new ArrayList<>(simulationList);
    }
    
    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
    @Override
    public void enable() {
        isEnabled = true;
        if (!this.thread.isAlive() && !this.thread.isInterrupted()) {
            this.thread.start();
        }
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
    public void run() {
        
        double desiredSleepms = 1000D/updatesPerSecond; //Desired sleep time in miliseconds
        double desiredSleepns = 1000000000D/updatesPerSecond;
        
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
                
                sleepTime = (long)(desiredSleepms*1000000) - (endTime-startTime);
                
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
        passSettings();
    }

    @Override
    public double getUpdatesPerSecond() {
        return updatesPerSecond;
    }

    @Override
    public void setUpdatesPerSecond(double updatesPerSecond) {
        this.updatesPerSecond = updatesPerSecond;
        passSettings();
    }

    @Override
    public int getTicksPerUpdate() {
        return ticksPerUpdate;
    }

    @Override
    public void setTicksPerUpdate(int ticksPerUpdate) {
        this.ticksPerUpdate = ticksPerUpdate;
        passSettings();
    }

    @Override
    public double getSimulatedSecondsPerTick() {
        return secondsPerTick;
    }

    @Override
    public void setSimulatedSecondsPerTick(double secondsPerTick) {
        this.secondsPerTick = secondsPerTick;
        passSettings();
    }


    @Override
    public Set getObjects() {
        return new HashSet(objects);
    }

    @Override
    public int getObjectsNumber() {
        return objects.size();
    }

    @Override
    public void clearObjects() {
        this.objects = new LinkedHashSet();
        passSettings();
    }

    @Override
    public void addObject(Object object) {
        this.objects.add(object);
        //for (Simulation simulation : simulationList) {
            //simulation.setObjects(objects);
            //simulation.addObject(object);
        //}
    }

    @Override
    public boolean removeObject(Object object) {
        boolean b = this.objects.remove(object);
        //for (Simulation simulation : simulationList) {
            //simulation.removeObject(object);
        //}
        return b;
    }

    @Override
    public Set setObjects(Set set) {
        Set oldSet = objects;
        this.objects = set;
        passSettings();
        return oldSet;
    }

}
