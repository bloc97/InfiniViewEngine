/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Physics2D.Integrators;

import Physics2D.Objects.PointBody;
import Physics2D.Vector2;
import Physics2D.Vectors2;

/**
 *
 * @author bowen
 */
public abstract class Symplectic {
    
    protected static void applySympleticPositionStep(PointBody[] bodies, double c, double time) {
        double cT = time * c;
        for (int i=0; i<bodies.length; i++) {
            bodies[i].addPosition(Vectors2.prod(bodies[i].velocity(), cT));
        }
    }
    
    protected static Vector2[] applySympleticVelocityStep(PointBody[] bodies, double d, double time) {
        double cT = time * d;
        Vector2[] currentAccel = NBody.getAccelerations(bodies);
        for (int i=0; i<bodies.length; i++) {
            bodies[i].addVelocity(Vectors2.prod(currentAccel[i], cT));
        }
        return currentAccel;
    }
}
