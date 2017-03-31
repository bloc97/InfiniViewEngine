/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package World2D;

import Physics.Simulation;
import World2D.Objects.DisplayObject;

/**
 *
 * @author bowen
 */
public interface World { //Interfaces for world objects to be passed to Scene. Contains DisplayObjects and optionally a Simulation.
    public DisplayObject[] getDisplayObjects();
    public Simulation[] getSimulations();
}
