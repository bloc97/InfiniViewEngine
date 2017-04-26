/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Physics2D.Fields;

/**
 *
 * @author bowen
 */
public class WaveEmitter {
    private int x, y, width, height;
    
    private int tick;
    
    private double amplitude;
    private double frequency;
    
    public WaveEmitter(int x, int y, int width, int height, double amplitude, int periodInTicks) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.amplitude = amplitude;
        this.frequency = (1d/periodInTicks) * 2 * Math.PI;
        this.tick = 0;
    }
    
    private double getValue() {
        return amplitude * Math.sin(frequency * tick++);
    }
    public void apply(double[][] addToState) {
        double value = getValue();
        System.out.println(value);
        WaveSimulation.setRect(addToState, x, y, width, height, value);
    }
    
}
