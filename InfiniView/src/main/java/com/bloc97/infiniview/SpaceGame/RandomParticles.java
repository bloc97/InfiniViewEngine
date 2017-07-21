/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bloc97.infiniview.SpaceGame;

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
public class RandomParticles extends SolarSystem {
    
    private static double AU = 1.496e+11D; //AU/m
    private static double LY = 9460730472580800D; //LY/m
    
    private static double M0 = 1.98855e+30D; //M0/kg
    private static double R0 = 6.957e+5D * 1000D; //R0/m
    
    private static double DAY = 86400D;
    
    List<SpaceNatural> bodies;

    public RandomParticles(int n, double scale, Galaxy galaxy, Vector2 offset, Date initialDate) {
        super(galaxy, "Random", offset, initialDate);
        bodies = spawnSystem(n, scale);
        
    }
    
    public List<SpaceNatural> spawnSystem(int n, double scale) {
        
        
        List<SpaceNatural> bodies = new LinkedList<>();
        
        //double density = 5500;
        double density = 5500/1E6;
        
        for (int i=0; i<n; i++) {
            double massScaleKg = 1E25 * Math.random();
            bodies.add(generateVisibleObject("R" + i, (Math.random()-0.5d) * scale, (Math.random()-0.5d) * scale, 0, 0, massScaleKg, density));
        }
        
        return bodies;
        
        //SpaceNatural[] bigObjects = new SpaceNatural[] {sun, mercury, venus, earth, mars, jupiter, saturn, uranus, neptune, luna, io};//, proximaCentauri, galacticCenter, andromeda};
        
    }
    
    public static double getRadiusFromVolume(double volume) {
        return Math.pow(volume * 3 / 4 * Math.PI, 1/3d);
    }
    
    public SpaceNatural generateBody(String name, double xAU, double yAU, double vxAUDay, double vyAUDay, double massKg, double radiusKm) {
        Vector2 pos = new Vector2(xAU*AU, yAU*AU);
        Vector2 vel = new Vector2(vxAUDay*AU/DAY, vyAUDay*AU/DAY);
        return new SpaceNatural(this, name, pos, vel, massKg, 0, 0, radiusKm*1000);
    }
    public SpaceNatural generateStar(String name, double xAU, double yAU, double vxAUDay, double vyAUDay, double massM0, double radiusR0) {
        Vector2 pos = new Vector2(xAU*AU, yAU*AU);
        Vector2 vel = new Vector2(vxAUDay*AU/DAY, vyAUDay*AU/DAY);
        return new SpaceNatural(this, name, pos, vel, massM0*M0, 0, 0, radiusR0*R0);
    }
    public SpaceNatural generateStar(String name, double xLy, double yLy, double massM0, double radiusR0) {
        Vector2 pos = new Vector2(xLy*LY, yLy*LY);
        Vector2 vel = new Vector2(0, 0);
        return new SpaceNatural(this, name, pos, vel, massM0*M0, 0, 0, radiusR0*R0);
    }
    
    
    public SpaceNatural generateVisibleObject(String name, double xAU, double yAU, double vxAUDay, double vyAUDay, double massKgScale, double density) {
        double massKg = massKgScale * (Math.random() + 0.5);
        Vector2 pos = new Vector2(xAU*AU, yAU*AU);
        Vector2 vel = new Vector2(vxAUDay*AU/DAY, vyAUDay*AU/DAY);
        return new SpaceNatural(this, name, pos, vel, massKg, 0, 0, getRadiusFromVolume(massKg/density));
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
