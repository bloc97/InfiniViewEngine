/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Deprec.SPHY;

/**
 *
 * @author bowen
 */
public class PhysObject {
    
    private Vector2 position, velocity, momentum, acceleration; //meters, meters/s, meters*kg/s, meters/s^2
    private VectorR rotation, angspeed, angaccel;
    private Vector2[] forces; //in N
    private double[] torques;
    
    private double mass; //in KG
    private double moment;
    
    private boolean immovable; //can give out infinite normal force
    
    
    public PhysObject() {
        
    }
}
