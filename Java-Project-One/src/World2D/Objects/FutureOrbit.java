/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package World2D.Objects;

import Physics2D.Vector2;
import World2D.Camera;
import static World2D.Objects.DisplayObject.getIx;
import static World2D.Objects.DisplayObject.getIy;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Path2D;

/**
 *
 * @author bowen
 */
public class FutureOrbit implements DisplayObject {
    
    private Vector2[] path;
    //private double per;
    //private double ratio;
    
    @Override
    public void renderTransform(Graphics2D g2, Camera camera) {
    }

    @Override
    public void renderNoTransform(Graphics2D g2, Camera camera) {
        Stroke originalStroke = g2.getStroke();
        Path2D.Double orbit = new Path2D.Double();
        
        double ix0 = getIx(path[0].get(0), camera);
        double iy0 = getIy(path[0].get(1), camera);
        
        orbit.moveTo(ix0, iy0);
            
        for (int i=1; i<path.length; i++) {
            double ix = getIx(path[i].get(0), camera);
            double iy = getIy(path[i].get(1), camera);
                orbit.lineTo(ix, iy);
            /*
            int maxX = camera.getxScrOffset() * 2;
            int maxY = camera.getyScrOffset() * 2;
            
            if (ix > 0 && ix < maxX && iy > 0 && iy < maxY) {
                orbit.lineTo(ix, iy);
            }
            
            //System.out.println(i + " " + Math.random());
            
            //if (i*(ratio) > per/3) {
                //System.out.println("True");
                //break;
            //}
            
            //if (i>20 && isWithinThreshold(ix0, iy0, ix, iy, 100)) {
                //break;
            //}
            */
            
        }
        float dashScale = 1E9f * (float)camera.getScale();
        //if (dashScale < 10) {
            //dashScale *= 10;
        //}
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
    
    private static boolean isWithinThreshold(double x0, double y0, double x, double y, double threshold) {
        return ((Math.abs(x-x0) < threshold) && (Math.abs(y-y0) < threshold));
    }
    
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
    
    public void setOrbitPath(Vector2[] paths) {//, double cyc, double ratio) {
        path = paths;
        //per = cyc;
        //this.ratio = ratio;
    }

    
}
