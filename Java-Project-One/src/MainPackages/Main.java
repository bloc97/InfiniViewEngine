/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MainPackages;

import Physics.Integrators.Integrator.IntegratorType;
import Physics2D.NBodyFutureOrbit;
import Physics2D.NBodyFuturePath;
import Physics2D.Objects.Planet;
import Physics2D.NBodySimulation;
import Physics2D.Vector2;
import World2D.Scene;
import World2D.Viewport;
import java.text.DateFormat;
import java.util.Date;
/**
 *
 * @author Lin-Li
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    static double AU = 1.496e+11; //AU/m
    static double DAY = 86400D;
    static NBodySimulation mainsimulation;
    public static void main(String[] args) {
        
        
        Planet sun = generateBody("Sun", 0, 0, 0, 0, 1.989E30, 695000);
        Planet mercury = generateBody("Mercury", 2.805339263696457E-01, 1.727431750445399E-01, -2.010150137126407E-02, 2.529075820940590E-02, 3.285E23, 2440); // 2017-03-16 - 2017-03-17
        Planet venus = generateBody("Venus", -7.028941647603416E-01, 1.359581091434492E-01, -3.813062436826709E-03, -1.996801334623488E-02, 4.867E24, 6052);
        Planet earth = generateBody("Earth", -9.882510901700633E-01, 8.499778853173919E-02, -1.680240369278054E-03, -1.719988462359221E-02, 5.972E24, 6378);
        Planet mars = generateBody("Mars", 7.780694849748854E-01, 1.279727996521010E+00, -1.143145066701317E-02, 8.466471250421175E-03, 6.39E23, 3397);
        Planet jupiter = generateBody("Jupiter", -5.232943445743797E+00, -1.525153837568292E+00, 2.022536238304965E-03, -6.887716446582768E-03, 1.898E27, 71492);
        Planet saturn = generateBody("Saturn", -1.480710269996489E+00, -9.935855469617195E+00, 5.212138334313683E-03, -8.394219517928074E-04, 5.683E26, 60268);
        Planet uranus = generateBody("Uranus", 1.822435404251011E+01, 8.083455869795067E+00, -1.623364621989834E-03, 3.411947644480543E-03, 8.681E25, 25559);
        Planet neptune = generateBody("Neptune", 2.841221822673949E+01, -9.468008842306654E+00, 9.711403807320941E-04, 2.996820640231039E-03, 1.024E26, 24766);
        
        Date initialDate = new Date(1489636800000l);
        
        Planet[] bigObjects = new Planet[] {sun, mercury, venus, earth, mars, jupiter, saturn, uranus, neptune};
        Planet[] smallObjects = new Planet[] {};
        
        double[] orbitalPeriodsInDays = new double[] {1E4, 87.97, 224.7, 365.26, 686.98, 4332.82, 10755.7, 30687.15, 60190.03};
        
        double[] orbitalPeriods = new double[orbitalPeriodsInDays.length];
        
        for (int i=0; i<orbitalPeriods.length; i++) {
            orbitalPeriods[i] = orbitalPeriodsInDays[i] * 86400;
            orbitalPeriods[i] = orbitalPeriods[i] / 2;
        }
        
        Planet[] allObjects = new Planet[bigObjects.length + smallObjects.length];
        for (int i=0; i<allObjects.length; i++) {
            if (i < smallObjects.length) {
                allObjects[i] = smallObjects[i];
            } else {
                allObjects[i] = bigObjects[i-smallObjects.length];
            }
        }
        
        
        NBodyFuturePath futureIntegrator = new NBodyFuturePath(IntegratorType.SYMPLECTIC4, 1E8, 200, 1, smallObjects, bigObjects);
        NBodyFutureOrbit orbitIntegrator = new NBodyFutureOrbit(IntegratorType.SYMPLECTIC4, 20, bigObjects, orbitalPeriods);
        NBodySimulation space = new NBodySimulation(IntegratorType.SYMPLECTIC4, 1E6, 30, 1, futureIntegrator, orbitIntegrator, initialDate, allObjects);
        
        Scene scene = new Scene(60, 1920, 1080, space);
        Viewport viewport = new Viewport(1920, 1080, scene);
        
        
        scene.setDisplayObjects(space.getDisplayObjects());
        
        scene.start();
        space.start();
        
        mainsimulation = space;
        
    }
    
    public static Planet generateBody(String name, double xAU, double yAU, double vxAUDay, double vyAUDay, double massKg, double radiusKm) {
        Vector2 pos = new Vector2(new double[]{xAU*AU, yAU*AU});
        Vector2 vel = new Vector2(new double[]{vxAUDay*AU/DAY, vyAUDay*AU/DAY});
        return new Planet(name, pos, vel, massKg, radiusKm*1000);
    }
    
}
