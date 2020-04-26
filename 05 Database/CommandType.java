import java.io.File;
import java.io.IOException;

public class CommandType {
    private String commandType;
    private boolean isValid;
    private String parsingError; // 错误提示

    public CommandType(){
        commandType ="";
        isValid = false;
        parsingError = "";
    }

    public boolean getIsValid(){
        return isValid;
    }

    public void setCommandValid(boolean fact){
        isValid = fact;
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

    public void executeCommand(DBController controller) throws IOException {

    }

    // Check database whether used
    public boolean checkDatabase(DBController controller){
        // Check current database
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
            controller.setErrorMessage("This table [" + tableFile.getName() + "] not exist in current database");
            controller.setExecuteStatus(false);
            return false;
        }
        return true;
    }
}
