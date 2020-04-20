// SubCommand
public class NameValueList {
    private boolean valid;
    private String errorMessage;

    public NameValueList(String[] incomingArray){ this.valid = isNameValueList(incomingArray);
    }

    private boolean isNameValueList(String[] incomingArray){
        for (int i = 0;i<incomingArray.length;i++){
            if (i % 4 == 1 && !incomingArray[i].equals("=")){
                errorMessage = "Incorrectly '=' in NameValueList";
                return false;
            }
            if (i % 4 == 3 && !incomingArray[i].equals(",")){
                errorMessage = "Incorrectly ',' in NameValueList";
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
