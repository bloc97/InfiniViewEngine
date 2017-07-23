/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bloc97.infinisim.OpenCL;

import com.aparapi.Kernel;
import com.aparapi.Range;
import com.aparapi.device.Device;
import com.aparapi.internal.kernel.KernelManager;
import com.bloc97.infinisim.Collision.Helpers;
import com.bloc97.infinisim.NBody.Equations;
import static com.bloc97.infinisim.NBody.Equations.G;
import com.bloc97.infinisim.NBody.Integrators;
import com.bloc97.infinisim.NBody.Optimisers;
import com.bloc97.infinisim.Spatial;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bowen
 */
public class IntegratorOpenCLKernel extends Kernel {

    public final static IntegratorOpenCLKernel INSTANCE = new IntegratorOpenCLKernel(); //Singleton
    
    int itype, otype, etype;
    
    int kernelLength;
    int step;

    Spatial[] spatialArr;
    int length;

    //General Information
    double[] positionXYZ, velocityXYZ, accelerationXYZ;
    double[] massArr, radiusArr;
    int[] collided;

    //Integrator information
    double dt;

    private IntegratorOpenCLKernel() {
        this.setExplicit(true);
    }

    public void integrate(Integrators.IntegratorType itype, Optimisers.OptimiserType otype, Equations.EquationType etype, Collection<Spatial> list, double dt, int ticks) {
        this.dt = dt;
        setTypes(itype, otype, etype);
        initArrays(list);
        for (int i=0; i<ticks; i++) {
            setStep(0);
            execute();
            setStep(1);
            execute();
            setStep(2);
            execute();
        }
        applyArrays();
        resolveCollisions(list);
    }

    public void resolveCollisions(Collection<Spatial> list) {
        for (int i=0; i<collided.length; i++) {
            if (collided[i] != i) { //If collided
                Helpers.merge(spatialArr[i], spatialArr[collided[i]]);
                list.remove(spatialArr[collided[i]]);
            }
        }
    }
    
    private void setTypes(Integrators.IntegratorType itype, Optimisers.OptimiserType otype, Equations.EquationType etype) {
        this.itype = itype.ordinal();
        this.otype = otype.ordinal();
        this.etype = etype.ordinal();
    }

    private void setStep(int step) {
        this.step = step;
        switch (step) {
            case 0:
                kernelLength = length;
                break;
            case 1:
                kernelLength = length * 3;
                break;
            case 2:
                kernelLength = length;
                break;
        }

        /*
        0 - Compute Acceleration
        1 - Integrate
        2 - Collision Check
        */
    }

    private void initArrays(Collection<Spatial> list) {
        length = list.size();

        spatialArr = new Spatial[length];
        positionXYZ = new double[length * 3];
        velocityXYZ = new double[length * 3];
        accelerationXYZ = new double[length * 3];

        massArr = new double[length];
        radiusArr = new double[length];

        collided = new int[length];

        int i=0;
        for (Spatial body : list) {

            spatialArr[i] = body;

            positionXYZ[i * 3] = body.position().get(0);
            positionXYZ[i * 3 + 1] = body.position().get(1);
            positionXYZ[i * 3 + 2] = body.position().get(2);

            velocityXYZ[i * 3] = body.velocity().get(0);
            velocityXYZ[i * 3 + 1] = body.velocity().get(1);
            velocityXYZ[i * 3 + 2] = body.velocity().get(2);

            massArr[i] = body.getMass();
            radiusArr[i] = body.getRadius();
            collided[i] = i;

            i++;
        }

        put(positionXYZ).put(velocityXYZ).put(accelerationXYZ).put(massArr).put(radiusArr).put(collided);
    }

    private void applyArrays() {
        get(positionXYZ).get(velocityXYZ).get(accelerationXYZ).get(massArr).get(radiusArr).get(collided);
        for (int n=0; n<length; n++) {
            spatialArr[n].position().set(positionXYZ[n * 3], positionXYZ[n * 3 + 1], positionXYZ[n * 3 + 2]);
            spatialArr[n].velocity().set(velocityXYZ[n * 3], velocityXYZ[n * 3 + 1], velocityXYZ[n * 3 + 2]);
        }
    }

    private void computeAccelerationStep(int i) {
        accelerationXYZ[i * 3] = 0;
        accelerationXYZ[i * 3 + 1] = 0;
        accelerationXYZ[i * 3 + 2] = 0;

        for (int j=0; j<massArr.length; j++) {
            if (i != j) {

                double rX = positionXYZ[j * 3] - positionXYZ[i * 3];
                double rY = positionXYZ[j * 3 + 1] - positionXYZ[i * 3 + 1];
                double rZ = positionXYZ[j * 3 + 2] - positionXYZ[i * 3 + 2];


                double distanceSquared = (rX * rX + rY * rY + rZ * rZ);

                double dOverMass = massArr[j] / (distanceSquared * sqrt(distanceSquared));

                accelerationXYZ[i * 3] += rX * dOverMass;
                accelerationXYZ[i * 3 + 1] += rY * dOverMass;
                accelerationXYZ[i * 3 + 2] += rZ * dOverMass;
            }
        }

        accelerationXYZ[i * 3] *= G;
        accelerationXYZ[i * 3 + 1] *= G;
        accelerationXYZ[i * 3 + 2] *= G;
    }

    private void integrateStep(int i) {
        positionXYZ[i] = positionXYZ[i] + (velocityXYZ[i] * dt);
        velocityXYZ[i] = velocityXYZ[i] + (accelerationXYZ[i] * dt);

    }
    
    private void checkCollisionStep(int i) {
        
        for (int j=i+1; j<collided.length; j++) {
            double relativePosX = positionXYZ[i * 3] - positionXYZ[j * 3];
            double relativePosY = positionXYZ[i * 3 + 1] - positionXYZ[j * 3 + 1];
            double relativePosZ = positionXYZ[i * 3 + 2] - positionXYZ[j * 3 + 2];

            double currentDistanceSqr = (relativePosX * relativePosX) + (relativePosY * relativePosY) + (relativePosZ * relativePosZ);

            double effectiveRadius = radiusArr[i] + radiusArr[j];
            double effectiveRadiusSqr = effectiveRadius * effectiveRadius;

            if (currentDistanceSqr <= effectiveRadiusSqr) {
                collided[i] = j;
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
        if (step == 0) {
            computeAccelerationStep(i);
        } else if (step == 1) {
            integrateStep(i);
        } else {
            checkCollisionStep(i);
        }
    }

    
}
