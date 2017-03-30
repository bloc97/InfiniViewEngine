/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package World2D.Objects;

import World2D.Camera;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author bowen
 */
public class Planet implements DisplayObject, Interpolable {

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
    
    public Planet(String name, double r) {
        this(name, Color.WHITE, r);
    }
    public Planet(String name, Color color, double r) {
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

    public int getRadius() {
        return (int)(radius*(scaleoffset/1000)+5);
    }
    public String getName() {
        return name;
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

    @Override
    public void render(Graphics2D g2, Camera camera) {
        AffineTransform originalTransform = g2.getTransform();
        AffineTransform transform = new AffineTransform();
        double scale = camera.getScale();
        transform.scale(scale, -scale);
        transform.translate(-camera.getxPos(), camera.getyPos());
        double r = (radius-25)/scale;
        g2.translate(camera.getxScrOffset(), camera.getyScrOffset());
        g2.transform(transform);
        g2.setColor(color);
        //System.out.println(scale);
        //System.out.println((int)(r*2/scale));
        //g2.fillOval(0, 0, (int)(r*2/scale), (int)(r*2/scale));
        //g2.fillOval((int)x, (int)y, r*2, r*2);
        Ellipse2D.Double circle = new Ellipse2D.Double(x-r, y-r, r*2, r*2);
        g2.fill(circle);
        //Rectangle2D.Double rectangle = new Rectangle2D.Double(1E7, 1E7, r, r);
        //g2.fill(rectangle);
        g2.setTransform(originalTransform);
    }
    
    
}
