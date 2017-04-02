/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SpaceGame;

import World2D.Scene;
import World2D.Viewport;
/**
 *
 * @author Lin-Li
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        Scene scene = new MainView(1920, 1080);
        Viewport viewport = new Viewport(1920, 1080, scene);
        
        
        scene.start();
        
        
    }
    
    
}
