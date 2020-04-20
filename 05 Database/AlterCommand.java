public class AlterCommand extends CommandType{

    public AlterCommand(String incoming){
        boolean isAlterCommand = checkAlter(incoming);
        super.setCommandValid(isAlterCommand);
    }

    private boolean checkAlter(String incoming){
        String[] incomingArray = incoming.split(" ");
        // Check Alter
        if (incomingArray[0].equals("alter")){
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
        if (!incomingArray[1].equals("table")){
            super.setParsingError("Unexpected token [" + incomingArray[1] + "], should be 'TABLE'");
            return false;
        }
        // Check AlterationType whether is  ADD or DROP
        if (!incomingArray[3].equals("add") && !incomingArray[3].equals("drop"))  {
            super.setParsingError("Unexpected token [" + incomingArray[3] + "], should be 'ADD' or 'Drop'");
            return false;
        }
        return true;
    }

}