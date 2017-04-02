/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Physics;

import Physics2D.Objects.Planet;

/**
 *
 * @author bowen
 */
public interface FutureSimulation extends Simulation {
    public void setRatio(double ratio);
    public void setBodies(Planet[] bodies);
}
