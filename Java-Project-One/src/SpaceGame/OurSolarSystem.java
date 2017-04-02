/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SpaceGame;

import Physics.Simulation;
import Physics2D.Integrators.Integrator;
import Physics2D.NBodySimulation;
import Physics2D.Vector2;
import SpaceGame.Objects.SpaceNatural;
import World2D.Objects.DisplayObject;
import World2D.World;
import java.util.Date;

/**
 *
 * @author bowen
 */
public class OurSolarSystem implements World {
    
    private static double AU = 1.496e+11D; //AU/m
    private static double LY = 9460730472580800D; //LY/m
    
    private static double M0 = 1.98855e+30D; //M0/kg
    private static double R0 = 6.957e+5D * 1000D; //R0/m
    
    private static double DAY = 86400D;
    private Simulation[] simulations;

    public OurSolarSystem() {
        
        
        //SpaceNatural sun = generateBody("Sun", 0, 0, 0, 0, 1.989E30, 695000);
        SpaceNatural sun = generateStar("Sun", 0, 0, 1, 1);
        SpaceNatural mercury = generateBody("Mercury", 2.805339263696457E-01, 1.727431750445399E-01, -2.010150137126407E-02, 2.529075820940590E-02, 3.285E23, 2440); // 2017-03-16 - 2017-03-17
        SpaceNatural venus = generateBody("Venus", -7.028941647603416E-01, 1.359581091434492E-01, -3.813062436826709E-03, -1.996801334623488E-02, 4.867E24, 6052);
        SpaceNatural earth = generateBody("Earth", -9.882510901700633E-01, 8.499778853173919E-02, -1.680240369278054E-03, -1.719988462359221E-02, 5.972E24, 6378);
        SpaceNatural mars = generateBody("Mars", 7.780694849748854E-01, 1.279727996521010E+00, -1.143145066701317E-02, 8.466471250421175E-03, 6.39E23, 3397);
        SpaceNatural jupiter = generateBody("Jupiter", -5.232943445743797E+00, -1.525153837568292E+00, 2.022536238304965E-03, -6.887716446582768E-03, 1.898E27, 71492);
        SpaceNatural saturn = generateBody("Saturn", -1.480710269996489E+00, -9.935855469617195E+00, 5.212138334313683E-03, -8.394219517928074E-04, 5.683E26, 60268);
        SpaceNatural uranus = generateBody("Uranus", 1.822435404251011E+01, 8.083455869795067E+00, -1.623364621989834E-03, 3.411947644480543E-03, 8.681E25, 25559);
        SpaceNatural neptune = generateBody("Neptune", 2.841221822673949E+01, -9.468008842306654E+00, 9.711403807320941E-04, 2.996820640231039E-03, 1.024E26, 24766);
        SpaceNatural proximaCentauri = generateStar("Proxima Centauri", 0.2, 0.1, 0.123, 0.141);
        SpaceNatural galacticCenter = generateStar("Galactic Centre", 15000, 10000, 40000, 20);
        SpaceNatural andromeda = generateStar("Andromeda Galaxy", -2.5E6, 0, 80000, 20);
        
        Date initialDate = new Date(1489636800000l);
        
        SpaceNatural[] bigObjects = new SpaceNatural[] {sun, mercury, venus, earth, mars, jupiter, saturn, uranus, neptune};//, proximaCentauri, galacticCenter, andromeda};
        SpaceNatural[] smallObjects = new SpaceNatural[] {};
        /*
        double[] orbitalPeriodsInDays = new double[] {1E4, 87.97, 224.7, 365.26, 686.98, 4332.82, 10755.7, 30687.15, 60190.03};
        
        double[] orbitalPeriods = new double[orbitalPeriodsInDays.length];
        
        for (int i=0; i<orbitalPeriods.length; i++) {
            orbitalPeriods[i] = orbitalPeriodsInDays[i] * 86400;
            orbitalPeriods[i] = orbitalPeriods[i] / 2;
        }*/
        
        SpaceNatural[] allObjects = new SpaceNatural[bigObjects.length + smallObjects.length];
        for (int i=0; i<allObjects.length; i++) {
            if (i < smallObjects.length) {
                allObjects[i] = smallObjects[i];
            } else {
                allObjects[i] = bigObjects[i-smallObjects.length];
            }
        }
        
        
        //NBodyFuturePath futureIntegrator = new NBodyFuturePath(Integrator.IntegratorType.SYMPLECTIC4, 1E8, 200, 1, smallObjects, bigObjects);
        //NBodyFutureOrbit orbitIntegrator = new NBodyFutureOrbit(Integrator.IntegratorType.SYMPLECTIC4, 20, bigObjects, orbitalPeriods);
        SpaceSimulation space = new SpaceSimulation(initialDate, allObjects);
        
        space.start();
        simulations = new Simulation[] {space};//, orbitIntegrator, futureIntegrator};
        
    }
    
    public static SpaceNatural generateBody(String name, double xAU, double yAU, double vxAUDay, double vyAUDay, double massKg, double radiusKm) {
        Vector2 pos = new Vector2(new double[]{xAU*AU, yAU*AU});
        Vector2 vel = new Vector2(new double[]{vxAUDay*AU/DAY, vyAUDay*AU/DAY});
        return new SpaceNatural(name, pos, vel, massKg, 0, 0, radiusKm*1000);
    }
    public static SpaceNatural generateStar(String name, double xLy, double yLy, double massM0, double radiusR0) {
        Vector2 pos = new Vector2(new double[]{xLy*LY, yLy*LY});
        Vector2 vel = new Vector2(new double[]{0, 0});
        return new SpaceNatural(name, pos, vel, massM0*M0, 0, 0, radiusR0*R0);
    }
    
    @Override
    public DisplayObject[] getDisplayObjects() {
        
        int objectsCount = 0;
        for (int i=0; i<simulations.length; i++) {
            objectsCount += simulations[i].getObjectsNumber();
        }
        
        DisplayObject[] displayObjects = new DisplayObject[objectsCount];
        int objectsIndex = 0;
        
        for (int i=0; i<simulations.length; i++) {
            DisplayObject[] objects = simulations[i].getDisplayObjects();
            for (DisplayObject object : objects) {
                displayObjects[objectsIndex] = object;
                objectsIndex++;
            }
        }
        return displayObjects;
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
    public Simulation[] getSimulations() {
        return simulations;
    }
    
}
