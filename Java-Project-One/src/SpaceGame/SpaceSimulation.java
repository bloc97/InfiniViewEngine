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
import Physics2D.Integrators.Symplectic4;
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
    private int focusIndex = -1;
    private Symplectic4 integrator;
    public SpaceSimulation(Date date, SpaceNatural... bodies) {
        super(Integrator.IntegratorType.SYMPLECTIC4, 1E5, 6, 12, date, bodies);
        this.bodies = bodies;
        this.integrator = new Symplectic4();
        
        for (int k=0; k<bodies.length; k++) {
            if (bodies[k] instanceof DisplayObject) {
                ((DisplayObject)(bodies[k])).registerUpdate(date);
            }
        }
    }
    @Override
    public void forward(int steps) {
        long newTime = date.getTime() + (long)(1000 * steps * secondsPerMiniStep/deccel);
        date = new Date(newTime);
        
        for (int i=0; i<steps; i++) {
            //for (int n=0; n<4; n++) {
                //integrator.partialApply(n, bodies, secondsPerMiniStep/deccel);
                integrator.apply(bodies, secondsPerMiniStep/deccel);
            //}
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
        
        for (int k=0; k<bodies.length; k++) {
            if (bodies[k] instanceof DisplayObject) {
                ((DisplayObject)(bodies[k])).registerUpdate(date);
            }
        }
        
    }
    
    @Override
    public void reCalculateOrbits() {

        double[] pers = new double[bodies.length];

        //if (focus == null) {
            double G = NBody.G;
            
            for (int i=0; i<pers.length; i++) {
                SpaceNatural orbiting = bodies[i].orbiting(bodies);
                double vels = Vectors2.sub(bodies[i].velocity(), orbiting.velocity()).norm();
                double M0 = orbiting.mass();
                double c0 = G*G*M0*M0;
                pers[i] = 2 * Math.PI * Math.sqrt(c0/Math.pow(vels, 6));
            }
            pers[0] = 5E8;
        /*} else {
            double velFocus = Vectors2.sub(focus.velocity(), focus.orbiting(bodies).velocity()).norm();
            double G = NBody.G;
            double M0 = focus.orbiting(bodies).mass();
            double c0 = G*G*M0*M0;
            double focusPeriod = 2 * Math.PI * Math.sqrt(c0/Math.pow(velFocus, 6));
            
            for (int i=0; i<vels.length; i++) {
                pers[i] = focusPeriod;
            }
        }*/
        final int div = 30;
        final int fi = focusIndex;
        
        if (fi == -1) {
            for (int i=0; i<bodies.length; i++) {
                //System.out.println((pers[i]/10/5));
                Vector2[][] posAndVel = Integrator.getFutureSingleWithVelInterpolated(bodies, i, integrator, (pers[i]/div/2), div);
                futureOrbitPos[i] = posAndVel[0];
                futureOrbitVel[i] = posAndVel[1];
                futureOrbitTime[i] = Integrator.getFutureSingleTimeStampsInterpolated(date, (pers[i]/div/2), div);
            }
        } else {
            for (int i=0; i<bodies.length; i++) {
                double realP = (pers[i] > pers[fi]) ? (pers[fi]/div/2) : (pers[i]/div/2);
                Vector2[][] posAndVel = Integrator.getFutureSingleWithVelRelativeInterpolated(bodies, i, fi, integrator, realP, div);
                futureOrbitPos[i] = posAndVel[0];
                futureOrbitVel[i] = posAndVel[1];
                futureOrbitTime[i] = Integrator.getFutureSingleTimeStampsInterpolated(date, realP, div);
            }
        }

        for (int i=0; i<bodies.length; i++) {
            if (bodies[i] instanceof SpaceNatural) {
                ((SpaceNatural)(bodies[i])).setOrbitPath(futureOrbitPos[i], futureOrbitVel[i], futureOrbitTime[i]);
            }
        }
    }
    public void setFocus(SpaceNatural body) {
        for (int i=0; i<bodies.length; i++) {
            if (body == bodies[i]) {
                focusIndex = i;
                return;
            }
        }
        focusIndex = -1;
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
