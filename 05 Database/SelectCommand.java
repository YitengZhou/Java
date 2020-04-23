import java.util.Arrays;

public class SelectCommand extends CommandType{

    public SelectCommand(String incoming){
        boolean isSelectCommand = checkSelect(incoming);
        super.setCommandValid(isSelectCommand);
    }

    private boolean checkSelect(String incoming){
        String[] incomingArray = incoming.split(" ");
        // Check SELECT
        if (incomingArray[0].toLowerCase().equals("select")){
            super.setCommandType("select");
        }
        else{
            return false;
        }
        // Check whether SELECT command has correct elements
        int length = incomingArray.length;
        if ( length < 4){
            super.setParsingError("Incorrect elements in SELECT command, expect >= 4");
            return false;
        }
        // Check FROM
        int position = 0;
        for(int i = 0;i < incomingArray.length;i++){
            if (incomingArray[i].toLowerCase().equals("from")){
                position =i;
            }
        }
        if (position==0){
            super.setParsingError("Expected FROM in SELECT");
            return false;
        }
        // Check WHERE and Condition
        if (position + 2 == incomingArray.length){
            super.setParsingError("Expected CONDITION in SELECT");
            return false;
        }
        if (position + 2 < incomingArray.length){
            if (!incomingArray[position + 2].toLowerCase().equals("where")) {
                super.setParsingError("Expected WHERE in SELECT");
                return false;
            }
            int conditionPosition = incomingArray.length-position-3;
            String[] condition = Arrays.copyOfRange(incomingArray,position+3,incomingArray.length);
            Condition con = new Condition(condition);
            if (!con.getValid()){
                super.setParsingError(con.getErrorMessage());
                return false;
            }
        }
        // Check WildAttributeList - * or AttributeList
        String[] wildAttributeList= Arrays.copyOfRange(incomingArray,1,position);
        if (wildAttributeList.length==1 && wildAttributeList[0].equals("*")){
            return true;
        }
        AttributeList list = new AttributeList(wildAttributeList);
        if (!list.getValid()){
            super.setParsingError(list.getErrorMessage());
            return false;
        }
        return true;
    }
}
