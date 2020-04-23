import java.util.Arrays;

public class DeleteCommand extends CommandType{
    public DeleteCommand(String incoming){
        boolean isDeleteCommand = checkDelete(incoming);
        super.setCommandValid(isDeleteCommand);
    }
    private boolean checkDelete(String incoming){
        String[] incomingArray = incoming.split(" ");
        // Check DELETE
        if (incomingArray[0].toLowerCase().equals("delete")){
            super.setCommandType("delete");
        }
        else{
            return false;
        }
        // Check whether DELETE command has correct elements
        int length = incomingArray.length;
        if ( length < 5){
            super.setParsingError("Incorrect elements in DELETE command, expect >= 5");
            return false;
        }
        // Check FROM
        if (!incomingArray[1].toLowerCase().equals("from")){
            super.setParsingError("Unexpected token [" + incomingArray[1] + "] in DELETE, should be 'FROM'");
            return false;
        }
        // Check WHERE
        if (!incomingArray[3].toLowerCase().equals("where")){
            super.setParsingError("Unexpected token [" + incomingArray[3] + "] in DELETE, should be 'WHERE'");
            return false;
        }
        // Check CONDITION
        String[] condition = Arrays.copyOfRange(incomingArray,4,incomingArray.length);
        Condition con = new Condition(condition);
        if (!con.getValid()){
            super.setParsingError(con.getErrorMessage());
            return false;
        }
        return true;
    }
}
