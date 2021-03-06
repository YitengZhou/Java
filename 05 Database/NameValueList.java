/** This class could identify name value list */
public class NameValueList {
    private boolean valid;
    private String errorMessage;
    private String[] nameValueList;

    public NameValueList(String[] incomingArray){ this.valid = isNameValueList(incomingArray);
    }

    // Identity mark = 50 , name = x or  mark = 50, name = x
    private boolean isNameValueList(String[] incomingArray){
        StringBuilder totalNameValueList= new StringBuilder();
        for (String s : incomingArray) {
            totalNameValueList.append(s);
        }
        int equalSymbol = 0;
        int commaSymbol = 0;
        for (int i = 0;i<totalNameValueList.length();i++){
            if (totalNameValueList.charAt(i)=='=') equalSymbol++;
            if (totalNameValueList.charAt(i)==',') commaSymbol++;
        }
        if (equalSymbol!=commaSymbol+1){
            errorMessage = "Incorrectly '='/',' in NameValueList";
            return false;
        }
        nameValueList= totalNameValueList.toString().split(",");
        return true;
    }

    public boolean getValid(){
        return valid;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String[] getNameValueList() {
        return nameValueList;
    }
}
