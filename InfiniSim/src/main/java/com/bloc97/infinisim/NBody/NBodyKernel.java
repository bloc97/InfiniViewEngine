/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bloc97.infinisim.NBody;

import com.aparapi.Kernel;
import static com.bloc97.infinisim.NBody.Equations.G;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author bowen
 */
public class NBodyKernel extends Kernel {
    
    double[] s4A = new double[] {0.1288461583653841854D, 0.4415830236164665242D, -0.0857820194129736460D, 0.5153528374311229364D, 1};
    double[] s4B = new double[] {0.3340036032863214255D, 0.7562300005156682911D, -0.2248198030794208058D, 0.1344961992774310892D, 1};
    
    int itype, otype, etype;
    
    int kernelLength;
    int step;
    
    int integratorStep = 4;
    
    int length;

    //General Information
    double[] position, velocity, acceleration; //Stored as i,j,k
    double[] mass, radius;
    int[] collided;
    boolean[] isActive;

    //Integrator information
    double dt;

    public NBodyKernel() {
        this.setExplicit(true);
    }

    public void integrate() {
        setStep(1).setIntegratorStep(0).execute();
        setStep(0).execute();
        setStep(2).execute();
        
        setStep(1).setIntegratorStep(1).execute();
        setStep(0).execute();
        setStep(2).execute();
        
        setStep(1).setIntegratorStep(2).execute();
        setStep(0).execute();
        setStep(2).execute();
        
        setStep(1).setIntegratorStep(3).execute();
        setStep(0).execute();
        setStep(2).execute();
        
        setStep(3).execute();
    }
    
    
    public void setTime(double dt) {
        this.dt = dt;
    }
    
    private NBodyKernel setIntegratorStep(int step) {
        this.integratorStep = step;
        return this;
    }
    
    public void setArrays(double[] position, double[] velocity, double[] mass, double[] radius, int[] collided, boolean[] isActive) {
        this.length = mass.length;
        
        this.position = position;
        this.velocity = velocity;
        this.acceleration = new double[position.length];
        this.mass = mass;
        this.radius = radius;
        this.collided = collided;
        this.isActive = isActive;
        
        put(this.position).put(this.velocity).put(this.acceleration).put(this.mass).put(this.radius).put(this.collided).put(this.isActive).put(s4A).put(s4B);
    }

    public void refreshArrays() {
        get(position).get(velocity).get(acceleration).get(mass).get(radius).get(collided).get(isActive);
    }
    
    public void resolveCollisions() {
        for (int i=0; i<collided.length; i++) {
            if (collided[i] != i && isActive[i] && isActive[collided[i]]) { //If collided and both objects are active
                merge(i, collided[i]);
            }
        }
    }
    
    public void merge(int a, int b) {
        
        int pa = a * 3;
        int pb = b * 3;
        
        double m = mass[a] + mass[b];
        
        position[pa] = (position[pa] * mass[a] + position[pb] * mass[b]) / m;
        position[pa + 1] = (position[pa + 1] * mass[a] + position[pb + 1] * mass[b]) / m;
        position[pa + 2] = (position[pa + 2] * mass[a] + position[pb + 2] * mass[b]) / m;
        
        velocity[pa] = (velocity[pa] * mass[a] + velocity[pb] * mass[b]) / m;
        velocity[pa + 1] = (velocity[pa + 1] * mass[a] + velocity[pb + 1] * mass[b]) / m;
        velocity[pa + 2] = (velocity[pa + 2] * mass[a] + velocity[pb + 2] * mass[b]) / m;
        
        mass[a] = m;
        
        double r1 = radius[a] * radius[a] * radius[a];
        double r2 = radius[b] * radius[b] * radius[b];
        double r = (Math.pow(r1 + r2, 1/3d)); //3D radius
        
        radius[a] = r;
        
        isActive[b] = false;
    }
    
    public void setTypes(Optimisers.OptimiserType otype, Equations.EquationType etype) {
        this.otype = otype.ordinal();
        this.etype = etype.ordinal();
    }

    private NBodyKernel setStep(int step) {
        this.step = step;
        switch (step) {
            case 0:
                kernelLength = length;
                break;
            case 1:
                kernelLength = length * 3;
                break;
            case 2:
                kernelLength = length * 3;
                break;
            case 3:
                kernelLength = length;
                break;
        }
        return this;
        /*
        0 - Compute Acceleration
        1 - Integrate
        2 - Collision Check
        */
    }

    private void computeAccelerationStep(int i) {
        acceleration[i * 3] = 0;
        acceleration[i * 3 + 1] = 0;
        acceleration[i * 3 + 2] = 0;

        for (int j=0; j<mass.length; j++) {
            if (i != j && isActive[j]) { //Ignore self and inactive bodies

                double rX = position[j * 3] - position[i * 3];
                double rY = position[j * 3 + 1] - position[i * 3 + 1];
                double rZ = position[j * 3 + 2] - position[i * 3 + 2];

                double distanceSquared = (rX * rX + rY * rY + rZ * rZ);

                double dOverMass = mass[j] / (distanceSquared * sqrt(distanceSquared));

                acceleration[i * 3] += rX * dOverMass;
                acceleration[i * 3 + 1] += rY * dOverMass;
                acceleration[i * 3 + 2] += rZ * dOverMass;
            }
        }

        acceleration[i * 3] *= G;
        acceleration[i * 3 + 1] *= G;
        acceleration[i * 3 + 2] *= G;
        
    }

    private void integratePositionStep(int i) {
        position[i] = position[i] + (velocity[i] * dt * s4A[integratorStep]);

    }
    private void integrateVelocityStep(int i) {
        velocity[i] = velocity[i] + (acceleration[i] * dt * s4B[integratorStep]);

    }
    
    private void checkCollisionStep(int i) {
        
        for (int j=i+1; j<collided.length; j++) {
            if (isActive[j]) { //Ignore inactive bodies
                double relativePosX = position[i * 3] - position[j * 3];
                double relativePosY = position[i * 3 + 1] - position[j * 3 + 1];
                double relativePosZ = position[i * 3 + 2] - position[j * 3 + 2];

                double currentDistanceSqr = (relativePosX * relativePosX) + (relativePosY * relativePosY) + (relativePosZ * relativePosZ);

                double effectiveRadius = radius[i] + radius[j];
                double effectiveRadiusSqr = effectiveRadius * effectiveRadius;

                if (currentDistanceSqr <= effectiveRadiusSqr) {
                    collided[i] = j;
                }
            }

        }
    }

    private void execute() {
        if (this.isExecuting()) {
            while (this.isExecuting()) {
                try {
                    TimeUnit.MILLISECONDS.sleep(10);
                } catch (InterruptedException ex) {
                    return;
                }
            }
        }
        this.execute(kernelLength);
    }

    @Override
    public void run() {
        int i = getGlobalId();
        
        if (step == 0 && step != -1) {
            if (isActive[i]) {
                computeAccelerationStep(i);
            }
        } else if (step == 1) {
            if (isActive[i/3]) {
                integratePositionStep(i);
            }
        } else if (step == 2) {
            if (isActive[i/3]) {
                integrateVelocityStep(i);
            }
        }
        if (step == 3) {
            if (isActive[i]) {
                checkCollisionStep(i);
            }
        }
        
    }

    
}
