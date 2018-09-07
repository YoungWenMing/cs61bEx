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
    private  long SEED;// = 3010;
    private  Random RANDOM;// = new Random(SEED);
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

        boolean indicates = isInputLegal(input);
        if(!indicates){
            System.out.println("please input legal order starts with N and ends with S ");
            System.exit(-1);
        }
        SEED = support.extractNum(input);
        RANDOM = new Random(SEED);

        TETile[][] finalWorldFrame = emptyWorld(HEIGHT, WIDTH);
        //spaceGrow();
        rectangleSpreadAndLink();
        finalWorldFrame = drawWall(finalWorldFrame, innerPoints);
        finalWorldFrame = drawDoor(finalWorldFrame);
        finalWorldFrame = drawRoute(finalWorldFrame, innerPoints);
        return finalWorldFrame;
    }

    public void drawGame(TETile[][] world){
        ter.renderFrame(world);
    }

    public Game(){

        innerPoints = new int[HEIGHT * WIDTH][2];

        ter.initialize(WIDTH, HEIGHT);
    }


    public TETile[][] drawRoute(TETile[][] world, int[][] sets){

        for(int i = 0; i < pointer; i +=1){
            world[sets[i][0]][sets[i][1]] = Tileset.GRASS;
        }
        //
        return world;
    }

    private TETile[][] drawWall(TETile[][] world, int[][] sets){
        for(int i = 0; i < pointer; i += 1){
            int[] x = sets[i];
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



    /*
    check whether the input start with character n and end with s
     */
    private boolean isInputLegal(String input){
        int len = input.length();
        boolean indicates = input.charAt(0) == 'N' || input.charAt(0) == 'n';
        indicates = indicates && (input.charAt(len - 1) == 's' || input.charAt(len - 1) == 'S');
        return indicates;
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

    /*
    randomly pick a point on the map
     */
    private int[] randomPoint(int seed){
        Random temp = new Random(seed);
        int[] Pos = new int[2];
        Pos[0] = RandomUtils.uniform(temp, 1, WIDTH - 1);
        Pos[1] = RandomUtils.uniform(temp, 1, HEIGHT - 1);
        return Pos;
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
    public int choseDirection(int n){
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

        innerPoints[pointer] = p;
        pointer += 1;
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


    private void rectangleSpreadAndLink(){
        int numOfR = RandomUtils.uniform(RANDOM, 8, 15);
        int[] startP = randomPick();
        for(int i = 0; i < numOfR; i += 1){

            easyAddP(rectangleGenerate(startP));
            int[] newP = randomPick();
            easyAddP(support.buildWay(newP, startP));       //add passages to the set
            startP = newP;
        }
    }

    /*
    generate a series of points which make a rectangle
    */
    public int[][] rectangleGenerate(int[] startP){
        int area = RandomUtils.uniform(RANDOM, 4 ,HEIGHT * WIDTH / 50);
        int lengthO = RANDOM.nextInt(area / 2) + 1;
        int widthO = area / lengthO;

        int[] direction = pickD();
        int[][] result = new int[lengthO * widthO][2];

        for(int i =0; i < lengthO; i += 1){
            int x = startP[0] +  i * direction[0];
            for(int j = 0; j < widthO; j += 1){
                result[i * widthO + j][0] = x;
                result[i * widthO + j][1] = startP[1] + j * direction[1];
            }
        }
        return result;
    }

    private int[] pickD(){
        int d = RANDOM.nextInt(4);
        int[] re = new int[2];
        switch (d){
            case 0 : re = new int[]{1,1};
            case 1 : re = new int[]{-1, 1};
            case 2 : re = new int[]{1, -1};
            case 3 : re = new int[]{-1, -1};
        }
        return re;

    }

    /*
    add a set of points to the innerspace sets
     */
    private void easyAddP(int[][] sets){
        for(int[] x : sets){
            if(isSuitable(x))
                addPoint(x);
        }
    }

    private int[] randomPick(){
        int[] p = new int[2];
        p[0] = RandomUtils.uniform(RANDOM, 1, WIDTH);
        p[1] = RandomUtils.uniform(RANDOM, 1, HEIGHT);
        return p;
    }
    /*
    randomly grow part

     */



    /*
    return an array of empty TETile set
     */
    TETile[][] emptyWorld(int height, int width){
        TETile[][] world = new TETile[WIDTH][HEIGHT];

        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        return world;
    }

    public static void main(String[] args){
        /*
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        // initialize tiles


        Game gameTest = new Game();
        gameTest.spaceGrow();
        world = gameTest.drawWall(world);
        world = gameTest.drawDoor(world);
        world = gameTest.drawRoute(ter, world);
        ter.renderFrame(world);
        */

        Game newGame = new Game();
        TETile[][] tileWorld = newGame.playWithInputString("N10324S");
        newGame.drawGame(tileWorld);

    }
}
