/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bloc97.infinisim.NBody;

import com.bloc97.infinisim.Spatial;
import com.bloc97.uvector.Vector;
import com.bloc97.uvector.Vectors;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author bowen
 */
public abstract class Integrators {
    
    public enum IntegratorType {
        NONE, EULER, SYMPLECTIC, LEAPFROG, SYMPLECTIC4
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
    public static void integrateSymplectic(Optimisers.OptimiserType otype, Equations.EquationType etype, Set<Spatial> list, double dt) {
        if (dt > 0) {
            applySympleticVelocityStep(otype, etype, list, 1, dt);
            applySympleticPositionStep(list, 1, dt);
        } else {
            applySympleticPositionStep(list, 1, dt);
            applySympleticVelocityStep(otype, etype, list, 1, dt);
        }
        
    }
    public static void integrateLeapfrog(Optimisers.OptimiserType otype, Equations.EquationType etype, Set<Spatial> list, double dt) {
        
    }
    public static void integrateSymplectic4(Optimisers.OptimiserType otype, Equations.EquationType etype, Set<Spatial> list, double dt) {
        if (dt > 0) {
            applySympleticVelocityStep(otype, etype, list, 0.1288461583653841854D, dt);
            applySympleticPositionStep(list, 0.3340036032863214255D, dt);
            applySympleticVelocityStep(otype, etype, list, 0.4415830236164665242D, dt);
            applySympleticPositionStep(list, 0.7562300005156682911D, dt);
            applySympleticVelocityStep(otype, etype, list, -0.0857820194129736460D, dt);
            applySympleticPositionStep(list, -0.2248198030794208058D, dt);
            applySympleticVelocityStep(otype, etype, list, 0.5153528374311229364D, dt);
            applySympleticPositionStep(list, 0.1344961992774310892D, dt);
        } else {
            applySympleticPositionStep(list, 0.1344961992774310892D, dt);
            applySympleticVelocityStep(otype, etype, list, 0.5153528374311229364D, dt);
            applySympleticPositionStep(list, -0.2248198030794208058D, dt);
            applySympleticVelocityStep(otype, etype, list, -0.0857820194129736460D, dt);
            applySympleticPositionStep(list, 0.7562300005156682911D, dt);
            applySympleticVelocityStep(otype, etype, list, 0.4415830236164665242D, dt);
            applySympleticPositionStep(list, 0.3340036032863214255D, dt);
            applySympleticVelocityStep(otype, etype, list, 0.1288461583653841854D, dt);
            
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
