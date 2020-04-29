/** This class could identify Condition and create result table according to the condition */
import java.util.ArrayList;
import java.util.HashMap;

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
        ArrayList<ArrayList<String[]>> newCopy = new ArrayList<>();
        HashMap<Integer,Integer> priority = handlePriority(conditionArray);
        for (int i = 0;i<conditionArray.length;i++){
            if (i % 2==0){
                conditionArray[i] = handleString(conditionArray[i]);
            }
            // Check whether the condition element is in the table;
            if (i % 4==0){
                boolean containFlag = false;
                for (String s: table.getTableData().get(0)){
                    if (s.equals(conditionArray[i])){
                        containFlag = true;
                        break;
                    }
                }
                if (!containFlag){
                    errorMessage = "[" + conditionArray[i] + "] is not in this table";
                    return null;
                }
            }
        }
        ArrayList<String> sequence= new ArrayList<>();
        // Get all condition table
        int loop = priority.size();
        for (int i = 0;i < loop;i++){
            int maxPriorityElement = getMaxPriority(priority);
            // Set AND/OR sequence
            if (maxPriorityElement+3<conditionArray.length){
                sequence.add(conditionArray[maxPriorityElement+3]);
            }
            newCopy.add(getConditionTable(conditionArray[maxPriorityElement],conditionArray[maxPriorityElement+1],conditionArray[maxPriorityElement+2],table));
            priority.put(maxPriorityElement,-1);
        }
        // Error
        if (errorMessage!=null){
            return null;
        }
        // Merge table
        if (sequence.size()>0){
            for (int i = 0;i<sequence.size();i++){
                newCopy.set(i+1,mergeTable(newCopy.get(i),newCopy.get(i+1),sequence.get(i)));
            }
            return newCopy.get(sequence.size());
        }
        else {
            return newCopy.get(0);
        }
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
                }
                if (secondId > firstId){
                    flag = j;
                    break;
                }
            }
            newTable.add(first.get(i));
        }
        return newTable;
    }

    // testing function print table
    private void printTable(ArrayList<String[]> table){
        StringBuilder s= new StringBuilder();
        for (int i = 0;i<table.size();i++){
            for (int j = 0;j<table.get(i).length;j++){
                s.append(table.get(i)[j]).append("!");
            }
            s.append("\n");
        }
    }

    // Handle priority based on bracket
    private HashMap<Integer,Integer> handlePriority(String[] conditionArray){
        HashMap<Integer,Integer> priority = new HashMap<>();
        int bracket = 0;
        for (int i = 0;i < conditionArray.length;i++){
            for (int j = 0;j<conditionArray[i].length();j++){
                if (conditionArray[i].charAt(j) == '(') bracket++;
                if (conditionArray[i].charAt(j) == ')') bracket--;
            }
            if (i % 4 == 0){
                priority.put(i,bracket);
            }
        }
        return priority;
    }

    // Get max priority element from priority map
    private int getMaxPriority(HashMap<Integer,Integer> priority){
        int maxPriority = priority.get(0);
        int maxPriorityElement = 0;
        for (int i = 0;i<priority.size();i++){
            if (priority.get(4*i)>maxPriority){
                maxPriority = priority.get(4*i);
                maxPriorityElement = 4*i;
            }
        }
        return maxPriorityElement;
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
