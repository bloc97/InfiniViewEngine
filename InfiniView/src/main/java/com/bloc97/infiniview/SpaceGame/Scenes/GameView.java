/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bloc97.infiniview.SpaceGame.Scenes;

import com.bloc97.infinisim.NBody.Equations;
import com.bloc97.infinisim.NBody.NBodySimulation;
import com.bloc97.infinisim.NBody.Optimisers;
import com.bloc97.infinisim.Spatial3DView;
import com.bloc97.infiniview.SpaceGame.Objects.SpaceNatural;
import com.bloc97.infiniview.SpaceGame.Universe;
import com.bloc97.infiniview.World2D.Camera;
import com.bloc97.infiniview.World2D.Objects.DisplayObject;
import com.bloc97.infiniview.World2D.Scene;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

/**
 *
 * @author bowen
 */
public class GameView extends Scene {
    
    private ExecutorService executor = Executors.newCachedThreadPool();
    
    private NBodySimulation mainNBodySimulation = new NBodySimulation(Equations.EquationType.NEWTON, Optimisers.OptimiserType.DIRECT, 100, 30d, 1, 1, true);
    
    private Boolean keyW = false;
    private Boolean keyS = false;
    private Boolean keyA = false;
    private Boolean keyD = false;
    
    private Spatial3DView trackedObject;
    private final Universe spaceWorld;
    
    private long msCounter = 1;
    private int fpsCounter = 0;
    
    public GameView() {
        this(60, 0, 0);
    }
    
    public GameView(int desiredFPS, int xsize, int ysize) {
        super(desiredFPS, xsize, ysize);
        spaceWorld = new Universe();
        setBackground(Color.getHSBColor(298F/360, 1F/100, 22F/100));
        
        //updateDisplayObjectsCanRenderByScale();
        initialiseHandlers();
        mainNBodySimulation.setSimulatedWorld(spaceWorld.getWorld());
        executor.submit(mainNBodySimulation);
    }
    
