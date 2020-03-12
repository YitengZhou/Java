import com.alexmerz.graphviz.ParseException;
import com.alexmerz.graphviz.Parser;
import com.alexmerz.graphviz.objects.Edge;
import com.alexmerz.graphviz.objects.Graph;
import com.alexmerz.graphviz.objects.Node;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ParseEntity {
    final private ArrayList<Location> totalLocation = new ArrayList<>();
    final private HashMap<Entity,String> totalEntities = new HashMap<>();
    final private HashMap<String,String> gameMap = new HashMap<>();
    public ParseEntity(String entityFilename) throws IOException {
        try {
            Parser parser = new Parser();
            FileReader reader = new FileReader(entityFilename);
            parser.parse(reader);
            ArrayList<Graph> graphs = parser.getGraphs();
            ArrayList<Graph> firstSubGraphs = graphs.get(0).getSubgraphs();
            // First layer identified the different between Locations and paths
            for(Graph first : firstSubGraphs){
                ArrayList<Graph> secondSubGraphs = first.getSubgraphs();
                // Second layer identified the different Locations
                for (Graph second : secondSubGraphs){
                    ArrayList<Node> nodeLocation = second.getNodes(false);
                    Node nLocation = nodeLocation.get(0);
                    totalLocation.add(new Location(nLocation.getId().getId(),nLocation.getAttribute("description")));
                    ArrayList<Graph> thirdGraphs = second.getSubgraphs();
                    // Third layer identified the items in that Location
                    for (Graph third : thirdGraphs) {
                        ArrayList<Node> nodesEnt = third.getNodes(false);
                        for (Node nEnt : nodesEnt) {
                            if (new String("artefacts").equals(third.getId().getId())){
                                totalEntities.put(new Artefact(nEnt.getId().getId(),nEnt.getAttribute("description")),nLocation.getId().getId());
                            }
                            else if (new String("furniture").equals(third.getId().getId())){
                                totalEntities.put(new Furniture(nEnt.getId().getId(),nEnt.getAttribute("description")),nLocation.getId().getId());
                            }
                            else if (new String("characters").equals(third.getId().getId())){
                                totalEntities.put(new Character(nEnt.getId().getId(),nEnt.getAttribute("description")),nLocation.getId().getId());
                            }
                        }
                    }
                }

                // Set the game map via paths
                ArrayList<Edge> edges = first.getEdges();
                for (Edge e : edges){
                    gameMap.put(e.getSource().getNode().getId().getId(),e.getTarget().getNode().getId().getId());
                }
            }
        } catch (FileNotFoundException fnfe) {
            System.out.println(fnfe);
        } catch (ParseException pe) {
            System.out.println(pe);
        }
    }

    public ArrayList<Location> getTotalLocation(){
        return totalLocation;
    }

    public HashMap<Entity,String> getTotalEntities(){
        return totalEntities;
    }

    public HashMap<String,String> getGameMap(){
        return gameMap;
    }
}
