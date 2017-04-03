/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SpaceGame.Objects;


import Physics2D.Integrators.FuturePath;
import Physics2D.Integrators.FutureContainer;
import Physics2D.Objects.RoundBody;
import Physics2D.Vector2;
import Physics2D.Vectors2;
import World2D.Camera;
import World2D.Objects.DisplayObject;
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
public class SpaceNatural extends RoundBody implements DisplayObject {

    public enum SpaceNaturalType {
        Massive, Big, Medium, Small, Tiny, Particle
    }// Superbig, Star, Planet, Moon, Asteroid, Rings
    
    final private SpaceNaturalType type;
    
    final private static Color color = Color.getHSBColor(16F/360, 33F/100, 100F/100);
    final private String name;
    private boolean isHidden;
        
    private double lastUpdateTime;
    
    private double x;
    private double y;
    
    private FutureContainer future;
    private FutureContainer futureReference;
    
    private Date currentDate;
    
    private double dst;
    
    public boolean isPaused = false;
    
    
    //private double lastTimeDiff = 0;
    

    public SpaceNatural(String name, Vector2 position, Vector2 velocity, double mass, double angPos, double angVel, double radius) {
        super(position, velocity, mass, angPos, angVel, radius);
        this.name = name;
        this.type = computeType(mass);
    }
    
    public SpaceNatural orbiting(SpaceNatural[] bodies) {
        SpaceNatural closestObject = null;
        double maxGravity = 0;
        for (int i=0; i<bodies.length; i++) {
            if ((bodies[i].type() == SpaceNaturalType.Medium || bodies[i].type() == SpaceNaturalType.Big) && this != bodies[i]) {
                double gravity = bodies[i].mass() / Vectors2.sub(position(), bodies[i].position()).normSquared();
                if (gravity > maxGravity) {
                    maxGravity = gravity;
                    closestObject = bodies[i];
                }
            }
        }
        return closestObject;
        
    }
    
    public SpaceNaturalType type() {
        return type;
    }
    
    public static SpaceNaturalType computeType(double mass) {
        if (mass > 1E32d) {
            return SpaceNaturalType.Massive;
        } else if (mass > 1.791e+29d) {
            return SpaceNaturalType.Big;
        } else if (mass > 5e+25d) {
            return SpaceNaturalType.Medium;
        } else if (mass > 3e+23d) {
            return SpaceNaturalType.Small;
        } else if (mass > 4e+19d) {
            return SpaceNaturalType.Tiny;
        } else {
            return SpaceNaturalType.Particle;
        }
    }
    

    @Override
    public void registerUpdate() {
        x = position().get(0);
        y = position().get(1);
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }
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
        
        if (!SpaceRender.canRenderNaturalObjectByScale(type, camera.getScale())) {
            return;
        }
        
        double r = getSr(camera);
        
        double sx = getSx(camera);
        double sy = getSy(camera);
        
        if (Math.abs(sx) > 1E4) {
            return;
        }
        if (Math.abs(sy) > 1E4) {
            return;
        }
        
        g2.setColor(color);
        
        
        
        //if ((radius>10000000 && camera.getScale() > 3E-11) || camera.getScale() > 3E-10) {
        
        if (SpaceRender.canRenderNaturalPathByScale(type, camera.getScale())) {
            renderFutureOrbit(g2, camera, sx, sy);
        }
        