    public final void initialiseHandlers() {
        
        this.addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int notches = e.getWheelRotation();
                
                    camera.addScale(notches);
                /*
                if (trackedObject == null) {
                    
                    Spatial3DView object = checkClosestObject(e.getX(), e.getY());
                    
                    Camera newCamera = camera.copy();
                    
                    int xDistanceFromCenter = 0;
                    int yDistanceFromCenter = 0;
                    
                    if (object == null) {
                        xDistanceFromCenter = (e.getX() - getBounds().width/2);
                        yDistanceFromCenter = (e.getY() - getBounds().height/2);
                    } else {
                        xDistanceFromCenter = (int)object.getSx(newCamera) - getBounds().width/2;
                        yDistanceFromCenter = (int)object.getSy(newCamera) - getBounds().height/2;
                    }
                    newCamera.addxPos(xDistanceFromCenter);
                    newCamera.addyPos(yDistanceFromCenter);
                    newCamera.addScale(notches);
                    newCamera.addxPos(-xDistanceFromCenter);
                    newCamera.addyPos(-yDistanceFromCenter);

                    camera.setScale(newCamera.getScale());
                    camera.setxPos(newCamera.getxPos());
                    camera.setyPos(newCamera.getyPos());
                        
                } else {
                    camera.addScale(notches);
                }
                */
                //updateDisplayObjectsCanRenderByScale();
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
                    case KeyEvent.VK_EQUALS :
                        mainNBodySimulation.setSimulatedSecondsPerTick(mainNBodySimulation.getSimulatedSecondsPerTick() * 2);
                        break;
                    case KeyEvent.VK_MINUS :
                        mainNBodySimulation.setSimulatedSecondsPerTick(mainNBodySimulation.getSimulatedSecondsPerTick() / 2);
                        break;
                    case KeyEvent.VK_CLOSE_BRACKET :
                        if (mainNBodySimulation.getTicksPerUpdate() > 1024) {
                            break;
                        }
                        mainNBodySimulation.setTicksPerUpdate(mainNBodySimulation.getTicksPerUpdate() * 2);
                        break;
                    case KeyEvent.VK_OPEN_BRACKET :
                        if (mainNBodySimulation.getTicksPerUpdate() == 1) {
                            break;
                        }
                        mainNBodySimulation.setTicksPerUpdate(mainNBodySimulation.getTicksPerUpdate() / 2);
                        break;
                    case KeyEvent.VK_SPACE:
                        togglePause();
                        break;
                    case KeyEvent.VK_M:
                        mainNBodySimulation.update();
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
        
        this.addMouseMotionListener(new MouseMotionAdapter() {
            
            private int lastX = -1;
            private int lastY = -1;

            @Override
            public void mouseMoved(MouseEvent e) {
                lastX = e.getX();
                lastY = e.getY();
            }
            
            @Override
            public void mouseDragged(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    releaseFocus();
                    if (lastX == -1 || lastY == -1) {
                        lastX = e.getX();
                        lastY = e.getY();
                    } else {
                        int changeX = lastX - e.getX();
                        int changeY = lastY - e.getY();
                        camera.addxPos(changeX);
                        camera.addyPos(changeY);
                        lastX = e.getX();
                        lastY = e.getY();
                    }
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
        mainNBodySimulation.toggle();
    }
    
    public Spatial3DView checkClosestObject(int x, int y) {
        for (int i=0; i<spaceWorld.getWorld().size(); i++) {
            Spatial3DView s = spaceWorld.getWorld().get3DView(i);
            
            if (s != null) {
                if (s.isActive()) {
                    double tr = camera.getScreenR(s.getRadius());
                    if (tr < 10) tr = 10;
                    //System.out.println("checking " + x + " " + y + " " + tr);
                    
                    double tx = camera.getScreenX(s.position().get(0));
                    if (!(x > tx-tr && x < tx+tr)) {
                        continue;
                    }
                    
                    double ty = camera.getScreenY(s.position().get(1));
                    if (!(y > ty-tr && y < ty+tr)) {
                        continue;
                    }
                    return s;
                }
            }
            
        }
        /*
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
                return currentObject;
            }
        }*/
        return null;
    }
    
    public boolean checkClickFocus(int x, int y) {
        
        Spatial3DView object = checkClosestObject(x, y);
        
        if (object == null) {
            releaseFocus();
            return false;
        } else {
            setFocus(object);
            return true;
        }
    }
    public void setFocus(Spatial3DView object) {
        trackedObject = object;
        focusCamera();
    }
    
    public void releaseFocus() {
        trackedObject = null;
        //mainSimulation.setFocus(null);
    }
    
    public void focusCamera() {
        if (trackedObject != null) {
            camera.setxPos(trackedObject.position().get(0));
            camera.setyPos(trackedObject.position().get(1));
            camera.setzPos(trackedObject.position().get(2));
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
        } else if (seconds < 60*60*24*365.25*100) {
            return Math.floor(seconds/60/60/24/365.25) + "years/s";
        } else if (seconds < 60*60*24*365.25*100000) {
            return Math.floor(seconds/60/60/24/365.25/100) + "centuries/s";
        }
        return seconds + "";
    }
    /*
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
    }*/
    
    @Override
    protected void beforePaint() {
        checkKeys();
        //updateDisplayObjectsPosition();
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
        if (mainNBodySimulation.getSimulatedWorld() != null) {
            g.drawString(mainNBodySimulation.getSimulatedWorld().getDate().toGMTString(), 10, 20);
        }
        g.drawString("Target Speed: " + secondsToText(mainNBodySimulation.getRealUPS() * mainNBodySimulation.getSimulatedSecondsPerUpdate()), 10, 40);
        g.drawString("Time Per Tick: " + secondsToText(mainNBodySimulation.getSimulatedSecondsPerTick()), 10, 60);
        g.drawString("Current Scale: " + camera.getScale(), 10, 80);
        g.drawString("FPS: " + fpsCounter, 10, 100);
        g.drawString("UPS: " + mainNBodySimulation.getRealUPS(), 10, 120);
        
        if (xsize == 0) {
            xsize = 1;
            ysize = 1;
        }
        
        BufferedImage image = new BufferedImage(xsize, ysize, BufferedImage.TYPE_INT_RGB);
        
        Graphics2D ig2 = image.createGraphics();
        
        Graphics2D g2 = (Graphics2D)g;
        
        focusCamera();
        
        for (int i=0; i<spaceWorld.getWorld().size(); i++) {
            Spatial3DView s = spaceWorld.getWorld().get3DView(i);
            if (s != null) {
                
                //int x = (int)((s.position().get(0)-camera.getxPos())*camera.getScale());
                //int y = (int)((s.position().get(1)-camera.getyPos())*camera.getScale());
                
                double x = camera.getScreenX(s.position().get(0));
                double y = camera.getScreenY(s.position().get(1));
                
                double r = camera.getScreenR(s.getRadius());
                
                Shape shape = null;
                
                if (r < 1) {
                    shape = new Rectangle2D.Double(x-r, y-r, 1, 1);
                } else if (r < 2) {
                    shape = new Rectangle2D.Double(x-r, y-r, r*2, r*2);
                } else if (r < 4) {
                    shape = new Rectangle2D.Double(x-(r/2), y-r, r, r*2);
                    g2.fill(new Rectangle2D.Double(x-r, y-(r/2), r*2, r));
                } else if (x > -xsize && x < xsize*2 && y > -ysize && y < ysize*2) {
                    shape = new Ellipse2D.Double(x-r, y-r, r*2, r*2);
                }
                
                if (shape != null) {
                    g2.fill(shape);
                }
                g2.drawString("Planet", (float)(x+r+4), (float)(y+5));
                
                //ig2.fillOval(x, y, r, r);
                
                //g2.fillOval(x, y, r, r);
                
            }
            
        }
        //g2.drawImage(image, null, 0, 0);
        
        
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
