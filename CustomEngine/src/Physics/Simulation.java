/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Physics;

import World2D.Objects.DisplayObject;
import java.util.Date;

/**
 *
 * @author bowen
 */
public interface Simulation { //Any collection of objects that are simulable
    public long getTicks();
    public Date getDate();
    public double getSpeed();
    
    public void start();
    public void pause();
    public void unpause();
    public void togglePause();
    
    public void step();
    public void speedUp();
    public void speedDown();
    
    public boolean isOverloaded();
    
    public DisplayObject[] getDisplayObjects();
    public int getObjectsNumber();
}
