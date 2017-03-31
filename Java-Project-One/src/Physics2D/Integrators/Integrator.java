/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Physics2D.Integrators;

import Physics2D.Objects.PointBody;
import Physics2D.Vector2;

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
    public Vector2[] getCurrentAccelerations();
    //public PointBody[] get(PointBody[] bodies, double dt, int steps);
    //public Vector2[][] getFuture(PointBody[] bodies, double dt, int steps);
    public IntegratorType type();
    
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
    
}
