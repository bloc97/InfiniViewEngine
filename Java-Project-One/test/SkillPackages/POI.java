/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SkillPackages;

/**
 *
 * @author Lin-Li
 */
public class POI {
    private String nom;
    private enum Type {
        GATHER,REFINE
    }
    private Type type;
    private String requiredSkillName;
    private int requiredSkillLevel;
    private int durability;

    public POI() {
        this.nom = nom;
        this.type = type;
        this.requiredSkillName = requiredSkillName;
        this.requiredSkillLevel = requiredSkillLevel;
        this.durability = durability;
        
    }
    
    
    
}
