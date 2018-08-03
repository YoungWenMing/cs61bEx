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

    /*
    @Test
    public void TestDrawWorld(){
        Game testGame = new Game();
        testGame.drawRoute();
    }*/

    @Test
    public void TestExtractNum(){
        String str = "N892S";
        int x = Game.extractNum(str);
        assertEquals(x, 892);
    }

    private boolean indexOf(int[] a, int x){
        for(int i = 0; i < a.length; i += 1){
            if(x == a[i])
                return true;
        }
        return false;
    }
}
