import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class JoinCommand extends CommandType{

    private String firstTableName;
    private String secondTableName;
    private String firstAttribute;
    private String secondAttribute;

    public JoinCommand(String incoming){
        boolean isJoinCommand = checkJoin(incoming);
        super.setCommandValid(isJoinCommand);
    }
    private boolean checkJoin(String incoming){
        String[] incomingArray = incoming.split(" ");
        // Check JOIN
        if (incomingArray[0].toLowerCase().equals("join")){
            super.setCommandType("join");
        }
        else{
            return false;
        }
        // Check whether JOIN command has correct elements
        int length = incomingArray.length;
        if ( length != 8){
            super.setParsingError("Incorrect elements in JOIN command, expect 8");
            return false;
        }
        // Check FROM
        if (!incomingArray[2].toLowerCase().equals("and")){
            super.setParsingError("Unexpected token [" + incomingArray[2] + "] in JOIN, should be 'AND'");
            return false;
        }
        // Check ON
        if (!incomingArray[4].toLowerCase().equals("on")){
            super.setParsingError("Unexpected token [" + incomingArray[4] + "] in JOIN, should be 'ON'");
            return false;
        }
        // Check AND
        if (!incomingArray[6].toLowerCase().equals("and")){
            super.setParsingError("Unexpected token [" + incomingArray[6] + "] in JOIN, should be 'AND'");
            return false;
        }
        this.firstTableName = incomingArray[1];
        this.secondTableName = incomingArray[3];
        this.firstAttribute = incomingArray[5];
        this.secondAttribute = incomingArray[7];
        return true;
    }

    public void executeCommand(DBController controller) throws IOException {
        // Check database and table
        if (!checkDatabase(controller)) return;
        File firstTableFile = new File("./database" + File.separator + controller.getCurrentDatabase()
                + File.separator + firstTableName + ".txt");
        File secondTableFile = new File("./database" + File.separator + controller.getCurrentDatabase()
                + File.separator + secondTableName + ".txt");
        if (!checkTable(controller,firstTableFile)) return;
        if (!checkTable(controller,secondTableFile)) return;
        Table firstTable = new Table(firstTableFile);
        Table secondTable = new Table(secondTableFile);
        int firstColumn = -1;
        for (int i = 0;i<firstTable.getColumns();i++){
            if (firstTable.getTableData().get(0)[i].equals(firstAttribute)){
                firstColumn = i;
            }
        }
        int secondColumn = -1;
        for (int i = 0;i<secondTable.getColumns();i++){
            if (secondTable.getTableData().get(0)[i].equals(secondAttribute)){
                secondColumn = i;
            }
        }
        if (secondColumn==-1||firstColumn==-1){
            controller.setErrorMessage("Incorrect Attributes when JOIN table, check[" +
                    firstAttribute + "]"+ " and [" + secondAttribute +"]");
            controller.setExecuteStatus(false);
            return;
        }
        ArrayList<Integer> joinList = firstTable.joinTable(secondTable,firstColumn,secondColumn);
        StringBuilder outputTable = new StringBuilder("id,");
        for (int i = 1;i<firstTable.getColumns();i++){
            outputTable.append(firstTableName).append(".").append(firstTable.getTableData().get(0)[i]).append(",");
        }
        for (int i = 1;i<secondTable.getColumns();i++){
            outputTable.append(secondTableName).append(".").append(secondTable.getTableData().get(0)[i]).append(",");
        }
        outputTable.append("\n");
        for (int i = 0;i<joinList.size()/2;i++){
            outputTable.append(i+1).append(",");
            for (int j = 1;j<firstTable.getColumns();j++){
                String outputString = firstTable.getTableData().get(joinList.get(2*i))[j];
                outputTable.append(outputString).append(",");
            }
            for (int k = 1;k<secondTable.getColumns();k++){
                String outputString = secondTable.getTableData().get(joinList.get(2 * i + 1))[k];
                outputTable.append(outputString).append(",");
            }
            outputTable.append("\n");
        }
        controller.setOutputMessage(outputTable.toString());
        controller.setExecuteStatus(true);
    }
}
