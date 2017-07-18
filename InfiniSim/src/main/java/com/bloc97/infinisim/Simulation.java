/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bloc97.infinisim;

import java.util.Date;
import java.util.Set;

/**
 *
 * @author bowen
 */
public interface Simulation {
    public long getTicks();
    public void step();
    public void step(int ticks);
    
    public Date getDate();
    public void setDate(Date date);
    
    public double getUpdatesPerSecond();
    public void setUpdatesPerSecond(double ups);
    
    public int getTicksPerUpdate();
    public void setTicksPerUpdate(int ticksPerUpdate);
    
    public double getSimulatedSecondsPerTick();
    public void setSimulatedSecondsPerTick(double simulatedSecondsPerTick);
    
    public default double getTicksPerSecond() {
        return getUpdatesPerSecond() * getTicksPerUpdate();
    }
    public default double getSimulatedSecondsPerSecond() {
        return getTicksPerSecond() * getSimulatedSecondsPerTick();
    }
    public default double getSimulatedSecondsPerUpdate() {
        return getTicksPerUpdate() * getSimulatedSecondsPerTick();
    }
    
    public boolean isEnabled();
    public void enable();
    public void disable();
    public void toggle();
    
    
    public Set getObjects();
    public Set setObjects(Set set);
    public int getObjectsNumber();
    
    public void clearObjects();
    public void addObject(Object object);
    public boolean removeObject(Object object);
}
