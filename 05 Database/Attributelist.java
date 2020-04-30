/** This class could identify AttributeList */
import java.util.ArrayList;

public class AttributeList {
    private boolean valid;
    private String errorMessage;
    private ArrayList<String> tempList = new ArrayList<>();
    private String stringList="";
    private String[] attributeList;

    public AttributeList (String[] incomingArray){
        this.valid = isAttributeList(incomingArray);
    }

    // Handle attribute list, robust with the space and identify incorrect ','
    private boolean isAttributeList(String[] incomingArray){
        // handle with multiple spaces
        for (String s : incomingArray) {
            if (!s.equals("")) {
                tempList.add(s);
            }
        }
        for (int i = 1;i<tempList.size();i++){
            if (tempList.get(i).charAt(0)!=','){
                if (tempList.get(i-1).charAt(tempList.get(i-1).length()-1)!=','){
                    errorMessage = "Incorrectly ',' in AttributeList";
                    return false;
                }
            }
        }
        for (String s : tempList) {
            stringList += s;
        }
        // Check incorrect bracket in attribute list
        if (stringList.contains("(") || stringList.contains(")")){
            errorMessage = "Incorrectly ( ) in AttributeList";
            return false;
        }
        // Check , number whether correct
        if (stringList.contains(",,")){
            errorMessage = "Incorrectly ',' in AttributeList";
            return false;
        }
        attributeList = stringList.split(",");
        return true;
    }

    public boolean getValid(){
        return valid;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String[] getAttributeList() {
        return attributeList;
    }

    public String getStringList(){
        return stringList;
    }
}
