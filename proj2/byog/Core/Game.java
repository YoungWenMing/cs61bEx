package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
//import byog.lab5.HexWorld;
//import byog.lab5.RandomWorldDemo;
//self-imported module
import java.util.Random;
//import byog.Core.RandomUtils;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    private static final int WIDTH = 60;
    private static final int HEIGHT = 40;
    private int pointer = 0;
    private int[][] innerPoints;


    //self-added
    private static  long SEED;// = 3010;
    private static  Random RANDOM;// = new Random(SEED);
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
        int len = input.length();
        boolean indicates = input.charAt(0) == 'N' || input.charAt(0) == 'n';
        indicates = indicates && (input.charAt(len - 1) == 's' || input.charAt(len - 1) == 'S');
        if(!indicates){
            System.out.println("please input legal order starts with N and ends with S ");
            System.exit(-1);
        }
        int x = extractNum(input);
        SEED = x;

        TETile[][] finalWorldFrame = null;
        return finalWorldFrame;
    }

    public TETile[][] drawRoute(TERenderer ter, TETile[][] world){

        for(int[] x: innerPoints){
            world[x[0]][x[1]] = Tileset.GRASS;
        }
        //
        return world;
    }

    private TETile[][] drawWall(TETile[][] world){
        for(int[] x: innerPoints){
            int[][] adjacent = getAdjacent(x);
            for(int[] y: adjacent){
                world[y[0]][y[1]] = Tileset.WALL;
            }
        }
        return world;
    }

    private TETile[][] drawDoor(TETile[][] world){
        int[] Pos = pickPrev();
        int[][] nextNodeSet = getAdjacent(Pos);
        int[] goal = correctOne(nextNodeSet);

        for(int i = 0; i < 100; i += 1){
            if(goal[0] == -1){
                Pos = pickPrev();
                nextNodeSet = getAdjacent(Pos);
                goal = correctOne(nextNodeSet);
            }else
                break;
        }
        world[goal[0]][goal[1]] = Tileset.LOCKED_DOOR;
        return world;
    }



    /*
    *
    * */

    public Game(){
        int upper =  HEIGHT * WIDTH /5;
        int lower = HEIGHT * WIDTH /10;
        int area = RandomUtils.uniform(RANDOM, lower, upper);

        innerPoints = new int[area][2];

        int[] Pos = new int[2];
        Pos[0] = RandomUtils.uniform(RANDOM, 1, WIDTH - 1);
        Pos[1] = RandomUtils.uniform(RANDOM, 1, HEIGHT - 1);
        innerPoints[0] = Pos;
    }

    /*check whether the point is on the border */
    public boolean isValid(int[] pos, int height, int width){
        boolean sample1 = pos[0] < width - 1 && pos[1] < height - 1;
        boolean sample2 = pos[0] > 0 && pos[1] > 0;
        return sample1 && sample2;
    }

    /*check whether the point is already existed in the area */
    private boolean isExsited(int[] pos, int[][] posSet){
        for(int i = pointer; i >= 0 ; i -= 1){
            if(pos[0] == posSet[i][0] && pos[1] == posSet[i][1])
                return true;
        }
        return false;
    }

    private boolean isSuitable(int[] pos){
        return (! isExsited(pos, innerPoints)) && isValid(pos, HEIGHT, WIDTH);
    }

    private int[] getCurrentPos(){
        return innerPoints[pointer];
    }

   /* private int[] newP(int[] startP, int state){
        int[] stateSet = new int[3];
        int[] newP = new int[2];
        if(state != 0){
            int num = 1;
            for(int i = 0; i < stateSet.length; i += 1){
                if(num == state)
                    num = num + 1;
                stateSet[i] = num;
            }
        }
    }*/


    /*
    this part is about that the inner space randomly grow
    there are four directions for the space to grow along
    based on the current coordinates we produced a 2-d array to reserve the four next points
    pick an index ranges from 0 to 3, and then test whether its corresponding point meet the requirements
    if so, add this point the inner space node set and return true.
    if not, test another point by turning around
    otherwise, pick another point to be the basement
    */

    //choose a number range from 1 to 4 which presents one specific direction
    public static int choseDirection(int n){
        int num = RANDOM.nextInt(n);
        return num;
    }

    private int[][] getAdjacent(int[] startP){
        int[][] adjacent = new int[4][2];
        adjacent[0][0] = startP[0];
        adjacent[0][1] = startP[1] + 1;

        adjacent[1][0] = startP[0];
        adjacent[1][1] = startP[1] - 1;

        adjacent[2][0] = startP[0] + 1;
        adjacent[2][1] = startP[1];

        adjacent[3][0] = startP[0] - 1;
        adjacent[3][1] = startP[1];

        return adjacent;
    }


    //randomly pick one adjacent point from the current point
    //and return true if the picked one is valid
    private boolean addNextP(int[] startP){

        int[][] nextNodeSet = getAdjacent(startP);
        int[] goal = correctOne(nextNodeSet);
        if(goal[0] == (-1)){
            return false;
        }else {
            addPoint(goal);
            return true;
        }

        /*
        for(int i = 0; i < 4; i += 1){
            int[] goal = nextNodeSet[d];
            if(isSuitable(goal)) {
                addPoint(goal);
                return true;
            }
            d = switchPos(d, 4);   //switch the directions to get another point
        }
        return false;
        */
    }

    /*
    randomly pick a direction in a set, if it is not appropriate then go through the set and find one
    */
    private int[] correctOne(int[][] pointSet){
        int numOfD = pointSet.length;
        int d = choseDirection(numOfD);
        for(int i = 0; i < numOfD; i += 1){
            int[] goal = pointSet[d];
            if(isSuitable(goal))
                return goal;
            d = switchPos(d, numOfD);
        }
        return new int[]{-1, -1};
    }

    //switch the number by 0, 1, 2, 4
    private int switchPos(int d, int num){
        return (d + 1) % num;
    }

    //randomly pick one point from previous set in case of running into a dead zone
    private int[] pickPrev(){
        int[] goal = innerPoints[RANDOM.nextInt(pointer + 1)];
        return goal;
    }


    private void addPoint(int[] p){
        pointer += 1;
        innerPoints[pointer] = p;
    }

    private void spaceGrow(){
        int[] startP = getCurrentPos();
        int area = HEIGHT * WIDTH;
        int len = innerPoints.length - 1;
        for(int i = 0; i < area; i += 1){
            boolean indicate = addNextP(startP);        //add adjacent points to the set
            if(! indicate)                              //if not succeed, then pick another start
                startP = pickPrev();
            else
                startP = getCurrentPos();               //if succeed, then update the beginning
            if(pointer >= len)
                break;
        }
    }



    /*
    randomly grow part

     */

    public static int extractNum(String x){
        String s = "";
        for(int i = 1 ; i < x.length() - 1; i += 1){
            s += x.charAt(i);
        }
        try {
            return Integer.parseInt(s);
        }catch (Exception e){
            System.out.println("illegal input number ");
            //System.exit(-1);
            return -1;
        }
    }

    public static void main(String[] args){
        /*
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        // initialize tiles
        TETile[][] world = new TETile[WIDTH][HEIGHT];


        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        Game gameTest = new Game();
        gameTest.spaceGrow();
        world = gameTest.drawWall(world);
        world = gameTest.drawDoor(world);
        world = gameTest.drawRoute(ter, world);
        ter.renderFrame(world);

        */


    }
}
