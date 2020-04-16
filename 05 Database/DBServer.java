import java.io.*;
import java.net.*;
import java.util.*;

class DBServer
{
    final static char EOT = 4;

    public static void main(String args[])
    {
        Socket socket = null;
        BufferedReader in = null;
        BufferedWriter out = null;
        int portNumber = 8888;
        try {
            ServerSocket ss = new ServerSocket(portNumber);
            System.out.println("Server Listening");
            socket = ss.accept();
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            while(true) {
                try{
                    String incoming= in.readLine();//卡在这
                    System.out.println("Server get message: " + incoming);
                    out.write(incoming +"\n");
                    out.write(incoming +"\n");
                    out.write(incoming + EOT + "\n");
                    out.flush();
                    if (incoming.equals("end")) break;
                }
                catch(IOException ioe) {
                    System.err.println(ioe);
                }
            }
        } catch(IOException ioe) {
            System.err.println(ioe);
        } finally {
            try {
                out.close();
                in.close();
                socket.close();
            }
            catch (IOException ioe){
                System.err.println(ioe);
            }
        }
    }

    private static void processNextCommand(BufferedReader in, BufferedWriter out) throws IOException {


    }
}
