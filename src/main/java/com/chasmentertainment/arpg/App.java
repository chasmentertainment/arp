package com.chasmentertainment.arpg;

public class App 
{
    static PartyMember[] p = new PartyMember[4];
    public static void main( String[] args )
    {
        String name = "Daniel";
        PartyMember w = new Warrior(name);
        PartyMember w2 = new Warrior("Marc");
        PartyMember w3 = new Warrior("Patrice");
        PartyMember w4 = new Warrior("Tyler");
        p[0]=w;
        p[1]=w2;
        p[2]=w3;
        p[3]=w4;
        Weapon w1 = new Weapon("The Harbinger of Death", 3000, 0, 0, false);
        w.equipWeapon(w1);
        System.out.println(w);
        w.unequipWeapon();
        System.out.println(w);
        for(int i = 0; i<p.length; i++)
        {
            System.out.println(p[i].name);
        }
    }
}
