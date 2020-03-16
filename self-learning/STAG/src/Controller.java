import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

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
            return gameText.getText("get",command[2],isGetEntity(command));
        }
        else if (command[1].equals("look")){
            return gameText.getText("look");
        }
        else if (command[1].equals("inventory") || command[1].equals("inv")){
            return gameText.getText("inv");
        }
        else if (command[1].equals("drop")){
            return gameText.getText("drop",command[2],isDropEntity(command));
        }
        else if (command[1].equals("goto")){
            return gameText.getText("goto",command[2],isMoveNewLocation(command));
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
        //return "I can't understand what you mean";
    }

    private boolean isGetEntity(String[] command){
        for (Entity entity : gameWorld.getTotalEntities().keySet()){
            if (entity.getName().equals(command[2]) &&
                gameWorld.getTotalEntities().get(entity).equals(currentLocation.getName()) &&
                entity.getIsMovable()) {
                gameWorld.getTotalEntities().remove(entity);
                String[] player = command[0].split(":");
                gameWorld.getTotalEntities().put(entity,player[0]); //需不需要强制转换
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

    //增加物品和道路，归属
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
            if (action.getSubjects()!=null){
                interSubject.retainAll(action.getSubjects());
            }
            // Set consumed
            HashSet<String> interConsumed =new HashSet<>();
            interConsumed.addAll(entitiesOnPlayer);
            if (action.getConsumed() != null){
                interConsumed.retainAll(action.getConsumed());
            }

            // Trigger and subject correct and get the subject in right location
            if (action.getTriggers().contains(trigger) &&
                    action.getSubjects().contains(subject) &&
                    interSubject.size() > 0 &&
                    interConsumed.size() > 0){
                /* remove entity */
                Entity consumeEntity = null;
                for (Entity entity : gameWorld.getTotalEntities().keySet()){
                    if (action.getConsumed().contains(entity.getName()) &&
                            gameWorld.getTotalEntities().get(entity).equals(currentPlayer.getName())){ //换到背包？
                        consumeEntity = entity;
                    }
                }
                gameWorld.getTotalEntities().remove(consumeEntity); //delete or hashmap变consumed
                /*
                for (Iterator<Entity> iter = gameWorld.getTotalEntities().keySet().iterator();
                     iter.hasNext();){
                    Entity entity = iter.next();
                    if (action.getConsumed().contains(entity.getName()) &&
                            gameWorld.getTotalEntities().get(entity).equals(currentPlayer.getName())){
                        gameWorld.getTotalEntities().remove(entity);
                        break;
                    }
                }*/
                // add path or entity
                Iterator<String> produceEntity = action.getProduced().iterator();
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
                        gameWorld.getTotalEntities().put(new Artefact(newEntity,""),currentPlayer.getName());
                    }
                }
                return action;
            }
        }
        return null;
    }
}