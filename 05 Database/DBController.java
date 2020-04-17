import java.io.*;

public class DBController {
    private boolean queryStatus;
    private DBParsing parsing; //可能需要更改
    private String currentDirectory;
    private String currentDatabase;

    public DBController(String incoming){
        this.queryStatus = false;
        this.currentDirectory = "";
        this.currentDatabase = "";
    }

    public void handleQuery(String incoming){
        queryStatus = checkQueryIsValid(incoming);
        DBParsing parsing = new DBParsing(incoming);
        for (CommandType com : parsing.getTotalCommandType()){
            System.out.println(com.getClass().getName() + " : "+ com.getIsCommandType());
            if (!com.getParsingError().equals("")) System.out.println(com.getParsingError());
        }
    }

    /* Check whether the entered query is valid */
    public boolean checkQueryIsValid(String incoming){
        if (incoming.charAt(incoming.length()-1) != ';'){
            return false;
        }
        return true;
    }

    public boolean getQueryStatues(){
        return queryStatus;
    }

    public DBParsing getParsing(){
        return parsing;
    }
}
