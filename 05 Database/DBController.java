import java.io.*;

public class DBController {
    private boolean parseStatus;
    private boolean executeStatus;
    private String errorMessage;
    private String outputMessage;
    private DBParsing parsing;
    private String currentDatabase;

    // Initialize the DBController
    public DBController() {
        resetController();
        this.currentDatabase = "";
        // Create database folder in this dir
        File databaseFolder = new File("./database");
        if (!databaseFolder.exists()){
            databaseFolder.mkdir();
        }
    }

    private void resetController(){
        this.parseStatus = false;
        this.executeStatus = false;
        this.errorMessage = "";
        this.outputMessage = "";
    }

    // Handle incoming query
    public void handleQuery(String incoming) throws IOException {
        resetController();
        String query = processPreIncoming(incoming);
        parseStatus = checkQueryIsValid(incoming);
        if (!parseStatus){
            return;
        }
        DBParsing parsing = new DBParsing(query);
        for (CommandType com : parsing.getTotalCommandType()){
            if (!com.getCommandType().equals("")) {
                if (com.getIsValid() && parseStatus){
                    com.executeCommand(this);
                }
                else {
                    this.errorMessage = com.getParsingError();
                }
                this.outputMessage = processPostIncoming(outputMessage);
                return;
            }
        }
        this.errorMessage = "Invalid query";
    }

    // Replace spaces in the string with '^' and
    private String processPreIncoming(String incoming) {
        // Handle Leading space
        if (incoming.length()>0){
            int count =0;
            while (incoming.charAt(count)==' '){
                count++;
            }
            incoming = incoming.substring(count,incoming.length());
        }
        // Handle ';' and space in 'String'
        String query = "";
        int quota = 0;
        for (int i = 0;i < incoming.length();i++){
            if (incoming.charAt(i)==';'){
                return query;
            }
            if (incoming.charAt(i)=='\''){
                quota++;
                continue;
            }
            if (quota % 2 == 1 && incoming.charAt(i) == ' '){
                query += '^';
            }
            else {
                query += incoming.charAt(i);
            }
        }
        return query;
    }

    // Replace '^' in the output with space ' '
    private String processPostIncoming(String outputMessage){
        String postOutputMessage = "";
        for (int i = 0;i<outputMessage.length();i++){
            if (outputMessage.charAt(i)=='^'){
                postOutputMessage += " ";
            }
            else{
                postOutputMessage += outputMessage.charAt(i);
            }
        }
        return postOutputMessage;
    }

    /* Check whether the entered query is valid */
    public boolean checkQueryIsValid(String incoming){
        if (incoming.length() < 3) {
            this.errorMessage = "Query too short";
            return false;
        }
        if (incoming.indexOf(';') < 0){
            this.errorMessage = "Semi colon missing at end of line";
            return false;
        }
        if (!checkSymbolsIsValid(incoming)){
            return false;
        }
        return true;
    }

    private boolean checkSymbolsIsValid(String incoming){
        int leftBracket = 0;
        int rightBracket = 0;
        int quotaNumber = 0;
        for (int i = 0;i<incoming.length();i++){
            if (incoming.charAt(i) == '\''){
                quotaNumber++;
            }
            else if (incoming.charAt(i) == '('){
                leftBracket++;
            }
            else if (incoming.charAt(i) == ')'){
                rightBracket++;
            }
        }
        if (leftBracket!=rightBracket){
            this.errorMessage = "Incorrect bracket ( ) in query";
            return false;
        }
        if (quotaNumber%2!=0){
            this.errorMessage = "Incorrect quota number ' in query ";
            return false;
        }
        return true;
    }

    public boolean getParseStatues(){
        return parseStatus;
    }

    public void setParseStatus(boolean status){
        this.parseStatus = status;
    }

    public boolean getExecuteStatus(){
        return executeStatus;
    }

    public void setExecuteStatus(boolean executeStatus) {
        this.executeStatus = executeStatus;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage){
        this.errorMessage = errorMessage;
    }

    public String getOutputMessage() {
        return outputMessage;
    }

    public void setOutputMessage(String outputMessage) {
        this.outputMessage = outputMessage;
    }

    public DBParsing getParsing(){
        return parsing;
    }

    public void setParsing(DBParsing parsing){
        this.parsing = parsing;
    }

    public String getCurrentDatabase() {
        return currentDatabase;
    }

    public void setCurrentDatabase(String DatabasePath){
        this.currentDatabase = DatabasePath;
    }

}
