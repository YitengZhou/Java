import java.io.File;
import java.io.IOException;

public class CommandType {
    private String commandType;
    private boolean commandValid;
    private String parsingError;

    public CommandType(){
        commandType ="";
        commandValid = false;
        parsingError = "";
    }

    public boolean getCommandValid(){
        return commandValid;
    }

    public void setCommandValid(boolean fact){
        commandValid = fact;
    }

    public String getParsingError(){
        return parsingError;
    }

    public void setParsingError(String error){
        parsingError = error;
    }

    public String getCommandType(){
        return commandType;
    }

    public void setCommandType(String type){
        commandType = type;
    }

    public void executeCommand(DBController controller) throws IOException {}

    // Check current database whether used
    public boolean checkDatabase(DBController controller){
        if (controller.getCurrentDatabase()==null){
            controller.setErrorMessage("You need to USE Database first");
            controller.setExecuteStatus(false);
            return false;
        }
        return true;
    }

    // Check table whether exist in current database
    public boolean checkTable(DBController controller,File tableFile){
        if (!tableFile.exists()){
            controller.setErrorMessage("This table [" + tableFile.getName().split("\\.")[0] + "] not exist in current database");
            controller.setExecuteStatus(false);
            return false;
        }
        return true;
    }
}
