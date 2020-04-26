import java.io.*;
import java.net.*;

class DBServer
{
    final static char EOT = 4;

    public static void main(String[] args)
    {
        int portNumber = 8888;
        try {
            ServerSocket ss = new ServerSocket(portNumber);
            System.out.println("Server Listening");
            Socket socket = ss.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            processNextCommand(in,out);
            out.close();
            in.close();
            socket.close();
        } catch(IOException ioe) {
            System.err.println(ioe);
        }

    }

    private static void processNextCommand(BufferedReader in, BufferedWriter out) throws IOException {
        DBController controller = new DBController();
        while(true) {
            try{
                String incoming= in.readLine();
                String outputMessage ="";
                System.out.println("Server get message: " + incoming);
                controller.handleQuery(incoming);
                if (controller.getParseStatues()&&controller.getExecuteStatus()){
                    outputMessage = "Server response OK\n";
                    if (!controller.getOutputMessage().equals("")){
                        outputMessage += controller.getOutputMessage();
                        controller.setOutputMessage("");
                    }
                }
                else {
                    outputMessage = "ERROR: " + controller.getErrorMessage() + "\n";
                }
                out.write(outputMessage + EOT + "\n");
                out.flush();
            }
            catch(IOException ioe) {
                System.err.println(ioe);
            }
        }
    }
}
