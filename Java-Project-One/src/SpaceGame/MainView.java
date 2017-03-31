/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SpaceGame;

import World2D.Objects.PlanetDisplay;
import World2D.Scene;
import World2D.World;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;

/**
 *
 * @author bowen
 */
public class MainView extends Scene {
    
    private Boolean keyW = false;
    private Boolean keyS = false;
    private Boolean keyA = false;
    private Boolean keyD = false;
    
    public MainView(int xsize, int ysize) {
        this(60, xsize, ysize);
    }
    
    public MainView(int desiredFPS, int xsize, int ysize) {
        super(desiredFPS, xsize, ysize, new World[] {new SolarSystem()});
        
        this.setBackground(Color.getHSBColor(298F/360, 1F/100, 22F/100));
        
        this.setDisplayObjects(worlds);
        
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
                        break;
                    case KeyEvent.VK_S :
                        keyS = true;
                        break;
                    case KeyEvent.VK_A :
                        keyA = true;
                        break;
                    case KeyEvent.VK_D :
                        keyD = true;
                        break;
                    case KeyEvent.VK_E :
                        worlds[0].getSimulations()[0].speedUp();
                        break;
                    case KeyEvent.VK_Q :
                        worlds[0].getSimulations()[0].speedDown();
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
        g.drawLine(0, 0, 0, 1080);
        g.drawLine(0, 0, 1920, 0);
        g.drawLine(1920, 0, 1920, 1080);
        g.drawLine(0, 1080, 1920, 1080);
        
        g.drawString(worlds[0].getSimulations()[0].getDate().toGMTString(), 10, 20);
        g.drawString(secondsToText(worlds[0].getSimulations()[0].getSpeed()), 10, 40);
        g.drawString("Current Scale: " + camera.getScale(), 10, 60);
        
        
        Graphics2D g2 = (Graphics2D)g;
        
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
        
        for (int i=0; i<displayObjects.length; i++) {
            if (displayObjects[i] instanceof PlanetDisplay) {
                PlanetDisplay planet = (PlanetDisplay) displayObjects[i];
                planet.renderNoTransform(g2, camera);
            }
        }
        
    }
    @Override
    protected void tick() {
        checkKeys();
    }
    


}
