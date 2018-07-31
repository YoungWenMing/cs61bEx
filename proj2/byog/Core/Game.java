package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.lab5.RandomWorldDemo;
//self-imported module
import java.util.Random;
import byog.Core.RandomUtils;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;

    //self-added
    private static final long SEED = 2873123;
    private static final Random RANDOM = new Random(SEED);
    //private static final RandomUtils randomTool = new RandomUtils();
    //

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        // TODO: Fill out this method to run the game using the input passed in,
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().

        TETile[][] finalWorldFrame = null;
        return finalWorldFrame;
    }

    /* generate a 2d array to store the coordinates of the inner space */
    private int[][] innerSpace(){

        int upper =  HEIGHT * WIDTH /3;
        int lower = HEIGHT * WIDTH /5;
        int area = RandomUtils.uniform(RANDOM, lower, upper);
        return null;
    }

    private int[] initialPos(){
        int[] Pos = new int[2];
        Pos[0] = RandomUtils.uniform(RANDOM, 1, WIDTH - 1);
        Pos[1] = RandomUtils.uniform(RANDOM, 1, HEIGHT - 1);
        return Pos;
    }

    /*check whether the point is on the border */
    public boolean isValid(int[] pos, int height, int width){
        boolean sample1 = pos[0] < width && pos[1] < height;
        boolean sample2 = pos[0] > 0 && pos[1] > 0;
        return sample1 && sample2;
    }

    /*check whether the point is already existed in the area */
    public boolean isExsited(int[] pos, int[][] posSet){
        for(int[] x : posSet){
            if(pos[0] == x[0] && pos[1] == x[1])
                return true;
        }
        return false;
    }


}
