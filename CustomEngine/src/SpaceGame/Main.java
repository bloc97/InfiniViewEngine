/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SpaceGame;

import SpaceGame.Scenes.GameView;
import SpaceGame.Scenes.MainMenu;
import World2D.Scene;
import World2D.Viewport;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
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
        
        scene.start();
        
    }
    
    
}
