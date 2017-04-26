/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Physics2D.Fields;

import Physics.SimulationThread;
import World2D.Objects.DisplayObject;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;

/**
 *
 * @author bowen
 */
public class WaveSimulation extends SimulationThread {
    
    
    //private double[][] prevState2;
    private double[][] prevState;
    private double[][] state;
    
    private int gridWidth;
    private int gridHeight;
    private double pixelPerGrid = 4;
    
    private static final int setMaxValue = 200;
    private static final int maxDisplayValue = 100;
    private static final double powAttenuation = 1;
    
    private BufferedImage image;
    
    
    private double[][] addToState;
    private boolean[][] obstacles;
    
    private LinkedList<WaveEmitter> emitters;
    
    public WaveSimulation(int desiredUPS, int xsize, int ysize, int pixelPerGrid) {
        super(1, desiredUPS, 1, new Date());
        
        gridWidth = xsize;
        gridHeight = ysize;
        this.pixelPerGrid = pixelPerGrid;
        
        image = new BufferedImage(gridWidth, gridHeight, BufferedImage.TYPE_INT_ARGB);
        
        //prevState2 = new double[gridSize][gridSize];
        prevState = new double[gridWidth][gridHeight];
        state = new double[gridWidth][gridHeight];
        addToState = new double[gridWidth][gridHeight];
        obstacles = new boolean[gridWidth][gridHeight];
        
        //setCircle(prevState2, gridSize/2, gridSize/2, 5, 100);
        //setCircle(addToState, gridSize/2, gridSize/2, 5, setMaxValue);
        //setCircle(state, gridSize/2, gridSize/2, 5, setMaxValue);
        
        setRect(obstacles, 0, gridHeight/16, gridWidth/2-12, 1, true);
        setRect(obstacles, gridWidth/2-4, gridHeight/16, 8, 1, true);
        setRect(obstacles, gridWidth/2+12, gridHeight/16, gridWidth/2-2, 1, true);
        
        emitters = new LinkedList<>();
        emitters.add(new WaveEmitter(0, 1, gridWidth, 1, 200, 32));
        
        //setCircle(state, gridSize/2, gridSize/4, 2, 100);
        //setCircle(state, gridSize/4, gridSize/2, 2, 200);
        
    }
    
    
    public void click(int x, int y) {
        setCircle(addToState, (int)(x/pixelPerGrid), (int)(y/pixelPerGrid), 3, setMaxValue);
    }
    
    private static double getFromState(double[][] thisState, int x, int y) {
        if (x < 0 || x >= thisState.length || y < 0 || y >= thisState[0].length) {
            return 0;
        } else {
            return thisState[x][y];
        }
    }
    
    private static double getFromState(double[][] thisState, double[][] prevState, int x, int y) {
        boolean isBorder = false;
        if (x < 0) {
            x = 0;
            isBorder = true;
        }
        if (x >= thisState.length) {
            x = thisState.length - 1;
            isBorder = true;
        }
        if (y < 0) {
            y = 0;
            isBorder = true;
        }
        if (y >= thisState[0].length) {
            y = thisState[0].length - 1;
            isBorder = true;
        }
        if (isBorder) {
            return prevState[x][y];
        }
        return thisState[x][y];
    }
    
    public static void setState(double[][] thisState, int x, int y, double d) {
        if (x < 0 || x >= thisState.length || y < 0 || y >= thisState[0].length) {
        } else {
            thisState[x][y] = d;
        }
    }
    public static void setState(boolean[][] thisState, int x, int y, boolean d) {
        if (x < 0 || x >= thisState.length || y < 0 || y >= thisState[0].length) {
        } else {
            thisState[x][y] = d;
        }
    }
    
    public static void setCircle(double[][] thisState, int x0, int y0, int r, double d) {
        for (int x=x0-r; x<=x0+r; x++) {
            for (int y=y0-r; y<=y0+r; y++) {
                int dx = x0 - x; // horizontal offset
                int dy = y0 - y; // vertical offset
                if ( (dx*dx + dy*dy) <= (r*r) ) {
                    setState(thisState, x, y, d);
                }
            }
        }
    }
    
    public static void setRect(double[][] thisState, int x0, int y0, int w, int h, double d) {
        for (int x=x0; x<=x0+w; x++) {
            for (int y=y0; y<=y0+h; y++) {
                setState(thisState, x, y, d);
            }
        }
    }
    public static void setRect(boolean[][] thisState, int x0, int y0, int w, int h, boolean d) {
        for (int x=x0; x<=x0+w; x++) {
            for (int y=y0; y<=y0+h; y++) {
                setState(thisState, x, y, d);
            }
        }
    }
    
    private int getRGB(int x, int y) {
        double value = (state[x][y]/maxDisplayValue);
        
        if (obstacles[x][y]) {
            return Color.gray.getRGB();
        }
        
        if (value > 0) {
            if (value >= 1) {
                return (255<<16)|(255<<24);
            } else {
                value = (float)Math.pow(value, powAttenuation);
            }
            int ivalue = (int)(value*256);
            return ((ivalue&0x0ff)<<16)|(255<<24);
        } else {
            value = -value;
            if (value >= 1) {
                return (255)|(255<<24);
            } else {
                value = (float)Math.pow(value, powAttenuation);
            }
            int ivalue = (int)(value*256);
            return (ivalue&0x0ff)|(255<<24);
        }
    }
    
    private int getRGB(int r, int g, int b) {
        return ((r&0x0ff)<<16)|((g&0x0ff)<<8)|(b&0x0ff)|(255<<24);
    }
    
    private int getARGB(int r, int g, int b, int a) {
        return ((r&0x0ff)<<16)|((g&0x0ff)<<8)|((b&0x0ff))|((a&0x0ff)<<24);
    }
    
