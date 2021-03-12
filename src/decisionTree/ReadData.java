package decisionTree;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ReadData {

    private int row, column;
    private String[][] tableData;
    private String[] tableHeader;

    public ReadData(String textFilePath){

        BufferedReader bufferedReader;
        ArrayList<String> tableRow = new ArrayList<>();

        try {

            bufferedReader = new BufferedReader(new FileReader(textFilePath));
            String line = bufferedReader.readLine();
            while(line!=null){

                tableRow.add(line);
                line = bufferedReader.readLine();
            }

        } catch (FileNotFoundException e) {
            System.out.println("File not found! error: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //System.out.println("Row : " + tableRow.size());
        row=tableRow.size();

        String[] tempStr = tableRow.get(0).split("    ");
        //System.out.println("Column : " + tempStr.length);
        column=tempStr.length;

        tableData= new String[row-1][column];
        tableHeader = new String[column];

        for(int i=0;i<tableRow.size();i++){
            if(i==0){
                tableHeader = tableRow.get(i).split("    ");
            }
            else{
                tableData[i-1] = tableRow.get(i).split("    ");
            }
        }

        /*for(int i=0; i<column; i++){
            System.out.print(tableHeader[i]+" ");
        }
        System.out.println();

        for(int i=0; i<row-1; i++){
            for(int j=0; j<column; j++){
                System.out.print(tableData[i][j]+" ");
            }
            System.out.println();
        }*/
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public String[][] getTableData() {
        return tableData;
    }

    public String[] getTableHeader() {
        return tableHeader;
    }

}
