import java.util.Arrays;

public class UpdateCommand extends CommandType{
    public UpdateCommand(String incoming){
        boolean isUpdateCommand = checkUpdate(incoming);
        super.setCommandValid(isUpdateCommand);
    }
    private boolean checkUpdate(String incoming){
        String[] incomingArray = incoming.split(" ");
        // Check UPDATE
        if (incomingArray[0].equals("update")){
            super.setCommandType("update");
        }
        else{
            return false;
        }
        // Check SET
        if (!incomingArray[2].equals("set")){
            super.setParsingError("Unexpected token [" + incomingArray[2] + "], should be 'SET'");
        }
        // Check WHERE
        int position = 0;
        for (int i = 0; i <incomingArray.length;i++){
            if (incomingArray[i].equals("where")){
                position = i;
            }
        }
        if (position==0){
            super.setParsingError("Unexpected WHERE in UPDATE");
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
