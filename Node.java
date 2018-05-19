package congkak;

import java.util.ArrayList;

public class Node{
    private ArrayList<Node> childNodes=new ArrayList<Node>();
    private Node parent;
    private Integer score;
    private int[] coordinates;
    private int[][] currentState;
    private int[] player1Score = {0};
    private int[] AiScore = {0};

    public Node(int[] coordinates, Node parent, int[][] currentState, Integer score){
        this.coordinates = coordinates;
        this.parent = parent;
        this.currentState = currentState;
        if (score != null){
            this.score = score;
        } 
        else{
            if(coordinates != null){
                evaluateScore();
            }
        }
    }

    public void reEvaluateScore(){
        evaluateScore();
    }

    public void setPlayer1Score(int[] player1Score){
        this.player1Score = player1Score;
    }

    public void setAiScore(int[] aiScore){
        this.AiScore = aiScore;
    }

    public ArrayList<Node> getChildNodes(){
        return childNodes;
    }

    public void setChildNodes(ArrayList<Node> childNodes){
        this.childNodes = childNodes;
    }

    public Node getParent(){
        return parent;
    }

    public void setParent(Node parent){
        this.parent = parent;
    }

    public Integer getScore(){
        return score;
    }

    public int[] getCoordinates(){
        return coordinates;
    }

    public void setCoordinates(int[] coordinates){
        this.coordinates = coordinates;
    }

    private void evaluateScore(){
        /*
        int tempMax=currentState[1][0];
        for(int i=1;i<currentState[1].length;i++){
                if(currentState[1][i]!=0){
                    if (currentState[1][i] > tempMax) {
                        tempMax=currentState[1][i];
                    }
                }

        }
        setScore(tempMax);
        */
        this.score = AiScore[0] - player1Score[0];
        this.setScore(score);
    }

    private void setScore(int mark){
        this.score += mark;

    }

    public int[][] getCurrentState(){
        return currentState;
    }

    public void setCurrentState(int[][] currentState){
        this.currentState = currentState;
    }

    public void addChildNode(Node n){
        childNodes.add(n);
    }

    public boolean isLeaf(){
        return childNodes.isEmpty();
    }
    
    public Node getFirstMove(Node node){
        if(parent != null){
            return getFirstMove(node);
        }
        return parent;
    }
    
    @Override
    public String toString(){
        return "Coordinates :" + coordinates[0] + coordinates[1];
    }

}
