import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

/* Control player and location and handle incoming commands */
public class Controller {
    private ParseGame gameWorld;
    private Location currentLocation;
    private Player currentPlayer;
    private ArrayList<Player> totalPlayer;

    public Controller(ParseGame parseGame) {
        this.gameWorld = parseGame;
        this.currentLocation = gameWorld.getTotalLocation().get(0);
        this.currentPlayer = null;
        this.totalPlayer = new ArrayList<>();
    }

    public ParseGame getGameWorld() {
        return gameWorld;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
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

    public void setCurrentPlayer(Player newPlayer)
    {
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

        if (isStandardCommand(command)) {
            return getStandardText(command);
        }

        Actions action = identifyAction(command);

        if (action != null){
            if (isMeetConditions(action,command)){
                doAction(action);
                return action.getNarration();
            }
            else{
                return "You don't have enough entities in you inventory or are not in the right location.";
            }
        }
        else {
            return "You can't trigger the action, please check [trigger] word again";
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

    private boolean isStandardCommand(String[] command)
    {
        if (gameWorld.getStandardCommands().contains(command[1])){
            return true;
        }
        else {
            return false;
        }
    }

    private String getStandardText(String[] command)
    {
        Text gameText = new Text(this);
        String action = command[1];
        // Look, inventory, inv, health and death
        if (action.equals("look") || action.equals("inventory")
                ||action.equals("inv")|| action.equals("health")){
            return gameText.getText(action);
        }
        else if (action.equals("death")){
            deathAction();
            return gameText.getText("death");
        }
        // Get X, drop X and goto X
        if (command.length>2){
            String entity = command [2];
            if (action.equals("get")){
                return gameText.getText("get",entity,isGetEntity(command));
            }
            else if (action.equals("drop")){
                return gameText.getText("drop",entity,isDropEntity(command));
            }
            else if (action.equals("goto")){
                return gameText.getText("goto",entity,isMoveNewLocation(command));
            }
        }
        return "Game standard command needs more input. e.g. get key/ drop key/ goto forest.";
    }

    private HashSet<String> getEntities (String samePlace)
    {
        HashSet<String> entitiesInSamePlace = new HashSet<>();
        for (Entity entity : gameWorld.getTotalEntities().keySet()){
            if (gameWorld.getTotalEntities().get(entity).equals(samePlace)){
                entitiesInSamePlace.add(entity.getName());
            }
        }
        return entitiesInSamePlace;
    }

    private Actions identifyAction(String[] command) {
        for (int i = 1;i < command.length;i++){
            for (Actions action : gameWorld.getTotalActions()){
                if (action.getTriggers().contains(command[i])){
                    return action;
                }
            }
        }
        return null;
    }

    private boolean isMeetConditions(Actions action,String[] command){
        boolean subjectFlag = false;
        for (int i = 1;i < command.length;i++){
            if (action.getSubjects().contains(command[i])){
                subjectFlag = true;
            }
        }
        // All entities in this location
        HashSet<String> entitiesInRoom = getEntities(currentLocation.getName());
        // All entities on player
        HashSet<String> entitiesOnPlayer = getEntities(currentPlayer.getName());
        HashSet<String> interSubject =new HashSet<>();
        interSubject.addAll(entitiesInRoom);
        interSubject.addAll(entitiesOnPlayer);
        if (action.getSubjects() != null){
            interSubject.retainAll(action.getSubjects());
        }
        if (subjectFlag && interSubject.size() == action.getSubjects().size()){
            return true;
        }
        return  false;
    }

    private void doAction(Actions action)
    {
        doConsumedAction(action.getConsumed());
        doProducedAction(action.getProduced());
    }

    private HashSet<String> hasHealthInConsumed(HashSet<String> setConsumed)
    {
        /* If health in consumed, the player health level will decrease 1 */
        currentPlayer.loseHealth();
        setConsumed.remove("health");
        return setConsumed;
    }

    private HashSet<String> hasHealthInProduced(HashSet<String> setProduced)
    {
        /* If health in produced, the player health level will increase 1 */
        currentPlayer.improveHealth();
        setProduced.remove("health");
        return setProduced;
    }

    private void doConsumedAction(HashSet<String> setConsumed)
    {
        HashSet<String> actionConsumed = new HashSet<>();
        actionConsumed.addAll(setConsumed);
        if (actionConsumed.contains("health")){
            actionConsumed = hasHealthInConsumed(actionConsumed);
        }
        Iterator<String> consumeEntity = actionConsumed.iterator();
        while (consumeEntity.hasNext()){
            String useEntity = consumeEntity.next();
            for (Entity entity : gameWorld.getTotalEntities().keySet()) {
                if (useEntity.equals(entity.getName())) {
                    gameWorld.getTotalEntities().put(entity,"consumed");
                }
            }
        }
    }

    private void doProducedAction(HashSet<String> setProduced)
    {
        HashSet<String> actionProduced = new HashSet<>();
        actionProduced.addAll(setProduced);
        if (actionProduced.contains("health")){
            actionProduced = hasHealthInProduced(actionProduced);
        }
        Iterator<String> produceEntity = actionProduced.iterator();
        while (produceEntity.hasNext()) {
            String newEntity = produceEntity.next();
            if (produceLocation(newEntity)) {
                continue;
            }
            else {
                produceEntity(newEntity);
            }
        }
    }

    private boolean produceLocation(String newlocation)
    {
        for (Location location : gameWorld.getTotalLocation()){
            if (location.getName().equals(newlocation)){
                gameWorld.getGameMap().put(new String(currentLocation.getName()),location.getName());
                return true;
            }
        }
        return false;
    }

    private void produceEntity(String newEntity)
    {
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
