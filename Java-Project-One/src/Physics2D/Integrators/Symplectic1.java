/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Physics2D.Integrators;

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
public class Symplectic1 implements Integrator {
    
    private Vector2[] accelerations;
    
    @Override
    public void apply(PointBody[] bodies, double dt) {
        if (dt > 0) {
            accelerations = Symplectic.applySympleticVelocityStep(bodies, 1, dt);
            Symplectic.applySympleticPositionStep(bodies, 1, dt);
        } else {
            Symplectic.applySympleticPositionStep(bodies, 1, dt);
            accelerations =  Symplectic.applySympleticVelocityStep(bodies, 1, dt);
        }
        
    }

    @Override
    public Vector2[] getCurrentAccelerations() {
        return accelerations;
    }
    
    @Override
    public IntegratorType type() {
        return IntegratorType.SYMPLECTIC1;
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
