/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package World2D.Objects;

import World2D.Camera;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author bowen
 */
public class Line implements DisplayObject {
    
    private double x0;
    private double y0;
    private double x1;
    private double y1;
    
    private int dix0;
    private int diy0;
    private int dix1;
    private int diy1;
    
    private double xoffset;
    private double yoffset;
    private double scaleoffset;
    
    private double xscroffset;
    private double yscroffset;
    
    private boolean isHidden;
    
    Color color;

    public Line() {
        this(Color.WHITE);
    }
    public Line(Color color) {
        this.color = color;
        this.isHidden = false;
    }

    @Override
    public void setPos(double x, double y) {
        throw new UnsupportedOperationException("Can't change position of a line from only two variables."); //To change body of generated methods, choose Tools | Templates.
    }
    public void setPos(double x0, double y0, double x1, double y1) {
        this.x0 = x0;
        this.x1 = x1;
        this.y0 = y0;
        this.y1 = y1;
    }

    public int getDix0() {
        return dix0;
    }
    public int getDiy0() {
        return diy0;
    }
    public int getDix1() {
        return dix1;
    }
    public int getDiy1() {
        return diy1;
    }

    @Override
    public void hide() {
        isHidden = true;
    }

    @Override
    public void show() {
        isHidden = false;
    }

    @Override
    public boolean isHidden() {
        return isHidden;
    }

    @Override
    public void render(Graphics2D g, Camera camera) {
        
    }
    
}
