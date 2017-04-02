/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Physics2D.Objects;

import Physics2D.Vector2;
import World2D.Objects.PlanetDisplay;
import World2D.Objects.DisplayObject;
import java.awt.Color;

/**
 *
 * @author bowen
 */
public class SpaceObject extends PointBody {
    
    public PlanetDisplay displayComponent;
    
    final private static Color color = Color.getHSBColor(16F/360, 33F/100, 100F/100);
    
    private double orbitalPeriod;
    
    public SpaceObject(String name, double mass) {
        super(mass);
        displayComponent = new PlanetDisplay(name, color, Math.log10(mass)+10);
        update();
    }

    public SpaceObject(String name, Vector2 position, double mass) {
        super(position, mass);
        displayComponent = new PlanetDisplay(name, color, Math.log10(mass)+10);
        update();
    }

    public SpaceObject(String name, Vector2 position, Vector2 velocity, double mass) {
        super(position, velocity, mass);
        displayComponent = new PlanetDisplay(name, color, Math.log10(mass)+10);
        update();
    }
    @Override
    public final void update() {
        displayComponent.setPos(position().get(0), position().get(1));
        displayComponent.setVel(velocity().get(0), velocity().get(1));
    }
    public void setColour(Color color) {
        displayComponent.setColor(color);
    }
    @Override
    public DisplayObject getDisplayObject() {
        return displayComponent;
    }
    @Override
    public SpaceObject clone() {
        SpaceObject newTestObject = new SpaceObject("Clone", this.position(), this.velocity(), this.mass());
        return newTestObject;
    }
}
