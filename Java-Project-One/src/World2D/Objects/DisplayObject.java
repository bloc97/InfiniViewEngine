/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package World2D.Objects;

import Physics2D.Vector2;
import World2D.Camera;
import java.awt.Graphics2D;
import java.util.Date;

/**
 *
 * @author bowen
 */
public interface DisplayObject {
    
    public void renderTransform(Graphics2D g2, Camera camera); //part of the rendering requiring affine transforms
    public void renderNoTransform(Graphics2D g2, Camera camera); //part of the rendering without transforms (per pixel)
    
    //public void setPos(double x, double y);
    
    public boolean isVisible(double scale);
    
    public void hide();
    public void show();
    
    public double getSx(Camera camera);
    public double getSy(Camera camera);
    public double getSr(Camera camera);
            
    public static double getIx(double x, Camera camera) {
        return ((x - camera.getxPos()) * camera.getScale() + camera.getxScrOffset());
    }

    public static double getIy(double y, Camera camera) {
        return ((y + camera.getyPos()) * -camera.getScale() + camera.getyScrOffset());
    }
    
}
