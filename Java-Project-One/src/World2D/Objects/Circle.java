/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package World2D.Objects;

import World2D.Camera;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JComponent;

/**
 *
 * @author bowen
 */
public class Circle implements DisplayObject, Interpolable {

    private double x;
    private double y;
    private double radius;
    
    private long stepsWithoutUpdate;
    
    private double vx;
    private double vy;
    private double dst;
    private double dft;
    
    private double xoffset;
    private double yoffset;
    private double scaleoffset;
    
    private double xscroffset;
    private double yscroffset;
    
    private int dix;
    private int diy;
    
    private boolean isHidden;
    
    private String name;
    private Color color;
    
    public Circle(String name, double r) {
        this(name, Color.WHITE, r);
    }
    public Circle(String name, Color color, double r) {
        //this.setSize(r*2, r*2);
        this.isHidden = false;
        this.name = name;
        this.radius = r;
        this.color = color;
    }
    /*
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(color);
        
        int r = (int)this.radius;
        
        g.fillOval(0, 0, r*2, r*2);
        this.setLocation(dispx, dispy);
    }*/
    private void interpolationStep() {
        int r = (int)(this.radius*(scaleoffset/1000)+5);
        double idt = dst * dft;
        
        
        double ipx = x + stepsWithoutUpdate * idt * vx;
        double ipy = y + stepsWithoutUpdate * idt * vy;
        
        dix = (int)(((ipx-xoffset)*scaleoffset)+xscroffset-r+0.5);
        diy = -(int)(((ipy+yoffset)*scaleoffset)-yscroffset+r+0.5);
        stepsWithoutUpdate++;
    }
    public void setColor(Color color) {
        this.color = color;
    }
    @Override
    public void setInterpolationFrameTime(double dft) {
        this.dft = dft;
    }
    @Override
    public void setInterpolationSimulationTime(double dst) {
        this.dst = dst;
    }
    /*
    @Override
    public JComponent getJComponent() {
        return this;
    }*/

    @Override
    public void update(Camera camera) {
        xoffset = camera.getxPos();
        yoffset = camera.getyPos();
        scaleoffset = camera.getScale();
        interpolationStep();
        xscroffset = camera.getxScrOffset();
        yscroffset = camera.getyScrOffset();
    }

    @Override
    public DisplayObjectType getType() {
        return DisplayObjectType.Circle;
    }
    @Override
    public int getDix() {
        return dix;
    }
    @Override
    public int getDiy() {
        return diy;
    }
    public int getRadius() {
        return (int)(radius*(scaleoffset/1000)+5);
    }
    @Override
    public Color getColor() {
        return color;
    }
    public String getName() {
        return name;
    }

    @Override
    public boolean isInView(int x0, int y0, int x1, int y1) {
        return (dix >= x0 && dix <= x1 && diy >= y0 && diy <= y1);
    }

    @Override
    public void setPos(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void setVel(double vx, double vy) {
        stepsWithoutUpdate = 0;
        this.vx = vx;
        this.vy = vy;
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
    
    
}
