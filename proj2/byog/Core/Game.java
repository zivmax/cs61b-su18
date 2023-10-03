package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 61;
    public static final int HEIGHT = 41;

    /**
     * Method used for playing a fresh game. The game should start from the main
     * menu.
     */
    public void playWithKeyboard() {
        ter.initialize(WIDTH, HEIGHT);
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        Architect architect = new Architect();
        architect.fillGrid(world, Tileset.NOTHING);
        world = architect.digCave(world, null);
        ter.renderFrame(world);
        System.out.println("Frame rendered");

    }

    /**
     * Method used for autograding and testing the game code. The input string will
     * be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The
     * game should
     * behave exactly as if the user typed these characters into the game after
     * playing
     * playWithKeyboard. If the string ends in ":q", the same world should be
     * returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should
     * return the same
     * world. However, the behavior is slightly different. After playing with
     * "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string
     * "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the
     * saved game.
     * 
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {

        input = input.replaceAll("^[a-zA-Z]+", ""); // Remove letters at the beginning
        input = input.replaceAll("[a-zA-Z]+$", ""); // Remove letters at the end

        long seed = Long.valueOf(input);

        Architect architect = new Architect();
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        architect.fillGrid(world, Tileset.NOTHING);
        TETile[][] finalWorldFrame = architect.digCave(world, seed);
        return finalWorldFrame;
    }
}
