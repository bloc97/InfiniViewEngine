/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bloc97.infinisim;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 *
 * @author bowen
 */
public interface Simulation {
    public long getTicks();
    public Date getDate();
    public void update();
    
    public boolean isEnabled();
    public void enable();
    public void disable();
    public void toggle();
    
    
    public int getTicksPerUpdate();
    public void setTicksPerUpdate(int ticksPerUpdate);

    public double getSimulatedSecondsPerTick();
    public void setSimulatedSecondsPerTick(double secondsPerTick);

    public default double getSimulatedSecondsPerUpdate() {
        return getTicksPerUpdate() * getSimulatedSecondsPerTick();
    }
    
    public void resetInitialSettings();
    
    /**
     *
     * @return Shallow copy of the objects set
     */
    public Set getObjects();
    
    /**
     *
     * @return Deep copy of the objects set, where each object is cloned.
     */
    public List getObjectsSnapshot();
    public int getObjectsNumber();
    public void setObjects(Set set);
}
