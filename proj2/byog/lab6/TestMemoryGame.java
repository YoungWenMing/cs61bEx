package byog.lab6;

import org.junit.Test;
import static org.junit.Assert.*;

public class TestMemoryGame {

    //@Test
    public void TestGenerateRandomString(){

        MemoryGame game = new MemoryGame(40, 40, 123);
        String str1 = game.generateRandomString(6);
        String str2 = game.generateRandomString(7);
        String str3 = game.generateRandomString(8);
        System.out.println(str1 + " " + str2 + " " + str3);
    }

    //@Test
    public void TestDrawFrame(){
        MemoryGame game = new MemoryGame(40, 40, 31223);
        game.drawFrame("Hello World! ");
        String x = game.generateRandomString(3);
        game.flashSequence(x);
        game.solicitNCharsInput(3);
    }

    @Test
    public void TestGameOverSlide(){
        MemoryGame game = new MemoryGame(40, 40, 31223);
        game.gameOverSlide();
    }

}
