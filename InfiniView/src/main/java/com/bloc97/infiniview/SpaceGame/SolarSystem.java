/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bloc97.infiniview.SpaceGame;

import com.bloc97.infinisim.Spatial;
import com.bloc97.infiniview.SpaceGame.Objects.SpaceNatural;
import com.bloc97.infiniview.World2D.Objects.DisplayObject;
import com.bloc97.uvector.Vector2;
import com.bloc97.uvector.Vectors;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author bowen
 */
public abstract class SolarSystem {
    Galaxy galaxy;
    String name;
    Vector2 offset;
    Date initialDate;
    
    protected SolarSystem(Galaxy galaxy, String name, Vector2 offset) {
        this.galaxy = galaxy;
        this.name = name;
        this.offset = offset;
    }
    protected SolarSystem(Galaxy galaxy, String name, Vector2 offset, Date initialDate) {
        this.galaxy = galaxy;
        this.name = name;
        this.offset = offset;
        this.initialDate = initialDate;
    }
    public String name() {
        return name;
    }
    public Vector2 offset() {
        return Vectors.add(offset, galaxy.offset());
    }
    public Galaxy galaxy() {
        return galaxy;
    }

    public Date getInitialDate() {
        return initialDate;
    }
    
    public abstract Set<Spatial> getBodies();
    public abstract void pushBodiesToList(List<?> list);
    
}