    private int[] getFromRGB(int rgb) {
        int red = (rgb>>16)&0x0ff;
        int green=(rgb>>8) &0x0ff;
        int blue= (rgb)    &0x0ff;
        
        return new int[]{red, green, blue};
    }
    
    private void drawVirtualPixel(Graphics2D g2, int x, int y, int pixsize) {
        
        if (obstacles[x][y]) {
            g2.setColor(Color.gray);
            g2.fillRect(pixsize*x, pixsize*y, pixsize, pixsize);
            return;
        }
        
        float value = (float)((state[x][y]/maxDisplayValue));
        //if (state[x][y] != 0) System.out.println(value);
        //if (value > 1) value = 1f;
        if (value > 0) {
            if (value > 1) {
                value = 1;
            } else {
                value = (float)Math.pow(value, powAttenuation);
            }
            g2.setColor(Color.getHSBColor(0, 1, value));
        } else {
            value = -value;
            if (value > 1) {
                value = 1;
            } else {
                value = (float)Math.pow(value, powAttenuation);
            }
            g2.setColor(Color.getHSBColor(0.7f, 1, value));
        }
        g2.fillRect(pixsize*x, pixsize*y, pixsize, pixsize);
        //g2.fillOval(pixsize*x, pixsize*y, pixsize, pixsize);
    }
    
    
    private void normalise(double[][] array) {
        double total = 0;
        int elements = array.length * array[0].length;
        
        for (int i=0; i<array.length; i++) {
            for (int j=0; j<array[i].length; j++) {
                total += array[i][j];
            }
        }
        total = total/elements;
        //System.out.println(total);
        
        for (int i=0; i<array.length; i++) {
            for (int j=0; j<array[i].length; j++) {
                array[i][j] -= total;
            }
        }
        
    }
    private static double[][] deepCopy(double[][] original) {
        if (original == null) {
            return null;
        }

        final double[][] result = new double[original.length][];
        for (int i = 0; i < original.length; i++) {
            result[i] = Arrays.copyOf(original[i], original[i].length);
            // For Java versions prior to Java 6 use the next:
            // System.arraycopy(original[i], 0, result[i], 0, original[i].length);
        }
        return result;
    }
    
    private void setAddEmitters() {
        for (WaveEmitter emitter : emitters) {
            emitter.apply(addToState);
        }
    }
    
    private void setAddStates() {
        for (int x=0; x<state.length; x++) {
            for (int y=0; y<state[x].length; y++) {
                if (addToState[x][y] != 0) {
                    state[x][y] = addToState[x][y];
                    prevState[x][y] = addToState[x][y];
                    addToState[x][y] = 0;
                }
            }
        }
    }
    
    private void setObstacles() {
        for (int x=0; x<state.length; x++) {
            for (int y=0; y<state[x].length; y++) {
                if (obstacles[x][y]) {
                    state[x][y] = 0;
                }
            }
        }
    }
    
    @Override
    public void forward() {
        //System.out.println(Arrays.deepToString(addToState));
        setAddEmitters();
        setAddStates();
        setObstacles();
        double[][] newState = new double[gridWidth][gridHeight];
        for (int x=0; x<newState.length; x++) {
            for (int y=0; y<newState[x].length; y++) {
                
                if (x == 0 || y == 0 || x == newState.length-1 || y == newState[0].length-1) {
                    newState[x][y] = (0.25d * (getFromState(state, prevState, x+1, y) + getFromState(state, prevState, x-1, y) + getFromState(state, prevState, x, y+1) + getFromState(state, prevState, x, y-1) - (4 * getFromState(state, prevState, x, y)))) - getFromState(prevState, x, y) + (2 * getFromState(state, prevState, x, y));
                } else {
                    newState[x][y] = (0.25d * (getFromState(state, x+1, y) + getFromState(state, x-1, y) + getFromState(state, x, y+1) + getFromState(state, x, y-1) - (4 * getFromState(state, x, y)))) - getFromState(prevState, x, y) + (2 * getFromState(state, x, y));
                }
                newState[x][y] *= 0.995d;
            }
        }
        //prevState2 = prevState;
        prevState = state;
        state = newState;
        
        //normalise(state);
        //normalise(prevState);
        //normalise(prevState2);
    }

    public void paint(Graphics2D g2, Component comp) {
        //double[][] drawState = state;
        //System.out.println(Color.getHSBColor(0, 1, 1f).getRGB());
        
        
        for (int x=0; x<gridWidth; x++) {
            for (int y=0; y<gridHeight; y++) {
                image.setRGB(x, y, getRGB(x, y));
            }
        }
        
        g2.drawImage(image.getScaledInstance((int)(gridWidth*pixelPerGrid), (int)(gridHeight*pixelPerGrid), BufferedImage.SCALE_FAST), 0, 0, comp);
        
        g2.setColor(Color.yellow);
        if (isOverloaded()) {
            g2.setColor(Color.yellow);
            g2.drawString("Simulation Overloaded", 20, 20);
        }
        g2.drawRect(0, 0, (int)(gridWidth*pixelPerGrid), (int)(gridHeight*pixelPerGrid));
        /*
        for (int x=0; x<drawState.length; x++) {
            for (int y=0; y<drawState[x].length; y++) {
                drawVirtualPixel(g2, x, y, pixelPerGrid);
            }
        }*/
    }
    
    public void setPixelSize(double d) {
        pixelPerGrid = d;
    }
    
    public int getWidth() {
        return gridWidth;
    }
    public int getHeight() {
        return gridHeight;
    }
    
    @Override
    public void updateInterpolationSimulationTime(double realLagRatio) {
        
    }

    @Override
    public long getTicks() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DisplayObject[] getDisplayObjects() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getObjectsNumber() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }



    
}
