/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package World2D;

import java.math.BigDecimal;

/**
 *
 * @author bowen
 */
public class Camera {
    private Scene currentScene;
    private double xPos;
    private double yPos;
    private double scale;
    
    private int screenOffsetx;
    private int screenOffsety;

    Camera(Scene currentScene, int xsize, int ysize) {
        this(currentScene, xsize/2, ysize/2, 1E-10, xsize, ysize);
    }
    
    Camera(Scene currenScene, double x, double y, double s, int xsize, int ysize) {
        this.currentScene = currenScene;
        xPos = x;
        yPos = y;
        scale = s;
        screenOffsetx = xsize/2;
        screenOffsety = ysize/2;
        //System.out.println(screenOffsetx);
    }
    
    public Scene scene() {
        return currentScene;
    }
    public void setScene(Scene scene) {
        currentScene = scene;
    }
    public void setScreenSize(int xsize, int ysize) {
        screenOffsetx = xsize/2;
        screenOffsety = ysize/2;
    }

    public double getxPos() {
        return xPos;
    }
    public void setxPos(double xPos) {
        this.xPos = xPos;
    }
    public double getyPos() {
        return yPos;
    }
    public void setyPos(double yPos) {
        this.yPos = yPos;
    }
    
    public double getScale() {
        return scale;
    }
    public void setScale(double scale) {
        this.scale = scale;
    }
    
    
    public int getxScrOffset() {
        return screenOffsetx;
    }
    public int getyScrOffset() {
        return screenOffsety;
    }
    
    public void addxPos(double x) {
        this.xPos += x*(1/scale);
    }
    public void addyPos(double y) {
        this.yPos += y*(1/scale);
    }
    public void addScale(int n) {
        if (n > 0) {
            for (int i=0; i<n; i++) {
                divideScale();
            }
        } else {
            n = -n;
            for (int i=0; i<n; i++) {
                multiplyScale();
            }
        }
    }
    private void multiplyScale() {
        if (scale < 1 && enoughPrecisionToZoom()) {
            scale *= 2;
        }
        if (scale > 1) {
            scale = 1;
        }
    }
    private void divideScale() {
        if (scale > 1E-20) {
            scale /= 2;
        }
        if (scale < 1E-20) {
            scale = 1E-20;
        }
    }
    private boolean enoughPrecisionToZoom() {
        if (xPos + (20*(0.5/scale)) == xPos || xPos - (20*(0.5/scale)) == xPos) {
            return false;
        } else {
            return true;
        }
    }
    private boolean enoughPrecisionToSee() {
        if (xPos + (1*(0.5/scale)) == xPos || xPos - (1*(0.5/scale)) == xPos) {
            return false;
        } else {
            return true;
        }
    }
}
