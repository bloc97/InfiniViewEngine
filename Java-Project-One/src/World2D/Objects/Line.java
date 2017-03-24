/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package World2D.Objects;

import World2D.Camera;
import java.awt.Color;

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
    public void update(Camera camera) {
        xoffset = camera.getxPos();
        yoffset = camera.getyPos();
        scaleoffset = camera.getScale();
        xscroffset = camera.getxScrOffset();
        yscroffset = camera.getyScrOffset();
        
        dix0 = (int)(((x0-xoffset)*scaleoffset)+xscroffset+0.5);
        diy0 = -(int)(((y0+yoffset)*scaleoffset)-yscroffset+0.5);
        dix1 = (int)(((x1-xoffset)*scaleoffset)+xscroffset+0.5);
        diy1 = -(int)(((y1+yoffset)*scaleoffset)-yscroffset+0.5);
    }

    @Override
    public DisplayObjectType getType() {
        return DisplayObjectType.Line;
    }

    @Override
    public boolean isInView(int x0, int y0, int x1, int y1) {
        return (((dix0 >= x0 && dix0 <= x1) || (dix1 >= x0 && dix1 <= x1)) && ((diy0 >= y0 && diy0 <= y1) || (diy1 >= y0 && diy1 <= y1)));
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
    public int getDix() {
        throw new UnsupportedOperationException("Can't get only one position of a line, use Dix0 and Dix1.");
    }

    @Override
    public int getDiy() {
        throw new UnsupportedOperationException("Can't get only one position of a line, use Diy0 and Diy1.");
    }

    @Override
    public Color getColor() {
        return color;
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
