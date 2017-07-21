package com.bloc97.infinisim;

import com.bloc97.uvector.Vector;
import com.bloc97.uvector.Vectors;




/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author bowen
 * @param <T>
 */
public interface Spatial<T extends Vector<T>> {
    T position(); //m
    T velocity(); //m/s
    
    default T getMomentum() { //kg*m/s
        return Vectors.mulElem(velocity(), getMass());
    }
    
    double getMass(); //kg
    void setMass(double mass);
    
    double getRadius(); //m
    void setRadius(double radius);
    
    default double getSpeed() {
        return velocity().norm();
    }
    
    default double getKineticEnergy() {
        double speed = getSpeed();
        return 0.5d*getMass()*speed*speed;
    }

    default boolean equals(Spatial body) {
        return getMass() == body.getMass() && getRadius() == body.getRadius() && position().equals(body.position()) && velocity().equals(body.velocity());
    }
    
    Spatial<T> clone();
    
    static class SpatialImpl implements Spatial {
        
        private final Vector position;
        private final Vector velocity;
        private double mass;
        private double radius;
            
        SpatialImpl(Spatial body) {
            position = body.position().copy();
            velocity = body.velocity().copy();
            mass = body.getMass();
            radius = body.getRadius();
        }
        
        @Override
        public Vector position() {
            return position;
        }

        @Override
        public Vector velocity() {
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
        public void setRadius(double radius) {
            this.radius = radius;
        }

        public String toString() {
            return "Spatial (Copy): " + position + velocity + " " + mass + " kg " + radius + " m";
        }

        @Override
        public Spatial clone() {
            return new SpatialImpl(this);
        }
        
    }
    
    public static Spatial createSpatialCopy(Spatial body) {
        return new SpatialImpl(body);
    }
    
}
