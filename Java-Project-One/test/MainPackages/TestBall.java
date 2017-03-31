package MainPackages;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JComponent;

/**
 *
 * @author Lin-Li
 */
public class TestBall extends JComponent {
    
    int x;
    int y;
    Color color;
    
    public TestBall() {
        this.setSize(100, 100);
        color = Color.white;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(color);
        g.fillOval(0, 0, 20, 20);
        this.setLocation(x, y);
    }
    public void setColor(Color color) {
        this.color = color;
    }
    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public void setPos(double x, double y) {
        this.x = (int)(x+0.5);
        this.y = (int)(y+0.5);
    }
    
}
