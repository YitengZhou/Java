/** This class could identify Condition and create result table according to the condition */
import java.util.ArrayList;
import java.util.Stack;

public class Condition {
    private boolean valid;
    private String errorMessage;
    private String[] conditionArray;

    public Condition(String[] incomingArray){
        this.valid = isCondition(incomingArray);
        this.conditionArray = incomingArray;
    }

    // Get result table in different condition
    public ArrayList<String[]> getTable(Table table){
        if (!checkElement(table)) return null;
        Stack<String> conditionStack = new Stack<>();
        ArrayList<String> orderArray= getOrderArray(conditionArray);
        ArrayList<ArrayList<String[]>> newCopy = new ArrayList<>();
        int tableCount = 0;
        for (String s : orderArray){
            if (s.equals(")")){
                if (!getTableFromStack(conditionStack,newCopy,table)) return null;
                conditionStack.push("table" + tableCount);
                tableCount++;
            }
            else {
                conditionStack.add(s);
            }
        }
        if (!conditionStack.empty()){
            if (!getTableFromStack(conditionStack,newCopy,table)) return null;
            conditionStack.push("table" + tableCount);
        }
        if (conditionStack.size()==1) return newCopy.get(newCopy.size()-1);
        else {
            errorMessage = "Invalid condition";
            return null;
        }
    }

    // Pop element from stack and create new table according to operator/and(or)
    private boolean getTableFromStack(Stack<String> conditionStack,ArrayList<ArrayList<String[]>> newCopy,Table table){
        if (conditionStack.size()<3){
            errorMessage = "Invalid condition";
            return false;
        }
        String right = conditionStack.pop();
        String operator = conditionStack.pop();
        String left = conditionStack.pop();
        if (!conditionStack.empty()) conditionStack.pop();
        if (isOperator(operator)) {
            newCopy.add(getConditionTable(left,operator,right,table));
        }
        else{
            newCopy.add(mergeTable(newCopy.get(left.charAt(5)-'0'),newCopy.get(right.charAt(5)-'0'),operator));
        }
        if (errorMessage!=null){
            return false;
        }
        return true;
    }

    // Check whether the condition element is in the table;
    private boolean checkElement(Table table){
        if (table.getRows()==-1){
            errorMessage = "Empty table, you need alter add more column";
            return false;
        }
        for (int i = 0;i<conditionArray.length;i++){
            if (i % 4==0){
                boolean containFlag = false;
                for (String s: table.getTableData().get(0)){
                    if (s.equals(handleString(conditionArray[i]))){
                        containFlag = true;
                        break;
                    }
                }
                if (!containFlag){
                    errorMessage = "[" + handleString(conditionArray[i]) + "] is not in this table";
                    return false;
                }
            }
        }
        return true;
    }

    // Re-order incoming condition to array
    private ArrayList<String> getOrderArray(String[] conditionArray){
        ArrayList<String> orderArray= new ArrayList<>();
        for (String s : conditionArray) {
            if (s.contains("(")) {
                int position = 0;
                for (int j = 0;j<s.length();j++){
                    if (s.charAt(j)=='('){
                        orderArray.add("(");
                        position=j;
                    }
                }
                orderArray.add(s.substring(position+1));
            }
            else if (s.contains(")")) {
                int position = s.indexOf(')');
                orderArray.add(s.substring(0,position));
                for (int j = position;j<s.length();j++){
                    if (s.charAt(j)==')'){
                        orderArray.add(")");
                    }
                }
            }
            else{
                orderArray.add(s);
            }
        }
        return orderArray;
    }

    // Merge table following AND/OR
    private ArrayList<String[]> mergeTable(ArrayList<String[]> first,ArrayList<String[]> second, String operator){
        if (operator.toLowerCase().equals("and")){
            return executeAndTable(first,second);
        }
        else{
            return executeOrTable(first,second);
        }
    }

    // Merge table following AND
    private ArrayList<String[]> executeAndTable(ArrayList<String[]> first,ArrayList<String[]> second){
        ArrayList<String[]> newTable = new ArrayList<>();
        newTable.add(first.get(0));
        for (int i = 1;i<first.size();i++){
            for (int j = 1;j<second.size();j++){
                int firstId = Integer.parseInt(first.get(i)[0]);
                int secondId = Integer.parseInt(second.get(j)[0]);
                if (secondId == firstId){
                    newTable.add(first.get(i));
                }
                if (secondId > firstId){
                    break;
                }
            }
        }
        return newTable;
    }

    // Merge table following OR
    /** Merge table Or is ordered by id */
    private ArrayList<String[]> executeOrTable(ArrayList<String[]> first,ArrayList<String[]> second){
        // first table is empty
        if (first.size()==1){
            return second;
        }
        ArrayList<String[]> newTable = new ArrayList<>();
        newTable.add(first.get(0));
        int flag = 1;
        for (int i = 1;i<first.size();i++){
            int firstId = Integer.parseInt(first.get(i)[0]);
            for (int j = flag;j<second.size();j++){
                int secondId = Integer.parseInt(second.get(j)[0]);
                if (secondId == firstId){
                    flag = j+1;
                    continue;
                }
                if (secondId < firstId){
                    newTable.add(second.get(j));
                    flag = j+1;
                }
                if (secondId > firstId){
                    flag = j;
                    break;
                }
            }
            newTable.add(first.get(i));
        }
        for (int j=flag;j<second.size();j++){
            newTable.add(second.get(j));
        }
        return newTable;
    }

