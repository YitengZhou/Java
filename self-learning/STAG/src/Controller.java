import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class Controller {
    private ParseGame gameWorld;
    private Location currentLocation;
    private Player currentPlayer;
    private ArrayList<Player> totalPlayer;

    public Controller(ParseGame parseGame){
        this.gameWorld = parseGame;
        this.currentLocation = gameWorld.getTotalLocation().get(0);
        this.currentPlayer = null;
        this.totalPlayer = new ArrayList<>();
    }

    public ParseGame getGameWorld()
    {
        return gameWorld;
    }

    public Location getCurrentLocation()
    {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation){
        if (currentLocation == null) {
            this.currentLocation = gameWorld.getTotalLocation().get(0);
        }
        else {
            this.currentLocation = currentLocation;
        }
    }

    public Player getCurrentPlayer()
    {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player newPlayer) {
        this.currentPlayer = newPlayer;
    }

    public ArrayList<Player> getTotalPlayer()
    {
        return totalPlayer;
    }

    public void addNewPlayer(Player newPlayer)
    {
        totalPlayer.add(newPlayer);
    }

    public String handleIncomingCommand(String[] command){
        Text gameText = new Text(this);

        if (command[1].equals("get")){
            return gameText.getText("get",command[2],isGetEntity(command));
        }
        else if (command[1].equals("look")){
            return gameText.getText("look");
        }
        else if (command[1].equals("inventory") || command[1].equals("inv")){
            return gameText.getText("inv");
        }
        else if (command[1].equals("health")){
            return gameText.getText("health");
        }
        else if (command[1].equals("death")){
            deathAction();
            return gameText.getText("death");
        }
        else if (command[1].equals("drop")){
            return gameText.getText("drop",command[2],isDropEntity(command));
        }
        else if (command[1].equals("goto")){
            return gameText.getText("goto",command[2],isMoveNewLocation(command));
        }
        else if (command.length <= 2){
            return "The input command has no related action, try again.";
        }
        else {
            Actions tempAction = getAction(command[1],command[2]);
            if (tempAction != null){
                return tempAction.getNarration();
            }
            else {
                return "I can't understand what you mean";
            }
        }
    }

    private boolean isGetEntity(String[] command){
        for (Entity entity : gameWorld.getTotalEntities().keySet()){
            if (entity.getName().equals(command[2]) &&
                gameWorld.getTotalEntities().get(entity).equals(currentLocation.getName()) &&
                entity.getIsMovable()) {
                String player = command[0].split(":")[0];
                gameWorld.getTotalEntities().put(entity,player);
                return true;
            }
        }
        return false;
    }

    private boolean isDropEntity(String[] command){
        for (Entity entity : gameWorld.getTotalEntities().keySet()){
            if (gameWorld.getTotalEntities().get(entity).equals(currentPlayer.getName()) &&
                    entity.getName().equals(command[2])){
                gameWorld.getTotalEntities().remove(entity);
                gameWorld.getTotalEntities().put(entity,currentLocation.getName());
                return true;
            }
        }
        return false;
    }

    private boolean isMoveNewLocation (String[] command) {
        for (String source : gameWorld.getGameMap().keySet()){
            if (currentLocation.getName().equals(source) &&
                    gameWorld.getGameMap().get(source).equals(command[2])){
                for (int i = 0;i < gameWorld.getTotalLocation().size();i++){
                    if (gameWorld.getTotalLocation().get(i).getName().equals(command[2])){
                        currentLocation = gameWorld.getTotalLocation().get(i);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private Actions getAction (String trigger, String subject) {
        // All entities in this location
        HashSet<String> entitiesInRoom = new HashSet<>();
        for (Entity entity : gameWorld.getTotalEntities().keySet()){
            if (gameWorld.getTotalEntities().get(entity).equals(currentLocation.getName())){
                entitiesInRoom.add(entity.getName());
            }
        }
        // All entities on player
        HashSet<String> entitiesOnPlayer = new HashSet<>();
        for (Entity entity : gameWorld.getTotalEntities().keySet()){
            if (gameWorld.getTotalEntities().get(entity).equals(currentPlayer.getName())){
                entitiesOnPlayer.add(entity.getName());
            }
        }

        for (Actions action : gameWorld.getTotalActions()){
            // Set Subject
            HashSet<String> interSubject =new HashSet<>();
            interSubject.addAll(entitiesInRoom);
            interSubject.addAll(entitiesOnPlayer);
            if (action.getSubjects() != null){
                interSubject.retainAll(action.getSubjects());
            }

            // Trigger and subject correct and get the subject in right location
            if (action.getTriggers().contains(trigger) &&
                    action.getSubjects().contains(subject) &&
                    interSubject.size() == action.getSubjects().size()
                    ){
                /* remove entity and health */
                if (action.getConsumed().contains("health")){
                    currentPlayer.loseHealth();
                }
                Entity consumeEntity = null;
                for (Entity entity : gameWorld.getTotalEntities().keySet()) {
                    if (action.getConsumed().contains(entity.getName())) {
                        consumeEntity = entity;
                    }
                }
                gameWorld.getTotalEntities().remove(consumeEntity);

                HashSet<String> tempActionProduced = action.getProduced();

                if (tempActionProduced.contains("health")){
                    currentPlayer.improveHealth();
                    tempActionProduced.remove("health");
                }

                Iterator<String> produceEntity = tempActionProduced.iterator();
                while (produceEntity.hasNext()) {
                    boolean locationFlag = false;
                    String newEntity = produceEntity.next();
                    for (Location location : gameWorld.getTotalLocation()){
                        if (location.getName().equals(newEntity)){
                            locationFlag = true;
                            gameWorld.getGameMap().put(new String(currentLocation.getName()),location.getName());
                        }
                    }
                    if (!locationFlag){
                        for (Entity entity : gameWorld.getTotalEntities().keySet()){
                            if (entity.getName().equals(newEntity) && entity.getIsMovable()){
                                gameWorld.getTotalEntities().put(entity,currentPlayer.getName());
                            }
                            else if (entity.getName().equals(newEntity) && !entity.getIsMovable()){
                                gameWorld.getTotalEntities().put(entity,currentLocation.getName());
                            }
                        }
                    }
                }
                return action;
            }
        }
        return null;
    }

    private void deathAction()
    {
        for (Entity entity : gameWorld.getTotalEntities().keySet()){
            if (gameWorld.getTotalEntities().get(entity).equals(currentPlayer.getName())){
                gameWorld.getTotalEntities().put(entity,currentLocation.getName());
            }
        }
        currentPlayer.resetHealth();
        currentLocation = gameWorld.getTotalLocation().get(0);
    }
}
