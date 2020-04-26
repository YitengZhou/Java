import java.io.File;
import java.io.IOException;

public class InsertCommand extends CommandType{

    private String insertValue;
    private String tableName;

    public InsertCommand(String incoming){
        boolean isInsertCommand = checkInsert(incoming);
        super.setCommandValid(isInsertCommand);
    }

    private boolean checkInsert(String incoming){
        String[] incomingArray = incoming.split(" ");
        // Check Insert
        if (incomingArray[0].toLowerCase().equals("insert")){
            super.setCommandType("insert");
        }
        else{
            return false;
        }
        // Check whether INSERT command has correct elements
        int length = incomingArray.length;
        if ( length < 5){
            super.setParsingError("Incorrect elements in INSERT command, expect >= 5");
            return false;
        }
        // Check INTO
        if (!incomingArray[1].toLowerCase().equals("into")){
            super.setParsingError("Unexpected token [" + incomingArray[1] + "] in INSERT, should be 'INTO'");
            return false;
        }
        // Check VALUES
        if (!incomingArray[3].toLowerCase().equals("values")){
            super.setParsingError("Unexpected token [" + incomingArray[3] + "] in INSERT, should be 'VALUES'");
            return false;
        }
        // Check <ValueList>
        if (!isValueList(incoming))  {
            super.setParsingError("Expected <ValueList> in INSERT");
            return false;
        }
        this.tableName = incomingArray[2];
        this.insertValue = incoming.substring(incoming.indexOf('(')+1,incoming.indexOf(')'));
        return true;
    }
    private boolean isValueList(String incoming){
        int leftFlag = incoming.indexOf('(');
        int rightFlag = incoming.indexOf(')');
        if (leftFlag == -1 || rightFlag == -1){
            super.setParsingError("Incorrectly ( or )");
            return false;
        }
        for (int i = leftFlag;i<incoming.length();i++){
            if (incoming.charAt(i) == ' ' &&
                    Character.isLetter(incoming.charAt(i - 1)) &&
                    Character.isLetter(incoming.charAt(i + 1))){
                super.setParsingError("Expected a ',' in AttributeList");
                return false;
            }
        }
        return true;
    }

    public void executeCommand(DBController controller) throws IOException {
        // Check database and table
        if (!checkDatabase(controller)) return;
        File tableFile = new File("./database" + File.separator + controller.getCurrentDatabase()
                + File.separator + tableName + ".txt");
        if (!checkTable(controller,tableFile)) return;

        Table table =new Table(tableFile);
        if (table.getColumns() != 1 + insertValue.split(",").length){
            controller.setErrorMessage("The number of insert value is not equal to table");
            controller.setExecuteStatus(false);
            return;
        }
        insertValue = table.handleLine(insertValue);
        table.insertRowIntoTable(tableFile,insertValue);
        controller.setExecuteStatus(true);
    }
}
