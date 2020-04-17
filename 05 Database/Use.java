public class Use extends CommandType{

    public Use(String incoming){
        boolean isUseCommand = checkUse(incoming);
        super.setCommandValid(isUseCommand);
    }

    private boolean checkUse(String incoming){
        String[] incomingArray = incoming.split(" ");
        if (incomingArray[0].equals("use")){
            super.setCommandType("use");
        }
        else{
            return false;
        }
        if (incomingArray.length != 2){
            super.setParsingError("Incorrect elements in USE command, expect 2");
            return false;
        }
        return true;
    }
}
