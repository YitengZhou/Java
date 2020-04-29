/** This class could parse query from DBController to generate all grammar in BNF */
import java.util.ArrayList;

public class DBParsing {
    final private ArrayList<CommandType> totalCommandType = new ArrayList<>();

    // Create all grammar class in DBParsing
    public DBParsing(String incoming){
       totalCommandType.add(new UseCommand(incoming));
       totalCommandType.add(new CreateCommand(incoming));
       totalCommandType.add(new DropCommand(incoming));
       totalCommandType.add(new AlterCommand(incoming));
       totalCommandType.add(new InsertCommand(incoming));
       totalCommandType.add(new SelectCommand(incoming));
       totalCommandType.add(new UpdateCommand(incoming));
       totalCommandType.add(new DeleteCommand(incoming));
       totalCommandType.add(new JoinCommand(incoming));
    }

    public ArrayList<CommandType> getTotalCommandType() {
        return totalCommandType;
    }
}
