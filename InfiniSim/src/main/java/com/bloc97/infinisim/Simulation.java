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
    public void step(double seconds);
    public void step(int ticks, double secondsPerTick);
    
    public boolean isEnabled();
    public void enable();
    public void disable();
    public void toggle();
    
    public Set getObjects();
    public int getObjectsNumber();
    public void setObjects(Set set);
}
