/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package World2D.Objects;

import Physics2D.Vector2;
import Physics2D.Vectors2;
import World2D.Camera;
import static World2D.Objects.DisplayObject.getIx;
import static World2D.Objects.DisplayObject.getIy;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.util.Date;

/**
 *
 * @author bowen
 */
public class FutureOrbit implements DisplayObject {
    
    //private double radius;
    private Vector2[] path;
    private Vector2[] vel;
    private long[] pathTime;
    private Date currentDate;
    //private double per;
    //private double ratio;
    
    @Override
    public void renderTransform(Graphics2D g2, Camera camera) {
    }

    @Override
    public void renderNoTransform(Graphics2D g2, Camera camera) {
        
        if (camera.getScale() < 6E-12) {
            return;
        }
        
        Stroke originalStroke = g2.getStroke();
        Path2D.Double orbit = new Path2D.Double();
        
        int initiali = -1;
        long currentTime = currentDate.getTime();
        
        for (int i=0; i<path.length; i++) {
            if (pathTime[i] > currentTime) {
                initiali = i-1;
                break;
            }
        }
        
        if (initiali >= path.length || initiali == -1) {
            return;
        }
        
        double ix0 = getIx(path[initiali].get(0), camera);
        double iy0 = getIy(path[initiali].get(1), camera);
        
        orbit.moveTo(ix0, iy0);
        
            
        for (int i=initiali+1; i<path.length; i++) {
            
            double ix = getIx(path[i].get(0), camera);
            double iy = getIy(path[i].get(1), camera);
            /*
            Vector2 controlPoint = getPointOfIntersection(path[i], path[i-1], vel[i], vel[i-1]);
            double cix = getIx(controlPoint.get(0), camera);
            double ciy = getIy(controlPoint.get(1), camera);
            
            double ixv = getIx(path[i].get(0) + vel[i].get(0), camera);
            double iyv = getIy(path[i].get(1) + vel[i].get(1), camera);
            
            g2.drawLine((int)(ix), (int)(iy), (int)(ixv), (int)(iyv));
            //g2.drawLine((int)(ix), (int)(iy), (int)(cix), (int)(ciy));
            */
            //Ellipse2D.Double circle = new Ellipse2D.Double(cix, ciy, 4, 4);
            //g2.fill(circle);
            //circle = new Ellipse2D.Double(ix, iy, 10, 10);
            //g2.fill(circle);
            
            //orbit.quadTo(cix, ciy, ix, iy);
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
    /*
    private static boolean isWithinThreshold(double x0, double y0, double x, double y, double threshold) {
        return ((Math.abs(x-x0) < threshold) && (Math.abs(y-y0) < threshold));
    }
    Vector2 getPointOfIntersection(Vector2 p1, Vector2 p2, Vector2 n1, Vector2 n2) {
        //Vector2 p1End = Vectors2.add(p1, n1); // another point in line p1->n1
        //Vector2 p2End = Vectors2.add(p2, n2); // another point in line p2->n2

        //double m1 = (p1End.get(1) - p1.get(1)) / (p1End.get(0) - p1.get(0)); // slope of line p1->n1
        //double m2 = (p2End.get(1) - p2.get(1)) / (p2End.get(0) - p2.get(0)); // slope of line p2->n2
        
        double m1 = n1.get(1)/n1.get(0);
        double m2 = n2.get(1)/n2.get(0);
        
        double b1 = p1.get(1) - m1 * p1.get(0); // y-intercept of line p1->n1
        double b2 = p2.get(1) - m2 * p2.get(0); // y-intercept of line p2->n2

        double px = (b2 - b1) / (m1 - m2); // collision x
        double py = m1 * px + b1; // collision y
        return new Vector2(new double[] {px, py});
        //return new Point(px, py); // return statement
    }*/
    
    @Override
    public void setPos(double x, double y) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
    
    public void setOrbitPath(Vector2[] paths, Vector2[] vels, long[] timeStamps) {//, double cyc, double ratio) {
        path = paths;
        vel = vels;
        pathTime = timeStamps;
        //per = cyc;
        //this.ratio = ratio;
    }
    public void setCurrentDate(Date date) {
        currentDate = date;
        
    }

    
}
