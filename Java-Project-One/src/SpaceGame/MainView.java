/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SpaceGame;

import MathExt.Ext;
import Physics2D.Objects.LinearMotion;
import SpaceGame.Objects.SpaceNatural;
import World2D.Camera;
import World2D.Objects.DisplayObject;
import World2D.Objects.Interpolable;
import World2D.Scene;
import World2D.World;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;

/**
 *
 * @author bowen
 */
public class MainView extends Scene {
    
    private Boolean keyW = false;
    private Boolean keyS = false;
    private Boolean keyA = false;
    private Boolean keyD = false;
    
    private DisplayObject trackedObject;
    
    public MainView(int xsize, int ysize) {
        this(60, xsize, ysize);
    }
    
    public MainView(int desiredFPS, int xsize, int ysize) {
        super(desiredFPS, xsize, ysize, new World[] {new OurSolarSystem()});
        
        this.setBackground(Color.getHSBColor(298F/360, 1F/100, 22F/100));
        
        this.setDisplayObjects(worlds);
        
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Rectangle r = e.getComponent().getBounds();
                int h = r.height;
                int w = r.width;
                Camera thisCamera = ((Scene)(e.getComponent())).getCamera();
                thisCamera.setScreenSize(w, h);
            }
        });
        
        this.addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int notches = e.getWheelRotation();
                camera.addScale(notches);
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
                        worlds[0].getSimulations()[0].speedUp();
                        //((NBodySimulation)(worlds[0].getSimulations()[0])).reCalculateOrbits();
                        break;
                    case KeyEvent.VK_Q :
                        worlds[0].getSimulations()[0].speedDown();
                        //((NBodySimulation)(worlds[0].getSimulations()[0])).reCalculateOrbits();
                        break;
                    case KeyEvent.VK_SPACE:
                        togglePause();
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
        worlds[0].getSimulations()[0].togglePause();
        for (int i=0; i<displayObjects.length; i++) {
            if (displayObjects[i] instanceof Interpolable) {
                ((Interpolable)(displayObjects[i])).togglePause();
            }
        }
    }
    
    public void checkClickFocus(int x, int y) {
        for (int i=0; i<displayObjects.length; i++) {
            
            DisplayObject currentObject = displayObjects[i];
            
            double tx = currentObject.getSx(camera);
            double ty = currentObject.getSy(camera);
            double tr = currentObject.getSr(camera);
            
            if (tr < 10) tr = 10;
            
            //if (tx >= 0 && ty >= 0 && tx <= camera.getxScrOffset()*2 && ty <= camera.getyScrOffset()) {
            if (currentObject.isVisible(camera.getScale())) {
                if (x > tx-tr && x < tx+tr && y > ty-tr && y < ty+tr) {
                    trackedObject = currentObject;
                    return;
                }
            }
        }
        releaseFocus();
    }
    public void releaseFocus() {
        trackedObject = null;
    }
    
    public void focusCamera() {
        if (trackedObject instanceof Interpolable) {
            Interpolable centerObject = (Interpolable)trackedObject;
            camera.setxPos(centerObject.getIx());
            camera.setyPos(-centerObject.getIy());
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
    
    
    @Override
    protected void drawAllObjects(Graphics g) {
        g.setColor(Color.YELLOW);
        /*
        g.drawLine(0, 0, 0, 1080);
        g.drawLine(0, 0, 1920, 0);
        g.drawLine(1920, 0, 1920, 1080);
        g.drawLine(0, 1080, 1920, 1080);*/
        
        g.drawString(worlds[0].getSimulations()[0].getDate().toGMTString(), 10, 20);
        g.drawString(secondsToText(worlds[0].getSimulations()[0].getSpeed()), 10, 40);
        g.drawString("Current Scale: " + camera.getScale(), 10, 60);
        
        
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
        for (int i=0; i<displayObjects.length; i++) {
            if (displayObjects[i] == trackedObject) {
                focusCamera();
            }
            displayObjects[i].renderNoTransform(g2, camera);
        }
        
    }
    @Override
    protected void tick() {
        checkKeys();
    }
    


}
