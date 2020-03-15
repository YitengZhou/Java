public class Controller {
    private ParseGame gameWorld;
    private Location currentLocation;
    private Player currentPlayer;
    public Controller(ParseGame parseGame){
        this.gameWorld = parseGame;
        this.currentLocation = gameWorld.getTotalLocation().get(0);
        this.currentPlayer = null;
    }

    public ParseGame getGameWorld() {
        return gameWorld;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player newPlayer) {
        this.currentPlayer = newPlayer;
    }

    public String handleIncomingCommand(String[] command){
        Text gameText = new Text(this);

        if (command[1].equals("get")){
            return gameText.getText("get",getEntity(command));
        }
        else if (command[1].equals("look")){
            return gameText.getText("look");
        }

        return "I can't understand what you mean";
    }

    private String getEntity(String[] command){
        for (Entity entity : gameWorld.getTotalEntities().keySet()){
            if (entity.getName().equals(command[2]) &&
                    gameWorld.getTotalEntities().get(entity).equals(currentLocation.getName())) {
                gameWorld.getTotalEntities().remove(entity);
                String[] player = command[0].split(":");
                gameWorld.getTotalEntities().put(entity,player[0]);
                return entity.getName();
            }
        }
        return null;
    }

    // void -> controller
    private void moveLocation (Location newLocation) {
        currentLocation = newLocation;
    }
}
