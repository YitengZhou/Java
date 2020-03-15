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

        return "You need to said something.";
    }

    public String getText(String trigger, String subject)
    {
        if (trigger.equals("get")){
            return getGetText(subject);
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
            for (String cLocation : currentGame.getGameWorld().getGameMap().keySet()){
                if (currentGame.getCurrentLocation().getName().equals(cLocation))
                    playerOutput = playerOutput.concat("\t" + cLocation + " -> " +
                            currentGame.getGameWorld().getGameMap().get(cLocation)+ "\n");
            }
        }
        return playerOutput;
    }

    // Get Action
    private String getGetText(String entityName){
        if (entityName != null){
            return "Successful, You get " + entityName + "!";
        }
        else {
            return "In this location, I can't get this entity.";
        }
    }
}
