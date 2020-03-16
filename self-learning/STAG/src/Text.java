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
        String playerOutput = "You are in location:\n\t" + currentGame.getCurrentLocation().getName() + "\n";
        if (currentGame.getGameWorld().getTotalEntities().containsValue(currentGame.getCurrentLocation().getName())){
            playerOutput = playerOutput.concat("In this room, entity list:\n");
            for (Entity entity : currentGame.getGameWorld().getTotalEntities().keySet()){
                if (currentGame.getGameWorld().getTotalEntities().get(entity)
                        .equals(currentGame.getCurrentLocation().getName()) && entity.getOwner() == null){
                    playerOutput = playerOutput.concat(entity.getClass().getTypeName() + ":\t");
                    playerOutput = playerOutput.concat(entity.getName() +
                            "\t(" + entity.getDescription() + ")\n");
                }
            }
        }
        if (currentGame.getGameWorld().getGameMap().containsKey(currentGame.getCurrentLocation().getName())){
            playerOutput = playerOutput.concat("This location could move to:\npath:");
            /*Set<Map.Entry<String, String>> allSet = null;
            allSet = currentGame.getGameWorld().getGameMap().entrySet();
            Iterator<Map.Entry<String,String>> iterLocation =null;
            iterLocation = allSet.iterator();
            while (iterLocation.hasNext()){
                Map.Entry<String,String> path = iterLocation.next();
                System.out.println(path.getKey() + path.getValue());
                if (path.getKey().equals(currentGame.getCurrentLocation().getName())){
                    playerOutput = playerOutput.concat("\t" + path.getKey() + " -> " + path.getValue() + "\n");
                }
            }*/
            for (String cLocation : currentGame.getGameWorld().getGameMap().keySet()){
                if (currentGame.getCurrentLocation().getName().equals(cLocation))
                    playerOutput = playerOutput.concat("\t" + cLocation + " -> " +
                            currentGame.getGameWorld().getGameMap().get(cLocation)+ "\n");
                System.out.println(cLocation);
            }
        }
        return playerOutput;
    }

    private String getGetText(String entityName,boolean isGet) {
        if (isGet) {
            return "Successful, You get " + entityName + "!";
        } else {
            for (Entity entity : currentGame.getGameWorld().getTotalEntities().keySet()) {
                if (entity.getName().equals(entityName) &&
                        !entity.getIsMovable()) {
                    return "You can't get " + entityName + " because it is immovable.";
                }
            }
            return "In this location, you can't get this " + entityName + " entity.";
        }
    }

    private String getInvText() {
        String invText = "You get all artefacts as following list:\n";
        boolean isEmpty = true;
        for (Entity entity : currentGame.getGameWorld().getTotalEntities().keySet()){
            if (currentGame.getGameWorld().getTotalEntities().get(entity)
                    .equals(currentGame.getCurrentPlayer().getName())){
                invText = invText.concat("\t" + entity.getName() +
                        "\t(" + entity.getDescription() + ")\n");
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

    private String getDropText(String entityName,boolean isDrop) {
        if (isDrop) {
            return "Successful, You drop " + entityName +
                    " to current location : " + currentGame.getCurrentLocation().getName();
        } else {
            return "you can't drop " + entityName + " entity.";
        }
    }

    private String getMoveText(String newLocation,boolean isMove) {
        if (isMove){
            return "Successful, You goto next location: " + newLocation + "!";
        }
        else {
            return "You can't goto " + newLocation + " location. Try another location again.";
        }
    }
}