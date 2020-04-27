import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class SelectCommand extends CommandType{

    private boolean hasCondition;
    private Condition condition;
    private String[] attributeList;
    private String tableName;

    public SelectCommand(String incoming){
        this.hasCondition = false;
        boolean isSelectCommand = checkSelect(incoming);
        super.setCommandValid(isSelectCommand);
    }

    private boolean checkSelect(String incoming){
        String[] incomingArray = incoming.split(" ");
        // Check SELECT
        if (incomingArray[0].toLowerCase().equals("select")){
            super.setCommandType("select");
        }
        else{
            return false;
        }
        // Check whether SELECT command has correct elements
        int length = incomingArray.length;
        if ( length < 4){
            super.setParsingError("Incorrect elements in SELECT command, expect >= 4");
            return false;
        }
        // Check FROM
        int position = 0;
        for(int i = 0;i < incomingArray.length;i++){
            if (incomingArray[i].toLowerCase().equals("from")){
                position =i;
            }
        }
        if (position==0){
            super.setParsingError("Expected FROM in SELECT");
            return false;
        }
        this.tableName = incomingArray[position+1];
        // Check WHERE and Condition
        if (position + 2 == incomingArray.length -1){
            super.setParsingError("Expected CONDITION in SELECT");
            return false;
        }
        if (position + 2 < incomingArray.length -1){
            if (!incomingArray[position + 2].toLowerCase().equals("where")) {
                super.setParsingError("Expected WHERE in SELECT");
                return false;
            }
            int conditionPosition = incomingArray.length-position-3;
            String[] condition = Arrays.copyOfRange(incomingArray,position+3,incomingArray.length);
            Condition con = new Condition(condition);
            if (!con.getValid()){
                super.setParsingError(con.getErrorMessage());
                return false;
            }
            this.hasCondition =true;
            this.condition = con;
        }
        // Check WildAttributeList - * or AttributeList
        String[] wildAttributeList= Arrays.copyOfRange(incomingArray,1,position);
        AttributeList list = new AttributeList(wildAttributeList);
        if (!list.getValid()){
            super.setParsingError(list.getErrorMessage());
            return false;
        }
        this.attributeList = list.getAttributeList();
        return true;
    }

    public void executeCommand(DBController controller) throws IOException {
        // Check database and table
        if (!checkDatabase(controller)) return;
        File tableFile = new File("./database" + File.separator + controller.getCurrentDatabase()
                + File.separator + tableName + ".txt");
        if (!checkTable(controller,tableFile)) return;
        Table table = new Table(tableFile);
        ArrayList<String[]> resultTable;
        if (hasCondition){
            resultTable = condition.getTable(table);}
        else {
            resultTable = table.getTableData();
        }
        // ERROR Convert strings to numbers
        if (resultTable==null){
            controller.setErrorMessage(condition.getErrorMessage());
            controller.setExecuteStatus(false);
            return;
        }
        StringBuilder outputTable = new StringBuilder();
        // attributeList is *
        if (attributeList.length == 1 && attributeList[0].equals("*")){
            for (int i=0;i<resultTable.size();i++){
                for (int j=0;j<resultTable.get(i).length;j++){
                    outputTable.append(resultTable.get(i)[j]).append(",");
                }
                outputTable.append("\n");
            }
        }
        // attributeList is wildAttributeList
        else{
            ArrayList<Integer> number = new ArrayList<>();
            for (int i = 0;i<attributeList.length;i++){
                for (int j = 0;j<resultTable.get(0).length;j++){
                    if (attributeList[i].equals(resultTable.get(0)[j])){
                        number.add(j);
                        System.out.println(number.get(0));
                        break;
                    }
                }
            }
            // Check attributeList whether all in table
            if (number.size()!=attributeList.length){
                controller.setErrorMessage("SELECT attribute does not exist in Table");
                controller.setExecuteStatus(false);
                return;
            }
            for (int i=0;i<resultTable.size();i++){
                for (int j=0;j<number.size();j++){
                    outputTable.append(resultTable.get(i)[number.get(j)]).append(",");
                }
                outputTable.append("\n");
            }
        }
        controller.setOutputMessage(outputTable.toString());
        controller.setExecuteStatus(true);
    }
}
