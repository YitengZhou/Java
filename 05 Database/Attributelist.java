public class AttributeList {
    private boolean valid;
    private String errorMessage;

    public AttributeList (String[] incomingArray){
        this.valid = isAttributeList(incomingArray);
    }

    private boolean isAttributeList(String[] incomingArray){
        for (int i = 0; i < incomingArray.length-1;i++){
            int length = incomingArray[i].length();
            if (incomingArray[i].charAt(length-1) != ','){
                errorMessage = "Incorrectly ',' in AttributeList";
                return false;
            }
        }
        return true;
    }

    public boolean getValid(){
        return valid;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
