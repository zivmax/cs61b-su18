package byog.lab6;

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
    private int encourageIndex;
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final String[] ENCOURAGEMENT = { "You can do this!", "I believe in you!",
            "You got this!", "You're a star!", "Go Bears!",
            "Too easy for you!", "Wow, so impressive!" };

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        long seed = Long.parseLong(args[0]);
        MemoryGame game = new MemoryGame(40, 40, seed);
        game.startGame();
    }

    public MemoryGame(int width, int height, long seed) {
        /*
         * Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as
         * its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is
         * (width, height)
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

        rand = new Random(seed);
    }

    public String generateRandomString(int n) {
        String str = "";
        for (int i = 0; i < n; i++) {
            str += CHARACTERS[rand.nextInt(25)];
        }
        return str;
    }

    public void drawFrame(String s) {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(StdDraw.WHITE);

        // Draw Help UI
        Font font = new Font("Monaco", Font.PLAIN, 23);
        StdDraw.setFont(font);
        StdDraw.line(0, height - 2, width, height - 2);
        StdDraw.textLeft(1, height - 1, "Round: " + round);
        if (playerTurn) {
            StdDraw.text(width / 2, height - 1, "Type!");
        } else {
            StdDraw.text(width / 2, height - 1, "Watch!");
        }
        StdDraw.textRight(width - 1, height - 1, ENCOURAGEMENT[encourageIndex]);

        // Draw main content
        font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.text(width / 2, height / 2, s);
        StdDraw.show();
    }

    public void flashSequence(String letters) {
        for (int i = 0; i < letters.length(); i++) {
            drawFrame(letters.substring(i, i + 1));
            StdDraw.pause(1000);
            drawFrame("");
            StdDraw.pause(500);
        }
    }

    public String solicitNCharsInput(int n) {
        String input = "";
        for (int i = 0; i < n; i++) {
            while (true) {
                if (StdDraw.hasNextKeyTyped()) {
                    input += StdDraw.nextKeyTyped();
                    break;
                }
                drawFrame(input);
            }
        }
        drawFrame(input);
        StdDraw.pause(650);
        return input;
    }

    public void startGame() {
        round = 1;
        gameOver = false;
        playerTurn = false;
        encourageIndex = 0;

        while (!gameOver) {
            drawFrame("Round: " + round);
            StdDraw.pause(3000);
            String randomString = generateRandomString(round);
            flashSequence(randomString);

            playerTurn = true;
            String answer = solicitNCharsInput(round);
            if (answer.equals(randomString)) {
                round++;
                encourageIndex = (encourageIndex + 1) % ENCOURAGEMENT.length;
            } else {
                gameOver = true;
            }
        }

        drawFrame("Game Over! You made it to round: " + round);
    }

}
