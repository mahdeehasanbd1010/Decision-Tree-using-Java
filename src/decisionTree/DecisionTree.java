package decisionTree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

class entropyOfAttributesFeature{
    String featureName;
    double [] arrayOfDecision;
    double InformationGainOfFeature;
    double totalAppearance;
}

class Node{
    String attributeName;
    ArrayList<Edge> edges;
}

class Edge{
    String edgeName;
    Node connectedNode;
}


public class DecisionTree {

    private String[] tableHeader,decisions;
    private String[][] tableData;
    private int row, column, numberOfDecision;;

    public DecisionTree(String[] tableHeader, String[][] tableData, int row, int column){
        this.tableHeader = tableHeader;
        this.tableData = tableData;
        this.row = row;
        this.column = column;
    }

    public String[] findUniqueValues(String[][] tableData, int row, int columnNumber){

        HashSet<String> hashSet = new HashSet<>();

        for(int i=0; i<row-1; i++)
            hashSet.add(tableData[i][columnNumber]);

        String[] uniqueValues = new String[hashSet.size()];
        int j = 0;

        for(Iterator<String> it = hashSet.iterator(); it.hasNext();)
            uniqueValues[j++] = it.next();

        return uniqueValues;

    }

    public double LOG(double number, int base){
        return (Math.log(number)/Math.log(base));
    }

    public double calculateEntropy(double[] arrayOfDecision, int row){
        double entropy=0.0;
        for(int i=0; i<arrayOfDecision.length; i++){
            if(arrayOfDecision[i]==0 || LOG(arrayOfDecision[i]/(row), numberOfDecision)==0) continue;
            entropy+= ((-arrayOfDecision[i]/(row))*(LOG(arrayOfDecision[i]/(row),numberOfDecision)));
        }
        return entropy;
    }

    public double entropyOfClass(String[][] tableData, int row){
        double[] arrayOfDecision = new double[numberOfDecision];
        for(int i=0; i<numberOfDecision; i++){
            arrayOfDecision[i]=0;
        }

        for(int i=0; i<row; i++){
            for(int j=0; j<numberOfDecision; j++){
                if(tableData[i][column-1].equals(decisions[j])){
                    arrayOfDecision[j]++;
                    break;
                }
            }
        }

        return calculateEntropy(arrayOfDecision, row);
    }

    public double entropyOfAttribute(String[][] tableData, int row, int columnNumber){
        String values[] = findUniqueValues(tableData, row, columnNumber);
        ArrayList<entropyOfAttributesFeature> entropyList = new ArrayList<>();

        for(int i=0; i<values.length; i++){
            double[] arrayOfDecision = new double[numberOfDecision];

            for(int j=0; j<row; j++){
                for(int k=0; k<numberOfDecision; k++){
                    if(tableData[j][columnNumber].equals(values[i]) && tableData[j][column-1].equals(decisions[k])){
                        arrayOfDecision[k]++;
                    }
                }
            }

            int totalRow=0;
            for(int j=0; j<numberOfDecision; j++){
                totalRow+=arrayOfDecision[j];
            }

            entropyOfAttributesFeature entropy = new entropyOfAttributesFeature();
            entropy.featureName = values[i];
            entropy.arrayOfDecision = arrayOfDecision;
            entropy.InformationGainOfFeature = calculateEntropy(arrayOfDecision, totalRow);
            entropy.totalAppearance = totalRow;
            entropyList.add(entropy);


        }

        double entropy=0.0;
        for(int i=0; i<entropyList.size(); i++){
            entropy+= (entropyList.get(i).totalAppearance/row)*entropyList.get(i).InformationGainOfFeature;

        }

        return entropy;
    }

    public double calculateGain(String[][] tableData, int row, int columnNumber){
        return (entropyOfClass(tableData, row) - entropyOfAttribute(tableData, row, columnNumber));
    }

    public Node getNode(String[][] tableData, int row){

        Node node = new Node();
        double maxGain=0;
        int maxColumnNumber=0;
        String maxGainAttributeName = tableHeader[0];

        for(int i=0; i< column-1; i++){
            double gain = calculateGain(tableData, row, i);
            if(gain > maxGain){
                maxGain=gain;
                maxColumnNumber=i;
                maxGainAttributeName=tableHeader[i];
            }
        }

        String[] values = findUniqueValues(tableData, row, maxColumnNumber);
        ArrayList<Edge> edges = new ArrayList<>();

        for(int i=0; i<values.length; i++){
            Edge edge = new Edge();
            edge.edgeName = values[i];
            edge.connectedNode = null;
            edges.add(edge);
        }

        node.attributeName= maxGainAttributeName;
        node.edges = edges;

        return node;
    }

