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
public abstract class NBody {
    final public static double G = 6.67408E-11; //m^3 * kg^-1 * s^-2 Gravitational Constant
    final public static double tp = 5.39116E-44; //Planck time
    
    public static Vector2 getAcceleration(Vector2 other, double otherMass, Vector2 target) {
        Vector2 accelerationVector = Vectors2.sub(target, other);
        double normSquared = accelerationVector.normSquared();
        accelerationVector.setNorm(1);
        double scalar = (-G * otherMass)/normSquared;
        accelerationVector.prod(scalar);
        return accelerationVector;
    }
    public static Vector2 getAcceleration(PointBody other, PointBody target) {
        return getAcceleration(other.position(), other.mass(), target.position());
    }
    public static Vector2[] getAccelerations(PointBody[] objects) {
        Vector2[] objectAccelerations = new Vector2[objects.length];
        for (int i=0; i<objects.length; i++) {
            Vector2[] accelerationVectors = new Vector2[objects.length];
            for (int n=0; n<objects.length; n++) {
                if (i != n) {
                    accelerationVectors[n] = getAcceleration(objects[n], objects[i]);
                } else {
                    accelerationVectors[n] = new Vector2(0);
                }
            }
            Vector2 sumAccelerations = Vectors2.add(accelerationVectors);
            objectAccelerations[i] = sumAccelerations;
        }
        return objectAccelerations;
    }
    
    public static Vector2[] getAccelerations(Vector2[] positions, double[] masses) {
        Vector2[] objectAccelerations = new Vector2[positions.length];
        for (int i=0; i<positions.length; i++) {
            Vector2[] accelerationVectors = new Vector2[positions.length];
            for (int n=0; n<positions.length; n++) {
                if (i != n) {
                    accelerationVectors[n] = getAcceleration(positions[n], masses[n], positions[i]);
                } else {
                    accelerationVectors[n] = new Vector2(0);
                }
            }
            Vector2 sumAccelerations = Vectors2.add(accelerationVectors);
            objectAccelerations[i] = sumAccelerations;
        }
        return objectAccelerations;
    }
    
    
}
