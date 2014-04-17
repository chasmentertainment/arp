package com.chasmentertainment.arpg;
/**
 * Abstract class PartyMember - write a description of the class here
 * 
 * @author (your name here)
 * @version (version number or date here)
 */
public abstract class PartyMember
{
    String name;
    
    public PartyMember(String name)
    {
        this.name=name;
    }
    
    public abstract void gainXP(int xp);
    
    public abstract void levelUp();
    
    public abstract void equipWeapon(Weapon w);
    
    public abstract void unequipWeapon();
       
}
