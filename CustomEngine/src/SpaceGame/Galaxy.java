/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SpaceGame;

import Physics2D.Vector2;

/**
 *
 * @author bowen
 */
public class Galaxy {
    private String name;
    private Vector2 offset;
    public Galaxy(String name, Vector2 offset) {
        this.name = name;
        this.offset = offset;
    }
    public String name() {
        return name;
    }
    public Vector2 offset() {
        return offset;
    }
}
