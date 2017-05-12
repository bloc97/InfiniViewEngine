/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SpaceGame;

import fmath.Approx;
import Physics.Simulation;
import Physics2D.Vector2;
import SpaceGame.Objects.SpaceNatural;
import World2D.Objects.DisplayObject;
import World2D.World;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;

/**
 *
 * @author bowen
 */
public class Universe implements World {
    
    private static double AU = 1.496e+11D; //AU/m
    private static double LY = 9460730472580800D; //LY/m
    
    private static double M0 = 1.98855e+30D; //M0/kg
    private static double R0 = 6.957e+5D * 1000D; //R0/m
    
    private static double DAY = 86400D;
    
    
    SpaceSimulation activeSimulation;
    LinkedList<DisplayObject> allDisplayObjects = new LinkedList<>();
    
    public Universe() {
        Date initialDate = new Date(1489636800000l);
        
        Galaxy milkyWay = new Galaxy("Milky Way", new Vector2(0));
        SolarSystem sol = new SolSystem(milkyWay, new Vector2(new double[] {2.469e+20, 0}), initialDate);
        sol.pushToArrayList(allDisplayObjects);
        //sol.simulation.start();
        
        SolarSystem sol2 = new SolSystem(milkyWay, new Vector2(new double[] {0, 2.469e+20}), initialDate);
        //sol2.pushToArrayList(allDisplayObjects);
        
        SolarSystem sol3 = new SolSystem(milkyWay, new Vector2(new double[] {2.469e+20, 2.469e+20}), initialDate);
        //sol3.pushToArrayList(allDisplayObjects);
        
        SolarSystem sol4 = new SolSystem(milkyWay, new Vector2(new double[] {3.469e+23, 3.469e+23}), initialDate);
        //sol4.pushToArrayList(allDisplayObjects);
        
        SolarSystem stress1 = new SolarSystem(milkyWay, "StressTest", new Vector2(new double[] {0, 0}), initialDate, scatterGen(8000));
        stress1.pushToArrayList(allDisplayObjects);
        
        //SolarSystem stress2 = new SolarSystem(milkyWay, "StressTest", new Vector2(new double[] {0, 0}), initialDate, scatterGenInvis(200000));
        //stress2.pushToArrayList(allDisplayObjects);
        
        activeSimulation = sol.simulation;
    }
    
    
    @Override
    public DisplayObject[] getDisplayObjects() {
        return allDisplayObjects.toArray(new DisplayObject[allDisplayObjects.size()]);
    }
    
    public void sortByX() {
        allDisplayObjects.sort(new Comparator<DisplayObject>() {
            @Override
            public int compare(DisplayObject o1, DisplayObject o2) {
                if (o1.getX() > o2.getX()) {
                    return 1;
                } else if (o1.getX() < o2.getX()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
    }
    
    public Simulation getSimulation() {
        return activeSimulation;
    }
    @Override
    public Simulation[] getSimulations() {
        return new Simulation[] {activeSimulation};
    }
    
    public SpaceNatural[] scatterGenInvis(int n) {
        SpaceNatural[] scatteredObjects = new SpaceNatural[n];
        
        for (int i=0; i<scatteredObjects.length; i++) {
            double r0 = randomTransformR(Math.random());
            double r = r0*3.094e+9;
            double th = randomTransformRot(Math.random(), r0)*Math.PI*2;
            Vector2 circ = new Vector2(0);
            circ.setNorm(r);
            circ.setRot(th);
            scatteredObjects[i] = generateBody("SG"+i, circ.get(0), circ.get(1), 0, 0, 1E10, 9500 + Math.random() * 10000);
        }
        
        return scatteredObjects;
    }
    
    public SpaceNatural[] scatterGen(int n) {
        SpaceNatural[] scatteredObjects = new SpaceNatural[n];
        
        for (int i=0; i<scatteredObjects.length; i++) {
            double r0 = randomTransformR(Math.random());
            double r = r0*3.094e+9;
            double th = randomTransformRot(Math.random(), r0)*Math.PI*2;
            Vector2 circ = new Vector2(0);
            circ.setNorm(r);
            circ.setRot(th);
            scatteredObjects[i] = generateBody("SG"+i, circ.get(0), circ.get(1), 0, 0, 1E30, 9500 + Math.random() * 10000);
        }
        
        return scatteredObjects;
    }
    public double randomTransformR(double d) {
        return 1/(Math.pow(d, 2))/90 - 0.01111;
    }
    public double randomTransformRot(double d, double r) {
        return Math.cos(d*16*Math.PI)/6 + (1-1/8) - d + Math.pow(r+1, 10) + 10*r;
    }
    public SpaceNatural generateBody(String name, double xAU, double yAU, double vxAUDay, double vyAUDay, double massKg, double radiusKm) {
        Vector2 pos = new Vector2(new double[]{xAU*AU, yAU*AU});
        Vector2 vel = new Vector2(new double[]{vxAUDay*AU/DAY, vyAUDay*AU/DAY});
        return new SpaceNatural(name, pos, vel, massKg, 0, 0, radiusKm*1000);
    }
    
    
}
