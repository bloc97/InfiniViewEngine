/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bloc97.infinisim.NBody;

import com.bloc97.infinisim.NBody.BarnesHut.BHTree;
import com.bloc97.infinisim.NBody.BarnesHut.Quad;
import com.bloc97.infinisim.Spatial;
import com.bloc97.uvector.Vector;
import com.bloc97.uvector.Vectors;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author bowen
 */
public abstract class Integrators {
    
    
    public final static double S2OA1 = 0.7071067811865475244D;
    public final static double S2OA2 = 1 - S2OA1;
    
    public final static double S2OB1 = S2OA1;
    public final static double S2OB2 = 1 - S2OB1;
    
    public final static double S4OA1 = 0.5153528374311229364D;
    public final static double S4OA2 =-0.0857820194129736460D;
    public final static double S4OA3 = 0.4415830236164665242D;
    public final static double S4OA4 = 0.1288461583653841854D;
    
    public final static double S4OB1 = 0.1344961992774310892D;
    public final static double S4OB2 =-0.2248198030794208058D;
    public final static double S4OB3 = 0.7562300005156682911D;
    public final static double S4OB4 = 0.3340036032863214255D;
    
    public enum IntegratorType {
        NONE, EULER, SYMPLECTIC, LEAPFROG, SYMPLECTIC2, SYMPLECTIC4
    }
    
    public static void integrate(IntegratorType itype, Optimisers.OptimiserType otype, Equations.EquationType etype, Set<Spatial> list, double dt) {
        
        switch (itype) {
            case NONE:
                integrateVel(list, dt);
                break;
            case EULER:
                integrateEuler(otype, etype, list, dt);
                break;
            case SYMPLECTIC:
                integrateSymplectic(otype, etype, list, dt);
                break;
            case LEAPFROG:
                integrateLeapfrog(otype, etype, list, dt);
                break;
            case SYMPLECTIC2:
                integrateSymplectic2(otype, etype, list, dt);
                break;
            case SYMPLECTIC4:
                integrateSymplectic4(otype, etype, list, dt);
                break;
            default:
                integrateEuler(otype, etype, list, dt);
        }
    }
    
    public static void integrateVel(Set<Spatial> set, double dt) {
        
        
        for (Spatial body : set) {
            body.position().add(Vectors.mulElem(body.velocity(), dt));
        }
    }
    
    public static void integrateEuler(Optimisers.OptimiserType otype, Equations.EquationType etype, Set<Spatial> list, double dt) {
        
        Map<Spatial, Vector> accelerationMap = Optimisers.optimise(otype, etype, list);
        
        for (Spatial body : accelerationMap.keySet()) {
            body.position().add(Vectors.mulElem(body.velocity(), dt));
            body.velocity().add(Vectors.mulElem(accelerationMap.get(body), dt));
        }
    }
    
    public static void integrateLeapfrog(Optimisers.OptimiserType otype, Equations.EquationType etype, Set<Spatial> list, double dt) {
        applySympleticVelocityStep(otype, etype, list, 0.5d, dt);
        applySympleticPositionStep(list, 1, dt);
        applySympleticVelocityStep(otype, etype, list, 0.5d, dt);
    }
    
    public static void integrateSymplectic(Optimisers.OptimiserType otype, Equations.EquationType etype, Set<Spatial> list, double dt) {
        
        if (dt > 0) {
            applySympleticPositionStep(list,               1, dt);
            applySympleticVelocityStep(otype, etype, list, 1, dt);
        } else {
            applySympleticVelocityStep(otype, etype, list, 1, dt);
            applySympleticPositionStep(list,               1, dt);
        }
        
    }
    public static void integrateSymplectic2(Optimisers.OptimiserType otype, Equations.EquationType etype, Set<Spatial> list, double dt) {
        
        if (dt > 0) {
            applySympleticPositionStep(list,               S2OA2, dt);
            applySympleticVelocityStep(otype, etype, list, S2OB2, dt);
            applySympleticPositionStep(list,               S2OA1, dt);
            applySympleticVelocityStep(otype, etype, list, S2OB1, dt);
        } else {
            applySympleticVelocityStep(otype, etype, list, S2OB1, dt);
            applySympleticPositionStep(list,               S2OA1, dt);
            applySympleticVelocityStep(otype, etype, list, S2OB2, dt);
            applySympleticPositionStep(list,               S2OA2, dt);
        }
    }
    public static void integrateSymplectic4(Optimisers.OptimiserType otype, Equations.EquationType etype, Set<Spatial> list, double dt) {
        if (dt > 0) {
            applySympleticPositionStep(list,               S4OA4, dt);
            applySympleticVelocityStep(otype, etype, list, S4OB4, dt);
            applySympleticPositionStep(list,               S4OA3, dt);
            applySympleticVelocityStep(otype, etype, list, S4OB3, dt);
            applySympleticPositionStep(list,               S4OA2, dt);
            applySympleticVelocityStep(otype, etype, list, S4OB2, dt);
            applySympleticPositionStep(list,               S4OA1, dt);
            applySympleticVelocityStep(otype, etype, list, S4OB1, dt);
        } else {
            applySympleticVelocityStep(otype, etype, list, S4OB1, dt);
            applySympleticPositionStep(list,               S4OA1, dt);
            applySympleticVelocityStep(otype, etype, list, S4OB2, dt);
            applySympleticPositionStep(list,               S4OA2, dt);
            applySympleticVelocityStep(otype, etype, list, S4OB3, dt);
            applySympleticPositionStep(list,               S4OA3, dt);
            applySympleticVelocityStep(otype, etype, list, S4OB4, dt);
            applySympleticPositionStep(list,               S4OA4, dt);
        }
    }
    
    protected static void applySympleticPositionStep(Set<Spatial> list, double c, double time) {
        double cT = time * c;
        
        for (Spatial body : list) {
            body.position().add(Vectors.mulElem(body.velocity(), cT));
        }
    }
    protected static void applySympleticVelocityStep(Optimisers.OptimiserType otype, Equations.EquationType etype, Set<Spatial> list, double d, double time) {
        double cT = time * d;
        
        Map<Spatial, Vector> accelerationMap = Optimisers.optimise(otype, etype, list);
        
        for (Map.Entry<Spatial, Vector> entry : accelerationMap.entrySet()) {
            entry.getKey().velocity().add(Vectors.mulElem(entry.getValue(), cT));
        }
    }
    
    
}
