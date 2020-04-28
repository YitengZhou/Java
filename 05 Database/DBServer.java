// This class is DBServer and communicate with DBClient
import java.io.*;
import java.net.*;

class DBServer
{
    final static char EOT = 4;

    // Server start and close
    public static void main(String[] args) {
        try{
            int portNumber = 8888;
            ServerSocket ss = new ServerSocket(portNumber);
            System.out.println("Server Listening");
            Socket socket = ss.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            processNextCommand(in,out);
            out.close();
            in.close();
            socket.close();
            System.out.println("Server shut down, thank you for using database!");
        }catch (IOException ioe){
            System.out.println(ioe);
        }
    }

    // Handling the incoming query
    private static void processNextCommand(BufferedReader in, BufferedWriter out) throws IOException {
        DBController controller = new DBController();
        while(true) {
            String incoming = in.readLine();
            String outputMessage = "";
            if (incoming==null) break;
            System.out.println("Server get message: " + incoming);
            controller.handleQuery(incoming);
            if (controller.getParseStatues() && controller.getExecuteStatus()) {
                if (!controller.getOutputMessage().equals("")) {
                    outputMessage = controller.getOutputMessage();
                } else {
                    outputMessage = "Server response OK\n";
                }
            } else {
                outputMessage = "ERROR: " + controller.getErrorMessage() + "\n";
            }
            out.write(outputMessage + EOT + "\n");
            out.flush();
        }
    }
}
