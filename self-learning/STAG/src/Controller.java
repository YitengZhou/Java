public class Controller {
    private ParseGame parseGame;
    private Location currentLocation;
    public Controller(ParseGame parseGame){
        this.parseGame =parseGame;
        this.currentLocation = getParseGame().getTotalLocation().get(0);
    }

    public ParseGame getParseGame() {
        return parseGame;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public moveLocation (Location newLocation) {
        currentLocation = newLocation;
    }

}
