public class Create extends CommandType{

    public Create(String incoming){
        boolean isCreateCommand = checkCreate(incoming);
        super.setCommandValid(isCreateCommand);
    }

    private boolean checkCreate(String incoming){
        String[] incomingArray = incoming.split(" ");
        if (incomingArray[0].equals("create")){
            super.setCommandType("create");
        }
        else{
            return false;
        }

        int length = incomingArray.length;
        if ( length < 3){
            super.setParsingError("Incorrect elements in Create command, expect >= 3");
            return false;
        }
        if (!incomingArray[1].equals("table") || !incomingArray[1].equals("database")){
            super.setParsingError("Unexpected token [" + incomingArray[1] + "], should be 'table' or 'database'");
            return false;
        }
        if (length > 3){
            int lastLength = incomingArray[length-1].length();
            if (incomingArray[3].charAt(0)!='(' ||
                    incomingArray[length-1].charAt(lastLength-2)!=')' ){
                super.setParsingError("Missing ( or )");
                return false;
            }
        }
        return true;
    }

}
