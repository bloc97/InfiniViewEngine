/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SpaceGame;

import Physics2D.Integrators.Integrator;
import Physics2D.Integrators.NBody;
import Physics2D.NBodySimulation;
import Physics2D.Integrators.FuturePath;
import Physics2D.Objects.PointBody;
import Physics2D.Vector2;
import Physics2D.Vectors2;
import SpaceGame.Objects.SpaceNatural;
import World2D.Objects.DisplayObject;
import World2D.Objects.Interpolable;
import java.util.Date;

/**
 *
 * @author bowen
 */
public class SpaceSimulation extends NBodySimulation {
    private SpaceNatural[] bodies;
    private SpaceNatural focus;
    public SpaceSimulation(Date date, SpaceNatural... bodies) {
        super(Integrator.IntegratorType.SYMPLECTIC4, 1E5, 10, 4, date, bodies);
        this.bodies = bodies;
    }
    @Override
    public void forward(int steps) {
        long newTime = date.getTime() + (long)(1000 * steps * secondsPerMiniStep/deccel);
        date = new Date(newTime);
        
        for (int i=0; i<steps; i++) {
            integrator.apply(bodies, secondsPerMiniStep/deccel);
        }/*
        //if (fCount%fWait == 0) {
        if (fCount > fWait/(ratio/initialRatio)) {
            if (focus != null) {
                reCalculateOrbits(focus);
            } else {
                reCalculateOrbits();
            }
            fCount = 0;
        }
        fCount++;*/
        reCalculateOrbits();
        
        for (int i=0; i<bodies.length; i++) {
            if (bodies[i] instanceof DisplayObject) {
                ((DisplayObject)(bodies[i])).registerUpdate();
            }
        }
    }
    
    @Override
    public void reCalculateOrbits() {

        double[] vels = new double[bodies.length];
        double[] pers = new double[bodies.length];

        if (focus == null) {
            for (int i=0; i<vels.length; i++) {
                vels[i] = Vectors2.sub(bodies[i].velocity(), bodies[i].orbiting(bodies).velocity()).norm();
            }
            double G = NBody.G;
            
            for (int i=0; i<vels.length; i++) {
                double M0 = bodies[i].orbiting(bodies).mass();
                double c0 = G*G*M0*M0;
                pers[i] = 2 * Math.PI * Math.sqrt(c0/Math.pow(vels[i], 6));
            }
            pers[5] = pers[9];
            pers[0] = 1E9;
        } else {
            double velFocus = Vectors2.sub(focus.velocity(), focus.orbiting(bodies).velocity()).norm();
            double G = NBody.G;
            double M0 = focus.orbiting(bodies).mass();
            double c0 = G*G*M0*M0;
            double focusPeriod = 2 * Math.PI * Math.sqrt(c0/Math.pow(velFocus, 6));
            
            for (int i=0; i<vels.length; i++) {
                pers[i] = focusPeriod;
            }
        }

                
        for (int i=0; i<bodies.length; i++) {
            //System.out.println((pers[i]/10/5));
            Vector2[][] posAndVel = Integrator.getFutureSingleWithVel(bodies, i, integrator, (pers[i]/50/2), 50);
            futureOrbitPos[i] = posAndVel[0];
            futureOrbitVel[i] = posAndVel[1];
            futureOrbitTime[i] = Integrator.getFutureSingleTimeStamps(date, (pers[i]/50/2), 50);
        }

        for (int i=0; i<bodies.length; i++) {
            if (bodies[i] instanceof SpaceNatural) {
                ((SpaceNatural)(bodies[i])).setOrbitPath(futureOrbitPos[i], futureOrbitVel[i], futureOrbitTime[i]);
            }
        }
    }
    public void setFocus(SpaceNatural body) {
        focus = body;
    }
    /*
    public void reCalculateOrbits(PointBody body) {
        //double futureSimRatio = ratio/4;
        //futureOrbitPos = Integrator.getFuture(bodies, integrator2, futureSimRatio, 500);
        //integrator2.reset();

        double[] vels = new double[bodies.length];
        double[] pers = new double[bodies.length];

        for (int i=0; i<vels.length; i++) {
            vels[i] = bodies[i].velocity().norm();
        }
        double G = NBody.G;
        double M0 = bodies[0].mass();
        double c0 = G*G*M0*M0;

        for (int i=0; i<vels.length; i++) {
            pers[i] = 2 * Math.PI * Math.sqrt(c0/Math.pow(vels[i], 6));
        }
        pers[0] = 1E9;
        
        Vector2[][] referencePosAndVel = new Vector2[2][0];
        Vector2[] referenceOrbitPos = referencePosAndVel[0];
        Vector2[] referenceOrbitVel;
                
        for (int i=0; i<bodies.length; i++) {
            //System.out.println((pers[i]/10/5));
            if (bodies[i] == body) {
                referencePosAndVel = Integrator.getFutureSingleWithVel(bodies, i, integrator, (5e4), 160);
                referenceOrbitPos = referencePosAndVel[0];
                referenceOrbitVel = referencePosAndVel[1];
            }
        }
                
        for (int i=0; i<bodies.length; i++) {
            //System.out.println((pers[i]/10/5));
            Vector2[][] posAndVel = Integrator.getFutureSingleWithVel(bodies, i, integrator, (5e4), 160);
            futureOrbitPos[i] = posAndVel[0];
            
            for (int n=0; n<futureOrbitPos[i].length; n++) {
                futureOrbitPos[i][n] = Vectors2.sub(posAndVel[0][n], Vectors2.sub(referenceOrbitPos[n], referenceOrbitPos[0]));
            }
            
            futureOrbitVel[i] = posAndVel[1];
            futureOrbitTime[i] = Integrator.getFutureSingleTimeStamps(date, (5e4), 160);
        }

        for (int i=0; i<bodies.length; i++) {
            if (bodies[i] instanceof FuturePath) {
                //((FuturePath)(bodies[i])).setOrbitPath(futureOrbitPos[i], futureOrbitVel[i], futureOrbitTime[i], date);
            }
        }
    }*/
}
