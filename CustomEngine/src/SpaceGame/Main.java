/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SpaceGame;

import SpaceGame.Scenes.GameView;
import World2D.Scene;
import World2D.Viewport;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;
/**
 *
 * @author bowen
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = gd.getDisplayMode().getWidth();
        int height = gd.getDisplayMode().getHeight();
        int defaultW = width*3/4;
        int defaultH = height*3/4;
        //System.setProperty("sun.java2d.opengl","True");
        Scene scene = new GameView();
        
        Viewport viewport = new Viewport(defaultW, defaultH);
        viewport.setScene(scene);
        
        //scene.start();
        
        try {
                AppGameContainer appgc;
                appgc = new AppGameContainer(new TestSlick("Simple Slick Game"));
                appgc.setDisplayMode(640, 480, false);
                appgc.start();
        } catch (SlickException ex) {
                Logger.getLogger(TestSlick.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
}
