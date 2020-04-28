// This class could parse and execute CREATE Command
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class CreateCommand extends CommandType{

    private String[] createArray;
    private String tableAttribute;

    public CreateCommand(String incoming){
        boolean isCreateCommand = checkCreate(incoming);
        super.setCommandValid(isCreateCommand);
    }

    private boolean checkCreate(String incoming){
        String[] incomingArray = incoming.split(" ");
        // Check CREATE
        if (incomingArray[0].toLowerCase().equals("create")){
            super.setCommandType("create");
        }
        else{
            return false;
        }
        // Check whether CREATE command has correct elements
        int length = incomingArray.length;
        if ( length < 3){
            super.setParsingError("Incorrect elements in CREATE command, expect >= 3");
            return false;
        }

        // Check TABLE or DATABASE
        if (!incomingArray[1].toLowerCase().equals("table") &&
                !incomingArray[1].toLowerCase().equals("database")){
            super.setParsingError("Unexpected token [" + incomingArray[1] + "] in CREATE, should be 'TABLE' or 'DATABASE'");
            return false;
        }

        // Check TABLE ( AttributeList )
        if (length > 3 ) {
            if (incomingArray[1].toLowerCase().equals("database")) {
                super.setParsingError("Expected no ( AttributeList ) when CREATE DATABASE");
                return false;
            }
            // Check '(' OR ')'
            int leftFlag = incoming.indexOf('(');
            int rightFlag = incoming.indexOf(')');
            if (leftFlag == -1 || rightFlag == -1){
                super.setParsingError("Incorrectly ( or ) in attributeList when CREATE");
                return false;
            }
            String[] attributeList= Arrays.copyOfRange(incomingArray,3,incomingArray.length);
            AttributeList list = new AttributeList(attributeList);
            if (!list.getValid()){
                super.setParsingError(list.getErrorMessage());
                return false;
            }
            this.tableAttribute = incoming.substring(leftFlag+1,rightFlag);
        }
        this.createArray = incomingArray;
        return true;
    }

    public void executeCommand(DBController controller) throws IOException {
        if (createArray[1].toLowerCase().equals("database")){
            createDatabase(controller);
        }
        else {
            createTable(controller);
        }
    }

    private void createDatabase(DBController controller) {
        File databaseFolder = new File("./database" +File.separator + createArray[2]);
        if (!databaseFolder.exists()){
            databaseFolder.mkdir();
            controller.setExecuteStatus(true); // 创建成功？
        }
        else{
            controller.setErrorMessage("You have already CREATE this database : " + createArray[2]);
            controller.setExecuteStatus(false);
        }
    }

    private void createTable(DBController controller) throws IOException {
        String database = controller.getCurrentDatabase();
        if (!database.equals("")){
            if (tableAttribute==null){
                Table newTable = new Table(database,createArray[2]);
                controller.setExecuteStatus(true); //创建成功
            }
            else {
                Table newTable = new Table(database,createArray[2],tableAttribute);
                controller.setExecuteStatus(true); //创建成功
            }
        }
        else{
            controller.setExecuteStatus(false);
            controller.setErrorMessage("You need to choose DATABASE fist");
        }
    }
}


