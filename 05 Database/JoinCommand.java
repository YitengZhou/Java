import java.util.Arrays;

public class JoinCommand extends CommandType{
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
        return true;
    }
}
