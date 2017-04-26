/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SpaceGame.Scenes;

import Physics2D.Fields.WaveSimulation;
import World2D.SceneSwing;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.Arrays;

/**
 *
 * @author bowen
 */
public class FieldTest extends SceneSwing {
    
    private WaveSimulation simulation;
    
    public FieldTest() {
        this(60, 0, 0);
    }
    private FieldTest(int desiredUPS, int xsize, int ysize) {
        super(desiredUPS, xsize, ysize);
        setBackground(Color.black);
        simulation = new WaveSimulation(60, 400, 400, 8);
        simulation.start();
        addListeners();
    }
    
    private void addListeners() {
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                FieldTest thisMenu = ((FieldTest)e.getComponent());
                thisMenu.simulation.click(e.getX(), e.getY());
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                //System.out.println("he");
                FieldTest thisMenu = ((FieldTest)e.getComponent());
                thisMenu.simulation.click(e.getX(), e.getY());
            }

        });
    }
    
    @Override
    protected void beforePaint() {
        if (xsize == 0 || ysize==0) {
            return;
        }
        //int newPixelSize = (int)Math.min(Math.floor(xsize/simulation.getWidth()), Math.floor(ysize/simulation.getHeight()));
        double newPixelSize = Math.min(1d*xsize/simulation.getWidth(), 1d*ysize/simulation.getHeight());
        if (newPixelSize > 0) {
            simulation.setPixelSize(newPixelSize);
        }
    }

    @Override
    protected void prePaint() {
    }
    
    
    @Override
    protected void onPaint(Graphics2D g2) {
        simulation.paint(g2, this);
    }

    @Override
    protected void postPaint() {
    }

    @Override
    protected void afterPaint() {
    }

    
}
