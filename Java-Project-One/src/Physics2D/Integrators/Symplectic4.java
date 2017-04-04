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
public class Symplectic4 implements Integrator {
    
    private Vector2[] accelerations;
    final double a1 = 0.5153528374311229364D;
    final double a2 =-0.0857820194129736460D;
    final double a3 = 0.4415830236164665242D;
    final double a4 = 0.1288461583653841854D;

    final double b1 = 0.1344961992774310892D;
    final double b2 =-0.2248198030794208058D;
    final double b3 = 0.7562300005156682911D;
    final double b4 = 0.3340036032863214255D;

    final double[] c = new double[] {a4, a3, a2, a1};
    final double[] d = new double[] {b4, b3, b2, b1};
    
    @Override
    public void apply(PointBody[] bodies, double dt) {
        
        if (dt > 0) {
            for (int i=0; i<4; i++) {
                Symplectic.applySympleticPositionStep(bodies, c[i], dt);
                accelerations = Symplectic.applySympleticVelocityStep(bodies, d[i], dt);
            }
        } else {
            for (int i=3; i>-1; i--) {
                accelerations = Symplectic.applySympleticVelocityStep(bodies, d[i], dt);
                Symplectic.applySympleticPositionStep(bodies, c[i], dt);
            }
        }
        
    }
    @Override
    public void partialApply(int n, PointBody[] bodies, double dt) {
        Symplectic.applySympleticPositionStep(bodies, c[n], dt);
        accelerations = Symplectic.applySympleticVelocityStep(bodies, d[n], dt);
    }

    @Override
    public Vector2[] getCurrentAccelerations() {
        return accelerations;
    }
    @Override
    public IntegratorType type() {
        return IntegratorType.SYMPLECTIC4;
    }
    @Override
    public void reset() {
        accelerations = null;
    }

    @Override
    public void applyByPart(PointBody[] bodies, double dt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
