/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Physics2D;

import Physics2D.Integrators.ExplicitEuler;
import Physics2D.Integrators.Integrator;
import Physics2D.Integrators.Integrator.IntegratorType;
import Physics2D.Integrators.Symplectic1;
import Physics2D.Integrators.Symplectic4;
import Physics2D.Objects.Planet;
import Physics2D.Vector2;
import World2D.Objects.DisplayObject;
import World2D.Objects.Line;
import java.awt.Color;
import java.util.Date;

/**
 *
 * @author bowen
 */
public class NBodyFutureOrbit1 implements Runnable, FutureSimulation { //TODO implement past data logging for better orbits
    private Thread thread;
    private Planet[] bigObjects;
    
    private double[] orbitalPeriods;
    
    private Line[] orbitLines;
    private Integrator integrator;
    
    
    private int futureTimeSteps; //How many steps does the integrator calculate for the amount of time
    
    //private double smallestOrbitalPeriod;
    private boolean updateOrbits;
    
    
    
    private boolean isPaused;
    
    
    public NBodyFutureOrbit1(IntegratorType integrator, int futureTimeSteps, Planet[] bigObjects, double[] orbitalPeriods) {
        this.isPaused = true;
        this.futureTimeSteps = futureTimeSteps;
        
        this.bigObjects = bigObjects;
        
        this.orbitalPeriods = orbitalPeriods;
        this.updateOrbits = false;
        //this.smallestOrbitalPeriod = orbitalPeriods[1];
        
        this.orbitLines = new Line[(futureTimeSteps+Math.floorDiv(futureTimeSteps, 100))*bigObjects.length];
        for (int i=0; i<orbitLines.length; i++) {
            orbitLines[i] = new Line(Color.getHSBColor(341F/360, 50F/100, 74F/100));
        }
        
        switch(integrator) {
            case NOFORCE:
                break;
            case EXPLICITEULER:
                this.integrator = new ExplicitEuler();
                break;
            case SYMPLECTIC4:
                this.integrator = new Symplectic4();
                break;
            case SYMPLECTIC1:
                this.integrator = new Symplectic1();
                break;
        }
        this.thread = new Thread(this);
    }
    public void compute() {
        //Vector2[][] positionTime = integrator.getFuture(objects, ratio/stepsPerRatioTime, futureRatioSteps*stepsPerRatioTime);
    }
    private static boolean isWithinThreshold(double x0, double y0, double x, double y, double threshold) {
        return ((Math.abs(x-x0) < threshold) && (Math.abs(y-y0) < threshold));
    }
    
    @Override
    public void step() {
        
        Vector2[][] positionTime = new Vector2[bigObjects.length][futureTimeSteps];
        for (int i=0; i<bigObjects.length; i++) {
            positionTime[i] = Integrator.getFutureSingle(bigObjects, i, this.integrator, orbitalPeriods[i]/futureTimeSteps, futureTimeSteps+Math.floorDiv(futureTimeSteps, 100));
        }
        int linei = 0;
        
        //final double orbitThreshold = 1E9;
        //final int timeTriggerThreshold = 100;
        //final int timeOverlapThreshold = 10;
        
        for (int n=0; n<bigObjects.length; n++) {
            //boolean isPeriodic = false;
            //int timeOverlapCounter = 0;
            for (int i=0; i<positionTime[n].length-1; i++) {
                //if (i > timeTriggerThreshold) {
                    //if (isWithinThreshold(positionTime[n][0].get(0), positionTime[n][1].get(1), positionTime[n][i].get(0), positionTime[n][i].get(1), orbitThreshold)) {
                        //isPeriodic = true;
                    //}
                //}
                //if (isPeriodic && timeOverlapCounter > timeOverlapThreshold) {
                    //orbitLines[linei].hide();
                //} else {
                    //if (isPeriodic) {
                        //timeOverlapCounter++;
                    //}
                    orbitLines[linei].setPos(positionTime[n][i].get(0), positionTime[n][i].get(1), positionTime[n][i+1].get(0), positionTime[n][i+1].get(1));
                    orbitLines[linei].show();
                //}
                
                linei++;
            }
                //orbitLines[linei].setPos(positionTime[n][positionTime[n].length-1].get(0), positionTime[n][positionTime[n].length-1].get(1), positionTime[n][0].get(0), positionTime[n][0].get(1));
                //orbitLines[linei].show();
                //linei++;
        }
    }
    
    public void forceUpdateOrbitPositions() {
        this.updateOrbits = true;
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
    public void run() {
        double desiredSleepms = 1000D/1;
        
        long startTime;
        long endTime;
        long sleepTime;
        
        step();
        
        while (true) {
            if (!isPaused) {
                
                startTime = System.nanoTime();
                if (updateOrbits) {
                    step();
                    updateOrbits = false;
                }
                endTime = System.nanoTime();
                
                sleepTime = (long)(desiredSleepms*1000000) - (endTime-startTime);
                
                if (sleepTime < 0) {
                    sleepTime = 0;
                    System.out.println("FutureSimulation Thread Overload");
                }
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

    @Override
    public DisplayObject[] getDisplayObjects() {
        return orbitLines;
    }


    @Override
    public long getTicks() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getSpeed() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void speedUp() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void speedDown() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setRatio(double ratio) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setBodies(Planet[] bodies) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Date getDate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getObjectsNumber() {
        return orbitLines.length;
    }
    
}
