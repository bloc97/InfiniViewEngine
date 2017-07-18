/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bloc97.infiniview.SpaceGame.Scenes;

import com.bloc97.infiniview.SpaceGame.Objects.SpaceNatural;
import com.bloc97.infiniview.SpaceGame.Universe;
import com.bloc97.infiniview.World2D.Objects.DisplayObject;
import com.bloc97.infiniview.World2D.Scene;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.util.Date;

/**
 *
 * @author bowen
 */
public class GameView extends Scene {
    
    private Boolean keyW = false;
    private Boolean keyS = false;
    private Boolean keyA = false;
    private Boolean keyD = false;
    
    private DisplayObject trackedObject;
    private Universe spaceWorld = new Universe();
    
    private long msCounter = 1;
    private int fpsCounter = 0;
    
    public GameView() {
        this(60, 0, 0);
    }
    
    public GameView(int desiredFPS, int xsize, int ysize) {
        super(desiredFPS, xsize, ysize);
        setBackground(Color.getHSBColor(298F/360, 1F/100, 22F/100));
        
        updateDisplayObjectsCanRenderByScale();
        initialiseHandlers();
        
    }
    
    public final void initialiseHandlers() {
        
        this.addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int notches = e.getWheelRotation();
                camera.addScale(notches);
                updateDisplayObjectsCanRenderByScale();
            }
            
        });
        
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                //System.out.println(e.getKeyChar());
                 switch(e.getKeyCode()) {
                    case KeyEvent.VK_W :
                        keyW = true;
                        releaseFocus();
                        break;
                    case KeyEvent.VK_S :
                        keyS = true;
                        releaseFocus();
                        break;
                    case KeyEvent.VK_A :
                        keyA = true;
                        releaseFocus();
                        break;
                    case KeyEvent.VK_D :
                        keyD = true;
                        releaseFocus();
                        break;
                    case KeyEvent.VK_E :
                        spaceWorld.getSimulation().setSimulatedSecondsPerTick(spaceWorld.getSimulation().getSimulatedSecondsPerTick() * 2);
                        //((NBodySimulation)(worlds[0].getSimulations()[0])).reCalculateOrbits();
                        break;
                    case KeyEvent.VK_Q :
                        spaceWorld.getSimulation().setSimulatedSecondsPerTick(spaceWorld.getSimulation().getSimulatedSecondsPerTick() / 2);
                        //spaceWorld.getSimulation().speedDown();
                        //((NBodySimulation)(worlds[0].getSimulations()[0])).reCalculateOrbits();
                        break;
                    case KeyEvent.VK_SPACE:
                        togglePause();
                        break;
                    case KeyEvent.VK_F11:
                        ((GameView)e.getComponent()).viewport.toggleFullScreen();
                        System.out.println("FullScreen");
                        break;
                    default :
                        break;
                 }
            }
            @Override
            public void keyReleased(KeyEvent e) {
                 switch(e.getKeyCode()) {
                    case KeyEvent.VK_W :
                        keyW = false;
                        break;
                    case KeyEvent.VK_S :
                        keyS = false;
                        break;
                    case KeyEvent.VK_A :
                        keyA = false;
                        break;
                    case KeyEvent.VK_D :
                        keyD = false;
                        break;
                    default :
                        break;
                 }
            }
        });
        
        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {
                int x = me.getX();
                int y = me.getY();
                checkClickFocus(x, y);
            }

            @Override
            public void mousePressed(MouseEvent me) {
            }

            @Override
            public void mouseReleased(MouseEvent me) {
            }

            @Override
            public void mouseEntered(MouseEvent me) {
            }

            @Override
            public void mouseExited(MouseEvent me) {
            }
        });
    }
    
    public void togglePause() {
        spaceWorld.getSimulation().toggle();
    }
    
    public void checkClickFocus(int x, int y) {
        for (DisplayObject currentObject : spaceWorld.getDisplayObjects()) {
            //if (tx >= 0 && ty >= 0 && tx <= camera.getxScrOffset()*2 && ty <= camera.getyScrOffset()) {
            if (currentObject.isVisible()) {
                double tr = currentObject.getSr(camera);
                if (tr < 10) tr = 10;
                
                double tx = currentObject.getSx(camera);
                if (!(x > tx-tr && x < tx+tr)) {
                    continue;
                }
                
                double ty = currentObject.getSy(camera);
                if (!(y > ty-tr && y < ty+tr)) {
                    continue;
                }
                
                //if (x > tx-tr && x < tx+tr && y > ty-tr && y < ty+tr) {
                setFocus(currentObject);
                return;
                //}
            }
        }
        releaseFocus();
    }
    public void setFocus(DisplayObject object) {
        trackedObject = object;
        focusCamera();
        if (trackedObject instanceof SpaceNatural) {
            //mainSimulation.setFocus((SpaceNatural)trackedObject);
        } else {
            //mainSimulation.setFocus(null);
        }
        //mainSimulation.reCalculateOrbits();
    }
    
    public void releaseFocus() {
        trackedObject = null;
        //mainSimulation.setFocus(null);
    }
    
    public void focusCamera() {
        if (trackedObject instanceof DisplayObject) {
            camera.setxPos(trackedObject.getX());
            camera.setyPos(-trackedObject.getY());
        }
    }
    
    
    
    public void checkKeys() {
        if (keyW) {
            camera.addyPos(-20);
        }
        if (keyS) {
            camera.addyPos(20);
        }
        if (keyA) {
            camera.addxPos(-20);
        }
        if (keyD) {
            camera.addxPos(20);
        }
    }
    public static String secondsToText(double seconds) {
        if (seconds < 60) {
            return seconds + "s/s";
        } else if (seconds < 60*60) {
            return Math.floor(seconds/60) + "min/s";
        } else if (seconds < 60*60*24) {
            return Math.floor(seconds/60/60) + "hours/s";
        } else if (seconds < 60*60*24*365.25) {
            return Math.floor(seconds/60/60/24) + "days/s";
        } else if (seconds < 60*60*24*365.25*10) {
            return Math.floor(seconds/60/60/24/365.25) + "years/s";
        }
        return seconds + "";
    }
    
    private void updateDisplayObjectsCanRenderByScale() {
        for (DisplayObject object : spaceWorld.getDisplayObjects()) {
            if (object instanceof SpaceNatural) {
                ((SpaceNatural)object).updateCanRenderByScale(camera);
            }
        }
    }
    
    private void updateDisplayObjectsPosition() {
        for (DisplayObject object : spaceWorld.getDisplayObjects()) {
            if (object instanceof SpaceNatural) {
                ((SpaceNatural)object).registerUpdate(new Date());
            }
        }
    }
    
    @Override
    protected void beforePaint() {
        checkKeys();
        updateDisplayObjectsPosition();
    }
    @Override
    protected void prePaint() {
        msCounter = System.currentTimeMillis();
    }
    @Override
    protected void onPaint(Graphics g) {
        g.setColor(Color.YELLOW);
        /*
        g.drawLine(0, 0, 0, 1080);
        g.drawLine(0, 0, 1920, 0);
        g.drawLine(1920, 0, 1920, 1080);
        g.drawLine(0, 1080, 1920, 1080);*/
        
        g.drawString(spaceWorld.getSimulation().getDate().toGMTString(), 10, 20);
        g.drawString(secondsToText(spaceWorld.getSimulation().getSimulatedSecondsPerSecond()), 10, 40);
        g.drawString("Current Scale: " + camera.getScale(), 10, 60);
        g.drawString("FPS: " + fpsCounter, 10, 80);
        
        
        Graphics2D g2 = (Graphics2D)g;
        /*
        AffineTransform originalTransform = g2.getTransform();
        AffineTransform transform = new AffineTransform();
        double scale = camera.getScale();
        transform.scale(scale, -scale);
        transform.translate(-camera.getxPos(), camera.getyPos());
        g2.translate(camera.getxScrOffset(), camera.getyScrOffset());
        g2.transform(transform);
        for (int i=0; i<displayObjects.length; i++) {
        displayObjects[i].renderTransform(g2, camera);
        }
        g2.setTransform(originalTransform);
         */
        for (DisplayObject displayObject : spaceWorld.getDisplayObjects()) {
            if (displayObject == trackedObject) {;// || trackedObject != null) {
                focusCamera();
            }
            /*
            if (displayObjects[i] instanceof FuturePath) {
            ((FuturePath)displayObjects[i]).setOrbitReferencePath(trackedObject.paths);
            }*/
            displayObject.renderNoTransform(g2, camera);
        }
        
    }
    @Override
    protected void postPaint() {
        int msDiff = (int)(System.currentTimeMillis() - msCounter);
        if (msDiff == 0) {
            msDiff = 1;
        }
        int newFps = Math.floorDiv(1000, msDiff);
        fpsCounter = (newFps <= 60) ? newFps : 60;
        
    }
    @Override
    protected void afterPaint() {
        
    }
    


}
