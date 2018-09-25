package hw4.puzzle;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;

import java.util.*;

public class Solver {

    private MinPQ<searchNode> pq;
    private ArrayList<WorldState> solution;
    private HashMap<WorldState, searchNode>     solutionPath;
    private HashMap<WorldState, searchNode>     nodeDict;

    static class searchNode {
        WorldState currentState;
        int movesMade;
        searchNode prevNode;


        public searchNode(WorldState state, int movesMade, searchNode prevNode){
            this.currentState = state;
            this.movesMade= movesMade;
            this.prevNode = prevNode;
        }

    }


    static class nodeComparator implements Comparator {
        @Override
        public int compare(Object nodeA, Object nodeB){
            int totalDistA = ((searchNode) nodeA).currentState.estimatedDistanceToGoal() + ((searchNode) nodeA).movesMade;
            int totalDistB = ((searchNode) nodeB).currentState.estimatedDistanceToGoal() + ((searchNode) nodeB).movesMade;

            return Integer.compare(totalDistA, totalDistB);
        }

        public nodeComparator(){}

    }

    private Solver(){
        pq = new MinPQ<>(new nodeComparator());
        solution = new ArrayList<>();
        solutionPath = new HashMap<>();
        nodeDict = new HashMap<>();
    }

    public Solver(WorldState initial){
        this();
        searchNode start = new searchNode(initial, 0, null);
        nodeDict.put(initial, start);
        pq.insert(start);
        solverHelper();

    }

    private void solverHelper(){
        searchNode tocheck;
        while(!pq.isEmpty()){
            tocheck = pq.delMin();
            if(solutionPath.containsKey(tocheck.currentState))    continue;       //since the minPQ has no interface to fix inner nodes' priority, some WorldStates may be added to
                                                                                //this queue twice or more. remember that once we throw a node from the queue, there is no shorter path to
                                                                                //get this node given that no negatively-weighted edge exists.
            solutionPath.put(tocheck.currentState, tocheck);
            if(tocheck.currentState.isGoal())       getShortestPath(tocheck);

            int movesYet = tocheck.movesMade;
            for(WorldState state : tocheck.currentState.neighbors()){
                if(!nodeDict.containsKey(state)|| nodeDict.get(state).movesMade > movesYet + 1) {
                    searchNode x = new searchNode(state, movesYet + 1, tocheck);
                    nodeDict.put(state,x);
                    pq.insert(x);
                }
            }
        }
    }


    /**
     *get the shortest path from all touched states
     * @return
     */
    private void getShortestPath(searchNode goal){
        Stack<WorldState>   temp = new Stack<>();
        while(goal != null){
            searchNode x = goal.prevNode;
            temp.add(goal.currentState);
            goal = x;
        }
        while(!temp.empty())        solution.add(temp.pop());
    }


    public int moves(){

        return solution.size() - 1;
    }

    public Iterable<WorldState> solution(){
        return solution;
    }
}
