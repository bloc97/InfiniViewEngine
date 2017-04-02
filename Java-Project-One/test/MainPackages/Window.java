package MainPackages;

import Physics2D.Vector2;
import Physics2D.Vectors2;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;

/**
 *
 * @author Lin-Li
 */
public class Window extends JFrame {

    World world = new World();

    public Window() {

        this.add(world);
        this.setTitle("ProjectOne");
        this.setSize(1920, 1080);
        
        //this.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        //this.setUndecorated(true);
        
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        keyEventAdd();
        //new Thread(world).start();
    }

    public void keyEventAdd() {
        
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                    //System.out.println("Triggered");
                 switch(e.getKeyCode()) {
                    case KeyEvent.VK_W :
                        world.vec1.addNorm(10);
                        break;
                    case KeyEvent.VK_S :
                        world.vec1.addNorm(-10);
                        break;
                    case KeyEvent.VK_A :
                         world.vec1.addRot(-Math.PI/20);
                        break;
                    case KeyEvent.VK_D :
                         world.vec1.addRot(Math.PI/20);
                        break;
                    case KeyEvent.VK_UP :
                        world.vec2.addNorm(10);
                        break;
                    case KeyEvent.VK_DOWN :
                        world.vec2.addNorm(-10);
                        break;
                    case KeyEvent.VK_LEFT :
                         world.vec2.addRot(-Math.PI/20);
                        break;
                    case KeyEvent.VK_RIGHT :
                         world.vec2.addRot(Math.PI/20);
                        break;
                    /*case KeyEvent.VK_E :
                        if (Vector2.useFastMath) {
                            Vector2.useFastMath = false;
                            System.out.println("Now using precise maths");
                        } else {
                            Vector2.useFastMath = true;
                            System.out.println("Now using fast math approximation");
                        }
                        break;*/
                    default :
                        break;
                 }
                world.vec3.set(Vectors2.proj(world.vec1, world.vec2));
                world.vec4.set(Vectors2.rej(world.vec1, world.vec2));
            }
        });
        
    }

}
