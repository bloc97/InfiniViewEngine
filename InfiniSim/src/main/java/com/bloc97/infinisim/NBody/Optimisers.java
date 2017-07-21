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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author bowen
 */
public abstract class Optimisers {
    
    public enum OptimiserType {
        DIRECT, BARNES_HUT
    }
    
    
    public static Map<Spatial, Vector> optimise(OptimiserType type, Equations.EquationType etype, Set<Spatial> set) {
        switch (type) {
            case DIRECT:
                return optimiseDirect(etype, set);
            case BARNES_HUT:
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
        Quad q = new Quad(0, 0, 1E15);
        BHTree tree = new BHTree(q, 0.5d);
        
        for (Spatial body : set) {
            tree.insert(body);
        }
        
        Map<Spatial, Vector> map = new LinkedHashMap<>();
        
        for (Spatial body : set) {
            Vector acceleration = body.velocity().shell();
            List<Spatial> otherList = tree.getBodiesAsList(body);
            
            for (Spatial other : otherList) {
                acceleration.add(Equations.equate(etype, body, other, otherList));
            }
            map.put(body, acceleration);
        }
        return map;
    }
    
}
