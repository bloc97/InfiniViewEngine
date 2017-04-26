/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package World2D;

import World2D.Objects.DisplayObject;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JPanel;

/**
 *
 * @author bowen
 */
public abstract class SceneSwing extends JPanel implements Runnable, Scene {
    protected Thread thread;
    protected boolean isActive;
    protected int desiredUPS;
    
    protected DisplayObject[] displayObjects = new DisplayObject[0];
    
    final protected Camera camera;
    
    protected int xsize, ysize;
    
    protected World[] worlds;
    protected Viewport viewport;
    
    public SceneSwing() {
        this(60);
    }
    public SceneSwing(int desiredUPS) {
        this(desiredUPS, 0, 0);
    }
    
    public SceneSwing(int desiredUPS, int xsize, int ysize) {
        this.isActive = false;
        this.desiredUPS = desiredUPS;
        this.camera = new Camera(this, xsize, ysize);
        
        this.xsize = xsize;
        this.ysize = ysize;
        
        this.setLayout(null);
        
        thread = new Thread(this);
        
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Rectangle r = e.getComponent().getBounds();
                int h = r.height;
                int w = r.width;
                SceneSwing thisScene = ((SceneSwing)(e.getComponent()));
                thisScene.xsize = w;
                thisScene.ysize = h;
                Camera thisCamera = thisScene.getCamera();
                thisCamera.setScreenSize(w, h);
            }
        });
    }
    public SceneSwing(int desiredUPS, int xsize, int ysize, World... worlds) {
        this(desiredUPS, xsize, ysize);
        this.worlds = worlds;
    }
    
    final public void setDisplayObjects(World... worlds) {
        int length = 0;
        for (World world : worlds) {
            length += world.getDisplayObjects().length;
        }
        this.displayObjects = new DisplayObject[length];
        for (World world : worlds) {
            DisplayObject[] nDisplayObjects = world.getDisplayObjects();
            System.arraycopy(nDisplayObjects, 0, this.displayObjects, 0, displayObjects.length); //this.add(displayObjects[i].getJComponent());
        }
    }
    final public void setDisplayObjects(DisplayObject... displayObjects) {
        this.displayObjects = new DisplayObject[displayObjects.length];
        System.arraycopy(displayObjects, 0, this.displayObjects, 0, displayObjects.length); //this.add(displayObjects[i].getJComponent());
    }
    
    @Override
    public Viewport getViewport() {
        return viewport;
    }
    
    @Override
    public void setViewport(Viewport viewport) {
        this.viewport = viewport;
    }
    
    @Override
    public void deactivate() {
        this.setVisible(false);
        isActive = false;
    }
    @Override
    public void activate() {
        this.setVisible(true);
        isActive = true;
    }
    @Override
    public void launch() {
        this.thread.start();
        activate();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        prePaint();
        super.paintComponent(g);
        onPaint((Graphics2D)g);
        postPaint();
    }
    
    protected abstract void beforePaint();
    protected abstract void prePaint();
    protected abstract void onPaint(Graphics2D g2);
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
    @Override
    public Component getComponent() {
        return this;
    }

}
