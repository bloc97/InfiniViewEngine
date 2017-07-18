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
public class SolSystem extends SolarSystem {
    
    private static double AU = 1.496e+11D; //AU/m
    private static double LY = 9460730472580800D; //LY/m
    
    private static double M0 = 1.98855e+30D; //M0/kg
    private static double R0 = 6.957e+5D * 1000D; //R0/m
    
    private static double DAY = 86400D;
    
    List<SpaceNatural> bodies = spawnSystem();

    public SolSystem(Galaxy galaxy, Vector2 offset, Date initialDate) {
        super(galaxy, "Sol", offset, initialDate);
        
        //SpaceNatural sun = generateBody("Sun", 0, 0, 0, 0, 1.989E30, 695000);
        //SpaceNatural[] smallObjects = new SpaceNatural[] {};
        /*
        double[] orbitalPeriodsInDays = new double[] {1E4, 87.97, 224.7, 365.26, 686.98, 4332.82, 10755.7, 30687.15, 60190.03};
        
        double[] orbitalPeriods = new double[orbitalPeriodsInDays.length];
        
        for (int i=0; i<orbitalPeriods.length; i++) {
            orbitalPeriods[i] = orbitalPeriodsInDays[i] * 86400;
            orbitalPeriods[i] = orbitalPeriods[i] / 2;
        }*/
        /*
        SpaceNatural[] allObjects = new SpaceNatural[bigObjects.length + smallObjects.length];
        for (int i=0; i<allObjects.length; i++) {
            if (i < smallObjects.length) {
                allObjects[i] = smallObjects[i];
            } else {
                allObjects[i] = bigObjects[i-smallObjects.length];
            }
        }*/
        
        
        //NBodyFuturePath futureIntegrator = new NBodyFuturePath(Integrator.IntegratorType.SYMPLECTIC4, 1E8, 200, 1, smallObjects, bigObjects);
        //NBodyFutureOrbit orbitIntegrator = new NBodyFutureOrbit(Integrator.IntegratorType.SYMPLECTIC4, 20, bigObjects, orbitalPeriods);
        
        
        //simulation.start();
        //System.out.println("Earth: " + earth.type());
        
    }
    
    public List<SpaceNatural> spawnSystem() {
        SpaceNatural sun = generateStar("Sun", 3.164698112995927E-03, 4.430714289164239E-03, -3.379874197009493E-06, 6.606862110289219E-06, 1, 1);
        SpaceNatural mercury = generateBody("Mercury", 2.805339263696457E-01, 1.727431750445399E-01, -2.010150137126407E-02, 2.529075820940590E-02, 3.285E23, 2440); // 2017-03-16 - 2017-03-17
        SpaceNatural venus = generateBody("Venus", -7.028941647603416E-01, 1.359581091434492E-01, -3.813062436826709E-03, -1.996801334623488E-02, 4.867E24, 6052);
        SpaceNatural earth = generateBody("Earth", -9.882510901700633E-01, 8.499778853173919E-02, -1.680240369278054E-03, -1.719988462359221E-02, 5.972E24, 6378);
        SpaceNatural mars = generateBody("Mars", 7.780694849748854E-01, 1.279727996521010E+00, -1.143145066701317E-02, 8.466471250421175E-03, 6.39E23, 3397);
        SpaceNatural jupiter = generateBody("Jupiter", -5.232943445743797E+00, -1.525153837568292E+00, 2.022536238304965E-03, -6.887716446582768E-03, 1.898E27, 71492);
        SpaceNatural saturn = generateBody("Saturn", -1.480710269996489E+00, -9.935855469617195E+00, 5.212138334313683E-03, -8.394219517928074E-04, 5.683E26, 60268);
        SpaceNatural uranus = generateBody("Uranus", 1.822435404251011E+01, 8.083455869795067E+00, -1.623364621989834E-03, 3.411947644480543E-03, 8.681E25, 25559);
        SpaceNatural neptune = generateBody("Neptune", 2.841221822673949E+01, -9.468008842306654E+00, 9.711403807320941E-04, 2.996820640231039E-03, 1.024E26, 24766);
        
        SpaceNatural luna = generateBody("Luna", -9.904577058616644E-01, 8.349623043211503E-02, -1.375089243780029E-03, -1.767737909657187E-02, 734.9E20, 1737.4);
        SpaceNatural io = generateBody("Io", -5.232407848072828E+00, -1.527931361742913E+00, 1.181296153693576E-02, -4.991777558911189E-03, 893.3E20, 1821.3);
        
        
        SpaceNatural proximaCentauri = generateStar("Proxima Centauri", 0.2, 0.1, 0.123, 0.141);
        SpaceNatural galacticCenter = generateStar("Galactic Centre", 15000, 10000, 40000, 20);
        SpaceNatural andromeda = generateStar("Andromeda Galaxy", -2.5E6, 0, 80000, 20);
        
        List<SpaceNatural> bodies = new LinkedList<>();
        bodies.add(sun);
        bodies.add(mercury);
        bodies.add(venus);
        bodies.add(earth);
        bodies.add(mars);
        bodies.add(jupiter);
        bodies.add(saturn);
        bodies.add(uranus);
        bodies.add(neptune);
        bodies.add(luna);
        bodies.add(io);
        
        return bodies;
        
        //SpaceNatural[] bigObjects = new SpaceNatural[] {sun, mercury, venus, earth, mars, jupiter, saturn, uranus, neptune, luna, io};//, proximaCentauri, galacticCenter, andromeda};
        
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
