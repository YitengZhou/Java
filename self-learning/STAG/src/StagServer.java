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
    private Controller controller;
    private ArrayList<Player> totalPlayer;

    public static void main(String args[])
    {
        if(args.length != 2) System.out.println("Usage: java StagServer <entity-file> <action-file>");
        else new StagServer(args[0], args[1], 8888);
    }

    public StagServer(String entityFilename, String actionFilename, int portNumber)
    {
        try {
            ParseGame parseGame = new ParseGame(entityFilename,actionFilename);
            this.controller = new Controller(parseGame);
            this.totalPlayer = controller.getTotalPlayer();
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
        String[] inputText = line.split(" ");
        String player = inputText[0].split(":")[0];
        Player currentPlayer = getPlayer(player);

        if (inputText.length > 1){
            out.write(controller.handleIncomingCommand(inputText));
        }
        else {
            out.write("You need type something to play this game.");
        }

        if (currentPlayer.getHealthLevel() == 0){
            inputText[1] = "death";
            out.write(controller.handleIncomingCommand(inputText));
        }
        currentPlayer.setPosition(controller.getCurrentLocation());
    }

    /** Get current player and set location (same to latest record),
     * meaning different player could stay at different location
     * If a new player enter into, add player to ArrayList and set location to start */
    private Player getPlayer(String playerName)
    {
        for (Player player : totalPlayer){
            if (player.getName().equals(playerName)){
                controller.setCurrentPlayer(player);
                controller.setCurrentLocation(player.getPosition());
                return player;
            }
        }
        Player newPlayer = new Player(playerName,"");
        controller.setCurrentPlayer(newPlayer);
        controller.addNewPlayer(newPlayer);
        controller.setCurrentLocation(newPlayer.getPosition());
        newPlayer.setPosition(controller.getCurrentLocation());
        return newPlayer;
    }
}
