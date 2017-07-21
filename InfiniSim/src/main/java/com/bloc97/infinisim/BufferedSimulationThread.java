/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bloc97.infinisim;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author bowen
 */
public class BufferedSimulationThread implements Runnable {
    private Thread thread;
    
    private Simulation simulation;
    
    private final double secondsPerTick; //How many "Simulated seconds" per tick
    private final int ticksPerBufferEntry; //How many ticks between each save to buffer
    private final int targetBufferSize;
    
    private final LinkedList<List> buffer = new LinkedList<>();
    
    private final Date currentDate;
    
    private boolean isEnabled = false;

    public BufferedSimulationThread(Simulation simulation, double secondsPerTick, int ticksPerBufferEntry, int targetBufferSize, Date date) {
        this.thread = new Thread(this);
        this.simulation = simulation;
        
        this.secondsPerTick = secondsPerTick;
        this.ticksPerBufferEntry = ticksPerBufferEntry;
        this.targetBufferSize = targetBufferSize;
        
        this.currentDate = date;
        
    }
    
    public Date getCurrentDate() {
        return currentDate;
    }
    
    /**
     *
     * @param n n-th Element of the buffer
     * @return Date of the n-th element of the buffer
     */
    public Date getTargetDate(int n) {
        return new Date(currentDate.getTime() + (long)(secondsPerTick * ticksPerBufferEntry * 1000 * n));
    }
    
    public Date getTargetBufferEndDate() {
        return getTargetDate(targetBufferSize);
    }
    
    public Date getBufferEndDate() {
        return getTargetDate(buffer.size());
    }
    
    public List<List> getBuffer() {
        return buffer;
    }
    
    public List forward() {
        return buffer.poll();
    }
    
    public void step() {
        step(1);
    }
    
    public void step(int ticks) {
        
        for (int i=0; i<ticks; i++) {
            simulation.step(secondsPerTick);
        }
        buffer.offer(simulation.getObjectsSnapshot());
    }
    
    public Simulation getSimulation() {
        return simulation;
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
        
        while (true) {
                
            if (isEnabled) {
                step(ticksPerBufferEntry);
            } else {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ex) {
                    System.out.println("Thread Error");
                }
            }
        }
    }

    public double getSimulatedSecondsPerTick() {
        return secondsPerTick;
    }
    public int getTicksPerBufferEntry() {
        return ticksPerBufferEntry;
    }
    public int getTargetBufferSize() {
        return targetBufferSize;
    }
    public int getBufferSize() {
        return buffer.size();
    }
    
}
