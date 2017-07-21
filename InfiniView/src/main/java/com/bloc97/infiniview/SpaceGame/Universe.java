/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bloc97.infiniview.SpaceGame;

import com.bloc97.infinisim.NBody.Equations;
import com.bloc97.infinisim.NBody.Integrators;
import com.bloc97.infinisim.NBody.NBodySimulation;
import com.bloc97.infinisim.NBody.Optimisers;
import com.bloc97.infinisim.Simulation;
import com.bloc97.infinisim.RealTimeSimulation;
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
    
    LinkedList<DisplayObject> allDisplayObjects = new LinkedList<>();
    
    private final RealTimeSimulation mainRealTimeSimulation;
    
    public Universe() {
        Date initialDate = new Date(1489636800000l);
        
        Galaxy milkyWay = new Galaxy("Milky Way", new Vector2(0));
        //SolarSystem sol = new SolSystem(milkyWay, new Vector2(2.469e+20, 0), initialDate);
        //SolarSystem sol = new SolSystem(milkyWay, new Vector2(0, 0), initialDate);
        SolarSystem sol = new RandomParticles(1000, 10, milkyWay, new Vector2(0, 0), initialDate);
        double ratio = 1685d;
        //SolarSystem sol = new MercuryGRTest(milkyWay, new Vector2(0, 0), initialDate, ratio);
        sol.pushBodiesToList(allDisplayObjects);
        
        //Simulation nBodySimulation = new NBodySimulation(Equations.EquationType.GR, Optimisers.OptimiserType.BARNES_HUT, Integrators.IntegratorType.SYMPLECTIC4, 1/30d/(ratio*ratio*ratio), 1, sol.getInitialDate());
        Simulation nBodySimulation = new NBodySimulation(Equations.EquationType.GR, Optimisers.OptimiserType.BARNES_HUT, Integrators.IntegratorType.SYMPLECTIC, 1/30d/(ratio*ratio*ratio), 1, sol.getInitialDate(), true);
        nBodySimulation.setObjects(sol.getBodies());
        
        mainRealTimeSimulation = new RealTimeSimulation(nBodySimulation, 60);
        //sol4.pushToArrayList(allDisplayObjects);
        
        
        //SolarSystem stress2 = new SolarSystem(milkyWay, "StressTest", new Vector2(new double[] {0, 0}), initialDate, scatterGenInvis(200000));
        //stress2.pushToArrayList(allDisplayObjects);
        
    }

    public RealTimeSimulation getRealTimeSimulation() {
        return mainRealTimeSimulation;
    }
    
    @Override
    public List<DisplayObject> getDisplayObjects() {
        LinkedList<DisplayObject> displayObjects = new LinkedList<>();
        
        //for (Simulation simulation : mainSimulationThread.getSimulations()) {
            
            Simulation simulation = mainRealTimeSimulation.getSimulation();
            
            for (Object o : simulation.getObjects()) {
                if (o instanceof DisplayObject) {
                    displayObjects.add((DisplayObject) o);
                }
            }
        //}
        
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
