import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class UpdateCommand extends CommandType{
    private String tableName;
    private Condition condition;
    private String[] nameValueList;

    public UpdateCommand(String incoming){
        boolean isUpdateCommand = checkUpdate(incoming);
        super.setCommandValid(isUpdateCommand);
    }
    private boolean checkUpdate(String incoming){
        String[] incomingArray = incoming.split(" ");
        // Check UPDATE
        if (incomingArray[0].toLowerCase().equals("update")){
            super.setCommandType("update");
        }
        else{
            return false;
        }
        // Check whether UPDATE command has correct elements
        int length = incomingArray.length;
        if ( length < 6){
            super.setParsingError("Incorrect elements in UPDATE command, expect >= 6");
            return false;
        }
        // Check SET
        if (!incomingArray[2].toLowerCase().equals("set")){
            super.setParsingError("Unexpected token [" + incomingArray[2] + "] in UPDATE, should be 'SET'");
            return false;
        }
        // Check WHERE
        int position = 0;
        for (int i = 0; i <incomingArray.length;i++){
            if (incomingArray[i].toLowerCase().equals("where")){
                position = i;
            }
        }
        if (position==0){
            super.setParsingError("Unexpected WHERE in UPDATE");
            return false;
        }
        // Check CONDITION
        int conditionPosition = incomingArray.length-position-1;
        String[] condition = Arrays.copyOfRange(incomingArray,position+1,incomingArray.length);
        Condition con = new Condition(condition);
        if (!con.getValid()){
            super.setParsingError(con.getErrorMessage());
            return false;
        }
        this.condition = con;
        // Check NameValueList
        String[] nameValueList = Arrays.copyOfRange(incomingArray,3,position);
        NameValueList list = new NameValueList(nameValueList);
        if (!list.getValid()){
            super.setParsingError(list.getErrorMessage());
            return false;
        }
        this.tableName = incomingArray[1];
        this.nameValueList = list.getNameValueList();
        return true;
    }

    public void executeCommand(DBController controller) throws IOException {
        // Check database and table
        if (!checkDatabase(controller)) return;
        File tableFile = new File("./database" + File.separator + controller.getCurrentDatabase()
                + File.separator + tableName + ".txt");
        if (!checkTable(controller,tableFile)) return;
        Table table = new Table(tableFile);
        ArrayList<String[]> resultTable = condition.getTable(table);

        // Check update information
        ArrayList<String[]> updateList = new ArrayList<>();
        int updateNumber = 0;
        for (int i = 0; i<nameValueList.length;i++){
            String[] updateColumn = nameValueList[i].split("=");
            for (int j=0;j<resultTable.get(0).length;j++){
                if (updateColumn[0].equals(resultTable.get(0)[j])){
                    updateNumber++;
                    for (int k=1;k<resultTable.size();k++){
                        updateList.add(new String[] {resultTable.get(k)[0],updateColumn[0],updateColumn[1]});
                    }
                }
            }
        }
        if (updateNumber!=nameValueList.length){
            controller.setErrorMessage("Some UPDATE values are not exist in table");
            controller.setExecuteStatus(false);
            return;
        }
        table.updateTable(updateList);
        table.saveTable(tableFile);
        controller.setExecuteStatus(true);
    }
}
