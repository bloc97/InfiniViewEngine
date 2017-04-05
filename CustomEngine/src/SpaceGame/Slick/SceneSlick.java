/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SpaceGame.Slick;

import World2D.Camera;
import World2D.Objects.DisplayObject;
import World2D.Scene;
import World2D.SceneSwing;
import World2D.Viewport;
import World2D.World;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.CanvasGameContainer;
import org.newdawn.slick.Game;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

/**
 *
 * @author bowen
 */

        
public abstract class SceneSlick extends CanvasGameContainer implements Scene {
    
    protected boolean isActive;
    protected int desiredUPS;
    
    protected DisplayObject[] displayObjects = new DisplayObject[0];
    
    final protected Camera camera;
    
    protected int xsize, ysize;
    
    protected World[] worlds;
    protected Viewport viewport;
    
    private Game game;
    
    public static class GameThread implements Game {
        
        String title;
        SceneSlick scene;
        public GameThread(String title) {
            this.title = title;
        }
        
        public void setScene(SceneSlick scene) {
            this.scene = scene;
        }
        
        @Override
        public void init(GameContainer gc) throws SlickException {
            
        }

        @Override
        public void update(GameContainer gc, int i) throws SlickException {
            scene.beforePaint();
            scene.afterPaint();
        }

        @Override
        public void render(GameContainer gc, Graphics g) throws SlickException {
            scene.prePaint();
            scene.onPaint(g);
            scene.postPaint();
        }

        @Override
        public boolean closeRequested() {
            return false;
        }

        @Override
        public String getTitle() {
            return title;
        }
        
    }
    
    
    
    
    public SceneSlick(Game game, int desiredUPS, int xsize, int ysize) throws SlickException {
        super(game);
        this.game = game;
        camera = new Camera(this, xsize, ysize);
        this.isActive = false;
        this.desiredUPS = desiredUPS;
        
        this.xsize = xsize;
        this.ysize = ysize;
        
        this.setVisible(true);
        
        
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Rectangle r = e.getComponent().getBounds();
                int h = r.height;
                int w = r.width;
                SceneSlick thisScene = ((SceneSlick)(e.getComponent()));
                thisScene.xsize = w;
                thisScene.ysize = h;
                Camera thisCamera = thisScene.getCamera();
                thisCamera.setScreenSize(w, h);
            }
        });
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
    
    public Camera getCamera() {
        return camera;
    }
    @Override
    public Component getComponent() {
        return this;
    }
    
    protected abstract void beforePaint();
    protected abstract void prePaint();
    protected abstract void onPaint(Graphics g);
    protected abstract void postPaint();
    protected abstract void afterPaint();



    @Override
    public void launch() {
        try {
            start();
        } catch (SlickException ex) {
            Logger.getLogger(SceneSlick.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public GameContainer getContainer() {
        return super.getContainer();
    }
    
}
