
/**
 * Write a description of class Warrior here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Warrior extends partyMember
{
    // instance variables - replace the example below with your own
    String name;
    int maxHP=30;
    int currentHP=30;
    int maxMana=20;
    int currentMana=20;
    int level=1;
    int xp=0;
    int xpToNextLevel=30;
    
    
    int strength=12;
    int defense=15;
    int agility=7;
    int intelligence=2;
    int luck=5;
    
    int attack = (int)(strength*.75);
    int armor = 0;
    int critChance = (int)(luck*.1);
    int spellPower = (int)(intelligence*.75);
    
    boolean evolved;
    
    Weapon w;
    Shield s;
    public Warrior(String name)
    {
        super(name);
    }
    
    public void equipWeapon(Weapon w1)
    {
         w = w1;
         this.attack+=w1.attackValue;
    }
    
    public void unequipWeapon()
    {
        w=null;
        this.attack=(int)(strength*.75);
    }
    
    public void equipShield(Shield s1)
    {
        s=s1;
    }
    
    public void equipHelment()
    {
    }
    

    
        
    public void gainXP(int xp)//needs work
    {
        boolean lvlUpAgain = true;
        int lvlValue = (200 + (level*50));
        xpToNextLevel += xp;
        System.out.println(name + " has gained " + xp + " xp!");
        while(lvlUpAgain == true)
        {
            lvlValue = (200 + (level*50));
            if(xpToNextLevel >= (lvlValue))
            {
                xpToNextLevel = xpToNextLevel -(lvlValue);
                levelUp();
            }
            else
                lvlUpAgain = false;
            }
    }
    
    public void levelUp() //needs work
    {
        level += 1;
        if(level<10)
        {
        }
        else if(level>=10 && level<20)
        {
        }
        else if(level>=10 && level<20)
        {
        }
        else if(level==20)
        {
            evolve();
        }
    }
    
    public void evolve()
    {
    }
    
    public String toString()
    {
        String s = name + " the Warrior \n";
        s+= "Level: " + level + "\n";
        s+= "HP: " +currentHP + "/" + maxHP + "\n";
        s+= "MP: " +currentMana + "/" + maxMana + "\n";
        s+= "XP: " +xp+ "/" + xpToNextLevel + "\n"  + "\n";;
        s+= "Strength: " + strength + "\n";
        s+= "Defense: " + defense + "\n";
        s+= "Agility: " + agility  + "\n";
        s+= "Intelligence: " + intelligence  + "\n";
        s+= "Luck: " + luck  + "\n \n";
        s+= "Attack: " + attack + "\n";
        s+= "Armor: " + armor + "\n \n";
        if(w!=null)
            s+= "Main Hand: " + w.name + " ATK: " + w.attackValue + "\n";
        else
            s+= "Main Hand: ";
               
        return s;        
    }
}
