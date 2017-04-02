/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package World2D.Objects;

import Physics2D.Vector2;
import World2D.Camera;
import World2D.Objects.DisplayObject;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.util.Date;

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
    public void renderTransform(Graphics2D g2, Camera camera) {
        g2.setColor(color);
        //Stroke lastStroke = g2.getStroke();
        Line2D.Double line = new Line2D.Double(x0, y0, x1, y1);
        //g2.setStroke(new BasicStroke((float)(1/camera.getScale())));
        g2.draw(line);
        //g2.drawLine(getDix0(), getDiy0(), getDix1(), getDiy1());
        //g2.setStroke(lastStroke);
    }

    @Override
    public void renderNoTransform(Graphics2D g, Camera camera) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setOrbitPath(Vector2[] paths, Vector2[] vels, long[] timeStamps) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setCurrentDate(Date date) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
