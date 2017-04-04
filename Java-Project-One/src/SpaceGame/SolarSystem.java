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
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;

/**
 *
 * @author bowen
 */
public class SolarSystem {
    Galaxy galaxy;
    String name;
    BigDecimal offsetX;
    BigDecimal offsetY;
    protected SpaceSimulation simulation;
    
    public SolarSystem() {
    }
    protected SolarSystem(Galaxy galaxy, String name, BigDecimal offsetX, BigDecimal offsetY) {
        this.galaxy = galaxy;
        this.name = name;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }
    protected SolarSystem(Galaxy galaxy, String name, Vector2 offset, Date initialDate, SpaceNatural[] bodies) {
        this.galaxy = galaxy;
        this.name = name;
        this.offsetX = new BigDecimal(offset.get(0));
        this.offsetY = new BigDecimal(offset.get(1));
        simulation = new SpaceSimulation(initialDate, bodies);
        
        for (SpaceNatural body : bodies) {
            body.setSystem(this);
        }
        //simulation.start();
    }
    public String name() {
        return name;
    }
    public BigDecimal offsetX() {
        return offsetX.add(galaxy.offsetX());
    }
    public BigDecimal offsetY() {
        return offsetY.add(galaxy.offsetY());
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
