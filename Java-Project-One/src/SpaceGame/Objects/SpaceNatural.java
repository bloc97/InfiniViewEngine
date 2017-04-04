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
import SpaceGame.SolarSystem;
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
    }// Superbig, Star, BigPlanet, Planet & Moons, Asteroid, Rings
    
    final private SpaceNaturalType type;
    
    final private static Color color = Color.getHSBColor(16F/360, 33F/100, 100F/100);
    final private String name;
    private boolean isHidden;
        
    private double lastUpdateTime;
    
    private double x;
    private double y;
    
    private FutureContainer future;
    
    private Date currentDate;
    
    private double dst;
    
    public boolean isPaused = false;
    
    private SolarSystem system;
    
    
    //private double lastTimeDiff = 0;
    
    public SpaceNatural(SolarSystem system, String name, Vector2 position, Vector2 velocity, double mass, double angPos, double angVel, double radius) {
        this(name, position, velocity, mass, angPos, angVel, radius);
        this.system = system;
    }
    
    public SpaceNatural(String name, Vector2 position, Vector2 velocity, double mass, double angPos, double angVel, double radius) {
        super(position, velocity, mass, angPos, angVel, radius);
        this.name = name;
        this.type = computeType(mass);
    }
    
    public void setSystem(SolarSystem system) {
        this.system = system;
    }
    
    public SpaceNatural orbiting(SpaceNatural[] bodies) {
        return orbitingBySphereOfInfluence(bodies);
    }
    
    public SpaceNatural orbitingBySphereOfInfluence(SpaceNatural[] bodies) {
        Vector2 starCM = new Vector2(0);
        double totalMass = 0;
        for (int i=0; i<bodies.length; i++) {
            if (bodies[i].type() == SpaceNaturalType.Big || bodies[i].type() == SpaceNaturalType.Massive) {
                totalMass += bodies[i].mass();
                starCM.add(Vectors2.prod(bodies[i].position(), bodies[i].mass()));
            }
        }
        if (totalMass == 0) {
            totalMass = 1;
        }
        starCM.div(totalMass);
        //System.out.println(totalMass);
        
        SpaceNatural nearbyObject = new SpaceNatural(system, "Barycenter", starCM, new Vector2(0), totalMass, 0, 0, 1);
        for (int i=0; i<bodies.length; i++) {
            if ((bodies[i].type() == SpaceNaturalType.Medium || bodies[i].type() == SpaceNaturalType.Small) && bodies[i] != this) {
                double spInfluence = Vectors2.sub(bodies[i].position(), starCM).norm() * (Math.pow(bodies[i].mass()/totalMass, 2d/5));
                if (Vectors2.sub(this.position(), bodies[i].position()).norm() <= spInfluence) {
                    nearbyObject = bodies[i];
                }
            }
        }
        
        
        return nearbyObject;
        
    }
    
    public SpaceNatural orbitingByGravity(SpaceNatural[] bodies) {
        
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
    public void registerUpdate(Date date) {
        x = position().get(0);
        y = position().get(1);
        currentDate = date;
    }

    @Override
    public double getX() {
        return x + offsetX();
    }

    @Override
    public double getY() {
        return y + offsetY();
    }
    
    public double offsetX() {
        if (system != null) {
            return system.offset().get(0);
        } else {
            return 0;
        }
    }
    public double offsetY() {
        if (system != null) {
            return system.offset().get(1);
        } else {
            return 0;
        }
    }
    
    @Override
    public SpaceNatural clone() {
        SpaceNatural newNatural = new SpaceNatural(system, "Clone", position(), velocity(), mass(), angle(), angVelocity(), radius());
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
        //g2.fillOval((int)(sx-r), (int)(sy-r), (int)(r*2), (int)(r*2));
    }
    
    public void renderFutureOrbit(Graphics2D g2, Camera camera, double sx, double sy) {
        
        if (future == null) {
            return;
        }
        
        
        Stroke originalStroke = g2.getStroke();
        Path2D.Double orbit = new Path2D.Double();

        long currentTime = currentDate.getTime();

        final int length = future.length();
        
        if (length == 0) {
            return;
        }
        int initiali = -1;
        for (int i=0; i<length; i++) {
            if (future.getTime(i) > currentTime) {
                initiali = i-1;
                break;
            }
        }
        
        if (initiali == -1) {
            return;
        }
        
        Vector2 p0 = future.getPos(initiali);
        double px0 = DisplayObject.getSx(p0.get(0), offsetX(), camera);
        double py0 = DisplayObject.getSy(p0.get(1), offsetY(), camera);
        orbit.moveTo(px0, py0);

        for (int i=initiali+1; i<length; i++) {
            Vector2 pi = future.getPos(i);
            double pxi = DisplayObject.getSx(pi.get(0), offsetX(), camera);
            double pyi = DisplayObject.getSy(pi.get(1), offsetY(), camera);
            orbit.lineTo(pxi, pyi);
        }
        
        float dashScale = 1E8f * (float)camera.getScale();
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
    public double getSx(Camera camera) {
        return ((x + (offsetX() - camera.getxPos())) * camera.getScale() + camera.getxScrOffset());
    }
    @Override
    public double getSy(Camera camera) {
        return ((y + (offsetY() + camera.getyPos())) * -camera.getScale() + camera.getyScrOffset());
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
