/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SpaceGame;

import Physics2D.Vector2;
import java.math.BigDecimal;

/**
 *
 * @author bowen
 */
public class Galaxy {
    private String name;
    private BigDecimal offsetX;
    private BigDecimal offsetY;
    private Vector2 offset;
    public Galaxy(String name, Vector2 offset) {
        this.name = name;
        this.offsetX = new BigDecimal(offset.get(0));
        this.offsetY = new BigDecimal(offset.get(1));
    }
    public String name() {
        return name;
    }
    public BigDecimal offsetX() {
        return offsetX;
    }
    public BigDecimal offsetY() {
        return offsetY;
    }
}
