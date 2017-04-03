/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SpaceGame;

import Physics2D.Integrators.Integrator;
import Physics2D.Integrators.NBody;
import Physics2D.NBodySimulation;
import Physics2D.Objects.FuturePath;
import Physics2D.Objects.PointBody;
import Physics2D.Vector2;
import Physics2D.Vectors2;
import World2D.Objects.Interpolable;
import java.util.Date;

/**
 *
 * @author bowen
 */
public class SpaceSimulation extends NBodySimulation {
    private PointBody focus;
    public SpaceSimulation(Date date, PointBody... bodies) {
        super(Integrator.IntegratorType.SYMPLECTIC4, 1E5, 30, 1, date, bodies);
    }
    public void setFocus(PointBody body) {
        this.focus = body;
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
            if (focus != null) {
                reCalculateOrbits(focus);
            } else {
                reCalculateOrbits();
            }
        
        for (int i=0; i<bodies.length; i++) {
            if (bodies[i] instanceof FuturePath) {
                ((FuturePath)(bodies[i])).setCurrentDate(date);
            }
            if (bodies[i] instanceof Interpolable) {
                ((Interpolable)(bodies[i])).registerUpdate();
            }
        }
    }
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
                referencePosAndVel = Integrator.getFutureSingleWithVel(bodies, i, integrator2, (1e5), 100);
                referenceOrbitPos = referencePosAndVel[0];
                referenceOrbitVel = referencePosAndVel[1];
            }
        }
                
        for (int i=0; i<bodies.length; i++) {
            //System.out.println((pers[i]/10/5));
            Vector2[][] posAndVel = Integrator.getFutureSingleWithVel(bodies, i, integrator2, (1e5), 100);
            futureOrbitPos[i] = posAndVel[0];
            
            for (int n=0; n<futureOrbitPos[i].length; n++) {
                futureOrbitPos[i][n] = Vectors2.sub(posAndVel[0][n], Vectors2.sub(referenceOrbitPos[n], referenceOrbitPos[0]));
            }
            
            futureOrbitVel[i] = posAndVel[1];
            futureOrbitTime[i] = Integrator.getFutureSingleTimeStamps(date, (1e5), 100);
        }

        for (int i=0; i<bodies.length; i++) {
            if (bodies[i] instanceof FuturePath) {
                ((FuturePath)(bodies[i])).setOrbitPath(futureOrbitPos[i], futureOrbitVel[i], futureOrbitTime[i], date);
            }
        }
    }
}
