import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Text {
    private Controller currentGame;

    public Text (Controller current){
        this.currentGame = current;
    }

    public String getText(String trigger)
    {
        // Look Action
        if (trigger.equals("look")){
            return getLookText();
        }
        else if (trigger.equals("inv")){
            return getInvText();
        }
        else if (trigger.equals("health")){
            return getHealthText();
        }
        else if (trigger.equals("death")){
            return getDeathTest();
        }
        return "You need to said something.";
    }


    public String getText(String trigger, String subject,boolean result)
    {
        if (trigger.equals("get")){
            return getGetText(subject,result);
        }
        else if (trigger.equals("drop")){
            return getDropText(subject,result);
        }
        else if (trigger.equals("goto")){
            return getMoveText(subject,result);
        }
        return "You need to said something.";
    }

    // Look Action
    private String getLookText(){
        String playerOutput = "You are in location:\n\t[" + currentGame.getCurrentLocation().getName() + "]\t(" +
        currentGame.getCurrentLocation().getDescription() + ")\n";
        if (currentGame.getGameWorld().getTotalEntities().containsValue(currentGame.getCurrentLocation().getName())){
            playerOutput = playerOutput.concat("In this room, entity list:\n");
            for (Entity entity : currentGame.getGameWorld().getTotalEntities().keySet()){
                if (currentGame.getGameWorld().getTotalEntities().get(entity)
                        .equals(currentGame.getCurrentLocation().getName()) && entity.getOwner() == null){
                    playerOutput = playerOutput.concat(entity.getClass().getTypeName() + ":\t[");
                    playerOutput = playerOutput.concat(entity.getName() +
                            "]\t(" + entity.getDescription() + ")\n");
                }
            }
        }
        for (int i = 0;i<currentGame.getTotalPlayer().size();i++){
            if (currentGame.getCurrentLocation().getName().equals(currentGame.getTotalPlayer().get(i).getPosition().getName()) &&
            !currentGame.getCurrentPlayer().getName().equals(currentGame.getTotalPlayer().get(i).getName())){
                playerOutput = playerOutput.concat("Player:\t" + currentGame.getTotalPlayer().get(i).getName() + "\n");
            }
        }
        playerOutput = playerOutput.concat("This location could move to:\npath:");
        for (String cLocation : currentGame.getGameWorld().getGameMap().keySet()){
            if (currentGame.getCurrentLocation().getName().equals(cLocation))
                playerOutput = playerOutput.concat("\t" + cLocation + " -> " +
                        currentGame.getGameWorld().getGameMap().get(cLocation)+ "\n");
        }
        return playerOutput;
    }

    private String getGetText(String entityName,boolean isGet) {
        if (isGet) {
            return "Successful, You get [" + entityName + "]";
        } else {
            for (Entity entity : currentGame.getGameWorld().getTotalEntities().keySet()) {
                if (entity.getName().equals(entityName) &&
                        !entity.getIsMovable()) {
                    return "You can't get [" + entityName + "] because it is immovable.";
                }
            }
            return "In this location, you can't get this [" + entityName + "] entity.";
        }
    }

    private String getInvText() {
        String invText = "You get all artefacts as following list:\n";
        boolean isEmpty = true;
        for (Entity entity : currentGame.getGameWorld().getTotalEntities().keySet()){
            if (currentGame.getGameWorld().getTotalEntities().get(entity)
                    .equals(currentGame.getCurrentPlayer().getName())){
                invText = invText.concat("\t[" + entity.getName() +
                        "]\t(" + entity.getDescription() + ")\n");
                isEmpty = false;
            }
        }
        if (isEmpty){
            return invText + "\tYou list is empty.\n";
        }
        else{
            return invText;
        }
    }

    private String getHealthText() {
        return "You health level is " + currentGame.getCurrentPlayer().getHealthLevel() +
                ".\n(If you health level reach 0, you will lose all entities in your inventory)";
    }

    private String getDeathTest() {
        return "\n!You health level is 0. You return the start location and lose all entities!";
    }

    private String getDropText(String entityName,boolean isDrop) {
        if (isDrop) {
            return "Successful, You drop [" + entityName +
                    "] to current location [" + currentGame.getCurrentLocation().getName() +"].";
        } else {
            return "you can't drop [" + entityName + "] entity.";
        }
    }

    private String getMoveText(String newLocation,boolean isMove) {
        if (isMove){
            return "Successful, You goto next location [" + newLocation + "]!";
        }
        else {
            if (newLocation.equals(currentGame.getCurrentLocation().getName())){
                return "You have already been in [" + newLocation + "] location. Try another location again.";
            }
            else{
                return "You can't goto -> [" + newLocation + "] location. Try another location again.";
            }
        }
    }
}