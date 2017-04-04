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
    private BigDecimal xPos;
    private BigDecimal yPos;
    private double scale;
    
    private int screenOffsetx;
    private int screenOffsety;

    Camera(Scene currentScene, int xsize, int ysize) {
        this(currentScene, xsize/2, ysize/2, 1E-10, xsize, ysize);
    }
    
    Camera(Scene currenScene, double x, double y, double s, int xsize, int ysize) {
        this.currentScene = currenScene;
        xPos = new BigDecimal(x);
        yPos = new BigDecimal(y);
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

    public BigDecimal getxPos() {
        return xPos;
    }
    public void setxPos(BigDecimal xPos) {
        this.xPos = xPos;
    }
    public BigDecimal getyPos() {
        return yPos;
    }
    public void setyPos(BigDecimal yPos) {
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
        xPos = xPos.add(new BigDecimal(x*(1/scale)));
    }
    public void addyPos(double y) {
        yPos = yPos.add(new BigDecimal(y*(1/scale)));
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
        if (scale < 2E28) {
            scale *= 2;
        }
    }
    private void divideScale() {
        if (scale > 2E-28) {
            scale /= 2;
        }
    }
}
