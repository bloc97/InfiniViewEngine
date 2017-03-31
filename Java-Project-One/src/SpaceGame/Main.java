/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SpaceGame;

import Physics2D.Integrators.Integrator.IntegratorType;
import Physics2D.NBodyFutureOrbit;
import Physics2D.NBodyFuturePath;
import Physics2D.Objects.Planet;
import Physics2D.NBodySimulation;
import Physics2D.Vector2;
import World2D.Scene;
import World2D.Viewport;
import java.util.Date;
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
