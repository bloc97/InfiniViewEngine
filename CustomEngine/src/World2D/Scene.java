/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package World2D;

import java.awt.Component;

/**
 *
 * @author bowen
 */
public interface Scene {
    
    public Component getComponent();
    public Viewport getViewport();
    
    public void launch();
    public void activate();
    public void deactivate();
    
    public void setViewport(Viewport viewport);
}