        if (SpaceRender.canRenderNaturalNameByScale(type, camera.getScale())) {
            g2.drawString(name, (float)(sx+r+4), (float)(sy+5));
        }
        
        
        Ellipse2D.Double circle = new Ellipse2D.Double(sx-r, sy-r, r*2, r*2);
        g2.fill(circle);
    }
    
    public void renderFutureOrbit(Graphics2D g2, Camera camera, double sx, double sy) {
        
        Stroke originalStroke = g2.getStroke();
        Path2D.Double orbit = new Path2D.Double();
        
        if (futureReference == null) {
            Vector2 p0 = future.getPos(0);
            double px0 = DisplayObject.getSx(p0.get(0), camera);
            double py0 = DisplayObject.getSy(p0.get(1), camera);
            orbit.moveTo(px0, py0);
            
            final int length = future.length();
            for (int i=1; i<length; i++) {
                Vector2 pi = future.getPos(i);
                double pxi = DisplayObject.getSx(pi.get(0), camera);
                double pyi = DisplayObject.getSy(pi.get(1), camera);
                orbit.lineTo(pxi, pyi);
            }
            
            
        } else {
            //System.out.println("step1");
            Vector2 p0 = Vectors2.add(future.getPos(0, futureReference), futureReference.getPos(0));
            double px0 = DisplayObject.getSx(p0.get(0), camera);
            double py0 = DisplayObject.getSy(p0.get(1), camera);
            orbit.moveTo(px0, py0);
            //System.out.println("step2");
            /*
            final int length = future.length(futureReference);
            
            for (int i=1; i<length; i++) {
                //System.out.println("step4." + i);
                Vector2 pi = Vectors2.add(future.getPos(i, futureReference), futureReference.getPos(0));
                double pxi = DisplayObject.getSx(pi.get(0), camera);
                double pyi = DisplayObject.getSy(pi.get(1), camera);
                orbit.lineTo(pxi, pyi);
                //System.out.println("step5." + i);
            }*/
            
            final int length;
            if (future.getTime(1) - future.getTime(0) > futureReference.getTime(1) - futureReference.getTime(0)) {
                length = futureReference.length(future);
                for (int i=1; i<length; i++) {
                    Vector2 pi = Vectors2.add(future.getPos(futureReference.getTime(i), futureReference), futureReference.getPos(0));
                    double pxi = DisplayObject.getSx(pi.get(0), camera);
                    double pyi = DisplayObject.getSy(pi.get(1), camera);
                    orbit.lineTo(pxi, pyi);
                }
            } else {
                length = future.length(futureReference);
                for (int i=1; i<length; i++) {
                    Vector2 pi = Vectors2.add(future.getPos(future.getTime(i), futureReference), futureReference.getPos(0));
                    double pxi = DisplayObject.getSx(pi.get(0), camera);
                    double pyi = DisplayObject.getSy(pi.get(1), camera);
                    orbit.lineTo(pxi, pyi);
                }
            }
           // System.out.println("step6");
            
            
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
    public void setReference(SpaceNatural body) {
        if (body == null) {
            this.futureReference = null;
        } else {
            this.futureReference = body.future;
        }
    }
    
    @Override
    public double getSx(Camera camera) {
        return ((x - camera.getxPos()) * camera.getScale() + camera.getxScrOffset());
    }
    @Override
    public double getSy(Camera camera) {
        return ((y + camera.getyPos()) * -camera.getScale() + camera.getyScrOffset());
    }
    @Override
    public double getSr(Camera camera) {
        double rOut = (Math.log10(radius())-6)*3;
        double rIn = radius()*camera.getScale();
        double r = (rIn > rOut) ? rIn : rOut;
        return r;
    }
    

    @Override
    public boolean isVisible(double scale) {
        return SpaceRender.canRenderNaturalObjectByScale(type, scale);
    }

    @Override
    public void hide() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void show() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void setCurrentDate(Date date) {
        currentDate = date;
    }
    public FutureContainer getFutureContainer() {
        return future;
    }
    public void setOrbitPath(Vector2[] paths, Vector2[] vels, long[] timeStamps) {
        if (future == null) {
            future = new FutureContainer(paths, vels, timeStamps);
        } else {
            future.setOrbitPath(paths, vels, timeStamps);
        }
    }
    
    /*
    public void renderFutureOrbit(Graphics2D g2, Camera camera, double idx, double idy) {
        if (camera.getScale() < 6E-12) {
            return;
        }
        
        Stroke originalStroke = g2.getStroke();
        Path2D.Double orbit = new Path2D.Double();
        
        int initiali = -1;
        
        long currentTime;// = currentDate.getTime() + (long)(dt * 1000);
        
        if (isPaused) {
            currentTime = currentDate.getTime();
        } else {
            currentTime = currentDate.getTime();
        }
        
        
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
        
        orbit.moveTo(idx, idy);
        
            
        for (int i=initiali+1; i<path.length; i++) {
            
            double ix = DisplayObject.getIx(path[i].get(0), camera);
            double iy = DisplayObject.getIy(path[i].get(1), camera);
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
    }*/
    
}
