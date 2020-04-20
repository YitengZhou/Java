import java.util.Arrays;

public class CreateCommand extends CommandType{

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
                super.setParsingError("Incorrectly ( or )");
                return false;
            }
            String[] attributeList= Arrays.copyOfRange(incomingArray,3,incomingArray.length);
            AttributeList list = new AttributeList(attributeList);
            if (!list.getValid()){
                super.setParsingError(list.getErrorMessage());
                return false;
            }
        }
        return true;
    }
}


