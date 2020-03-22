import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;


public class ParseGame {
    private ArrayList<Location> totalLocation = new ArrayList<>();
    private HashMap<Entity, String> totalEntities = new HashMap<>();
    private IdentityHashMap<String,String> gameMap = new IdentityHashMap<>();
    private ArrayList<Actions> totalActions = new ArrayList<>();
    private HashSet<String> standardCommands = new HashSet<>();

    public ParseGame(String entityFilename, String actionFilename) throws IOException {
        ParseEntity parseEntity = new ParseEntity(entityFilename);
        totalLocation = parseEntity.getTotalLocation();
        totalEntities = parseEntity.getTotalEntities();
        gameMap = parseEntity.getGameMap();

        ParseAction parseAction = new ParseAction(actionFilename);
        totalActions = parseAction.getTotalActions();
        addStandardCommands();
    }

    private void addStandardCommands()
    {
        // Inventory
        standardCommands.add("inventory");
        standardCommands.add("inv");
        // Get
        standardCommands.add("get");
        // Drop
        standardCommands.add("drop");
        // Goto
        standardCommands.add("goto");
        // Look
        standardCommands.add("look");
        // Health
        standardCommands.add("health");
        // Death
        standardCommands.add("death");
        // Game cheating for test and quickly game
        standardCommands.add("gamecheat");
    }

    public ArrayList<Actions> getTotalActions()
    {
        return totalActions;
    }

    public HashMap<Entity, String> getTotalEntities()
    {
        return totalEntities;
    }

    public ArrayList<Location> getTotalLocation()
    {
        return totalLocation;
    }

    public IdentityHashMap<String, String> getGameMap()
    {
        return gameMap;
    }

    public HashSet<String> getStandardCommands()
    {
        return standardCommands;
    }

    public HashSet<String> getTotalEntityNames()
    {
        HashSet<String> totalEntityNames = new HashSet<>();
        for (Entity entity : totalEntities.keySet()){
            totalEntityNames.add(entity.getName());
        }
        return totalEntityNames;
    }

}
