/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package World2D.Objects;

import World2D.Camera;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import javafx.scene.shape.Ellipse;

/**
 *
 * @author bowen
 */
public class PlanetDisplay implements DisplayObject, Interpolable {

    private double x;
    private double y;
    private double radius;
    
    private long stepsWithoutUpdate;
    
    private double vx;
    private double vy;
    private double dst;
    private double dft;
    
    
    private double lastUpdateTime;
    
    private boolean isHidden;
    
    private String name;
    private Color color;
    
    public PlanetDisplay(String name, double r) {
        this(name, Color.WHITE, r);
    }
    public PlanetDisplay(String name, Color color, double r) {
        //this.setSize(r*2, r*2);
        this.isHidden = false;
        this.name = name;
        this.radius = r;
        this.color = color;
        this.lastUpdateTime = 0;
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
    public void setColor(Color color) {
        this.color = color;
    }
    @Override
    @Deprecated
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

    @Deprecated
    public int getRadius() {
        return (int)radius;
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
        this.lastUpdateTime = System.currentTimeMillis();
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
/*
    @Override
    public void renderTransform(Graphics2D g2, Camera camera) {
        
        double rOut = (Math.log10(radius)-6)*3;
        double rIn = radius;
        double r = (rIn*camera.getScale() > rOut) ? rIn : rOut/camera.getScale();
        g2.setColor(color);
        //System.out.println(scale);
        //System.out.println((int)(r*2/scale));
        //g2.fillOval(0, 0, (int)(r*2/scale), (int)(r*2/scale));
        //g2.fillOval((int)x, (int)y, r*2, r*2);
        
        double realTime = (System.currentTimeMillis() - lastUpdateTime)/1000;
        double simulatedTime = dst * realTime;
        double ix = x + vx * simulatedTime;
        double iy = y + vy * simulatedTime;
        Ellipse2D.Double circle = new Ellipse2D.Double(ix-r, iy-r, r*2, r*2);
        
        g2.fill(circle);
        //Rectangle2D.Double rectangle = new Rectangle2D.Double(1E7, 1E7, r, r);
        //g2.fill(rectangle);
    }
    @Override
    public void renderNoTransform(Graphics2D g2, Camera camera) {
        double rOut = (Math.log10(radius)-6)*3;
        double rIn = radius*camera.getScale();
        double r = (rIn > rOut) ? rIn : rOut;
        
        double realTime = (System.currentTimeMillis() - lastUpdateTime)/1000;
        double simulatedTime = dst * realTime;
        double ix = x + vx * simulatedTime;
        double iy = y + vy * simulatedTime;
        float idx = (float)((ix - camera.getxPos()) * camera.getScale() + camera.getxScrOffset());
        float idy = (float)((iy + camera.getyPos()) * -camera.getScale() + camera.getyScrOffset());
        
        g2.setColor(color);
        g2.drawString(name, idx+(float)r+4, idy+5);
        
        
    }*/
    
    @Override
    public void renderTransform(Graphics2D g2, Camera camera) {
        
    }
    @Override
    public void renderNoTransform(Graphics2D g2, Camera camera) {
        double rOut = (Math.log10(radius)-6)*3;
        double rIn = radius*camera.getScale();
        double r = (rIn > rOut) ? rIn : rOut;
        
        double realTime = (System.currentTimeMillis() - lastUpdateTime)/1000;
        double simulatedTime = dst * realTime;
        double ix = x + vx * simulatedTime;
        double iy = y + vy * simulatedTime;
        double idx = ((ix - camera.getxPos()) * camera.getScale() + camera.getxScrOffset());
        double idy = ((iy + camera.getyPos()) * -camera.getScale() + camera.getyScrOffset());
        
        g2.setColor(color);
        
        if (Math.abs(idx) > 1E4) {
            idx = 1E4;
            return;
        }
        if (Math.abs(idy) > 1E4) {
            idy = 1E4;
            return;
        }
        
        if ((radius>10000000 && camera.getScale() > 3E-11) || camera.getScale() > 3E-10) {
            g2.drawString(name, (float)(idx+r+4), (float)(idy+5));
        }
        
        
        
        Ellipse2D.Double circle = new Ellipse2D.Double(idx-r, idy-r, r*2, r*2);
        g2.fill(circle);
        
        
    }
    
    
}
