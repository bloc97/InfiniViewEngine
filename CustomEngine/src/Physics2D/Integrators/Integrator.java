/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Physics2D.Integrators;

import Physics2D.Objects.PointBody;
import SpaceGame.Objects.SpaceArtificial;
import Physics2D.Vector2;
import Physics2D.Vectors2;
import java.util.Date;

/**
 *
 * @author bowen
 */
public interface Integrator {
    public enum IntegratorType {
        NOFORCE, EXPLICITEULER, EXPLICITMIDPOINT, LEAPFROG, RK4, 
        SYMPLECTIC1, SYMPLECTIC2, SYMPLECTIC3, SYMPLECTIC4
    }
    public void apply(PointBody[] bodies, double dt);
    public void applyByPart(PointBody[] bodies, double dt); //Check for mass to categorise all different bodies
    public void partialApply(int n, PointBody[] bodies, double dt);
    
    public Vector2[] getCurrentAccelerations();
    //public PointBody[] get(PointBody[] bodies, double dt, int steps);
    //public Vector2[][] getFuture(PointBody[] bodies, double dt, int steps);
    public IntegratorType type();
    public void reset();
    
    public static PointBody[] get(PointBody[] bodies, Integrator integrator, double dt, int steps) {
        PointBody[] bodiesClone = new PointBody[bodies.length];
        
        for (int i=0; i<bodies.length; i++) {
            bodiesClone[i] = bodies[i].clone();
        }
        for (int i=0; i<steps; i++) {
            integrator.apply(bodiesClone, dt);
        }
        return bodiesClone;
    }
    
    public static Vector2[][] getFuture(PointBody[] bodies, Integrator integrator, double dt, int steps) {
        PointBody[] bodiesClone = new PointBody[bodies.length];
        
        for (int i=0; i<bodies.length; i++) {
            bodiesClone[i] = bodies[i].clone();
        }
        
        Vector2[][] positionTime = new Vector2[bodiesClone.length][steps];
        
        
        for (int n=0; n<bodiesClone.length; n++) {
            positionTime[n][0] = bodiesClone[n].position().clone();
        }
        
        for (int i=1; i<steps; i++) {
            integrator.apply(bodiesClone, dt);
            for (int n=0; n<bodiesClone.length; n++) {
                positionTime[n][i] = bodiesClone[n].position().clone();
            }
        }
        return positionTime;
    }
    
