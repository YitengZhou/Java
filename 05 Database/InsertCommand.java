//可能可以改成AttributeList类中的方法
public class InsertCommand extends CommandType{

    public InsertCommand(String incoming){
        boolean isInsertCommand = checkInsert(incoming);
        super.setCommandValid(isInsertCommand);
    }

    private boolean checkInsert(String incoming){
        String[] incomingArray = incoming.split(" ");
        // Check Insert
        if (incomingArray[0].toLowerCase().equals("insert")){
            super.setCommandType("insert");
        }
        else{
            return false;
        }
        // Check whether INSERT command has correct elements
        int length = incomingArray.length;
        if ( length < 5){
            super.setParsingError("Incorrect elements in INSERT command, expect >= 5");
            return false;
        }
        // Check INTO
        if (!incomingArray[1].toLowerCase().equals("into")){
            super.setParsingError("Unexpected token [" + incomingArray[1] + "] in INSERT, should be 'INTO'");
            return false;
        }
        // Check VALUES
        if (!incomingArray[3].toLowerCase().equals("values")){
            super.setParsingError("Unexpected token [" + incomingArray[3] + "] in INSERT, should be 'VALUES'");
            return false;
        }
        // Check <ValueList>
        if (!isValueList(incoming))  {
            super.setParsingError("Expected <ValueList> in INSERT");
            return false;
        }
        return true;
    }
    private boolean isValueList(String incoming){
        int leftFlag = incoming.indexOf('(');
        int rightFlag = incoming.indexOf(')');
        if (leftFlag == -1 || rightFlag == -1){
            super.setParsingError("Incorrectly ( or )");
            return false;
        }
        for (int i = leftFlag;i<incoming.length();i++){ //可能需要更改,字符串问题
            if (incoming.charAt(i) == ' ' &&
                    Character.isLetter(incoming.charAt(i - 1)) &&
                    Character.isLetter(incoming.charAt(i + 1))){
                super.setParsingError("Expected a ',' in AttributeList");
                return false;
            }
        }
        return true;
    }
}
