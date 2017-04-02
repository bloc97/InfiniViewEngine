/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package World2D.Objects;

import World2D.Camera;

/**
 *
 * @author bowen
 */
public interface Interpolable {
    public void setInterpolationSimulationTime(double dst);
    public void registerUpdate();
    public double getIx();
    public double getIy();
    
    
    public void pause();
    public void unpause();
    public void togglePause();
    
    //public void setVel(double vx, double vy);
    
}
