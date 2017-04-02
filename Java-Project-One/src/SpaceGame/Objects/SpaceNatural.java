/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SpaceGame.Objects;


import Physics2D.Objects.FuturePath;
import Physics2D.Objects.RoundBody;
import Physics2D.Vector2;
import World2D.Camera;
import World2D.Objects.DisplayObject;
import static World2D.Objects.DisplayObject.getIx;
import static World2D.Objects.DisplayObject.getIy;
import World2D.Objects.Interpolable;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.util.Date;

/**
 *
 * @author bowen
 */
public class SpaceNatural extends RoundBody implements DisplayObject, Interpolable, FuturePath {
    
    final private static Color color = Color.getHSBColor(16F/360, 33F/100, 100F/100);
    final private String name;
    private boolean isHidden;
        
    private double lastUpdateTime;
    
    private double x;
    private double y;
    private double vx;
    private double vy;
    
    private Vector2[] path;
    private Vector2[] vel;
    private long[] pathTime;
    private Date currentDate;
    
    private double dst;
    
    //private double lastTimeDiff = 0;
    

    public SpaceNatural(String name, Vector2 position, Vector2 velocity, double mass, double angPos, double angVel, double radius) {
        super(position, velocity, mass, angPos, angVel, radius);
        this.name = name;
    }/*
    @Override
    public final void update() {
        displayComponent.setPos(position().get(0), position().get(1));
        displayComponent.setVel(velocity().get(0), velocity().get(1));
    }
    public void setColour(Color color) {
        displayComponent.setColor(color);
    }
    @Override
    public DisplayObject getDisplayObject() {
        return displayComponent;
    }*/
    @Override
    public SpaceNatural clone() {
        SpaceNatural newNatural = new SpaceNatural("Clone", position(), velocity(), mass(), angle(), angVelocity(), radius());
        return newNatural;
    }

    @Override
    public void renderTransform(Graphics2D g2, Camera camera) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void renderNoTransform(Graphics2D g2, Camera camera) {
        final double radius = radius();
        
        
        double rOut = (Math.log10(radius)-6)*3;
        double rIn = radius*camera.getScale();
        double r = (rIn > rOut) ? rIn : rOut;
        
        double realTimeDiff = (System.currentTimeMillis() - lastUpdateTime)/1000;
        //double simulatedTimeDiff = 0;
        
        double simulatedTimeDiff = dst * realTimeDiff;
        /*
        System.out.println(simulatedTimeDiff);
        if (lastTimeDiff < simulatedTimeDiff) {
            lastTimeDiff = simulatedTimeDiff;
        } else {
            //simulatedTimeDiff = lastTimeDiff;
        }*/
        
        double ix = x + vx * simulatedTimeDiff;
        double iy = y + vy * simulatedTimeDiff;
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
        
        renderFutureOrbit(g2, camera, ix, iy, simulatedTimeDiff);
        
        if ((radius>10000000 && camera.getScale() > 3E-11) || camera.getScale() > 3E-10) {
            g2.drawString(name, (float)(idx+r+4), (float)(idy+5));
        }
        
        Ellipse2D.Double circle = new Ellipse2D.Double(idx-r, idy-r, r*2, r*2);
        g2.fill(circle);
    }
    public void renderFutureOrbit(Graphics2D g2, Camera camera, double cx, double cy, double dt) {
        if (camera.getScale() < 6E-12) {
            return;
        }
        
        Stroke originalStroke = g2.getStroke();
        Path2D.Double orbit = new Path2D.Double();
        
        int initiali = -1;
        long currentTime = currentDate.getTime() + (long)(dt * 1000);
        
        if (path == null) {
            return;
        }
        
        for (int i=0; i<path.length; i++) {
            if (pathTime[i] > currentTime) {
                initiali = i-1;
                break;
            }
        }
        
        if (initiali >= path.length || initiali == -1) {
            return;
        }
        
        //double ix0 = getIx(path[initiali].get(0), camera);
        //double iy0 = getIy(path[initiali].get(1), camera);
        double ix0 = getIx(cx, camera);
        double iy0 = getIy(cy, camera);
        
        orbit.moveTo(ix0, iy0);
        
            
        for (int i=initiali+1; i<path.length; i++) {
            
            double ix = getIx(path[i].get(0), camera);
            double iy = getIy(path[i].get(1), camera);
            orbit.lineTo(ix, iy);
            
        }
        float dashScale = 1E9f * (float)camera.getScale();
        while (dashScale < 5) {
            dashScale *= 5;
        }
        g2.setStroke(new BasicStroke(1.0f,                      // Width
                           BasicStroke.CAP_SQUARE,    // End cap
                           BasicStroke.JOIN_BEVEL,    // Join style
                           1000.0f,                     // Miter limit
                           new float[] {dashScale, dashScale}, // Dash pattern
                           0.5f));
        g2.draw(orbit);
        g2.setStroke(originalStroke);
    }

    @Override
    public boolean isHidden() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void hide() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void show() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setInterpolationSimulationTime(double dst) {
        this.dst = dst;
    }

    @Override
    public void registerUpdate() {
        lastUpdateTime = System.currentTimeMillis();
        x = position().get(0);
        y = position().get(1);
        vx = velocity().get(0);
        vy = velocity().get(1);
    }
    @Override
    public void setOrbitPath(Vector2[] paths, Vector2[] vels, long[] timeStamps, Date date) {//, double cyc, double ratio) {
        path = paths;
        vel = vels;
        pathTime = timeStamps;
        currentDate = date;
    }
    @Override
    public void setCurrentDate(Date date) {
        currentDate = date;
        //registerUpdate();
    }

}
