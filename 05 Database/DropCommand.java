public class DropCommand extends CommandType{

    public DropCommand(String incoming){
        boolean isDropCommand = checkDrop(incoming);
        super.setCommandValid(isDropCommand);
    }

    private boolean checkDrop(String incoming){
        String[] incomingArray = incoming.split(" ");
        // Check DROP
        if (incomingArray[0].equals("drop")){
            super.setCommandType("drop");
        }
        else{
            return false;
        }
        // Check whether DROP command has correct elements
        int length = incomingArray.length;
        if ( length != 3){
            super.setParsingError("Incorrect elements in DROP command, expect 3");
            return false;
        }
        // Check TABLE or DATABASE
        if (!incomingArray[1].equals("table") && !incomingArray[1].equals("database")){
            super.setParsingError("Unexpected token [" + incomingArray[1] + "], should be 'TABLE' or 'DATABASE'");
            return false;
        }
        return true;
    }

}
