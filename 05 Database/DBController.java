// This class could control incoming query and output message
import java.io.*;

public class DBController {
    private boolean parseStatus;
    private boolean executeStatus;
    private String errorMessage;
    private String outputMessage;
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

    // Set all parameters to default
    private void resetController(){
        this.parseStatus = false;
        this.executeStatus = false;
        this.errorMessage = "";
        this.outputMessage = "";
    }

    // Handle incoming query
    public void handleQuery(String incoming) throws IOException {
        resetController();
        parseStatus = checkQueryIsValid(incoming);
        if (!parseStatus) return;
        String query = processPreIncoming(incoming);
        DBParsing parsing = new DBParsing(query);
        for (CommandType com : parsing.getTotalCommandType()){
            if (!com.getCommandType().equals("")) {
                if (com.getCommandValid()){
                    com.executeCommand(this);
                    this.outputMessage = processPostIncoming(outputMessage);
                }
                else {
                    this.errorMessage = com.getParsingError();
                }
                return;
            }
        }
        this.errorMessage = "Invalid query";
    }

    // Replace spaces in the string with '^' and
    private String processPreIncoming(String incoming) {
        // Handle Leading space
        int count =0;
        while (incoming.charAt(count)==' '){
            count++;
        }
        incoming = incoming.substring(count);
        // Handle ';' and space in 'String'
        StringBuilder query = new StringBuilder();
        int quota = 0;
        for (int i = 0;i < incoming.length();i++){
            if (incoming.charAt(i)==';'){
                return query.toString();
            }
            if (incoming.charAt(i)=='\''){
                quota++;
                continue;
            }
            if (quota % 2 == 1 && incoming.charAt(i) == ' '){
                query.append('^');
            }
            else {
                query.append(incoming.charAt(i));
            }
        }
        return query.toString();
    }

    // Replace '^' in the output with space ' '
    private String processPostIncoming(String outputMessage){
        if (outputMessage.equals("")){
            return outputMessage;
        }
        String postOutputMessage = "";
        for (int i = 0;i<outputMessage.length();i++){
            if (outputMessage.charAt(i)=='^'){
                postOutputMessage += " ";
            }
            else{
                postOutputMessage += outputMessage.charAt(i);
            }
        }
        postOutputMessage = setLayout(postOutputMessage);
        return postOutputMessage;
    }

    /* Check whether the entered query is valid */
    private boolean checkQueryIsValid(String incoming){
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

    /* Check bracket and quota in query */
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

    // Make the output table layout beautiful
    private String setLayout(String outputTable){
        String[] tableRows = outputTable.split("\n");
        String[] headRow = tableRows[0].split(",");
        int[] maxLength = new int[headRow.length];
        for (int i = 0;i<tableRows.length;i++){
            String[] row = tableRows[i].split(",");
            for (int j = 0;j<row.length;j++){
                if (maxLength[j]<row[j].length()){
                    maxLength[j]=row[j].length();
                }
            }
        }
        for (int i = 0;i<headRow.length;i++){
            maxLength[i] = ((maxLength[i]/8)+1)*8;
        }
        StringBuilder neatTable = new StringBuilder();
        for (String tableRow : tableRows) {
            String[] row = tableRow.split(",");
            for (int j = 0; j < row.length; j++) {
                neatTable.append(row[j]);
                int cell = row[j].length();
                do{
                    neatTable.append('\t');
                    cell += 8;
                } while (cell < maxLength[j]);
            }
            neatTable.append('\n');
        }
        return neatTable.toString();
    }

    public boolean getParseStatues(){
        return parseStatus;
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

    public String getCurrentDatabase() {
        return currentDatabase;
    }

    public void setCurrentDatabase(String DatabasePath){
        this.currentDatabase = DatabasePath;
    }
}
