// Player class represent the player and is able to improve and lose health
public class Player extends Entity {
    private int healthLevel;
    private Location position;
    public Player(String inputName, String inputDescription) {
        super(inputName, inputDescription);
        this.healthLevel = 3;
        this.position=null;
    }

    public int getHealthLevel() {
        return healthLevel;
    }

    public Location getPosition() {
        return position;
    }
    public void setPosition(Location currentLocation){
        position = currentLocation;
    }

    public void loseHealth(){
        healthLevel--;
    }

    public void improveHealth(){
        healthLevel++;
    }

    public void resetHealth()
    {
        healthLevel = 3;
    }
}
