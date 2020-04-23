import java.io.*;
import java.util.ArrayList;

public class Table {
    int rows; // data
    int columns; // 数值
    ArrayList<ArrayList<String>> tableData = new ArrayList<>();

    public Table(String database,String name) throws IOException {
        String tableName = "./database" + File.separator + database + File.separator + name + ".txt";
        File fileToOpen = new File(tableName);
        boolean isCreated = false;
        try {
            isCreated =fileToOpen.createNewFile();
        } catch (IOException ioe){
            System.out.println(ioe);
        }
    }

    public Table(String database,String name, String firstLine) throws IOException {
        String tableName = "./database" + File.separator + database + File.separator + name + ".txt";
        File fileToOpen = new File(tableName);
        boolean isCreated = false;
        try {
            isCreated =fileToOpen.createNewFile();
        } catch (IOException ioe){
            System.out.println(ioe);
        }
        FileWriter writer = new FileWriter(fileToOpen);
        writer.write(firstLine + "\n");
        writer.flush();
        writer.close();
    }

//    this.rows = 0;
//        this.columns = head.length;
//    ArrayList<String> headTitle = new ArrayList<>();
//    String firstLine = "";
//        for (String s : head){
//        headTitle.add(s);
//        firstLine += s;
//    }
//        this.tableData.add(headTitle);
}
