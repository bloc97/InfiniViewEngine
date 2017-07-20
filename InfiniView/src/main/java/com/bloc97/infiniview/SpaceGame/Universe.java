/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bloc97.infiniview.SpaceGame;

import com.bloc97.infinisim.Collision.CollisionSimulation;
import com.bloc97.infinisim.NBody.Equations;
import com.bloc97.infinisim.NBody.Integrators;
import com.bloc97.infinisim.NBody.NBodySimulation;
import com.bloc97.infinisim.NBody.Optimisers;
import com.bloc97.infinisim.Simulation;
import com.bloc97.infinisim.SimulationThread;
import com.bloc97.infiniview.World2D.Objects.DisplayObject;
import com.bloc97.infiniview.World2D.World;
import com.bloc97.uvector.Vector2;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

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
    
    
    LinkedList<DisplayObject> allDisplayObjects = new LinkedList<>();
    
    private final SimulationThread mainSimulationThread;
    
    public Universe() {
        Date initialDate = new Date(1489636800000l);
        
        Galaxy milkyWay = new Galaxy("Milky Way", new Vector2(0));
        //SolarSystem sol = new SolSystem(milkyWay, new Vector2(2.469e+20, 0), initialDate);
        SolarSystem sol = new SolSystem(milkyWay, new Vector2(0, 0), initialDate);
        //SolarSystem sol = new RandomParticles(4000, 10, milkyWay, new Vector2(0, 0), initialDate);
        double ratio = 1685d;
        //SolarSystem sol = new MercuryGRTest(milkyWay, new Vector2(0, 0), initialDate, ratio);
        sol.pushBodiesToList(allDisplayObjects);
        
        Simulation nBodySimulation = new NBodySimulation(Equations.EquationType.GR, Optimisers.OptimiserType.BARNES_HUT, Integrators.IntegratorType.SYMPLECTIC4);
        nBodySimulation.setObjects(sol.getBodies());
        
        List<Simulation> simulationList = new LinkedList<>();
        
        simulationList.add(nBodySimulation);
        simulationList.add(new CollisionSimulation());
        
        mainSimulationThread = new SimulationThread(simulationList, 60, 1, 1/30d/(ratio*ratio*ratio), sol.getInitialDate());
        mainSimulationThread.enable();
        
        simulationList.forEach((s) -> {s.enable();});
        
        //sol4.pushToArrayList(allDisplayObjects);
        
        
        //SolarSystem stress2 = new SolarSystem(milkyWay, "StressTest", new Vector2(new double[] {0, 0}), initialDate, scatterGenInvis(200000));
        //stress2.pushToArrayList(allDisplayObjects);
        
    }

    public SimulationThread getSimulationThread() {
        return mainSimulationThread;
    }
    
    @Override
    public List<DisplayObject> getDisplayObjects() {
        LinkedList<DisplayObject> displayObjects = new LinkedList<>();
        
        for (Simulation simulation : mainSimulationThread.getSimulations()) {
            for (Object o : simulation.getObjects()) {
                if (o instanceof DisplayObject) {
                    displayObjects.add((DisplayObject) o);
                }
            }
        }
        
        return displayObjects;
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
    
    
    
    
    
}
