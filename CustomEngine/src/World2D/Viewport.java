/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package World2D;

import SpaceGame.MainView;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;

/**
 *
 * @author bowen
 */
public class Viewport extends JFrame {
    private Scene scene;
    
    private boolean fullScreen = false;
    private int lastX, lastY, lastW, lastH;
    
    /*
    public Viewport(int xsize, int ysize, World world) {
        this(xsize, ysize, new Scene(xsize, ysize, world));
    }*/
    public Viewport(int xsize, int ysize) {
        this.setTitle("Space!");
        this.setSize(xsize, ysize);
        
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        
    }
    public Scene getScene() {
        return scene;
    }
    public void setScene(Scene scene) {
        if (this.scene != null) {
            this.scene.deactivate();
            this.remove(this.scene);
        }
        this.scene = scene;
        this.add(scene);
        this.scene.setViewport(this);
        this.scene.setFocusable(true);
        this.scene.activate();
    }
    
    public void toggleFullScreen() {
        if (fullScreen) {
            dispose();
            setUndecorated(false);
            setVisible(true);
            setBounds(lastX, lastY, lastW, lastH);
            
            fullScreen = false;
        } else {
            lastX = getX();
            lastY = getY();
            lastW = getWidth();
            lastH = getHeight();
            dispose();
            setUndecorated(true);
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            setVisible(true);
            fullScreen = true;
        }
    }
}
