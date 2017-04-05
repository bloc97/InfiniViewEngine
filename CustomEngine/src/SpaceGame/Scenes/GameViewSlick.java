/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SpaceGame.Scenes;

import SpaceGame.Objects.SpaceNatural;
import SpaceGame.SpaceSimulation;
import SpaceGame.GameWorld.Universe;
import SpaceGame.Slick.SceneSlick;
import World2D.Objects.DisplayObject;
import World2D.Objects.Interpolable;
import World2D.SceneSwing;
import World2D.World;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Transform;

/**
 *
 * @author bowen
 */
public class GameViewSlick extends SceneSlick {
    
    private Boolean keyW = false;
    private Boolean keyS = false;
    private Boolean keyA = false;
    private Boolean keyD = false;
    
    private DisplayObject trackedObject;
    private Universe spaceWorld = new Universe();
    private SpaceSimulation mainSimulation;
    
    private long msCounter = 1;
    private int fpsCounter = 0;
    
    private int objPaintCounter = 0;
    
    public static SceneSlick newGameViewSlick() throws SlickException {
        GameThread game = new GameThread("");
        GameViewSlick newScene = new GameViewSlick(game, 60, 0, 0);
        game.setScene(newScene);
        return newScene;
    }
    
    public GameViewSlick(GameThread game, int desiredFPS, int xsize, int ysize) throws SlickException {
        super(game, desiredFPS, xsize, ysize);
        setBackground(Color.getHSBColor(298F/360, 1F/100, 22F/100));
        this.worlds = new World[] {spaceWorld};
        this.mainSimulation = (SpaceSimulation)spaceWorld.getSimulations()[0];
        this.setDisplayObjects(worlds);
        updateDisplayObjectsCanRenderByScale();
        initialiseHandlers();
        
    }
    
    public final void initialiseHandlers() {
        /*
        if (this.getContainer().getInput() == null) {
            System.out.println("null");
        }
        
        
        this.getContainer().getInput().addMouseListener(new org.newdawn.slick.MouseListener() {
            @Override
            public void mouseWheelMoved(int i) {
                camera.addScale(i);
                updateDisplayObjectsCanRenderByScale();
                
            }

            @Override
            public void mouseClicked(int i, int i1, int i2, int i3) {
                
            }

            @Override
            public void mousePressed(int i, int i1, int i2) {
                
            }

            @Override
            public void mouseReleased(int i, int i1, int i2) {
                
            }

            @Override
            public void mouseMoved(int i, int i1, int i2, int i3) {
                
            }

            @Override
            public void mouseDragged(int i, int i1, int i2, int i3) {
                
            }

            @Override
            public void setInput(Input input) {
                
            }

            @Override
            public boolean isAcceptingInput() {
                return true;
            }

            @Override
            public void inputEnded() {
                
            }

            @Override
            public void inputStarted() {
                
            }
        });*/
        
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
                        worlds[0].getSimulations()[0].speedUp();
                        break;
                    case KeyEvent.VK_Q :
                        worlds[0].getSimulations()[0].speedDown();
                        break;
                    case KeyEvent.VK_SPACE:
                        togglePause();
                        break;
                    case KeyEvent.VK_F11:
                        ((GameViewSlick)e.getComponent()).viewport.toggleFullScreen();
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
        worlds[0].getSimulations()[0].togglePause();
        for (int i=0; i<displayObjects.length; i++) {
            if (displayObjects[i] instanceof Interpolable) {
                ((Interpolable)(displayObjects[i])).togglePause();
            }
        }
    }
    
    public void checkClickFocus(int x, int y) {
        for (DisplayObject currentObject : displayObjects) {
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
            mainSimulation.setFocus((SpaceNatural)trackedObject);
        } else {
            mainSimulation.setFocus(null);
        }
        //mainSimulation.reCalculateOrbits();
    }
    
    public void releaseFocus() {
        if (trackedObject != null) {
            trackedObject = null;
            mainSimulation.setFocus(null);
            //mainSimulation.reCalculateOrbits();
        }
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
        for (DisplayObject object : displayObjects) {
            if (object instanceof SpaceNatural) {
                ((SpaceNatural)object).updateCanRenderByScale(camera);
            }
        }
    }
    
    @Override
    protected void beforePaint() {
        checkKeys();
    }
    @Override
    protected void prePaint() {
        msCounter = System.currentTimeMillis();
    }
    @Override
    protected void onPaint(Graphics g) {
        g.setColor(org.newdawn.slick.Color.yellow);
        /*
        g.drawLine(0, 0, 0, 1080);
        g.drawLine(0, 0, 1920, 0);
        g.drawLine(1920, 0, 1920, 1080);
        g.drawLine(0, 1080, 1920, 1080);*/
        
        g.drawString(mainSimulation.getDate().toGMTString(), 10, 20);
        g.drawString(secondsToText(worlds[0].getSimulations()[0].getSpeed()), 10, 40);
        g.drawString("Current Scale: " + camera.getScale(), 10, 60);
        g.drawString("FPS: " + fpsCounter, 10, 80);
        
        
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
        
        
        for (DisplayObject displayObject : displayObjects) {
            if (displayObject == trackedObject) {;// || trackedObject != null) {
                focusCamera();
            }
            displayObject.renderNoTransform(g, camera);
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
