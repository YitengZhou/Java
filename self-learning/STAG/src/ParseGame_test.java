import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;


public class ParseGame_test {
    private ArrayList<Location> totalLocation = new ArrayList<>();
    private HashMap<Entity, String> totalEntities = new HashMap<>();
    private IdentityHashMap<String,String> gameMap = new IdentityHashMap<>();
    private ArrayList<Actions> totalActions = new ArrayList<>();

    public ParseGame_test(String entityFilename, String actionFilename) throws IOException {
        ParseEntity parseEntity = new ParseEntity(entityFilename);
        totalLocation = parseEntity.getTotalLocation();
        totalEntities = parseEntity.getTotalEntities();
        gameMap = parseEntity.getGameMap();
        /** test the function of parseEntity()
        System.out.println("Read " + entityFilename + " OK");
        System.out.println("There are " + totalLocation.size() + " locations:");
        for (int j=0;j<totalLocation.size();j++) {
            System.out.println("\t"+totalLocation.get(j).getName());
        }
        for (String L:gameMap.keySet()){
            System.out.println( L + " -> " + gameMap.get(L));
        }

        for (Entity e : totalEntities.keySet()){
            System.out.println(e.getName() + " in " + totalEntities.get(e));
        }*/
        ParseAction parseAction = new ParseAction(actionFilename);
        totalActions = parseAction.getTotalActions();
        addStandardAction();

        /** test the function of parseActions()
         System.out.println(totalActions.get(2).getTriggers().contains("drink"));
         System.out.println(totalActions.size());

         for (int i=0;i<totalActions.size();i++) {
             System.out.println(totalActions.get(i).getTriggers());
             System.out.println(totalActions.get(i).getNarration());
             if (totalActions.get(i).getTriggers().contains("inv")){
                 System.out.println(totalActions.get(i).getNarration());
             }
         }*/
    }

    private void addStandardAction(){
        // Inventory
        HashSet<String> standardActionInv = new HashSet<>();
        standardActionInv.add("inventory");
        standardActionInv.add("inv");
        totalActions.add(new Actions(
                standardActionInv,
                null,
                null,
                null,
                "Lists all of the artefacts"));

        // Get
        HashSet<String> standardActionGet = new HashSet<>();
        standardActionGet.add("get");
        totalActions.add(new Actions(
                standardActionGet,
                null,
                null,
                null,
                "Pick up an artefact from current location"));

        // Drop
        HashSet<String> standardActionDrop = new HashSet<>();
        standardActionDrop.add("drop");
        totalActions.add(new Actions(
                standardActionDrop,
                null,
                null,
                null,
                "Puts down an artefact"));

        // Goto
        HashSet<String> standardActionGoto = new HashSet<>();
        standardActionGoto.add("goto");
        totalActions.add(new Actions(
                standardActionGoto,
                null,
                null,
                null,
                "Move to another location"));

        // Look
        HashSet<String> standardActionLook = new HashSet<>();
        standardActionLook.add("look");
        totalActions.add(new Actions(
                standardActionLook,
                null,
                null,
                null,
                "reports entities and paths"));
    }

    public ArrayList<Actions> getTotalActions() {
        return totalActions;
    }

    public HashMap<Entity, String> getTotalEntities() {
        return totalEntities;
    }

    public ArrayList<Location> getTotalLocation() {
        return totalLocation;
    }

    public IdentityHashMap<String, String> getGameMap() {
        return gameMap;
    }
}
