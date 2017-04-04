/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Deprec.SPHY;

/**
 *
 * @author bowen
 */
public class Timer {
    
    private int ticks = 0;
    private int iticks = 0;
    
    public Timer(int ticks) {
        this.ticks = ticks;
        this.iticks = ticks;
    }
    
    public Timer(double seconds, double ticksPerSecond) {
        this.ticks = (int)(ticksPerSecond * seconds);
        this.iticks = (int)(ticksPerSecond * seconds);
    }
    
    public void decrement() {
        this.ticks--;
    }
    
    public boolean isFinished() {
        if (this.ticks <= 0) {
            return true;
        } else {
            return false;
        }
    }
    
    public void reset() {
        this.ticks = this.iticks;
    }
    
    public void reset(int ticks) {
        this.ticks = ticks;
        this.iticks = ticks;
    }
    
    public void reset(double seconds, double ticksPerSecond) {
        this.ticks = (int)(ticksPerSecond * seconds);
        this.iticks = (int)(ticksPerSecond * seconds);
    }
    
}
