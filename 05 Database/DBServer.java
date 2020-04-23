import java.io.*;
import java.net.*;
import java.util.*;

class DBServer
{
    final static char EOT = 4;

    public static void main(String args[])
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
        DBController controller = new DBController("");
        while(true) {
            try{
                String incoming= in.readLine();
                System.out.println("Server get message: " + incoming);
                controller.handleQuery(incoming);
                // 处理字符串中的空格
                if (controller.getParseStatues()){
                    incoming = "valid\n" + incoming + '\n'; // 注意\n
                }
                out.write(incoming + EOT + "\n");
                out.flush();
                if (incoming.equals("end")) break;
            }
            catch(IOException ioe) {
                System.err.println(ioe);
            }
        }
    }
}
