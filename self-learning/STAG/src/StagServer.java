import com.alexmerz.graphviz.ParseException;
import com.alexmerz.graphviz.Parser;
import com.alexmerz.graphviz.objects.Edge;
import com.alexmerz.graphviz.objects.Graph;
import com.alexmerz.graphviz.objects.Node;

import java.io.*;
import java.net.*;
import java.util.*;

class StagServer
{
    public static void main(String args[])
    {
        if(args.length != 2) System.out.println("Usage: java StagServer <entity-file> <action-file>");
        else new StagServer(args[0], args[1], 8888);
    }

    public StagServer(String entityFilename, String actionFilename, int portNumber)
    {
        try {
            setEntities(entityFilename);
            setActions(actionFilename);
            ServerSocket ss = new ServerSocket(portNumber);
            System.out.println("Server Listening");
            while(true) acceptNextConnection(ss);
        } catch(IOException ioe) {
            System.err.println(ioe);
        }
    }

    private void acceptNextConnection(ServerSocket ss)
    {
        try {
            // Next line will block until a connection is received
            Socket socket = ss.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            processNextCommand(in, out);
            out.close();
            in.close();
            socket.close();
        } catch(IOException ioe) {
            System.err.println(ioe);
        }
    }

    private void processNextCommand(BufferedReader in, BufferedWriter out) throws IOException
    {
        String line = in.readLine();
        out.write("You said... " + line + "\n");
        System.out.println("in:"+in);
        System.out.println("out:"+out);
    }

    private void setEntities(String entityFilename)
    {
        int i = 0;
        ArrayList<Location> totalLocation = new ArrayList<Location>();
        try {
            Parser parser = new Parser();
            FileReader reader = new FileReader(entityFilename);
            parser.parse(reader);
            ArrayList<Graph> graphs = parser.getGraphs();
            ArrayList<Graph> firstSubGraphs = graphs.get(0).getSubgraphs();
            for(Graph first : firstSubGraphs){
                ArrayList<Graph> secondSubGraphs = first.getSubgraphs();
                for (Graph second : secondSubGraphs){
                    ArrayList<Node> nodeLocation = second.getNodes(false);
                    Node nLocation = nodeLocation.get(0);
                    System.out.printf("\tid = %s, name = %s\n",second.getId().getId(), nLocation.getId().getId());
                    totalLocation.add(new Location(nLocation.getId().getId(),nLocation.getAttribute("description")));
                    System.out.println(totalLocation.get(0).getName() + totalLocation.get(0).getDescription());
                    ArrayList<Graph> thirdGraphs = second.getSubgraphs();
                    for (Graph third : thirdGraphs) {
                        System.out.printf("\t\tid = %s\n", third.getId().getId());
                        ArrayList<Node> nodesEnt = third.getNodes(false);
                        for (Node nEnt : nodesEnt) {
                            System.out.printf("\t\t\tid = %s, description = %s\n", nEnt.getId().getId(), nEnt.getAttribute("description"));
                        }
                    }
                }
                ArrayList<Edge> edges = first.getEdges();
                for (Edge e : edges){
                    System.out.printf("Path from %s to %s\n", e.getSource().getNode().getId().getId(), e.getTarget().getNode().getId().getId());
                }
            }
            for (int j=0;j<totalLocation.size();j++) {
                System.out.println("room: " + totalLocation.get(j).getName());
                System.out.println("description: "+ totalLocation.get(j).getDescription());
            }
        } catch (FileNotFoundException fnfe) {
            System.out.println(fnfe);
        } catch (ParseException pe) {
            System.out.println(pe);
        }
    }

    private void setActions(String actionsFilename){

    }

}
