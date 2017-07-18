/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bloc97.infinisim;

import com.bloc97.infinisim.Spatial;
import com.bloc97.uvector.Vector2;

/**
 *
 * @author bowen
 */
public class Body2D implements Spatial<Vector2>, Angular2D {
    
    private final Vector2 position;
    private final Vector2 velocity;
    private double mass, radius, angMass;
    private double angPosition, angVelocity;

    public Body2D(Vector2 position, Vector2 velocity, double mass, double radius, double angMass, double angPosition, double angVelocity) {
        this.position = position;
        this.velocity = velocity;
        this.mass = mass;
        this.radius = radius;
        this.angMass = angMass;
        this.angPosition = angPosition;
        this.angVelocity = angVelocity;
    }

    @Override
    public Vector2 position() {
        return position;
    }

    @Override
    public Vector2 velocity() {
        return velocity;
    }

    @Override
    public double getMass() {
        return mass;
    }

    @Override
    public void setMass(double mass) {
        this.mass = mass;
    }

    @Override
    public double getRadius() {
        return radius;
    }

    @Override
    public double getAngMass() {
        return angMass;
    }

    @Override
    public double angPosition() {
        return angPosition;
    }

    @Override
    public double angVelocity() {
        return angVelocity;
    }

    @Override
    public void setAngle(double angle) {
        angPosition = angle;
    }

    @Override
    public void setAngVelocity(double angVel) {
        angVelocity = angVel;
    }

    @Override
    public void addAngle(double angle) {
        angPosition += angle;
    }

    @Override
    public void addAngVelocity(double angVel) {
        angVelocity += angVel;
    }

    @Override
    public void setRadius(double radius) {
        this.radius = radius;
    }

    @Override
    public void setAngMass(double angMass) {
        this.angMass = angMass;
    }
    
    
    
}