    public static Vector2[] getFutureSingle(PointBody[] bodies, int k, Integrator integrator, double dt, int steps) {
        PointBody[] bodiesClone = new PointBody[bodies.length];
        
        for (int i=0; i<bodies.length; i++) {
            bodiesClone[i] = bodies[i].clone();
        }
        
        Vector2[] positionTime = new Vector2[steps];
        
        positionTime[0] = bodiesClone[k].position().clone();
        for (int i=1; i<steps; i++) {
            integrator.apply(bodiesClone, dt);
            positionTime[i] = bodiesClone[k].position().clone();
        }
        return positionTime;
    }
    public static Vector2[][] getFutureSingleWithVel(PointBody[] bodies, int k, Integrator integrator, double dt, int steps) {
        PointBody[] bodiesClone = new PointBody[bodies.length];
        
        for (int i=0; i<bodies.length; i++) {
            bodiesClone[i] = bodies[i].clone();
        }
        
        Vector2[][] positionTime = new Vector2[2][steps];
        
        positionTime[0][0] = bodiesClone[k].position().clone();
        positionTime[1][0] = bodiesClone[k].velocity().clone();
        for (int i=1; i<steps; i++) {
            integrator.apply(bodiesClone, dt);
            positionTime[0][i] = bodiesClone[k].position().clone();
            positionTime[1][i] = bodiesClone[k].velocity().clone();
        }
        return positionTime;
    }
    public static Vector2[][] getFutureSingleWithVelRelative(PointBody[] bodies, int k, int kr, Integrator integrator, double dt, int steps) {
        PointBody[] bodiesClone = new PointBody[bodies.length];
        
        for (int i=0; i<bodies.length; i++) {
            bodiesClone[i] = bodies[i].clone();
        }
        
        Vector2[][] positionTime = new Vector2[2][steps];
        
        Vector2 referencePosition0 = bodiesClone[kr].position().clone();
        positionTime[0][0] = bodiesClone[k].position().clone();
        positionTime[1][0] = Vectors2.sub(bodiesClone[k].velocity(), bodiesClone[kr].velocity());
        for (int i=1; i<steps; i++) {
            integrator.apply(bodiesClone, dt);
            positionTime[0][i] = Vectors2.sub(bodiesClone[k].position(), bodiesClone[kr].position()).add(referencePosition0);
            positionTime[1][i] = Vectors2.sub(bodiesClone[k].velocity(), bodiesClone[kr].velocity());
        }
        return positionTime;
    }
    public static Vector2[][] getFutureSingleWithVelInterpolated(PointBody[] bodies, int k, Symplectic4 integrator, double dt, int steps) {
        PointBody[] bodiesClone = new PointBody[bodies.length];
        
        for (int i=0; i<bodies.length; i++) {
            bodiesClone[i] = bodies[i].clone();
        }
        final int steps4 = steps * 4+1;
        Vector2[][] positionTime = new Vector2[2][steps4];
        
        positionTime[0][0] = bodiesClone[k].position().clone();
        positionTime[1][0] = bodiesClone[k].velocity().clone();
        for (int i=1; i<steps4; i+=4) {
            for (int n=0; n<4; n++) {
                integrator.partialApply(n, bodiesClone, dt);
                positionTime[0][i+n] = bodiesClone[k].position().clone();
                positionTime[1][i+n] = bodiesClone[k].velocity().clone();
            }
            Vector2 x2 = positionTime[0][i+1];
            Vector2 b2 = positionTime[1][i+1];
            positionTime[0][i+1] = positionTime[0][i+2];
            positionTime[1][i+1] = positionTime[1][i+2];
            positionTime[0][i+2] = x2;
            positionTime[1][i+2] = b2;
        }
        return positionTime;
    }
    public static Vector2[][] getFutureSingleWithVelRelativeInterpolated(PointBody[] bodies, int k, int kr, Symplectic4 integrator, double dt, int steps) {
        PointBody[] bodiesClone = new PointBody[bodies.length];
        
        for (int i=0; i<bodies.length; i++) {
            bodiesClone[i] = bodies[i].clone();
        }
        
        final int steps4 = steps * 4+1;
        Vector2[][] positionTime = new Vector2[2][steps4];
        
        Vector2 referencePosition0 = bodiesClone[kr].position().clone();
        positionTime[0][0] = bodiesClone[k].position().clone();
        positionTime[1][0] = Vectors2.sub(bodiesClone[k].velocity(), bodiesClone[kr].velocity());
        for (int i=1; i<steps4; i+=4) {
            for (int n=0; n<4; n++) {
                integrator.partialApply(n, bodiesClone, dt);
                positionTime[0][i+n] = Vectors2.sub(bodiesClone[k].position(), bodiesClone[kr].position()).add(referencePosition0);
                positionTime[1][i+n] = Vectors2.sub(bodiesClone[k].velocity(), bodiesClone[kr].velocity());
            }
            Vector2 x2 = positionTime[0][i+1];
            Vector2 b2 = positionTime[1][i+1];
            positionTime[0][i+1] = positionTime[0][i+2];
            positionTime[1][i+1] = positionTime[1][i+2];
            positionTime[0][i+2] = x2;
            positionTime[1][i+2] = b2;
        }
        return positionTime;
    }
    public static long[] getFutureSingleTimeStamps(Date date, double dt, int steps) {
        long time = date.getTime();
        long[] timeStamps = new long[steps];
        for (int i=0; i<steps; i++) {
            timeStamps[i] = time + (long)((dt*i)*1000);
        }
        return timeStamps;
    }
    public static long[] getFutureSingleTimeStampsInterpolated(Date date, double dt, int steps) {
        final double a1 = 0.5153528374311229364D;
        final double a2 =-0.0857820194129736460D;
        final double a3 = 0.4415830236164665242D;
        final double a4 = 0.1288461583653841854D;
        final double[] ist = new double[] {a4, a3, a2, a1};
        long time = date.getTime();
        final int steps4 = steps * 4+1;
        long[] timeStamps = new long[steps4];
        timeStamps[0] = time;
        for (int i=1; i<steps4; i+=4) {
            for (int n=0; n<4; n++) {
                timeStamps[i+n] = time + (long)((dt*i * ist[n])*1000);
            }
        }
        return timeStamps;
    }
    
}
