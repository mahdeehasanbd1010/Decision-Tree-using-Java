package decisionTree;

public class Main {

    public static void main (String [] arg){

        ReadData readData = new ReadData("data.txt");
        DecisionTree decisionTree = new DecisionTree(readData.getTableHeader(),readData.getTableData(),
                readData.getRow(),readData.getColumn());
        decisionTree.makeTree();
    }
}
