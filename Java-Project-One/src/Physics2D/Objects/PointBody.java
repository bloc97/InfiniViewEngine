/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Physics2D.Objects;

import Physics2D.Vector2;
import World2D.Objects.DisplayObject;

/**
 *
 * @author bowen
 */
public class PointBody implements LinearMotion {
    private Vector2 position;
    private Vector2 velocity;
    final private double mass;
    
    public PointBody() {
        position = new Vector2(0);
        velocity = new Vector2(0);
        mass = 1;
    }
    public PointBody(double mass) {
        position = new Vector2(0);
        velocity = new Vector2(0);
        this.mass = mass;
    }
    public PointBody(Vector2 position, double mass) {
        this.position = position.clone();
        velocity = new Vector2(0);
        this.mass = mass;
    }
    public PointBody(Vector2 position, Vector2 velocity, double mass) {
        this.position = position.clone();
        this.velocity = velocity.clone();
        this.mass = mass;
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
    public double momentum(int i) {
        return velocity.get(i) * mass;
    }
    
    public double speed() {
        return velocity.norm();
    }
    
    public double kineticEnergy() {
        double speed = velocity.norm();
        return 0.5*mass*speed*speed;
    }

    @Override
    public double mass() {
        return mass;
    }
    
    @Override
    public void setPosition(Vector2 position) {
        this.position = position;
    }
    
    @Override
    public void addPosition(Vector2 position) {
        this.position.add(position);
    }

    @Override
    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }
    
    @Override
    public void addVelocity(Vector2 velocity) {
        this.velocity.add(velocity);
    }
    @Override
    public PointBody clone() {
        return new PointBody(position, velocity, mass);
    }

    public void update() {
        throw new UnsupportedOperationException("No graphic component to update.");
    }
    
    public DisplayObject getDisplayObject() {
        throw new UnsupportedOperationException("No graphic component to display.");
    }
}
