
/**
 * Abstract class partyMember - write a description of the class here
 * 
 * @author (your name here)
 * @version (version number or date here)
 */
public abstract class partyMember
{
    String name;
    
    public partyMember(String name)
    {
        this.name=name;
    }
    
    public abstract void gainXP(int xp);
    
    public abstract void levelUp();
    
    public abstract void equipWeapon(Weapon w);
    
    public abstract void unequipWeapon();
       
}
