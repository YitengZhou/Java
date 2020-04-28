public class AttributeList {
    private boolean valid;
    private String errorMessage;
    private String[] attributeList;

    public AttributeList (String[] incomingArray){
        this.attributeList = new String[incomingArray.length];
        this.valid = isAttributeList(incomingArray);
    }

    private boolean isAttributeList(String[] incomingArray){
        for (int i = 0; i < incomingArray.length-1;i++){
            int length = incomingArray[i].length();
            if (incomingArray[i].charAt(length-1) != ','){
                errorMessage = "Incorrectly ',' or space in AttributeList";
                return false;
            }
            this.attributeList[i] = incomingArray[i].substring(0,length-1);
        }
        this.attributeList[incomingArray.length-1]= incomingArray[incomingArray.length-1];
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
}
