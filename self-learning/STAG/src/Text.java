public class Text {
    private Controller currentgame;
    public Text (Controller current){
        this.currentgame = current;
    }

    public getText(String trigger)
    {
        if (trigger.equals("look")){
            System.out.println("You are in location " + currentgame.getCurrentLocation().getName());
        }
    }
}
