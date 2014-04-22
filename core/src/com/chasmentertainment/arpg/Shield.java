package com.chasmentertainment.arpg;
/**
 * Write a description of class Shield here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Shield
{
    String name;
    int armor;
    
    public Shield(String name, int armor)
    {
       this.name=name;
       this.armor=armor;
    }

    public String toString()
    {
        String s = name;
        s+= " Armor: ";
        return s;
    }
}
