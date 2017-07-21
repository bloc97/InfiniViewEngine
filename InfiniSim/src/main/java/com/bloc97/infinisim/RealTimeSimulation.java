/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bloc97.infinisim;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author bowen
 */
public class RealTimeSimulation implements Runnable {
    
    private Simulation simulation;
    
    private double targetUpdatesPerSecond; //How many "Big steps" per second
    
    private double realUpdatesPerSecond = 0; //How many "Big steps" per were executed last time
    
    private long targetTime; //Target frame time
    private long realTime; //Real time that took the thread to run
    
    private volatile boolean isRunning = true;
    
    public RealTimeSimulation(Simulation simulation, double updatesPerSecond) {
        this.simulation = simulation;
        
        this.targetUpdatesPerSecond = updatesPerSecond;
        targetTime = (long)(1000000000D/targetUpdatesPerSecond);
        realTime = targetTime;
    }
    
    public Simulation getSimulation() {
        return simulation;
    }
    
    public void stop() {
        isRunning = false;
    }
    
    @Override
    public void run() {
        
        long startTime;
        long endTime;
        long sleepTime;
        
        
        while (isRunning) {

            startTime = System.nanoTime();
            simulation.update();
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
