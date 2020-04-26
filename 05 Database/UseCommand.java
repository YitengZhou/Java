import java.io.File;

public class UseCommand extends CommandType{
    private String useDatabase;

    public UseCommand(String incoming){
        boolean isUseCommand = checkUse(incoming);
        super.setCommandValid(isUseCommand);
    }

    private boolean checkUse(String incoming){
        if (incoming.length()<3){
            super.setParsingError("Need more information for query");
            return false;
        }
        String[] incomingArray = incoming.split(" ");
        // Check USE
        if (incomingArray[0].toLowerCase().equals("use")){
            super.setCommandType("use");
        }
        else{
            return false;
        }
        // Check whether USE command has correct elements
        if (incomingArray.length != 2){
            super.setParsingError("Incorrect elements in USE, expect 2");
            return false;
        }
        this.useDatabase = incomingArray[1];
        return true;
    }

    public void executeCommand(DBController controller) {
        File databaseFolder = new File("./database" + File.separator +useDatabase);
        if (databaseFolder.exists() && databaseFolder.isDirectory()){
            controller.setCurrentDatabase(useDatabase);
            controller.setExecuteStatus(true);
            return;
        }
        controller.setExecuteStatus(false);
        controller.setErrorMessage("You need to CREATE database [" + useDatabase + "] before USE it");
    }
}
