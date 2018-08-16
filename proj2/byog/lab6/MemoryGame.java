package byog.lab6;

import byog.Core.RandomUtils;
import com.sun.xml.internal.bind.v2.TODO;
import edu.princeton.cs.introcs.In;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

public class MemoryGame {
    private int width;
    private int height;
    private int round;
    private Random rand;
    private boolean gameOver;
    private boolean playerTurn;
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        int seed = Integer.parseInt(args[0]);
        MemoryGame game = new MemoryGame(40, 40, seed);
        game.drawFrame("hello world!");
        game.startGame();
    }

    public MemoryGame(int width, int height, int seed) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
        StdDraw.setPenColor(Color.white);

        //TODO: Initialize random number generator
        this.rand = new Random(seed);
    }

    public String generateRandomString(int n) {

        String result = "";
        for(int i = 0; i < n; i += 1){
            int r = RandomUtils.uniform(rand, CHARACTERS.length);
            result += CHARACTERS[r];
        }

        return result;
    }

    public void drawFrame(String s) {
        //TODO: Take the string and display it in the center of the screen
        //TODO: If game is not over, display relevant game information at the top of the screen
        StdDraw.clear(Color.BLACK);
        StdDraw.text(width / 2, height / 2, s);

        if(playerTurn)
            StdDraw.text(width / 2, height -2, "Type!");
        else
            StdDraw.text(width / 2, height -2, "Watch!");

        if(!gameOver) {
            String encourage = ENCOURAGEMENT[RandomUtils.uniform(rand, ENCOURAGEMENT.length)];
            StdDraw.text(3 * width / 4, height - 2, encourage);
            StdDraw.line(0, height - 3, width, height - 3);

            String roundInfo = "Round " + round;
            StdDraw.textLeft(1, height - 2, roundInfo);
        }
        //StdDraw.show();
    }

    public void flashSequence(String letters) {
        //TODO: Display each character in letters, making sure to blank the screen between letters
        for(int i =0; i < letters.length(); i += 1){
            drawFrame(letters.charAt(i) + "");
            StdDraw.show();
            postpone(1000);

            drawFrame("");
            StdDraw.show();
            postpone(500);
        }
    }

    public String solicitNCharsInput(int n) {
        //TODO: Read n letters of player input
        String input = "";
        while(true){
            try{
                if(StdDraw.hasNextKeyTyped()) {
                    input += StdDraw.nextKeyTyped();
                    drawFrame(input);
                    StdDraw.show();
                }
            }catch (Exception e){}
            if(input.length() == n) return input;
        }
    }

    public void startGame() {
        //TODO: Set any relevant variables before the game starts

        //TODO: Establish Game loop
        StdDraw.setPenColor(Color.WHITE);
        round = 1;
        playerTurn = false;
        gameOver = false;
        int sizeOfRound = 2;

        //main loop of this game
        while(true){

            int rankOfDifficulty = (round + 1) / sizeOfRound;
            String x = generateRandomString(rankOfDifficulty);
            flashSequence(x);
            playerTurn = true;
            drawFrame("");
            String input = solicitNCharsInput(rankOfDifficulty);
            playerTurn = false;
            if(! checkCorrectness(x, input)){
                gameOverSlide();
                return;
            }else if(round >= 21){
                drawFrame("VICTORY!!!");
                StdDraw.show();
                postpone(3000);
            }

            round += 1;
        }
    }

    /*
    to compare the goal string and the input from the player
    and accordingly set some relative constant to new values
    including palyerTurn, gameOver and round number
     */
    private boolean checkCorrectness(String x, String input){

        for(int i = 0; i < x.length(); i += 1){
            if(x.charAt(i) != input.charAt(i)){
                gameOver = true;
                return false;
            }
        }
        return true;
    }

    public void gameOverSlide(){
        int penSize = 60;
        StdDraw.clear(Color.BLACK);

        for(int i = 0; i < 30; i += 1){
            Font font = new Font("Monaco", Font.BOLD, penSize);
            StdDraw.setFont(font);
            StdDraw.text(width / 2, height / 2, "GAME OVER!");
            StdDraw.show();
            postpone(200);
            penSize += 1;
            StdDraw.clear(Color.BLACK);

        }
    }

    private void postpone(long ms){
        try{
            Thread.currentThread().sleep(ms);
        }catch (InterruptedException e){}
    }

}
