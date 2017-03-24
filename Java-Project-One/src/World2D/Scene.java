/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package World2D;

import MainPackages.Main;
import World2D.Objects.Circle;
import World2D.Objects.DisplayObject;
import World2D.Objects.Interpolable;
import World2D.Objects.Line;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseWheelEvent;
import javax.swing.JPanel;

/**
 *
 * @author bowen
 */
public class Scene extends JPanel implements Runnable {
    private Thread thread;
    private Boolean isActive;
    private int desiredFPS;
    
    private DisplayObject[] displayObjects = new DisplayObject[0];
    
    private Camera camera;
    
    private Boolean keyW = false;
    private Boolean keyS = false;
    private Boolean keyA = false;
    private Boolean keyD = false;
    
    private int xsize, ysize;
    
    private World[] worlds;
    
    
    public Scene(int xsize, int ysize, World... worlds) {
        this(60, xsize, ysize, worlds);
    }
    
    public Scene(int desiredFPS, int xsize, int ysize, World... worlds) {
        this.isActive = false;
        this.desiredFPS = desiredFPS;
        this.camera = new Camera(this, xsize, ysize);
        this.worlds = worlds;
        
        this.xsize = xsize;
        this.ysize = ysize;
        
        this.setLayout(null);
        this.setVisible(true);
        this.setBackground(Color.getHSBColor(298F/360, 1F/100, 22F/100));
        
        thread = new Thread(this);
        
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
                        worlds[0].getSimulation().speedUp();
                        break;
                    case KeyEvent.VK_Q :
                        worlds[0].getSimulation().speedDown();
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
    
    public void setDisplayObjects(World... worlds) {
        int length = 0;
        for (int i=0; i<worlds.length; i++) {
            length += worlds[i].getDisplayObjects().length;
        }
        this.displayObjects = new DisplayObject[length];
        for (int n=0; n<worlds.length; n++) {
            DisplayObject[] nDisplayObjects = worlds[n].getDisplayObjects();
            for (int i=0; i<displayObjects.length; i++) {
                this.displayObjects[i] = nDisplayObjects[i];
                
                if (this.displayObjects[i] instanceof Interpolable) {
                    ((Interpolable)this.displayObjects[i]).setInterpolationFrameTime(1D/desiredFPS); //TODO Calculate FPS to interpolate when frametime changes
                }
                //this.add(displayObjects[i].getJComponent());
            }
        }
    }
    public void setDisplayObjects(DisplayObject... displayObjects) {
        this.displayObjects = new DisplayObject[displayObjects.length];
        for (int i=0; i<displayObjects.length; i++) {
            this.displayObjects[i] = displayObjects[i];
            
                if (this.displayObjects[i] instanceof Interpolable) {
                    ((Interpolable)this.displayObjects[i]).setInterpolationFrameTime(1D/desiredFPS); //TODO Calculate FPS to interpolate when frametime changes
                }
            //this.add(displayObjects[i].getJComponent());
        }
    }
    private void updateCameraToObjects() {
        for (int i=0; i<displayObjects.length; i++) {
            if (!displayObjects[i].isHidden()) {
                this.displayObjects[i].update(camera);
            }
        }
    }
    
    public void deactivate() {
        isActive = false;
    }
    public void activate() {
        isActive = true;
    }
    public void start() {
        this.thread.start();
        activate();
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
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        drawAllObjects(g);
        g.setColor(Color.YELLOW);
        g.drawLine(0, 0, 0, 1080);
        g.drawLine(0, 0, 1920, 0);
        g.drawLine(1920, 0, 1920, 1080);
        g.drawLine(0, 1080, 1920, 1080);
        
        Graphics2D g2 = (Graphics2D)g;
        g.drawString(worlds[0].getSimulation().getDate().toGMTString(), 10, 20);
        g.drawString(secondsToText(worlds[0].getSimulation().getSpeed()), 10, 40);
        
        /*g.setColor(Color.RED);
        g.drawRect(150,10,100,20);  
        g.fillRect(150,10,100,20);
        g.drawLine(200,10 , 200, 2000);
        g.setColor(Color.BLACK);
        g.drawString("UE",190 ,25 );
         ... All drawing code ... */
    }
    
    private void drawAllObjects(Graphics g) {
        for (int i=0; i<displayObjects.length; i++) {
            //if (displayObjects[i].isInView(-50, -50, 1920+50, 1080+50) && !displayObjects[i].isHidden()) {
            if (!displayObjects[i].isHidden()) {
                switch(displayObjects[i].getType()) {
                    case Circle:
                        drawCircle(g, (Circle)displayObjects[i]);
                        break;
                    case Line:
                        drawLine(g, (Line)displayObjects[i]);
                        break;
                    default:
                        break;
                }
            }
        }
    }
    
    private void drawCircle(Graphics g, Circle circle) {
        int r = circle.getRadius();
        g.setColor(circle.getColor());
        g.fillOval(circle.getDix(), circle.getDiy(), r*2, r*2);
        g.drawString(circle.getName(), circle.getDix()+20, circle.getDiy()+r*2);
    }
    private void drawLine(Graphics g, Line line) {
        g.setColor(line.getColor());
        g.drawLine(line.getDix0(), line.getDiy0(), line.getDix1(), line.getDiy1());
    }
    
    @Override
    public void run() {
        
        double desiredSleepms = 1000D/desiredFPS;
        
        long startTime;
        long endTime;
        long sleepTime;
        
        while (true) {
            if (isActive) {
                
                startTime = System.nanoTime();
                updateCameraToObjects();
                //invalidate();
                repaint();
                checkKeys();
                endTime = System.nanoTime();
                
                sleepTime = (long)(desiredSleepms*1000000) - (endTime-startTime);
                if (sleepTime < 0) {
                    System.out.println("Graphics Thread Overload");
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


}
