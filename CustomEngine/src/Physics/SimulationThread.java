/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Physics;

import java.util.Date;

/**
 *
 * @author bowen
 */
public abstract class SimulationThread implements Runnable, Simulation {
    private Thread thread;
    
    protected double updatesPerSecond; //How many "Big steps" per second
    protected int miniSteps; //How many "Small Steps" per Big step
    protected double secondsPerMiniStep; //How many in game seconds pass in each "Small Steps"
    protected double initialRatio; //Initial ratio
    protected double ratio; //Ratio between simulated time and real time, higher is faster (In game seconds/real seconds)
    
    protected int accel = 1;
    protected boolean isAccel = true;
    protected int deccel = 1;
    
    protected short fCount = 120;
    protected short fWait = 120;
    
    protected Date date;
    
    private boolean isPaused;
    
    private boolean isOverloaded = false;
    /*
    public NBodySimulation(IntegratorType integrator, double ratio, double updatesPerSecond, int miniSteps, SpaceObject... objects) {
        this(integrator, ratio, updatesPerSecond, miniSteps, new NBodyFuturePath(integrator, ratio, 100, updatesPerSecond/2, objects), objects);
    }*/
    public SimulationThread(double ratio, double updatesPerSecond, int miniSteps, Date date) {
        this.isPaused = true;
        
        this.date = date;
        
        this.initialRatio = ratio;
        this.ratio = ratio;
        this.updatesPerSecond = updatesPerSecond;
        this.secondsPerMiniStep = ratio/updatesPerSecond/miniSteps;
        this.miniSteps = (miniSteps > 0) ? miniSteps : 1;
        
        this.thread = new Thread(this);
        
    }
    @Override
    public void step() {
        forward();
    }
    public abstract void forward();
    public void forward(int steps) {
        for (int i=0; i<steps; i++) {
            forward();
        }
    };
    public abstract void updateInterpolationSimulationTime(double realLagRatio);
    @Override
    public void speedUp() {
        if (isAccel) {
            if (accel < 1E2) {
                accel *= 2;
                ratio = initialRatio * accel;
            }
        } else {
            if (deccel > 2) {
                deccel /= 2;
                ratio = initialRatio / deccel;
            } else {
                deccel /= 2;
                isAccel = true;
                ratio = initialRatio * accel;
            }
        }
        if (ratio < 1) {
            ratio = 1;
        }
    }
    @Override
    public void speedDown() {
        if (isAccel) {
            if (accel > 1) {
                accel /= 2;
                ratio = initialRatio * accel;
            } else {
                isAccel = false;
                deccel *= 2;
                ratio = initialRatio / deccel;
            }
        } else {
            if (deccel < 1E6 && ratio > 1.) {
                deccel *= 2;
                ratio = initialRatio / deccel;
            }
        }
        
        if (ratio < 1) {
            ratio = 1;
        }

    }
    @Override
    public double getSpeed() {
        return ratio;
    }
    @Override
    public void start() {
        this.thread.start();
        unpause();
    }
    @Override
    public void pause() {
        this.isPaused = true;
    }
    @Override
    public void unpause() {
        this.isPaused = false;
    }
    @Override
    public void togglePause() {
        if (isPaused) {
            unpause();
        } else {
            pause();
        }
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
                
                
                if (!isPaused) {
                    forward(miniSteps*accel);
                }
                
                
                endTime = System.nanoTime();
                
                
                sleepTime = (long)(desiredSleepms*1000000) - (endTime-startTime);
                realLagRatio = desiredSleepns/(endTime-startTime)*ratio;
                //realLagRatio = 0;
                updateInterpolationSimulationTime((realLagRatio > ratio) ? ratio : realLagRatio);
                if (sleepTime < 0) {
                    //sleepTime = 0;
                    //System.out.println("Simulation Thread Overload");
                    isOverloaded = true;
                    //speedDown();
                } else {
                  
                    isOverloaded = false;
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
    public Date getDate() {
        return date;
    }
    @Override
    public boolean isOverloaded() {
        return isOverloaded;
    }
    

}
