/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SpaceGame;

import SpaceGame.Slick.TestSlick;
import SpaceGame.Scenes.GameView;
import SpaceGame.Scenes.GameViewSlick;
import static SpaceGame.Scenes.GameViewSlick.newGameViewSlick;
import World2D.Scene;
import World2D.Viewport;
import java.awt.DisplayMode;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import static java.lang.Thread.sleep;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.CanvasGameContainer;
import org.newdawn.slick.SlickException;
/**
 *
 * @author bowen
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SlickException {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        DisplayMode[] dms = gd.getDisplayModes();
        for (DisplayMode mode : dms) {
            System.out.println(mode.getWidth() + " x " + mode.getHeight() + " " + mode.getBitDepth() + "b @" + mode.getRefreshRate());
        }
        int width = gd.getDisplayMode().getWidth();
        int height = gd.getDisplayMode().getHeight();
        int defaultW = width*9/10;
        int defaultH = height*9/10;
        //System.setProperty("sun.java2d.opengl","True");
        Scene scene = new GameView();
        //Scene scene = newGameViewSlick();
        Viewport viewport = new Viewport(defaultW, defaultH);
        viewport.setScene(scene);
        
        scene.launch();
        /*
        try {
                //AppGameContainer appgc;
                //appgc = new AppGameContainer(new TestSlick("Simple Slick Game"));
                //appgc.setDisplayMode(width, height, true);
                //appgc.setVSync(true);
                //appgc.start();
                
                CanvasGameContainer appcgc = new CanvasGameContainer(new TestSlick("Simple Slick Game"), true);
                Viewport viewport = new Viewport(defaultW, defaultH);
                viewport.add(appcgc);
                appcgc.getContainer().setVSync(true);
                
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    
                }
                
                appcgc.start();
                
        } catch (SlickException ex) {
                Logger.getLogger(TestSlick.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        
    }
    
    
}