    // Testing function for printing table
    private void printTable(ArrayList<String[]> table){
        StringBuilder s= new StringBuilder();
        for (int i = 0;i<table.size();i++){
            for (int j = 0;j<table.get(i).length;j++){
                s.append(table.get(i)[j]).append("!");
            }
            s.append("\n");
        }
        System.out.println(s.toString());
    }

    // Get table in one condition
    private ArrayList<String[]> getConditionTable(String first, String operator,String second,Table table){
        ArrayList<String[]> newTable = new ArrayList<>();
        newTable.add(table.getTableData().get(0)); // Heading column
        int column = findColumn(first,table);
        if (operator.equals("==")||operator.equals("!=")){
            equalCondition(column,operator,second,table,newTable);
        }
        else if (operator.toLowerCase().equals("like")){
            likeCondition(column,second,table,newTable);
        }
        else {
            compareCondition(column,operator,second,table,newTable);
        }
        return newTable;
    }

    // Get table in equal condition
    private void equalCondition(int firstColumn,String operator, String second,Table table, ArrayList<String[]> newTable){
        for (int i = 1;i<table.getTableData().size();i++){
            if (operator.equals("==")){
                if (table.getTableData().get(i)[firstColumn].equals(second)){
                    newTable.add(table.getTableData().get(i));
                }
            }
            else{
                if (!table.getTableData().get(i)[firstColumn].equals(second)){
                    newTable.add(table.getTableData().get(i));
                }
            }
        }
    }

    // Get table in like condition, could deal with <name Like 'an'> and <name Like an>
    private void likeCondition(int firstColumn,String second,Table table, ArrayList<String[]> newTable){
        if (checkNumerical(second)) {
            errorMessage = "Expected textual String in LIKE";
            return;
        }
        if (second.charAt(0)=='\'' && second.charAt(second.length()-1)=='\''){
            second = second.substring(1,second.length()-1);
        }
        for (int i = 1;i<table.getTableData().size();i++){
            if (table.getTableData().get(i)[firstColumn].contains(second)){
                newTable.add(table.getTableData().get(i));
            }
        }
    }

    // Get table in compare condition, compare int and float
    private void compareCondition(int firstColumn,String operator, String second,Table table, ArrayList<String[]> newTable){
        for (int i = 1;i<table.getTableData().size();i++){
            if (!checkNumerical(table.getTableData().get(i)[firstColumn])){
                errorMessage = "Attribute cannot be converted to number";
                return;
            }
            Float columnValue = Float.parseFloat(table.getTableData().get(i)[firstColumn]);
            Float secondValue = Float.parseFloat(second);
            if (operator.equals(">")){
                if (columnValue > secondValue){
                    newTable.add(table.getTableData().get(i));
                }
            }
            else if (operator.equals(">=")){
                if (columnValue >= secondValue){
                    newTable.add(table.getTableData().get(i));
                }
            }
            else if (operator.equals("<")){
                if (columnValue < secondValue){
                    newTable.add(table.getTableData().get(i));
                }
            }
            else if (operator.equals("<=")){
                if (columnValue <= secondValue){
                    newTable.add(table.getTableData().get(i));
                }
            }
        }
    }

    // Check whether string is numerical
    private boolean checkNumerical(String strToFloat){
        int dotNumber = 0;
        for (int i = 0;i<strToFloat.length();i++){
            if (Character.isLetter(strToFloat.charAt(i))){
                return false;
            }
            else if (strToFloat.charAt(i)=='.'){
                dotNumber++;
            }
        }
        if (dotNumber>1){
            return false;
        }
        return true;
    }

    // Find column number if this column in table
    private int findColumn(String first, Table table){
        String[] firstLine = table.getTableData().get(0);
        for (int i = 0; i<firstLine.length;i++){
            if (firstLine[i].equals(first)){
                return i;
            }
        }
        return 0;
    }

    // Handle space
    private String handleString(String s){
        String newString = "";
        for (int i = 0;i < s.length();i++){
            if (s.charAt(i)=='(' || s.charAt(i)==')'){
                continue;
            }
            else{
                newString += s.charAt(i);
            }
        }
        return newString;
    }

    // Identify whether incoming is condition
    private boolean isCondition(String[] incomingArray){
        if (incomingArray.length<3){
            errorMessage = "CONDITION is too short";
            return false;
        }
        for (int i = 0;i<incomingArray.length;i++){
            // Check operator
            if (i % 4 == 1 && !isOperator(incomingArray[i])){
                errorMessage = "Incorrectly operator in CONDITION, please use ==, >=, <=, >, <, !=, Like";
                return false;
            }
            // Check AND/OR in CONDITION
            if (i % 4 == 3 && !isAndOr(incomingArray[i])){
                errorMessage = "Incorrectly AND/OR in CONDITION";
                return false;
            }
        }
        return true;
    }

    // Identify whether incoming is operator
    private boolean isOperator(String str){
        if (str.equals("==")||str.equals(">")||str.equals("<")|| str.toLowerCase().equals("like") ||
                str.equals(">=")||str.equals("<=")||str.equals("!=")){
            return true;
        }
        return false;
    }

    // Identify whether incoming is AND/OR
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
