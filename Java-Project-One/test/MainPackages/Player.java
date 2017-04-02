/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MainPackages;

import SkillPackages.FishingSkill;

/**
 *
 * @author Lin-Li
 */
public class Player extends Movable {
    private float xspeed;
    private float yspeed;
    private FishingSkill fishingSkill = new FishingSkill();
    
    
    public Player() {
        this.fishingSkill = fishingSkill;
    }
    
    
}
