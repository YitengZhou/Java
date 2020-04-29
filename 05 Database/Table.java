/** This class could manipulate table, including read, write, insert, delete, etc. */
import java.io.*;
import java.util.ArrayList;

public class Table {
    private int rows;
    private int columns;
    private ArrayList<String[]> tableData = new ArrayList<>();
    private ArrayList<String> tableStringData = new ArrayList<>();

    // Create table
    public Table(String createTableName) {
        File table = new File(createTableName);
        try {
            table.createNewFile();
        } catch (IOException ioe){
            System.out.println(ioe);
        }
    }

    // Create table with attribute
    public Table(String createTableName, String firstLine) throws IOException {
        File table = new File(createTableName);
        try {
            table.createNewFile();
        } catch (IOException ioe){
            System.out.println(ioe);
        }
        FileWriter writer = new FileWriter(table);
//        firstLine = handleLine(firstLine);
        writer.write("id," + firstLine + "\n");
        writer.flush();
        writer.close();
    }

    // Create table class from table file and storage data in this class
    public Table(File tableFile) throws IOException {
        FileReader reader = new FileReader(tableFile);
        BufferedReader bufferedReader = new BufferedReader(reader);
        String firstLine = bufferedReader.readLine();
        if (firstLine==null){

            return;
        }
        this.tableStringData.add(firstLine);
        String[] headTitle = firstLine.split(",");
        this.tableData.add(headTitle);
        this.columns = headTitle.length;
        String nextLine = "";
        int count = 0;
        while ((nextLine = bufferedReader.readLine()) != null) {
            this.tableStringData.add(nextLine);
            count++;
            String[] row = nextLine.split(",");
            this.tableData.add(row);
        }
        if (count == 0) {
            this.rows = 0;
        } else {
            this.rows = Integer.parseInt(tableData.get(tableData.size() - 1)[0]);
        }
    }

    private String handleLine(String line){
        String newLine = "";
        for (int i = 0; i< line.length();i++){
            if (line.charAt(i) == ' '){
                continue;
            }
            else {
                newLine += line.charAt(i);
            }
        }
        return newLine;
    }

    // Insert row to table
    public void insertRowIntoTable(File tableFile,String insertRow) throws IOException {
        insertRow = handleLine(insertRow);
        try{
            FileWriter writer = new FileWriter(tableFile,true);
            int newRows = rows + 1;
            writer.write(newRows + "," + insertRow + "\n");
            writer.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    // Update column/row to table
    public void updateTable(ArrayList<String[]> updateList) {
        for (int i=0;i<updateList.size();i++){
            int originTableId = Integer.valueOf(updateList.get(i)[0]);
            int updateColumn = 0;
            for (int k=0;k<tableData.get(0).length;k++){
                if (updateList.get(i)[1].equals(tableData.get(0)[k])){
                    updateColumn = k;
                }
            }
            for (int j=1;j<tableData.size();j++){
                int updateTableId = Integer.valueOf(tableData.get(j)[0]);
                if (originTableId == updateTableId){
                    tableData.get(j)[updateColumn]= updateList.get(i)[2];
                }
            }
        }
    }

    // Save table to file
    public void saveTable(File tableFile) throws IOException {
        FileWriter writer = new FileWriter(tableFile);
        for (int i=0;i<tableData.size();i++){
            for (int j=0;j<columns;j++){
                if (j==columns-1){
                    writer.write(tableData.get(i)[j]+"\n");
                }
                else{
                    writer.write(tableData.get(i)[j]+",");
                }
            }
        }
        writer.flush();
        writer.close();
    }

    // Delete table
    public void deleteTable(ArrayList<String[]> deleteList){
        for (int i = 1;i<deleteList.size();i++){
            for (int j = 1;j<tableData.size();j++){
                if (deleteList.get(i)[0].equals(tableData.get(j)[0])){
                    tableData.remove(tableData.get(j));
                    break;
                }
            }
        }
    }

    // Join table with first column and second column
    public ArrayList<Integer> joinTable(Table secondTable,int firstColumn,int secondColumn){
        // Chang head title column
        ArrayList<Integer> joinList = new ArrayList<>();
        for (int i = 1;i<tableData.size();i++){
            for (int j = 1;j<secondTable.getTableData().size();j++){
                if (tableData.get(i)[firstColumn].equals(secondTable.getTableData().get(j)[secondColumn])){
                    joinList.add(i);
                    joinList.add(j);
                }
            }
        }
        return joinList;
    }

    // Alter add table even it a empty table
    public void alterTableAdd(String addColumn){
        ArrayList<String[]> newTable = new ArrayList<>();
        String firstLine = "";
        // handle empty table
        if (tableStringData.size()==0){
            firstLine = "id," + addColumn;
            newTable.add(firstLine.split(","));
            this.columns = 2;
        }
        else {
            firstLine = tableStringData.get(0) + "," + addColumn;
            newTable.add(firstLine.split(","));
            for (int i = 1;i<tableStringData.size();i++){
                String line = tableStringData.get(i) + "," + "default";
                newTable.add(line.split(","));
            }
            this.columns++;
        }
        this.tableData = newTable;
    }

    // Alter drop table, it could identify empty table
    public void alterTableDrop(int dropColumn){
        ArrayList<String[]> newTable = new ArrayList<>();
        String firstLine = "";
        for(int i = 0;i<columns;i++){
            if (i==dropColumn){
                continue;
            }
            firstLine += tableData.get(0)[i] + ",";
        }
        newTable.add(firstLine.split(","));
        for (int i = 1;i<tableData.size();i++){
            String line = "";
            for(int j = 0;j<columns;j++){
                if (j==dropColumn){
                    continue;
                }
                line += tableData.get(i)[j] + ",";
            }
            newTable.add(line.split(","));
        }
        this.columns--;
        this.tableData = newTable;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public ArrayList<String[]> getTableData() {
        return tableData;
    }

    public void setTableData(ArrayList<String[]> tableData) {
        this.tableData = tableData;
    }
}
