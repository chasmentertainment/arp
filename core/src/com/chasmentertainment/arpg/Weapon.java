package com.chasmentertainment.arpg;
/**
 * Write a description of class Weapon here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Weapon
{
    // instance variables - replace the example below with your own
    String name;
    int attackValue;
    int spellPower;
    boolean isMagic;
    boolean isElemental;
    boolean hasAbility;
    int ability;
    int element;

    //abilities
    //1.
    //2.
    //3.
    //4.
    
    //physical
    public Weapon(String name, int attackValue, int element, int ability, boolean isElemental)
    {
        this.name = name;      
        this.element = element;
        this.isElemental=isElemental;
        this.attackValue=attackValue;
        this.ability=ability;
    }
    
    //magical
    public Weapon(String name, int attackValue, int spellPower, int ability)
    {
        this.name=name;
        this.attackValue=attackValue;
        this.ability=ability;
        this.isMagic=true;
    }
    
    public String toString()
    {
        String n = name;
        if(isMagic==true)
        {
            n+=" Attack:" + attackValue + " Spell Power:" + spellPower;
        }
        else
        {
            n+=" Attack:" + attackValue;
        }
        if(isElemental==true)
        {
            n+= " Element:" + element;
        }
        if(hasAbility==true)
        {
            n+= " Ability:" + ability;
        }
        return n;
    }
}
