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
                    if (!controller.getOutputMessage().equals("")){
                        outputMessage = setType(controller.getOutputMessage());
                    }
                    else{
                        outputMessage = "Server response OK\n";
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

    // Make the output table layout beautiful
    private static String setType(String outputTable){
        System.out.println(outputTable);
        String[] tableRows = outputTable.split("\n");
        String[] headRow = tableRows[0].split(",");
        int[] maxLength = new int[headRow.length];
        for (int i = 0;i<tableRows.length;i++){
            String[] row = tableRows[i].split(",");
            for (int j = 0;j<row.length;j++){
                if (maxLength[j]<row[j].length()){
                    maxLength[j]=row[j].length();
                }
            }
        }
        for (int i = 0;i<headRow.length;i++){
            maxLength[i] = ((maxLength[i]/8)+1)*8;
        }
        StringBuilder neatTable = new StringBuilder();
        for (String tableRow : tableRows) {
            String[] row = tableRow.split(",");
            for (int j = 0; j < row.length; j++) {
                neatTable.append(row[j]);
                int cell = row[j].length();
                do{
                    neatTable.append('\t');
                    cell += 8;
                } while (cell < maxLength[j]);
            }
            neatTable.append('\n');
        }
        return neatTable.toString();
    }
}
