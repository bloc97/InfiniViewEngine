/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Physics2D;

import Physics.FutureSimulation;
import Physics.Simulation;
import Physics2D.Integrators.ExplicitEuler;
import Physics2D.Integrators.Integrator;
import Physics2D.Integrators.Integrator.IntegratorType;
import Physics2D.Integrators.NBody;
import Physics2D.Integrators.Symplectic1;
import Physics2D.Integrators.Symplectic4;
import Physics2D.Objects.Planet;
import World2D.Objects.DisplayObject;
import World2D.Objects.FutureOrbit;
import World2D.Objects.Line;
import World2D.World;
import java.util.Date;

/**
 *
 * @author bowen
 */
public class NBodySimulation implements Runnable, Simulation {
    private Thread thread;
    
    private Planet[] bodies;
    private Vector2[][] futureOrbitPos;
    private FutureOrbit[] futureOrbits;
    
    private Integrator integrator;
    
    private double updatesPerSecond; //How many "Big steps" per second
    private int miniSteps; //How many "Small Steps" per Big step
    private double secondsPerMiniStep; //How many in game seconds pass in each "Small Steps"
    private double initialRatio; //Initial ratio
    private double ratio; //Ratio between simulated time and real time, higher is faster (In game seconds/real seconds)
    
    private int accel = 1;
    private boolean isAccel = true;
    private int deccel = 1;
    
    private byte fCount = 0;
    private byte fWait = 30;
    
    private Date date;
    
    private boolean isPaused;
    /*
    public NBodySimulation(IntegratorType integrator, double ratio, double updatesPerSecond, int miniSteps, SpaceObject... objects) {
        this(integrator, ratio, updatesPerSecond, miniSteps, new NBodyFuturePath(integrator, ratio, 100, updatesPerSecond/2, objects), objects);
    }*/
    public NBodySimulation(IntegratorType integrator, double ratio, double updatesPerSecond, int miniSteps, Date date, Planet... bodies) {
        this.isPaused = true;
        
        this.date = date;
        
        this.bodies = bodies;
        this.initialRatio = ratio;
        this.ratio = ratio;
        this.updatesPerSecond = updatesPerSecond;
        this.secondsPerMiniStep = ratio/updatesPerSecond/miniSteps;
        this.miniSteps = (miniSteps > 0) ? miniSteps : 1;
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
        
        futureOrbitPos = new Vector2[bodies.length][0];
        futureOrbits = new FutureOrbit[bodies.length];
        for (int i=0; i<futureOrbits.length; i++) {
            futureOrbits[i] = new FutureOrbit();
        }
    }
    @Override
    public void step() {
        forward(1);
    }
    public void forward(int steps) {
        long newTime = date.getTime() + (long)(1000 * steps * secondsPerMiniStep/deccel);
        date = new Date(newTime);
        
        for (int i=0; i<steps; i++) {
            integrator.apply(bodies, secondsPerMiniStep/deccel);
        }
        if (fCount%fWait == 0) {
            reCalculateOrbits();
            fCount = 0;
        }
        fCount++;
    }
    public void reCalculateOrbits() {
            futureOrbitPos = Integrator.getFuture(bodies, integrator, ratio*4, 50);
            
            double[] vels = new double[bodies.length];
            double[] pers = new double[bodies.length];
            
            for (int i=0; i<vels.length; i++) {
                vels[i] = bodies[i].velocity().norm();
            }
            
            double G = NBody.G;
            double M0 = bodies[0].mass();
            double c0 = G*G*M0*M0;
            
            for (int i=0; i<vels.length; i++) {
                pers[i] = 2 * Math.PI * Math.sqrt(c0/Math.pow(vels[i], 6));
            }

            for (int i=0; i<futureOrbits.length; i++) {
                futureOrbits[i].setOrbitPath(futureOrbitPos[i], pers[i], ratio*4);
            }
    }
    private void updateSpatialPositions() {
        //Vector2[] currentAccelerations = integrator.getCurrentAccelerations();
        for (int i=0; i<bodies.length; i++) {
            bodies[i].update();
        }
    }
    private void updateInterpolationSimulationTime(double time) { //Total time to interpolate before next physics Big Step
        for (int i=0; i<bodies.length; i++) {
            bodies[i].displayComponent.setInterpolationSimulationTime(time);
        }
    }
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
    public void run() {
        
        double desiredSleepms = 1000D/updatesPerSecond; //Desired sleep time in miliseconds
        double desiredSleepns = 1000000000D/updatesPerSecond;
        
        long startTime;
        long endTime;
        long sleepTime;
        
        
        double realLagRatio;
        
        while (true) {
            if (!isPaused) {
                
                startTime = System.nanoTime();
                
                forward(miniSteps*accel);
                updateSpatialPositions();
                
                endTime = System.nanoTime();
                
                
                sleepTime = (long)(desiredSleepms*1000000) - (endTime-startTime);
                realLagRatio = desiredSleepns/(endTime-startTime)*ratio;
                //realLagRatio = 0;
                updateInterpolationSimulationTime((realLagRatio > ratio) ? ratio : realLagRatio);
                if (sleepTime < 0) {
                    //sleepTime = 0;
                    System.out.println("Simulation Thread Overload");
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
    }
    /*
    @Override
    public DisplayObject[] getDisplayObjects() {
        
        int objectsCount = 0;
        
        for (Planet object : bodies) {
            if (!object.displayComponent.isHidden()) {
                objectsCount++;
            }
        }
        DisplayObject[] displayObjects = new DisplayObject[objectsCount];
        int objectsIndex = 0;
        
        for (Planet object : bodies) {
            if (!object.displayComponent.isHidden()) {
                displayObjects[objectsIndex] = object.displayComponent;
                objectsIndex++;
            }
        }
        return displayObjects;
    }*/
    @Override
    public DisplayObject[] getDisplayObjects() {
        
        int objectsCount = 0;
        
        for (Planet object : bodies) {
            if (!object.displayComponent.isHidden()) {
                objectsCount++;
            }
        }
        DisplayObject[] displayObjects = new DisplayObject[objectsCount];
        int objectsIndex = 0;
        
        for (Planet object : bodies) {
            if (!object.displayComponent.isHidden()) {
                displayObjects[objectsIndex] = object.displayComponent;
                objectsIndex++;
            }
        }
        return concat(displayObjects, futureOrbits);
    }
    private DisplayObject[] concat(DisplayObject[] a, DisplayObject[] b) {
        int aLen = a.length;
        int bLen = b.length;
        DisplayObject[] c= new DisplayObject[aLen+bLen];
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);
        return c;
     }
    @Override
    public int getObjectsNumber() {
        return bodies.length + futureOrbits.length;
    }

    @Override
    public long getTicks() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    @Override
    public Date getDate() {
        return date;
    }
}
