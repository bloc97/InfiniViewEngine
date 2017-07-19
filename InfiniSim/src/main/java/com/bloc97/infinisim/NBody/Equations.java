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
        NEWTON, POST_NEWTON, GR_APPROX, GR_DERIV
    }
    /*
    public static Map<Spatial, Vector> equate(EquationType type, Map<Spatial, Set<Spatial>> map) {
        switch (type) {
            case NEWTON:
                return equateNewton(map);
            case ADAPTED_NEWTON:
                return equateAdaptedNewton(map);
            case POST_NEWTON:
                return equatePostNewton(map);
            case GR_APPROX:
                return equateGrApprox(map);
            default:
                return equateNewton(map);
        }
    }*/
    
    public static Vector equate(EquationType type, Spatial here, Spatial other, Set<Spatial> set) {
        if (!here.equals(other)) {
            switch (type) {
                case NEWTON:
                    return equateNewton(here, other);
                case POST_NEWTON:
                    return equatePostNewton(here, other);
                case GR_APPROX:
                    return equateGrApprox(here, other);
                case GR_DERIV:
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
    
    private static Vector equateAdaptedNewton(Spatial here, Spatial other) {
        Vector relativePosition = Vectors.sub(here.position(), other.position());
        Vector relativeVelocity = Vectors.sub(here.velocity(), other.velocity());
        final double distance = relativePosition.norm();
        final double distanceCubed = distance * distance * distance;
        final double relativeSpeedSqr = relativeVelocity.normSqr();
        final double gm = G * other.getMass();

        double e1 = -gm / (distanceCubed * CSQR);
        Vector e2 = Vectors.mulElem(relativePosition, 4 * gm / distance - relativeSpeedSqr);
        Vector e3 = relativeVelocity.mulElem(Vectors.dot(relativeVelocity, relativePosition) * 4);

        return e2.add(e3).mulElem(e1).add(equateNewton(here, other));
    }
    
    private static Vector equatePostNewton(Spatial here, Spatial other) {
        Vector r = Vectors.sub(here.position(), other.position()); //Vector from this other pointing to this
        double rSqr = r.normSqr();
        
        double gmrSqr = G * other.getMass() / rSqr;
    }
    
    private static Vector equateGrDeriv(Spatial here, Spatial other, Set<Spatial> set) {
        
        Vector ve = here.velocity().shell();
        double mr = 0;
        for (Spatial i : set) {
            if (here.equals(i)) {
                continue;
            }
            double ri = Vectors.sub(here.position(), other.position()).norm(); //Vector from this other pointing to this
            Vector vi = Vectors.sub(here.velocity(), i.velocity());
            double miri = i.getMass() / ri;
            mr += miri;
            vi.mulElem(miri);
            ve.add(vi);
        }

        ve.div(mr);

        double ceSqr = CSQR * (1 - (2 * G * mr / CSQR));
        
        
        Vector r = Vectors.sub(here.position(), other.position()); //Vector from this other pointing to this
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
