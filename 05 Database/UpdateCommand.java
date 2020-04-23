import java.util.Arrays;

public class UpdateCommand extends CommandType{
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
        // Check NameValueList
        String[] nameValueList = Arrays.copyOfRange(incomingArray,3,position);
        NameValueList list = new NameValueList(nameValueList);
        if (!list.getValid()){
            super.setParsingError(con.getErrorMessage());
            return false;
        }
        return true;
    }
}
