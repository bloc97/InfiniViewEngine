/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package World2D.Objects;

import World2D.Camera;
import java.awt.Graphics2D;

/**
 *
 * @author bowen
 */
public interface DisplayObject {
    public enum DisplayObjectType {
        Circle, Line
    }
    public void render(Graphics2D g, Camera camera);
    
    public void setPos(double x, double y);
    
    public boolean isHidden();
    
    public void hide();
    public void show();
    
}
