/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MainPackages;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import javax.swing.JComponent;
import Physics2D.Vector2;

/**
 *
 * @author Lin-Li
 */
public class TestLine extends JComponent {
    private Vector2 vec;
    private Color colour;
    private int thickness;
    public TestLine(Vector2 vec, Color colour, int thickness) {
        this.vec = vec;
        this.colour = colour;
        this.thickness = thickness;
        this.setSize(10000, 10000);
        
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g.setColor(this.colour);
        g2.setStroke(new BasicStroke(this.thickness));
        g2.drawLine(500, 500, 500+(int)vec.get(0), 500+(int)vec.get(1));
    }
    
}
