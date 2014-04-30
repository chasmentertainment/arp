package com.chasmentertainment.arpg;

public abstract class PartyMember
{
    String name;
    
    public PartyMember(String name) {
        this.name=name;
    }
    
    public abstract void gainXP(int xp);
    
    public abstract void levelUp();
    
    public abstract void equipWeapon(Weapon w);
    
    public abstract void unequipWeapon();
       
}
