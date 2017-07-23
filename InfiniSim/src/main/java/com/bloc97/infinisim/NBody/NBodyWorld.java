/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bloc97.infinisim.NBody;

import com.bloc97.infinisim.Spatial;
import com.bloc97.infinisim.Spatial3DView;
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author bowen
 */
public class NBodyWorld {
    
    //General Information
    private double[] position, velocity; //Stored as i,j,k
    private double[] mass, radius;
    private int[] collided;
    private boolean[] isActive;
    private int currentSize;
    
    private Date date;
    

    public NBodyWorld(Collection<Spatial> list, int initialSize, Date date) {
        this.date = date;
        initArrays(list, initialSize);
    }
    
    public double[] getPosition() {
        return position;
    }

    public double[] getVelocity() {
        return velocity;
    }

    public double[] getMass() {
        return mass;
    }

    public double[] getRadius() {
        return radius;
    }

    public int[] getCollided() {
        return collided;
    }

    public boolean[] getIsActive() {
        return isActive;
    }

    public Date getDate() {
        return date;
    }
    
    public int size() {
        return mass.length;
    }
    
    public Spatial3DView get3DView(int i) {
        if (i < 0 || i >= mass.length) {
            return null;
        } else if (!isActive[i]) {
            return null;
        } else {
            return new Spatial3DView(i, this);
        }
    }
    
    public final void initArrays(Collection<Spatial> list, int initialSize) {
        
        int length = Math.max(list.size(), initialSize);
        
        position = new double[length * 3];
        velocity = new double[length * 3];

        mass = new double[length];
        radius = new double[length];

        collided = new int[length];
        isActive = new boolean[length];

        int i=0;
        for (Spatial body : list) {

            position[i * 3] = body.position().get(0);
            position[i * 3 + 1] = body.position().get(1);
            position[i * 3 + 2] = body.position().get(2);

            velocity[i * 3] = body.velocity().get(0);
            velocity[i * 3 + 1] = body.velocity().get(1);
            velocity[i * 3 + 2] = body.velocity().get(2);

            mass[i] = body.getMass();
            radius[i] = body.getRadius();
            collided[i] = i;
            isActive[i] = true;

            i++;
        }
        currentSize = i;
    }
    
    public boolean addObject(double x, double y, double z, double vx, double vy, double vz, double mass, double radius) {
        for (int i=0; i<this.mass.length; i++) {
            if (!isActive[i]) {
                position[i * 3] = x;
                position[i * 3 + 1] = y;
                position[i * 3 + 2] = z;
                velocity[i * 3] = vx;
                velocity[i * 3 + 1] = vy;
                velocity[i * 3 + 2] = vz;
                this.mass[i] = mass;
                this.radius[i] = radius;
                collided[i] = i;
                isActive[i] = true;
                return true;
            }
        }
        return false;
    }
    
    public boolean removeObject(int i) {
        if (i >= 0 && i < isActive.length) {
            if (!isActive[i]) {
                isActive[i] = false;
                return true;
            }
        }
        return false;
    }
}
