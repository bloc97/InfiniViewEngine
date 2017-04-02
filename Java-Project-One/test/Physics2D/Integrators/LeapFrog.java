/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Physics2D.Integrators;

import Physics2D.Integrators.Integrator;
import Physics2D.Integrators.NBody;
import Physics2D.Objects.Moon;
import Physics2D.Objects.Particle;
import Physics2D.Objects.Planet;
import Physics2D.Objects.PointBody;
import Physics2D.Objects.Spacecraft;
import Physics2D.Objects.Star;
import Physics2D.Vector2;
import Physics2D.Vectors2;

/**
 *
 * @author bowen
 */
public class LeapFrog implements Integrator {
    
    private Vector2[] accelerations;
    
    @Override
    public void apply(PointBody[] bodies, double dt) {
        if (dt < 0) {
            throw new IllegalArgumentException("NEGATIVE TIME NOT IMPLEMENTED");
        }
        
        if (accelerations == null) {
            accelerations = NBody.getAccelerations(bodies);
        } else if (accelerations.length != bodies.length) {
            accelerations = NBody.getAccelerations(bodies);
        }
        Vector2[] velocityHalfArray = new Vector2[bodies.length];
        
        for (int i=0; i<bodies.length; i++) {
            Vector2 velocityHalf = Vectors2.add(bodies[i].velocity(), Vectors2.prod(accelerations[i], dt/2));
            bodies[i].addPosition(Vectors2.prod(velocityHalf, dt));
            velocityHalfArray[i] = velocityHalf;
        }
        
        accelerations = NBody.getAccelerations(bodies);
        
        for (int i=0; i<bodies.length; i++) {
            bodies[i].setVelocity(Vectors2.add(velocityHalfArray[i], Vectors2.prod(accelerations[i], dt/2)));
        }
    }

    @Override
    public Vector2[] getCurrentAccelerations() {
        return accelerations;
    }
    
    @Override
    public IntegratorType type() {
        return IntegratorType.LEAPFROG;
    }

    @Override
    public void reset() {
        accelerations = null;
    }

    @Override
    public void applyByPart(Star[] stars, Planet[] planets, Moon[] moons, Particle[] particles, Spacecraft[] spacecrafts) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
