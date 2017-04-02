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
public class RoundBody extends PointBody implements AngularMotion {
    
    private final double radius;
    private double angPos; //angle in radians
    private double angVel;
    private final double angMass; //Moment of inertia perpendicular to disk surface
    
    public RoundBody(Vector2 position, Vector2 velocity, double mass, double angPos, double angVel, double radius) {
        super(position, velocity, mass);
        this.angPos = angPos;
        this.angVel = angVel;
        this.radius = radius;
        this.angMass = (mass() * radius * radius)/2;
    }
    
    @Override
    public double radius() {
        return radius;
    }

    @Override
    public double angle() {
        return angPos;
    }

    @Override
    public double angVelocity() {
        return angVel;
    }

    @Override
    public double angMomentum() {
        return angMass * angVel;
    }

    @Override
    public double angMass() {
        return angMass;
    }

    @Override
    public void setAngle(double angle) {
        this.angPos = angle;
    }

    @Override
    public void setAngVelocity(double angVel) {
        this.angVel = angVel;
    }

    @Override
    public void addAngle(double angle) {
        this.angPos += angle;
    }

    @Override
    public void addAngVelocity(double angVel) {
        this.angVel += angVel;
    }
    
}
