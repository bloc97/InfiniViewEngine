/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SpaceGame;

import Physics2D.Vector2;
import Physics2D.Vectors2;
import SpaceGame.Objects.SpaceNatural;
import World2D.Objects.DisplayObject;
import java.util.Date;
import java.util.LinkedList;

/**
 *
 * @author bowen
 */
public class SolarSystem {
    Galaxy galaxy;
    String name;
    Vector2 offset;
    protected SpaceSimulation simulation;
    
    public SolarSystem() {
    }
    protected SolarSystem(Galaxy galaxy, String name, Vector2 offset) {
        this.galaxy = galaxy;
        this.name = name;
        this.offset = offset;
    }
    protected SolarSystem(Galaxy galaxy, String name, Vector2 offset, Date initialDate, SpaceNatural[] bodies) {
        this.galaxy = galaxy;
        this.name = name;
        this.offset = offset;
        simulation = new SpaceSimulation(initialDate, bodies);
        
        for (SpaceNatural body : bodies) {
            body.setSystem(this);
        }
        //simulation.start();
    }
    public String name() {
        return name;
    }
    public Vector2 offset() {
        return Vectors2.add(offset, galaxy.offset());
    }
    public Galaxy galaxy() {
        return galaxy;
    }
    public void pushToArrayList(LinkedList<DisplayObject> allDisplayObjects) {
        DisplayObject[] objects = simulation.getDisplayObjects();
        for (DisplayObject object : objects) {
            allDisplayObjects.push(object);
        }
    }
}
