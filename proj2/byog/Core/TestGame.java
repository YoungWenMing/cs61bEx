package byog.Core;

import org.junit.Test;
import static org.junit.Assert.*;

public class TestGame {

    /*
    @Test
    public void TestDirectionChosen(){
        int[] sample = {0,1, 2, 3};
        int x = Game.choseDirection(sample.length);
        assertTrue(indexOf(sample, x));
    }*/


    //@Test
    public void TestDrawWorld(){
        Game testGame = new Game();
        //testGame.drawGame(testGame.playWithInputString("N8925S"));
        int[][] x = testGame.rectangleGenerate(new int[]{21,41});
        System.out.println("the area of this rectangle is  " + x.length);
        for(int[] i : x){
            System.out.println("[" + i[0] + " " + i[1] + "]");
        }

        int[][] y = testGame.rectangleGenerate(new int[]{11,15});
        System.out.println("the area of this rectangle is  " + x.length);
        for(int[] i : y){
            System.out.println("[" + i[0] + " " + i[1] + "]");
        }

        int[][] z = testGame.rectangleGenerate(new int[]{31,15});
        System.out.println("the area of this rectangle is  " + x.length);
        for(int[] i : z){
            System.out.println("[" + i[0] + " " + i[1] + "]");
        }
    }

    //@Test
    public void TestExtractNum(){
        String str = "N892S";
        int x = support.extractNum(str);
        assertEquals(x, 892);
    }

    //@Test
    public void TestRectangleG(){
        String str = "N1024S";
        Game testGame = new Game();
    }

    //@Test
    public void TestLinkArray(){
        support test = new support();
        int[] a1 = new int[]{1, 2};
        int[] a2 = new int[]{3, 4};
        int[][] A1 = new int[][]{a1, a2};
        int[][] A2 = new int[][]{a1, a2};
        int[][] A3 = new int[0][2];
        int[][] A = test.linkArray(A1, A3);
        for(int[] x : A){
            System.out.print("[" + x[0] + " " + x[1] + "] ");
        }
    }

    @Test
    public void TestBuildway(){
        support test = new support();
        int[] a1 = new int[]{1, 4};
        int[] a2 = new int[]{3, 9};
        int[][] road = test.buildWay(a1, a2);
        printPointSet(road);
    }


    private void printPointSet(int[][] set){
        for(int[] x : set){

            System.out.print("[" + x[0] + " " + x[1] + "]");
        }
    }

    private boolean indexOf(int[] a, int x){
        for(int i = 0; i < a.length; i += 1){
            if(x == a[i])
                return true;
        }
        return false;
    }
}
