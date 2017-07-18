/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bloc97.infinisim;


/**
 *
 * @author bowen
 */
public interface Angular2D {
    double angPosition(); //rad
    double angVelocity(); //rad/s
    
    default double getAngMomentum() { //kg*m^2/s
        return angVelocity() * getAngMass();
    }
    
    double getRadius(); //m
    void setRadius(double radius);
    
    double getAngMass(); //Moment of intertia
    void setAngMass(double angMass);
    
    default double getAngKineticEnergy() {
        double angSpeed = angVelocity();
        return 0.5d*getAngMass()*angSpeed*angSpeed;
    }
    
    void setAngle(double angle);
    void setAngVelocity(double angVel);
    
    void addAngle(double angle);
    void addAngVelocity(double angVel);
    
}
