/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Physics2D.Objects;

import Physics2D.Vector2;

/**
 *
 * @author bowen
 */
public interface LinearMotion {
    Vector2 position(); //m
    Vector2 velocity(); //m/s
    //double acceleration(int i); //m/s^2
    //double force(int i); //N
    
    double momentum(int i); //kg*m/s
    double mass(); //kg
    
    void setPosition(Vector2 position);
    void setVelocity(Vector2 velocity);
    
    void addPosition(Vector2 position);
    void addVelocity(Vector2 velocity);
    //void setForce(Vector2 vector2);
    //void revert();
    void update();
}
