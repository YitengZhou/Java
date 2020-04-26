import java.io.File;
import java.io.IOException;

public class AlterCommand extends CommandType{

    private String alterationType;
    private String tableName;
    private String attributeName;

    public AlterCommand(String incoming){
        boolean isAlterCommand = checkAlter(incoming);
        super.setCommandValid(isAlterCommand);
    }

    private boolean checkAlter(String incoming){
        String[] incomingArray = incoming.split(" ");
        // Check Alter
        if (incomingArray[0].toLowerCase().equals("alter")){
            super.setCommandType("alter");
        }
        else{
            return false;
        }

        // Check whether Alter command has correct elements
        int length = incomingArray.length;
        if ( length != 5){
            super.setParsingError("Incorrect elements in ALTER command, expect 5");
            return false;
        }
        // Check table
        if (!incomingArray[1].toLowerCase().equals("table")){
            super.setParsingError("Unexpected token [" + incomingArray[1] + "] in ALTER, should be 'TABLE'");
            return false;
        }
        // Check AlterationType whether is  ADD or DROP
        if (!incomingArray[3].toLowerCase().equals("add") && !incomingArray[3].toLowerCase().equals("drop"))  {
            super.setParsingError("Unexpected token [" + incomingArray[3] + "] in ALTER, should be 'ADD' or 'Drop'");
            return false;
        }
        this.tableName = incomingArray[2];
        this.alterationType = incomingArray[3];
        this.attributeName = incomingArray[4];
        return true;
    }

    public void executeCommand(DBController controller) throws IOException {
        if (!checkDatabase(controller)) return;
        File tableFile = new File("./database" + File.separator + controller.getCurrentDatabase()
                + File.separator + tableName + ".txt");
        if (!checkTable(controller,tableFile)) return;
        Table table = new Table(tableFile);
        if (alterationType.toLowerCase().equals("add")){
            table.alterTableAdd(attributeName);
            table.saveTable(tableFile);
            controller.setExecuteStatus(true);
            return;
        }
        else{
            int column = -1;
            for (int i = 0;i<table.getColumns();i++){
                if (table.getTableData().get(0)[i].equals(attributeName)){
                    column = i;
                }
            }
            if (column==-1){
                controller.setErrorMessage("Incorrect Attributes when ALTER table, check[" + attributeName + "]");
                controller.setExecuteStatus(false);
                return;
            }
            table.alterTableDrop(column);
        }
        table.saveTable(tableFile);
        controller.setExecuteStatus(true);
    }
}