    public int getColumnNumber(String attributeName, int row){
        for(int i=0; i<column; i++){
            if(tableHeader[i].equals(attributeName)){
                return i;
            }
        }
        return -1;
    }

    public ArrayList<String> findSpliteNode(Node node, String[][] tableData, int row){
        ArrayList<String> listOfSpliteNode = new ArrayList<>();
        int attributeColumnNumber = getColumnNumber(node.attributeName, row);
        String[] values = findUniqueValues(tableData, row, attributeColumnNumber);

        for(int v=0; v<values.length; v++){
            String decision = null;
            for(int i=0; i<row; i++){
                if(tableData[i][attributeColumnNumber].equals(values[v])){
                    decision = tableData[i][column-1];
                }
            }

            int featureValue=0,decisionValue=0;
            for(int i=0; i< row; i++){
                if(tableData[i][attributeColumnNumber].equals(values[v])){
                    featureValue++;
                }

                if(tableData[i][attributeColumnNumber].equals(values[v]) && tableData[i][column-1].equals(decision)){
                    decisionValue++;
                }
            }

            if(featureValue!=decisionValue){
                listOfSpliteNode.add(values[v]);
            }

            else if(featureValue==decisionValue){
                Node connectedNode = new Node();
                connectedNode.edges = null;
                connectedNode.attributeName = decision;
                for(int i=0; i<node.edges.size(); i++){
                    if(node.edges.get(i).edgeName.equals(values[v])){
                        node.edges.get(i).connectedNode = connectedNode;
                    }
                }
            }
        }

        if(listOfSpliteNode.size() > 0){
            return listOfSpliteNode;
        }

        return null;


    }

    public String[][] getNewTable(Node node, String[][] tableData, int row, String featureName){

        int attributeColumnNumber = getColumnNumber(node.attributeName, row);
        String[][] newTableData;
        int newTableRow = 0;

        for(int i=0; i<row; i++){
            if(tableData[i][attributeColumnNumber].equals(featureName)){
                newTableRow++;
            }
        }

        newTableData = new String[newTableRow][column];

        int j=0;
        for(int i=0; i<row; i++){
            if(tableData[i][attributeColumnNumber].equals(featureName))
            System.arraycopy(tableData[i], 0, newTableData[j++], 0, tableData[i].length);
        }

        return newTableData;
    }

    public void buildDecisionTree(Node node, String[][] tableData, int row){
        if(node==null){
            return;
        }

        ArrayList<String> listOfSpliteNode = findSpliteNode(node, tableData, row);

        if(listOfSpliteNode==null) return;

        for(int i=0; i<listOfSpliteNode.size(); i++){
            String[][] newTableData = getNewTable(node, tableData, row, listOfSpliteNode.get(i));

            Node connectedNode = getNode(newTableData, newTableData.length);

            for(int j=0; j<node.edges.size(); j++){

                Edge nodeEdge = node.edges.get(j);

                if(nodeEdge.edgeName.equals(listOfSpliteNode.get(i))){
                    node.edges.get(j).connectedNode = connectedNode;
                    buildDecisionTree(connectedNode, newTableData, newTableData.length);
                }
            }

        }



    }

    public void printGap(int count, char character){
        for(int i=0; i<count; i++){
            System.out.print(character);
        }
    }

    public void printTree(Node node, int gap){

        gap++;

        if(node==null){
            return;
        }

        printGap(gap, '-');
        System.out.println("Node : "+node.attributeName);
        gap++;
        if(node.edges!=null){
            for(int i=0; i<node.edges.size(); i++){
                printGap(gap, '-');

                System.out.println("edge : "+node.edges.get(i).edgeName);
                printTree(node.edges.get(i).connectedNode, gap);
            }
        }
    }


    public void makeTree(){

        decisions = findUniqueValues(tableData, row-1,column-1);

        numberOfDecision = decisions.length;

        Node root = getNode(tableData, row-1);
        buildDecisionTree(root, tableData, row-1);
        printTree(root,-1);


    }



}
