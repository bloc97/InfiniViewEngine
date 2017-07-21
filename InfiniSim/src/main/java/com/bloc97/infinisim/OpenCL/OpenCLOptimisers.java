/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bloc97.infinisim.OpenCL;

import com.aparapi.Kernel;
import com.bloc97.infinisim.NBody.Equations;
import static com.bloc97.infinisim.NBody.Equations.G;
import com.bloc97.infinisim.NBody.Optimisers;

/**
 *
 * @author bowen
 */
public class OpenCLOptimisers {
    
    private static DirectKernel directKernel = new DirectKernel();
    
    public static double[] optimise(Optimisers.OptimiserType type, Equations.EquationType etype, double[] positionXYZ, double[] massArr) {
        
        double[] accelerationXYZ = new double[positionXYZ.length];
        
        switch (type) {
            case DIRECT:
                directKernel.set(positionXYZ, accelerationXYZ, massArr);
                directKernel.execute();
            //case BARNES_HUT:
                //return optimiseBH(etype, list);
            default:
                directKernel.set(positionXYZ, accelerationXYZ, massArr);
                directKernel.execute();
        }
        return accelerationXYZ;
    }
    
    static class DirectKernel extends Kernel {
        
        double[] positionXYZ, accelerationXYZ, massArr;
        
        public void set(double[] positionXYZ, double[] accelerationXYZ, double[] massArr) {
            this.positionXYZ = positionXYZ;
            this.accelerationXYZ = accelerationXYZ;
            this.massArr = massArr;
        }
        
        public void execute() {
            this.execute(massArr.length);
        }
        
        @Override
        public void run() {
            int i = getGlobalId();
            
            for (int j=0; j<massArr.length; j++) {
                if (i == j) continue;
                
                double rX = positionXYZ[j * 3] - positionXYZ[i * 3];
                double rY = positionXYZ[j * 3 + 1] - positionXYZ[i * 3 + 1];
                double rZ = positionXYZ[j * 3 + 2] - positionXYZ[i * 3 + 2];
                
                double distanceSqr = rX * rX + rY * rY + rZ * rZ;
                double distance = sqrt(distanceSqr);
                
                double rXHat = rX / distance;
                double rYHat = rY / distance;
                double rZHat = rZ / distance;
                
                accelerationXYZ[i * 3] += rXHat * massArr[j] / distanceSqr;
                accelerationXYZ[i * 3 + 1] += rYHat * massArr[j] / distanceSqr;
                accelerationXYZ[i * 3 + 2] += rZHat * massArr[j] / distanceSqr;
            }
            
            accelerationXYZ[i * 3] *= G;
            accelerationXYZ[i * 3 + 1] *= G;
            accelerationXYZ[i * 3 + 2] *= G;
        }
        
    }
}
