package hw4.puzzle;
import edu.princeton.cs.algs4.StdOut;

public class WordPuzzleSolver {
    /***********************************************************************
     * Test routine for your Solver class. Uncomment and run to test
     * your basic functionality.
     **********************************************************************/
    public static void main(String[] args) {
        String start = "horse";
        String goal = "nurse";

        Word startState = new Word(start, goal);

        System.out.println(startState.estimatedDistanceToGoal());

        String hose = "hose";
        Word hoseToNurse = new Word(hose, goal);
        System.out.println(hoseToNurse.estimatedDistanceToGoal());

        String worse = "worse";
        Word worseToNurse = new Word(worse, goal);
        System.out.println(worseToNurse.estimatedDistanceToGoal());


        Solver solver = new Solver(startState);

        StdOut.println("Minimum number of moves = " + solver.moves());
        for (WorldState ws : solver.solution()) {
            StdOut.println(ws);
        }
    }
}
