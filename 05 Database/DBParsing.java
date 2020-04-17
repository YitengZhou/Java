import java.util.ArrayList;

public class DBParsing {
    final private ArrayList<CommandType> totalCommandType = new ArrayList<>();

    public DBParsing(String incoming){
       totalCommandType.add(new Use(incoming));
       totalCommandType.add(new Create(incoming));
    }

    public ArrayList<CommandType> getTotalCommandType() {
        return totalCommandType;
    }
}
