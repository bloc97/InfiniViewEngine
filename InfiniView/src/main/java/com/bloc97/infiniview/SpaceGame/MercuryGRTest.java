/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bloc97.infiniview.SpaceGame;

import com.bloc97.infinisim.NBody.Equations;
import com.bloc97.infinisim.Spatial;
import com.bloc97.infiniview.SpaceGame.Objects.SpaceNatural;
import com.bloc97.uvector.Vector2;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author bowen
 */
public class MercuryGRTest extends SolarSystem {
    
    private static double AU = 1.496e+11D; //AU/m
    private static double LY = 9460730472580800D; //LY/m
    
    private static double M0 = 1.98855e+30D; //M0/kg
    private static double R0 = 6.957e+5D * 1000D; //R0/m
    
    private static double DAY = 86400D;
    
    private final List<SpaceNatural> bodies;

    public MercuryGRTest(Galaxy galaxy, Vector2 offset, Date initialDate, double scale) {
        super(galaxy, "Sol", offset, initialDate);
        bodies = spawnSystem(scale);
    }
    
    public List<SpaceNatural> spawnSystem(double scale) {
        SpaceNatural sun = generateStar("Sun", 0, 0, 1.3278E20 / Equations.G, 1 / (scale * scale));
        SpaceNatural mercury = generateBody("Mercury", 6.982E10 / (scale * scale), 0, 0, 38860 * scale, 3.285E23, 2440 / (scale * scale)); // 2017-03-16 - 2017-03-17
        
        List<SpaceNatural> bodies = new LinkedList<>();
        bodies.add(sun);
        bodies.add(mercury);
        
        return bodies;
        
        //SpaceNatural[] bigObjects = new SpaceNatural[] {sun, mercury, venus, earth, mars, jupiter, saturn, uranus, neptune, luna, io};//, proximaCentauri, galacticCenter, andromeda};
        
    }
    
    public SpaceNatural generateBody(String name, double xm, double ym, double xm_s, double ym_s, double massKg, double radiusKm) {
        Vector2 pos = new Vector2(xm, ym);
        Vector2 vel = new Vector2(xm_s, ym_s);
        return new SpaceNatural(this, name, pos, vel, massKg, 0, 0, radiusKm*1000);
    }
    public SpaceNatural generateStar(String name, double xLy, double yLy, double massKg, double radiusR0) {
        Vector2 pos = new Vector2(xLy*LY, yLy*LY);
        Vector2 vel = new Vector2(0, 0);
        return new SpaceNatural(this, name, pos, vel, massKg, 0, 0, radiusR0*R0);
    }
    
    /*
    @Override
    public DisplayObject[] getDisplayObjects() {
        DisplayObject[] planets = simulations[0].getDisplayObjects();
        DisplayObject[] orbits = simulations[1].getDisplayObjects();
        DisplayObject[] lines = simulations[2].getDisplayObjects();
        
        int objectsCount = 0;
        
        for (DisplayObject planet : planets) {
            if (!planet.isHidden()) {
                objectsCount++;
            }
        }
        for (DisplayObject orbit : orbits) {
            if (!orbit.isHidden()) {
                objectsCount++;
            }
        }
        for (DisplayObject line : lines) {
            if (!line.isHidden()) {
                objectsCount++;
            }
        }
        DisplayObject[] displayObjects = new DisplayObject[objectsCount];
        int objectsIndex = 0;
        
        for (DisplayObject orbit : orbits) {
            if (!orbit.isHidden()) {
                displayObjects[objectsIndex] = orbit;
                objectsIndex++;
            }
        }
        for (DisplayObject line : lines) {
            if (!line.isHidden()) {
                displayObjects[objectsIndex] = line;
                objectsIndex++;
            }
        }
        for (DisplayObject planet : planets) {
            if (!planet.isHidden()) {
                displayObjects[objectsIndex] = planet;
                objectsIndex++;
            }
        }
        return displayObjects;
    }*/


    @Override
    public void pushBodiesToList(List list) {
        for (SpaceNatural spaceNatural : bodies) {
            list.add(spaceNatural);
        }
    }

    @Override
    public Set<Spatial> getBodies() {
        Set<Spatial> set = new LinkedHashSet<>();
        for (SpaceNatural spaceNatural : bodies) {
            set.add((Spatial) spaceNatural);
        }
        return set;
    }

    
}
