/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Physics2D;

import Physics.Simulation;
import Physics2D.Integrators.Integrator;
import Physics2D.Integrators.Integrator.IntegratorType;
import Physics2D.Integrators.NBody;
import Physics2D.Integrators.Symplectic1;
import Physics2D.Integrators.Symplectic4;
import Physics2D.Integrators.FuturePath;
import Physics2D.Objects.PointBody;
import World2D.Objects.DisplayObject;
import World2D.Objects.Interpolable;
import java.util.Date;

/**
 *
 * @author bowen
 */
public class NBodySimulation implements Runnable, Simulation {
    private Thread thread;
    
    protected PointBody[] bodies;
    protected Vector2[][] futureOrbitPos;
    protected Vector2[][] futureOrbitVel;
    protected long[][] futureOrbitTime;
    
    protected Integrator integrator;
    protected Integrator integrator2;
    
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
    /*
    public NBodySimulation(IntegratorType integrator, double ratio, double updatesPerSecond, int miniSteps, SpaceObject... objects) {
        this(integrator, ratio, updatesPerSecond, miniSteps, new NBodyFuturePath(integrator, ratio, 100, updatesPerSecond/2, objects), objects);
    }*/
    public NBodySimulation(IntegratorType integrator, double ratio, double updatesPerSecond, int miniSteps, Date date, PointBody... bodies) {
        this.isPaused = true;
        
        this.date = date;
        
        this.bodies = bodies;
        this.initialRatio = ratio;
        this.ratio = ratio;
        this.updatesPerSecond = updatesPerSecond;
        this.secondsPerMiniStep = ratio/updatesPerSecond/miniSteps;
        this.miniSteps = (miniSteps > 0) ? miniSteps : 1;
        switch(integrator) {
            case SYMPLECTIC4:
                this.integrator = new Symplectic4();
                break;
            case SYMPLECTIC1:
                this.integrator = new Symplectic1();
                break;
            default:
                break;
        }
        this.integrator2 = new Symplectic1();
        this.thread = new Thread(this);
        
        futureOrbitPos = new Vector2[bodies.length][0];
        futureOrbitVel = new Vector2[bodies.length][0];
        futureOrbitTime = new long[bodies.length][0];
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
        //if (fCount%fWait == 0) {
        if (fCount > fWait/(ratio/initialRatio)) {
            //reCalculateOrbits();
            fCount = 0;
        }
        fCount++;
        
        for (int i=0; i<bodies.length; i++) {
            if (bodies[i] instanceof FuturePath) {
                //((FuturePath)(bodies[i])).setCurrentDate(date);
            }
            if (bodies[i] instanceof DisplayObject) {
                ((DisplayObject)(bodies[i])).registerUpdate(date);
            }
        }
    }
    public void reCalculateOrbits() {
        //double futureSimRatio = ratio/4;
        //futureOrbitPos = Integrator.getFuture(bodies, integrator2, futureSimRatio, 500);
        //integrator2.reset();

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
        pers[0] = 1E9;

                
        for (int i=0; i<bodies.length; i++) {
            //System.out.println((pers[i]/10/5));
            Vector2[][] posAndVel = Integrator.getFutureSingleWithVel(bodies, i, integrator2, (pers[i]/50/4), 50);
            futureOrbitPos[i] = posAndVel[0];
            futureOrbitVel[i] = posAndVel[1];
            futureOrbitTime[i] = Integrator.getFutureSingleTimeStamps(date, (pers[i]/50/4), 50);
        }

        for (int i=0; i<bodies.length; i++) {
            if (bodies[i] instanceof FuturePath) {
                //((FuturePath)(bodies[i])).setOrbitPath(futureOrbitPos[i], futureOrbitVel[i], futureOrbitTime[i], date);
            }
        }
    }/*
    private void updateSpatialPositions() {
        //Vector2[] currentAccelerations = integrator.getCurrentAccelerations();
        for (int i=0; i<bodies.length; i++) {
            bodies[i].update();
        }
    }*/
    private void updateInterpolationSimulationTime(double time) { //Total time to interpolate before next physics Big Step
        for (int i=0; i<bodies.length; i++) {
            if (bodies[i] instanceof Interpolable) {
                ((Interpolable)(bodies[i])).setInterpolationSimulationTime(time);
            }
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
        
        for (PointBody object : bodies) {
            if (object instanceof DisplayObject) {
                objectsCount++;
            }
        }
        DisplayObject[] displayObjects = new DisplayObject[objectsCount];
        int objectsIndex = 0;
        
        for (PointBody object : bodies) {
            if (object instanceof DisplayObject) {
                displayObjects[objectsIndex] = (DisplayObject)object;
                objectsIndex++;
            }
        }
        return displayObjects;
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
        return bodies.length;
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
