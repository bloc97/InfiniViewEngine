/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bloc97.infinisim.NBody;

import com.bloc97.infinisim.Spatial;
import com.bloc97.infinisim.NBody.BarnesHut.BHTree;
import com.bloc97.infinisim.NBody.BarnesHut.Quad;
import com.bloc97.uvector.Vector;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author bowen
 */
public abstract class Optimisers {
    
    public enum OptimiserType {
        DIRECT, TRUNCATED_DIRECT, BARNES_HUT, TRUNCATED_BARNES_HUT
    }
    
    
    public static Map<Spatial, Vector> optimise(OptimiserType type, Equations.EquationType etype, Set<Spatial> set) {
        switch (type) {
            case DIRECT:
                return optimiseDirect(etype, set);
            case TRUNCATED_DIRECT:
                return optimiseDirect(etype, set);
            case BARNES_HUT:
                return optimiseBH(etype, set);
            case TRUNCATED_BARNES_HUT:
                return optimiseBH(etype, set);
            default:
                return optimiseDirect(etype, set);
        }
    }
    
    public static Map<Spatial, Vector> optimise(OptimiserType type, Equations.EquationType etype, Set<Spatial> set, double massRatioThreshold) {
        switch (type) {
            case DIRECT:
                return optimiseDirect(etype, set);
            case TRUNCATED_DIRECT:
                return optimiseDirect(etype, set);
            case BARNES_HUT:
                return optimiseBH(etype, set);
            case TRUNCATED_BARNES_HUT:
                return optimiseBH(etype, set);
            default:
                return optimiseDirect(etype, set);
        }
    }
    
    
    public static Map<Spatial, Vector> optimiseDirect(Equations.EquationType etype, Set<Spatial> set) {
        Map<Spatial, Vector> map = new LinkedHashMap<>();
        
        for (Spatial body : set) {
            Vector acceleration = body.velocity().shell();
            for (Spatial other : set) {
                acceleration.add(Equations.equate(etype, body, other, set));
            }
            map.put(body, acceleration);
        }
        
        return map;
    }
    
    public static Map<Spatial, Vector> optimiseBH(Equations.EquationType etype, Set<Spatial> set) {
        Map<Spatial, Vector> map = new LinkedHashMap<>();
        //Quad q = new Quad(0, 0, 1E15);
        Quad q = new Quad(0, 0, 1E15);
        BHTree tree = new BHTree(q, 0.5d);
        
        for (Spatial body : set) {
            tree.insert(body);
        }
        
        for (Spatial body : set) {
            Vector acceleration = body.velocity().shell();
            Set<Spatial> otherList = tree.getBodies(body);
            
            for (Spatial other : otherList) {
                acceleration.add(Equations.equate(etype, body, other, set));
            }
            map.put(body, acceleration);
        }
        return map;
    }
    
    /*
    
    public static Map<Spatial, Set<Spatial>> optimise(OptimiserType type, Set<Spatial> list) {
        switch (type) {
            case PATCHED_CONIC:
                return optimisePC(list);
            case DIRECT:
                return optimiseDirect(list);
            case BARNES_HUT:
                return optimiseBH(list);
            case FAST_MULTIPOLE:
                return optimiseFM(list);
            default:
                return optimiseDirect(list);
        }
    }
    
    public static Map<Spatial, Set<Spatial>> optimise(OptimiserType type, double clipMass, Set<Spatial> list) {
        switch (type) {
            case PATCHED_CONIC:
                return optimisePC(list);
            case DIRECT:
                return optimiseDirect(list);
            case BARNES_HUT:
                return optimiseBH(list);
            case FAST_MULTIPOLE:
                return optimiseFM(list);
            default:
                return optimiseDirect(list);
        }
    }
    
    public static Map<Spatial, Set<Spatial>> optimisePC(Set<Spatial> list) {
        return null;
    }
    
    public static Map<Spatial, Set<Spatial>> optimiseDC(Set<Spatial> list) {
        return null;
    }
    
    public static Map<Spatial, Set<Spatial>> optimiseDirect(Set<Spatial> list) {
        Map<Spatial, Set<Spatial>> map = new LinkedHashMap<>();
        
        for (Spatial body : list) {
            map.put(body, list);
        }
        return map;
    }
    
    public static Map<Spatial, Set<Spatial>> optimiseBH(Set<Spatial> list) {
        Map<Spatial, Set<Spatial>> map = new LinkedHashMap<>();
        //Quad q = new Quad(0, 0, 1E15);
        Quad q = new Quad(0, 0, 1E15);
        BHTree tree = new BHTree(q, 0.5d);
        
        for (Spatial body : list) {
            tree.insert(body);
        }
        
        for (Spatial body : list) {
            map.put(body, tree.getBodies(body));
        }
        return map;
    }
    public static Map<Spatial, Set<Spatial>> optimiseFM(Set<Spatial> list) {
        return null;
    }
    
    */
}
