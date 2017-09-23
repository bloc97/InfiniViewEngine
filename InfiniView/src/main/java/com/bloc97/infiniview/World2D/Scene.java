/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bloc97.infiniview.World2D;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JPanel;

/**
 *
 * @author bowen
 */
public abstract class Scene extends JPanel implements Runnable {
    private Thread thread;
    private Boolean isActive = false;
    
    protected int desiredUPS;
    
    protected Camera camera;
    
    protected int xsize, ysize;
    
    protected Viewport viewport;
    
    public Scene() {
        this(60);
    }
    public Scene(int desiredUPS) {
        this(desiredUPS, 0, 0);
    }
    
    public Scene(int desiredUPS, int xsize, int ysize) {
        this.desiredUPS = desiredUPS;
        this.camera = new Camera(this, xsize, ysize);
        
        this.xsize = xsize;
        this.ysize = ysize;
        
        this.setLayout(null);
        this.setVisible(true);
        
        thread = new Thread(this);
        
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Rectangle r = e.getComponent().getBounds();
                int h = r.height;
                int w = r.width;
                Scene thisScene = ((Scene)(e.getComponent()));
                thisScene.xsize = w;
                thisScene.ysize = h;
                Camera thisCamera = thisScene.getCamera();
                thisCamera.setScreenSize(w, h);
            }
        });
    }
    
    
    public Viewport viewport() {
        return viewport;
    }
    
    public void setViewport(Viewport viewport) {
        this.viewport = viewport;
        if (!this.equals(viewport.getScene())) {
            viewport.setScene(this);
        }
    }
    
    public void start() {
        this.thread.start();
        activate();
    }
    public void activate() {
        isActive = true;
    }
    public void deactivate() {
        isActive = false;
    }
    public void toggle() {
        isActive = !isActive;
    }

    public boolean isActive() {
        return isActive;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        prePaint();
        super.paintComponent(g);
        onPaint(g);
        postPaint();
    }
    
    protected abstract void beforePaint();
    protected abstract void prePaint();
    protected abstract void onPaint(Graphics g);
    protected abstract void postPaint();
    protected abstract void afterPaint();
    
    @Override
    public void run() {
        
        double desiredSleepms = 1000D/desiredUPS;
        
        long startTime;
        long endTime;
        long sleepTime;
        
        while (true) {
            if (isActive) {
                
                startTime = System.nanoTime();
                //updateCameraToObjects();
                //invalidate();
                beforePaint();
                repaint();
                afterPaint();
                endTime = System.nanoTime();
                
                sleepTime = (long)(desiredSleepms*1000000) - (endTime-startTime);
                if (sleepTime < 0) {
                    System.out.println("Scene Thread Overload");
                } else {
                    long sleepms = Math.floorDiv(sleepTime, 1000000);
                    int sleepns = (int)Math.floorMod(sleepTime, 1000000);
                    try {
                        Thread.sleep(sleepms, sleepns);
                    } catch (InterruptedException ex) {
                        System.out.println("Thread Error");
                    }
                }
            }
        }
        
    }
    
    public Camera getCamera() {
        return camera;
    }

}
