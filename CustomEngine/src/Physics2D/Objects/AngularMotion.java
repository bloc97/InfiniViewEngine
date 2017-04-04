/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Physics2D.Objects;

import Physics2D.Vector2;

/**
 *
 * @author bowen
 */
public interface AngularMotion {
    double radius();
    double angle();
    double angVelocity();
    //double angAcceleration();
    //double torque();
    
    double angMomentum();
    double angMass();
    
    
    void setAngle(double angle);
    void setAngVelocity(double angVel);
    
    void addAngle(double angle);
    void addAngVelocity(double angVel);
    
    //void setTorque(double c);
    //void update(double time);
    //void revert();
}
