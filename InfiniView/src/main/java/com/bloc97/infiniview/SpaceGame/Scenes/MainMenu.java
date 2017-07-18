/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bloc97.infiniview.SpaceGame.Scenes;

import com.bloc97.infiniview.World2D.Scene;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;

/**
 *
 * @author bowen
 */
public class MainMenu extends Scene {
    
    private int x;
    private int y;
    
    private int offsetX;
    private int offsetY;
    
    public MainMenu() {
        this(60, 0, 0);
    }
    private MainMenu(int desiredUPS, int xsize, int ysize) {
        super(desiredUPS, xsize, ysize);
        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                MainMenu thisMenu = ((MainMenu)e.getComponent());
                thisMenu.x = e.getX() - thisMenu.offsetX;
                thisMenu.y = e.getY() - thisMenu.offsetY;
            }

            @Override
            public void mouseMoved(MouseEvent e) {
            }
        });
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                MainMenu thisMenu = ((MainMenu)e.getComponent());
                thisMenu.offsetX = e.getX() - thisMenu.x;
                thisMenu.offsetY = e.getY() - thisMenu.y;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
    }
    
    @Override
    protected void beforePaint() {
    }

    @Override
    protected void prePaint() {
    }

    @Override
    protected void onPaint(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        g2.draw3DRect(x-40, y-40, 80, 80, true);
        g2.draw3DRect(x+100-40, y+20-40, 80, 80, true);
    }

    @Override
    protected void postPaint() {
    }

    @Override
    protected void afterPaint() {
    }
    
}
