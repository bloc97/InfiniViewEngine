/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bloc97.infiniview.World2D;

import com.bloc97.infinisim.Simulation;
import com.bloc97.infiniview.World2D.Objects.DisplayObject;
import java.util.List;


/**
 *
 * @author bowen
 */
public interface World { //Interfaces for world objects to be passed to Scene. Contains DisplayObjects.
    public List<DisplayObject> getDisplayObjects();
}
