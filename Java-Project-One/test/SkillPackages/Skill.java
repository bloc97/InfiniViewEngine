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
public abstract class Skill {
    
    private String name;
    private String disc;
    private int level;
    private int requLevelUp;
    private int exp;
    private enum Type {
        GATHER,REFINE
    }
    private Type type;

    public Skill() {
        this.name = name;
        this.disc = disc;
        this.level = 0;
        this.exp = 0;
        this.requLevelUp = 1;
        this.type = type;
        
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getDisc() {
        return disc;
    }

    public int getExp() {
        return exp;
    }

    public int getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }

    public int getRequLevelUp() {
        return requLevelUp;
    }

    public void setDisc(String disc) {
        this.disc = disc;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRequLevelUp(int requLevelUp) {
        this.requLevelUp = requLevelUp;
    }
    
    
    
    public void skillUsed(int expToAdd) {
        
        if (exp+expToAdd >= requLevelUp) {
            level++;
            exp = exp+expToAdd-requLevelUp;
            //
        }
        else {
            exp= exp+expToAdd;
        }
    }
    
    
    
    
    
}
