/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SpaceGame.Objects;

import Physics2D.Vector2;


/**
 *
 * @author bowen
 */
public class SpaceArtificial extends SpaceNatural {
    
    public SpaceArtificial(String name, Vector2 position, Vector2 velocity, double mass, double angPos, double angVel, double radius) {
        super(name, position, velocity, mass, angPos, angVel, radius);
    }
    
}
