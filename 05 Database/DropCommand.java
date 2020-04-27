import java.io.File;

public class DropCommand extends CommandType{

    private String dropType;
    private String dropName;

    public DropCommand(String incoming){
        boolean isDropCommand = checkDrop(incoming);
        super.setCommandValid(isDropCommand);
    }

    private boolean checkDrop(String incoming){
        String[] incomingArray = incoming.split(" ");
        // Check DROP
        if (incomingArray[0].toLowerCase().equals("drop")){
            super.setCommandType("drop");
        }
        else{
            return false;
        }
        // Check whether DROP command has correct elements
        int length = incomingArray.length;
        if ( length != 3){
            super.setParsingError("Incorrect elements in DROP command, expect 3");
            return false;
        }
        // Check TABLE or DATABASE
        if (!incomingArray[1].toLowerCase().equals("table") && !incomingArray[1].toLowerCase().equals("database")){
            super.setParsingError("Unexpected token [" + incomingArray[1] + "] in DROP, should be 'TABLE' or 'DATABASE'");
            return false;
        }
        this.dropType = incomingArray[1];
        this.dropName = incomingArray[2];
        return true;
    }

    public void executeCommand(DBController controller){
        if (dropType.toLowerCase().equals("table")){
            // Check database and table
            if (!checkDatabase(controller)) return;
            File dropTable = new File("./database" + File.separator + controller.getCurrentDatabase()
                    + File.separator + dropName + ".txt");
            if (!checkTable(controller,dropTable)) return;
            dropTable.delete();
        }
        else{
            File dropDatabase = new File("./database" + File.separator + dropName);
            if (!dropDatabase.exists() || !dropDatabase.isDirectory()){
                controller.setErrorMessage("This database [" + dropName + "] not exist");
                controller.setExecuteStatus(false);
                return;
            }
            File[] files = dropDatabase.listFiles();
            for (int i = 0;i<files.length;i++){
                files[i].delete();
            }
            dropDatabase.delete();
            if (dropName.toLowerCase().equals(controller.getCurrentDatabase())){
                controller.setCurrentDatabase("");
            }
        }
        controller.setExecuteStatus(true);
    }
}
