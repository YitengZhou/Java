import java.io.*;

public class DBController {
    private boolean parseStatus;
    private boolean executeStatus;
    private String errorMessage;
    private String outputMessage;
    private DBParsing parsing;
    private String currentDatabase;

    public DBController(String incoming){
        this.parseStatus = false;
        this.executeStatus = false;
        this.errorMessage = "";
        initialiseDatabaseFolder();
    }

    private void initialiseDatabaseFolder(){
        File databaseFolder = new File("./database");
        if (!databaseFolder.exists()){
            databaseFolder.mkdir();
        }
    }

    public void handleQuery(String incoming) throws IOException {
        String query = preprocessIncoming(incoming);
        parseStatus = checkQueryIsValid(incoming); // parse失败直接跳出
        DBParsing parsing = new DBParsing(query);
        for (CommandType com : parsing.getTotalCommandType()){
//            System.out.println(com.getClass().getName() + " : "+ com.getIsValid());
            if (!com.getCommandType().equals("")) {
                if (com.getIsValid() && parseStatus){
                    System.out.println("Server will execute " + com.getCommandType());
                    executeCommand(com);
                }
                else {
                    System.out.println(com.getParsingError());
                }
            }
        }
    }

    private void executeCommand(CommandType command) throws IOException {
        command.executeCommand(this);
        if (!executeStatus){
            System.out.println(errorMessage);
        }
    }

    private String preprocessIncoming(String incoming) {
        String query = "";
        int quota = 0;
        // Handle ';' and space in 'String'
        for (int i = 0;i < incoming.length();i++){
            if (incoming.charAt(i)==';'){
                return query;
            }
            if (incoming.charAt(i)=='\''){
                quota++;
            }
            if (quota % 2 == 1 && incoming.charAt(i) == ' '){
                query += '^';
            }
            else {
                query += incoming.charAt(i);
            }
        }
        if (quota % 2 == 1 ){
            errorMessage = "Incorrect ' in query";
        }
        return query;
    }

    /* Check whether the entered query is valid */
    public boolean checkQueryIsValid(String incoming){
        if (incoming.length()<2) {
            return false;
        }
        if (incoming.charAt(incoming.length()-1) != ';'){
            return false;
        }
        if (incoming.charAt(incoming.length()-1) == ';' && incoming.charAt(incoming.length()-2) == ' '){
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
