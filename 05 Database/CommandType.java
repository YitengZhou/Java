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
}
