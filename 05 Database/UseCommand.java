public class UseCommand extends CommandType{

    public UseCommand(String incoming){
        boolean isUseCommand = checkUse(incoming);
        super.setCommandValid(isUseCommand);
    }

    private boolean checkUse(String incoming){
        if (incoming.length()<3){
            super.setParsingError("Need more information for query");
            return false;
        }
        String[] incomingArray = incoming.split(" ");
        // Check USE
        if (incomingArray[0].toLowerCase().equals("use")){
            super.setCommandType("use");
        }
        else{
            return false;
        }
        // Check whether USE command has correct elements
        if (incomingArray.length != 2){
            super.setParsingError("Incorrect elements in USE, expect 2");
            return false;
        }
        return true;
    }
}
