/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bloc97.infinisim;

import com.bloc97.infinisim.NBody.NBodySimulationK;
import com.bloc97.infinisim.NBody.NBodyWorld;
import com.bloc97.infinisim.Spatial;
import com.bloc97.uvector.Vector3;

/**
 *
 * @author bowen
 */

public class Spatial3DView implements Spatial<Vector3> {

    private int i;
    private NBodyWorld world;

    public Spatial3DView(int i, NBodyWorld world) {
        if (i < 0) {
            this.i = 0;
        } else if (i >= world.getMass().length) {
            this.i = world.getMass().length-1;
        } else {
            this.i = i;
        }
        this.world = world;
    }

    @Override
    public Vector3 position() {
        return new Vector3(world.getPosition()[i * 3], world.getPosition()[i * 3 + 1], world.getPosition()[i * 3 + 2]);
    }

    @Override
    public Vector3 velocity() {
        return new Vector3(world.getVelocity()[i * 3], world.getVelocity()[i * 3 + 1], world.getVelocity()[i * 3 + 2]);
    }

    @Override
    public double getMass() {
        return world.getMass()[i];
    }
    
    public boolean isActive() {
        return world.getIsActive()[i];
    }

    @Override
    public void setMass(double mass) {
    }

    @Override
    public double getRadius() {
        return world.getRadius()[i];
    }

    @Override
    public void setRadius(double radius) {
    }

    @Override
    public Spatial<Vector3> clone() {
        return new Spatial3DView(i, world);
    }

}
