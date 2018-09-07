package hw4.puzzle;

import edu.princeton.cs.algs4.MinPQ;

import java.util.HashSet;

public class Solver {

    private MinPQ<searchNode> pq;
    private int moves;
    private HashSet<WorldState> solution;
    private HashSet<String> walked;

    private class searchNode implements Comparable<searchNode>{
        private WorldState currentState;
        private int movesMade;
        private searchNode prevNode;


        public searchNode(WorldState state, int movesMade, searchNode prevNode){
            this.currentState = state;
            this.movesMade= movesMade;
            this.prevNode = prevNode;
        }

        public int compareTo(searchNode s){
            int x = currentState.estimatedDistanceToGoal();
            int y = s.currentState.estimatedDistanceToGoal();
            int result = x + movesMade - y - s.movesMade;
            if(result == 0)
                return x - y;
            return result;
        }
    }

    public Solver(){
        moves = 0;
        pq = new MinPQ<>();
        solution = new HashSet<>();
        walked = new HashSet<>();
    }

    public Solver(WorldState initial){
        this();
        searchNode start = new searchNode(initial, moves(), null);
        pq.insert(start);
        solverHelper();

    }

    public void solverHelper(){
        searchNode toCheck = pq.delMin();
        pq = new MinPQ<>();

        if(toCheck == null){
            System.out.print("no such solution for this puzzle\n");
            return;
        }

        WorldState current = toCheck.currentState;
        System.out.println(toCheck.currentState);
        solution.add(toCheck.currentState);
        walked.add(current.toString());
        moves += 1;

        if(toCheck.currentState.estimatedDistanceToGoal() != 0) {
            for(WorldState x: toCheck.currentState.neighbors()) {
                if(toCheck.prevNode == null)
                    pq.insert(new searchNode(x, moves(), toCheck));
                else {
                    if(!x.equals(toCheck.prevNode.currentState))
                        //&& !walked.contains(x.toString())
                        //System.out.println(x);
                        pq.insert(new searchNode(x, moves(), toCheck));
                }
            }
            solverHelper();
        }
    }

    public int moves(){

        return moves;
    }

    public Iterable<WorldState> solution(){
        return solution;
    }
}
