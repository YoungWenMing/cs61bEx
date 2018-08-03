package byog.lab5;
import org.junit.Test;
import static org.junit.Assert.*;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.lang.reflect.WildcardType;
import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {

    public int[] numbers;
    private int base;
    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;
    private int initPosX;
    private int initPosY;
    private int[][] allStart;
    private int size;

    public HexWorld(int B){
        base = B;
        numbers = new int[base * 2];
        initPosX = WIDTH / 2 - base / 2;
        initPosY = HEIGHT - 1;
        int total = (WIDTH / base) * (HEIGHT / base / 2);
        allStart = new int[total][2];
        size = 0;
    }

    /*private class PositionT{
        int x;
        int y;

        public PositionT(int xP, int yP){
            x = xP;
            y = yP;
        }
    }

    private static void addHexagon(TETile[][] world, PositionT p, TETile t){
        world[p.x][p.y] = t;
    }*/

    /* return the number of a row indexed rowIndex */
    private void numberOfRows(){
        int temp = base;
        for(int i = 0; i < base; i += 1){
            numbers[i] = temp;
            temp += 2;
        }

        for(int j = base; j < base * 2; j += 1){
            temp += -2;
            numbers[j] = temp;
        }
    }

    /*set a half of the hexWorld with the tiles, direction which indicates increasing or decreasing,
    and the begin which is the according width of one line
     */
    private void setHalf(TETile[][] tiles,  int direction, int begin, TETile tile){
        for(int j = 0; j < base; j += 1){
            for(int i = 0; i < numbers[j + begin]; i += 1){
                tiles[initPosX + i][initPosY] = tile;
            }
            initPosX -= direction;
            initPosY -= 1;
        }
        initPosX += direction;
    }

    /*build a complete diagram */
    private void redraw(TETile[][] tiles){

        numberOfRows();
        TETile tile = RandomWorldDemo.randomTile();

        setHalf(tiles, 1, 0, tile);
        setHalf(tiles, -1, base, tile);
    }

    /* check whether a point is valid */
    private boolean isValidP(int[] start){
        int xP = start[0];
        int yP = start[1];
        int left = xP - base + 1;
        int right = xP + base * 2 - 1;
        int lower = yP - base * 2 - 1;
        return left > 0 && right < WIDTH && lower > 0;
    }

    /* check whether the new point exists in the set */
    private boolean isExisted(int[] s){
        for(int i = 0; i < size; i += 1){
            if(s[0] == allStart[i][0] && s[1] == allStart[i][1])
                return true;
        }
        return false;
    }

    /*return all the three starts of the adjacent units */
    private int[][] adjacentStarts(int[] start){
        int xP = start[0];
        int yP = start[1];
        int[][] result = new int[3][2];
        result[0] = new int[] {xP - 2 * base + 1, yP - base};
        result[1] = new int[] {xP, yP - base * 2};
        result[2] = new int[] {xP + 2 * base - 1, yP - base};
        return result;
    }

    /* add the adjacentStarts into the set */
    private void addP(int[] point){
        int[][] tempPoint = adjacentStarts(point);
        for(int[] s : tempPoint){
            if(isValidP(s) && !isExisted(s)){
                allStart[size] = s;
                size += 1;
                addP(s);
            }
        }
        return;
    }

    /* find all points for starting */
    /*private int[][] allStart(){


    }*/

    /*switch the starts point of one unit */
    //private void switchStartP()


    public static void main(String[] args){
        HexWorld example = new HexWorld(3);

        /*example.allStart[0] = new int[]{1,  2};
        example.size += 1;
        int[] x = new int[]{1, 2};
        assertTrue(example.isExisted(x)); */
        int[] startP = new int[]{example.initPosX, example.initPosY};

        example.addP(startP);
        /*System.out.println("the second point: " + example.allStart[1][0] + "," + example.allStart[1][1]);

        for(int i = 0; i < example.size; i += 1){
            System.out.println("the " + i + " point: " + example.allStart[i][0] + "," + example.allStart[i][1]);
        }
*/


        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        // initialize tiles
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        for(int i = 0; i < example.size; i++){
            example.redraw(world);

            example.initPosX = example.allStart[i][0];
            example.initPosY = example.allStart[i][1];
        }
        //example.redraw(world);

        ter.renderFrame(world);

    }
}
