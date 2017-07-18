/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bloc97.infinisim.Collision;

import com.bloc97.infinisim.Spatial;
import com.bloc97.uvector.Vector;
import com.bloc97.uvector.Vectors;
import java.util.AbstractMap;
import java.util.Map;

/**
 *
 * @author bowen
 */
public abstract class Helpers {

    public static Vector computeInelasticCollision(Spatial obj, Spatial obj2) {
        return Vectors.mulElem(obj.velocity(), obj.getMass()).add(Vectors.mulElem(obj2.velocity(), obj2.getMass())).div(obj.getMass() + obj2.getMass());
    }

    public static Map.Entry<Vector, Vector> computeElasticCollision(Spatial obj, Spatial obj2, double restitutionCoefficient) {
        Vector vel1 = Vectors.mulElem(obj.velocity(), obj.getMass()).add(Vectors.mulElem(obj2.velocity(), obj2.getMass())).add(Vectors.sub(obj2.velocity(), obj.velocity()).mulElem(restitutionCoefficient * obj2.getMass())).div(obj.getMass() + obj2.getMass());
        Vector vel2 = Vectors.mulElem(obj.velocity(), obj.getMass()).add(Vectors.mulElem(obj2.velocity(), obj2.getMass())).add(Vectors.sub(obj.velocity(), obj2.velocity()).mulElem(restitutionCoefficient * obj.getMass())).div(obj.getMass() + obj2.getMass());
        return new AbstractMap.SimpleEntry<>(vel1, vel2);
    }
    
    public static void merge(Spatial modify, Spatial other) {
        
        Spatial a = modify;
        Spatial b = other;
        
        double m = a.getMass() + b.getMass();
        a.position().mulElem(a.getMass()).add(Vectors.mulElem(b.position(), b.getMass())).div(m);
        a.velocity().set(computeInelasticCollision(a, b));
        
        a.setMass(m);
        
        double r1 = a.getRadius() * a.getRadius() * a.getRadius();
        double r2 = b.getRadius() * b.getRadius() * b.getRadius();
        double radius = (Math.pow(r1 + r2, 1/3d)); //3D radius
        a.setRadius(radius);
        
        
    }
    
}
