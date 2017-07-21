/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bloc97.infinisim.OpenCL;

import com.aparapi.Kernel;
import com.bloc97.infinisim.NBody.Equations;
import static com.bloc97.infinisim.NBody.Equations.G;
import com.bloc97.infinisim.NBody.Integrators;
import com.bloc97.infinisim.NBody.Optimisers;
import com.bloc97.infinisim.Spatial;
import java.util.Collection;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bowen
 */
public class OpenCLIntegrators {
    
    
    /*
    private static IntegratorOpenCLKernel integratorKernel = new IntegratorOpenCLKernel();
    public static void integrate(Integrators.IntegratorType itype, Optimisers.OptimiserType otype, Equations.EquationType etype, Set<Spatial> list, double dt, int ticks) {
        integratorKernel.integrate(itype, otype, etype, list, dt, ticks);
    }
    
    
    static class ApBxCKernel extends Kernel {
        
        double[] aXYZ, bXYZ;
        double c;
        
        public void set(double[] aXYZ, double[] bXYZ, double c) {
            this.aXYZ = aXYZ;
            this.bXYZ = bXYZ;
            this.c = c;
        }
        
        public void execute() {
            this.execute(aXYZ.length);
        }
        
        @Override
        public void run() {
            int i = getGlobalId();
            aXYZ[i] = aXYZ[i] + (bXYZ[i] * c);
            
        }
    }
    static class ApBxCDualKernel extends Kernel {
        
        double[] aXYZ, bXYZ, cXYZ;
        double c, d;
        
        public void set(double[] aXYZ, double[] bXYZ, double[] cXYZ, double c, double d) {
            this.aXYZ = aXYZ;
            this.bXYZ = bXYZ;
            this.cXYZ = cXYZ;
            this.c = c;
            this.d = d;
        }
        
        public void execute() {
            this.execute(aXYZ.length);
        }
        
        @Override
        public void run() {
            int i = getGlobalId();
            aXYZ[i] = aXYZ[i] + (bXYZ[i] * c);
            bXYZ[i] = bXYZ[i] + (cXYZ[i] * d);
        }
    }
    
    static class CollisionCheckKernel extends Kernel {
        
        double[] positionXYZ, velocityXYZ, radiusArr;
        boolean[] collided;
        
        public void set(double[] positionXYZ, double[] velocityXYZ, double[] radiusArr, boolean[] collided) {
            this.positionXYZ = positionXYZ;
            this.velocityXYZ = velocityXYZ;
            this.radiusArr = radiusArr;
            this.collided = collided;
        }
        
        public void execute() {
            this.execute(positionXYZ.length);
        }
        
        @Override
        public void run() {
            int i = getGlobalId();

            for (int j=i+1; j<collided.length; j++) {
                double relativePosX = positionXYZ[i * 3] - positionXYZ[j * 3];
                double relativePosY = positionXYZ[i * 3 + 1] - positionXYZ[j * 3 + 1];
                double relativePosZ = positionXYZ[i * 3 + 2] - positionXYZ[j * 3 + 2];

                double currentDistanceSqr = (relativePosX * relativePosX) + (relativePosY * relativePosY) + (relativePosZ * relativePosZ);

                double effectiveRadius = radiusArr[i] + radiusArr[j];
                double effectiveRadiusSqr = effectiveRadius * effectiveRadius;

                if (currentDistanceSqr <= effectiveRadiusSqr) {
                    collided[i] = true;
                    collided[j] = true;
                }

            }
        }
        
    }
    
    public static void integrate(Integrators.IntegratorType itype, Optimisers.OptimiserType otype, Equations.EquationType etype, Set<Spatial> list, double dt, int ticks) {
        
        //Array creation
        int length = list.size();
        
        Spatial[] spatialArr = new Spatial[length];
        
        double[] positionXYZ = new double[length * 3];
        double[] velocityXYZ = new double[length * 3];
        
        double[] massArr = new double[length];
        double[] radiusArr = new double[length];
        
        
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
            
            i++;
        }
        
        //Optimization algorithms
        
        for (int k=0; k<ticks; k++) {
        
            double[] accelerationXYZ = OpenCLOptimisers.optimise(otype, etype, positionXYZ, massArr);

            //Start of integration

            switch (itype) {
                case NONE:
                    abcKernel.set(positionXYZ, velocityXYZ, dt);
                    abcKernel.execute();
                    break;
                    
                case EULER:
                    integrateEuler(otype, etype, list, dt);
                    break;
    
                case SYMPLECTIC:
                    //abcnKernel.set(new double[] {1, 1}, positionXYZ, velocityXYZ, accelerationXYZ);
                    abcDualKernel.set(positionXYZ, velocityXYZ, accelerationXYZ, dt, dt);
                    abcDualKernel.execute();
                    break;

                default:
                    abcKernel.set(positionXYZ, velocityXYZ, dt);
                    abcKernel.execute();
                    //integrateEuler(otype, etype, list, dt);
            }
        }
        
        boolean[] collided = new boolean[length];
        
        collKernel.set(positionXYZ, velocityXYZ, radiusArr, collided);
        collKernel.execute();
    }
    */
    
}
