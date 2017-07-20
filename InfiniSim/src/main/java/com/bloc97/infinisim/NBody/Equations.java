/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bloc97.infinisim.NBody;

import com.bloc97.infinisim.Spatial;
import com.bloc97.uvector.Vector;
import com.bloc97.uvector.Vectors;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author bowen
 */
public abstract class Equations {
    public static final double G = 6.67408E-11; //m^3 * kg^-1 * s^-2 Gravitational Constant
    public static final double C = 299792458d; //m/s Speed of light
    public static final double CSQR = 89875517873681764d;
    
    public enum EquationType {
        NEWTON, GR
    }
    
    public static Vector equate(EquationType type, Spatial here, Spatial other, Set<Spatial> set) {
        if (!here.equals(other)) {
            switch (type) {
                case NEWTON:
                    return equateNewton(here, other);
                case GR:
                    return equateGrDeriv(here, other, set);
                default:
                    return equateNewton(here, other);
            }
        } else {
            return here.position().shell();
        }
    }
    
    private static Vector equateNewton(Spatial here, Spatial other) {
            Vector relativePosition = Vectors.sub(other.position(), here.position()); //Vector from this body pointing to other
            double distanceSqr = relativePosition.normSqr();
            return Vectors.mulElem(relativePosition.div(Math.sqrt(distanceSqr)), other.getMass() * G / distanceSqr);
    }
    
    private static Vector equateGrDeriv(Spatial here, Spatial other, Set<Spatial> set) {
        
        Vector ve = here.velocity().shell();
        double mr = 0;
        for (Spatial i : set) {
            if (here.equals(i)) {
                continue;
            }
            double ri = Vectors.sub(here.position(), other.position()).norm(); //Vector from other pointing to this
            Vector vi = Vectors.sub(here.velocity(), i.velocity());
            double miri = i.getMass() / ri;
            mr += miri;
            vi.mulElem(miri);
            ve.add(vi);
        }

        ve.div(mr);

        double ceSqr = CSQR * (1 - (2 * G * mr / CSQR));
        
        
        Vector r = Vectors.sub(here.position(), other.position());
        double rSqr = r.normSqr();
        
        Vector rhat = r.copy().normalise();
        Vector vehat = ve.copy().normalise();
        
        double veSqr = ve.normSqr();
        double veFour = veSqr * veSqr;
        double ceFour = ceSqr * ceSqr;
        
        double gmrSqr = G * other.getMass() / rSqr;
        double dot = Vectors.dot(rhat, vehat);
        double cve = (1 - (3 * veSqr / ceSqr) + (veFour / ceFour));
        
        Vector ve2Cross = Vectors.cross_AxBxB(rhat, vehat);
        
        return ve2Cross.mulElem(gmrSqr).add(vehat.mulElem(-gmrSqr * dot * cve));
    }
    
}
