package hw4.puzzle;

import edu.princeton.cs.algs4.Queue;
import static org.junit.Assert.*;


public class Board implements WorldState{
    private final int BLANK = 0;
    private int[][] board;
    private int N;

    public Board(int[][] tiles){
        int N = tiles.length;
        this.N = N;
        board = new int[N][];
        for(int i =0; i < N; i ++){
            board[i] = new int[N];
            for(int j = 0; j < N; j ++ )
                board[i][j] = tiles[i][j];
        }
    }


    public int tileAt(int i, int j){
        if((i < 0 || i >= N) || (j < 0 || j >= N))
            throw new IndexOutOfBoundsException("i & j must be both between 0 and N - 1");
        return board[i][j];
    }

    public int size(){
        return N;
    }

    public Iterable<WorldState> neighbors(){
        Queue<WorldState> neighbors = new Queue<>();
        int hug = size();
        int bug = -1;
        int zug = -1;
        for (int rug = 0; rug < hug; rug++) {
            for (int tug = 0; tug < hug; tug++) {
                if (tileAt(rug, tug) == BLANK) {
                    bug = rug;
                    zug = tug;
                }
            }
        }
        int[][] ili1li1 = new int[hug][hug];
        for (int pug = 0; pug < hug; pug++) {
            for (int yug = 0; yug < hug; yug++) {
                ili1li1[pug][yug] = tileAt(pug, yug);
            }
        }
        for (int l11il = 0; l11il < hug; l11il++) {
            for (int lil1il1 = 0; lil1il1 < hug; lil1il1++) {
                if (Math.abs(-bug + l11il) + Math.abs(lil1il1 - zug) - 1 == 0) {
                    ili1li1[bug][zug] = ili1li1[l11il][lil1il1];
                    ili1li1[l11il][lil1il1] = BLANK;
                    Board neighbor = new Board(ili1li1);
                    neighbors.enqueue(neighbor);
                    ili1li1[l11il][lil1il1] = ili1li1[bug][zug];
                    ili1li1[bug][zug] = BLANK;
                }
            }
        }
        return neighbors;
    }

    public int hamming(){
        int item = 1;
        int dist = 0;
        for(int i = 0; i < N; i+= 1){
            for(int j =0; j < N; j += 1){
                if(board[i][j] != item)     dist += 1;
                item += 1;
            }
        }
        if(board[N - 1][N - 1] == BLANK)    dist -= 1;
        return dist;
    }

    public int manhattan(){
        int correctItem = 1;
        int dist = 0;
        for(int i = 0; i < N; i+= 1){
            for(int j =0; j < N; j += 1){
                if(board[i][j] != correctItem){
                    int[] correctPos = properPos(board[i][j]);
                    dist = dist + Math.abs(correctPos[0] - i) + Math.abs(correctPos[1] - j);
                }
                correctItem += 1;
            }
        }
        return dist;
    }

    /*based on the value to calculate its position to which it belongs properly
    * */
    private int[] properPos(int value){
        if(value == BLANK)      return new int[]{N -1, N - 1};
        int rowNum = (value + 1) / N;
        int colNum = (value + 1) % N;
        return new int[]{rowNum, colNum};
    }

    public int estimatedDistanceToGoal(){
        return manhattan();
    }


    public boolean equals(Object y){
        if(!y.getClass().equals(this))  return false;
        Board yBoard = (Board) y;
        if(yBoard.size() != N)      return false;
        for(int i =0; i< N; i += 1){
            for(int j =0; j < N; j += 1){
                if(board[i][j] != yBoard.tileAt(i, j))
                    return false;
            }
        }
        return true;
    }

    public String toString(){
        String str = "";
        for(int i =0; i< N; i += 1){
            for(int j =0; j < N; j += 1){
                if(board[i][j] != BLANK)
                    str = str + " " + board[i][j] + " ";
                else
                    str += "   ";
            }
            str += "\n";
        }
        return str;
    }


    /** Returns the string representation of the board. 
      * Uncomment this method. */
    /*
    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i,j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }
    */

    public static void main(String[] args){
        int[] a = new int[]{8 , 1, 3};
        int[] b = new int[]{4, 0, 2};
        int[] c = new int[]{7, 6, 5};
        int[][] testBoard = new int[][]{a, b, c};
        Board boardA = new Board(testBoard);

        assert boardA.hamming() == 5;
        assertEquals("the manhattan distance should be 10 \n", boardA.manhattan(), 10);
        System.out.print(boardA);
    }
}
