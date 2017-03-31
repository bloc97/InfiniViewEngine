/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package World2D.Objects;

/**
 *
 * @author bowen
 */
public interface Interpolable {
    public void setInterpolationSimulationTime(double dst);
    @Deprecated
    public void setInterpolationFrameTime(double dft);
    public void setVel(double vx, double vy);
}
