/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bloc97.infiniview.SpaceGame;

import com.bloc97.infinisim.NBody.NBodyWorld;
import com.bloc97.uvector.Vector2;
import java.util.Date;

/**
 *
 * @author bowen
 */
public class Universe {
    
    //LinkedList<DisplayObject> allDisplayObjects = new LinkedList<>();
    
    private NBodyWorld world;
    
    public Universe() {
        Date initialDate = new Date(1489636800000l);
        
        Galaxy milkyWay = new Galaxy("Milky Way", new Vector2(0));
        //SolarSystem sol = new SolSystem(milkyWay, new Vector2(2.469e+20, 0), initialDate);
        SolarSystem sol = new SolSystem(milkyWay, new Vector2(0, 0), initialDate);
        //SolarSystem sol = new RandomParticles(2000, 100, milkyWay, new Vector2(0, 0), initialDate);
        double ratio = 1685d;
        //SolarSystem sol = new MercuryGRTest(milkyWay, new Vector2(0, 0), initialDate, ratio);
        //sol.pushBodiesToList(allDisplayObjects);
        
        world = new NBodyWorld(sol.getBodies(), 0, initialDate);
        
        //IntegratorOpenCLKernel.INSTANCE.setExecutionModeWithoutFallback(Kernel.EXECUTION_MODE.CPU);
        
        //Simulation nBodySimulation = new NBodySimulation(Equations.EquationType.GR, Optimisers.OptimiserType.BARNES_HUT, Integrators.IntegratorType.SYMPLECTIC4, 1/30d/(ratio*ratio*ratio), 1, sol.getInitialDate());
        //mainNBodySimulation = new NBodySimulationK(Equations.EquationType.GR, Optimisers.OptimiserType.BARNES_HUT, sol.getBodies(), 100, 30d, 1/30d/(ratio*ratio*ratio), 1, sol.getInitialDate(), true);
        
        //mainRealTimeSimulation = new RealTimeSimulation(nBodySimulation, 30);
        //sol4.pushToArrayList(allDisplayObjects);
        
        
        //SolarSystem stress2 = new SolarSystem(milkyWay, "StressTest", new Vector2(new double[] {0, 0}), initialDate, scatterGenInvis(200000));
        //stress2.pushToArrayList(allDisplayObjects);
        
    }
    

    public NBodyWorld getWorld() {
        return world;
    }
    
    /*
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
    }*/
    
    
    
    
    
}
