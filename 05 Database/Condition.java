public class Condition {
    private boolean valid;
    private String errorMessage;

    public Condition(String[] incomingArray){
        this.valid = isCondition(incomingArray);
    }

    private boolean isCondition(String[] incomingArray){
        int leftBracket = 0;
        int rightBracket = 0;
        for (int i = 0;i<incomingArray.length;i++){
            // Check operator
            if (i % 4 == 1 && !isOperator(incomingArray[i])){
                errorMessage = "Incorrectly operator in CONDITION";
                return false;
            }
            // Check AND/OR in CONDITION
            if (i % 4 == 3 && !isAndOr(incomingArray[i])){
                errorMessage = "Incorrectly AND/OR in CONDITION";
                return false;
            }
            for (int j = 0;j<incomingArray[i].length();j++){
                if (incomingArray[i].charAt(j) == '(') leftBracket++;
                if (incomingArray[i].charAt(j) == ')') rightBracket++;
            }
        }
        // Check '(' and ')'
        if (leftBracket!=rightBracket){
            errorMessage = "Incorrectly '(', ')' in CONDITION";
            return false;
        }
        return true;
    }

    private boolean isOperator(String str){
        if (str.equals("==")||str.equals(">")||str.equals("<")|| str.toLowerCase().equals("like") ||
                str.equals(">=")||str.equals("<=")||str.equals("!=")){
            return true;
        }
        return false;
    }

    private boolean isAndOr(String str){
        if (str.toLowerCase().equals("and") || str.toLowerCase().equals("or")){
            return true;
        }
        return false;
    }

    public boolean getValid(){
        return valid;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
