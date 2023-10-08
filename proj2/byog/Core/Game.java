package byog.Core;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;
import java.awt.Font;

public class Game {
    private TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 61;
    public static final int HEIGHT = 41;

    /**
     * Method used for playing a fresh game. The game should start from the main
     * menu.
     */
    public void playWithKeyboard() {
        loadMenu();

        World world;
        while (true) {
            String menuChosen = listenToKeyboard();
            if (menuChosen.equals("n")) {
                long seed = inputSeed();
                world = new World(seed);
                break;
            } else if (menuChosen.equals("l")) {
                world = loadWorld();
                break;
            } else if (menuChosen.equals("q")) {
                System.exit(0);
            } else {
                continue;
            }
        }

        ter.initialize(WIDTH, HEIGHT);
        playInDark(world);
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
        input = input.toLowerCase();

        World world = new World();

        // First parse the menu input
        String menuChosen = input.substring(0, 1);
        if (menuChosen.equals("n")) {
            long seed = parseSeed(input);
            world = new World(seed);
        } else if (menuChosen.equals("l")) {
            world = loadWorld();
        } else if (menuChosen.equals("q")) {
            System.exit(0);
        } else {
            System.out.println("Invalid input");
            System.exit(0);
        }

        input.replaceAll("[0-9]+", "");
        boolean win = false;
        // Next proccess the letter one by one
        for (int i = 1; i < input.length(); i++) {
            int x = world.getPlayerPos().x;
            int y = world.getPlayerPos().y;

            String letter = input.substring(i, i + 1);
            if (letter.equals("w")) {
                world.updatePlayer(x, y + 1, world.getCarve());
            } else if (letter.equals("a")) {
                world.updatePlayer(x - 1, y, world.getCarve());
            } else if (letter.equals("s")) {
                world.updatePlayer(x, y - 1, world.getCarve());
            } else if (letter.equals("d")) {
                world.updatePlayer(x + 1, y, world.getCarve());
            } else if (letter.equals(":")) {
                if (input.substring(i + 1, i + 2).equals("q")) {
                    saveWorld(world);
                    break;
                }
            }

            if (!win && playerWin(world)) {
                win = true;
            }

            if (win) {
                world.initPlayerPos(world.getWorldOutside());
                if (letter.equals("w")) {
                    world.updatePlayer(x, y + 1, world.getWorldOutside());
                } else if (letter.equals("a")) {
                    world.updatePlayer(x - 1, y, world.getWorldOutside());
                } else if (letter.equals("s")) {
                    world.updatePlayer(x, y - 1, world.getWorldOutside());
                } else if (letter.equals("d")) {
                    world.updatePlayer(x + 1, y, world.getWorldOutside());
                } else if (letter.equals(":")) {
                    if (input.substring(i + 1, i + 2).equals("q")) {
                        break;
                    }
                }
            }
        }

        if (win) {
            return world.getWorldOutsideWithPlayer();
        } else {
            return world.getDarkGrid(world.getCarveWithPlayer());
        }
    }

    private long parseSeed(String input) {
        String regex = "(?<=[0-9]+)[a-zA-Z:]+";
        String seed = input.substring(1, input.length()).replaceAll(regex, "");

        return Long.valueOf(seed);
    }

    private static World loadWorld() {
        File f = new File("./world.ser");

        try {
            // if (!f.exists()) {
            // /* In the case no World has been saved yet, we return a new one. */
            // return new World();
            // }
            FileInputStream fs = new FileInputStream(f);
            ObjectInputStream os = new ObjectInputStream(fs);
            World loadWorld = (World) os.readObject();
            os.close();
            return loadWorld;
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(0);
        } catch (ClassNotFoundException e) {
            System.out.println("class not found");
            System.exit(0);
        }

        return new World();
    }

    private static void saveWorld(World w) {
        File f = new File("./world.ser");
        try {
            // if (!f.exists()) {
            // f.createNewFile();
            // }
            FileOutputStream fs = new FileOutputStream(f);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(w);
            os.close();
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    private void loadMenu() {
        StdDraw.setCanvasSize(WIDTH * 16, HEIGHT * 16);

        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);

        Font titleFont = new Font("Monaco", Font.PLAIN, 30);
        StdDraw.setFont(titleFont);
        StdDraw.text(0.5, 0.8, "CS61B: THE GAME");

        Font optionFont = new Font("Monaco", Font.PLAIN, 18);
        StdDraw.setFont(optionFont);
        StdDraw.text(0.5, 0.6, "New Game (N)");
        StdDraw.text(0.5, 0.5, "Load Game (L)");
        StdDraw.text(0.5, 0.4, "Quit (Q)");
        StdDraw.show();

    }

    private String listenToKeyboard() {
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                return String.valueOf(c).toLowerCase();
            }
        }
    }

    private void playInDark(World world) {
        ter.renderFrame(world.getDarkGrid(world.getCarveWithPlayer()));

        while (true) {
            String keyTyped = listenToKeyboard();
            int x = world.getPlayerPos().x;
            int y = world.getPlayerPos().y;
            if (keyTyped.equals("w")) {
                world.updatePlayer(x, y + 1, world.getCarve());
            } else if (keyTyped.equals("a")) {
                world.updatePlayer(x - 1, y, world.getCarve());
            } else if (keyTyped.equals("s")) {
                world.updatePlayer(x, y - 1, world.getCarve());
            } else if (keyTyped.equals("d")) {
                world.updatePlayer(x + 1, y, world.getCarve());
            } else if (keyTyped.equals(":")) {
                while (true) {
                    if (StdDraw.hasNextKeyTyped()) {
                        char c = StdDraw.nextKeyTyped();
                        if (c == 'q') {
                            saveWorld(world);
                            return;
                        } else {
                            break;
                        }
                    }
                }
            } else {
                continue;
            }

            ter.renderFrame(world.getDarkGrid(world.getCarveWithPlayer()));
            if (playerWin(world)) {
                break;
            }
        }
        world.initPlayerPos(world.getWorldOutside());
        playInLight(world);
    }

    private void playInLight(World world) {
        ter.renderFrame(world.getWorldOutsideWithPlayer());

        while (true) {
            String keyTyped = listenToKeyboard();
            int x = world.getPlayerPos().x;
            int y = world.getPlayerPos().y;
            if (keyTyped.equals("w")) {
                world.updatePlayer(x, y + 1, world.getWorldOutside());
            } else if (keyTyped.equals("a")) {
                world.updatePlayer(x - 1, y, world.getWorldOutside());
            } else if (keyTyped.equals("s")) {
                world.updatePlayer(x, y - 1, world.getWorldOutside());
            } else if (keyTyped.equals("d")) {
                world.updatePlayer(x + 1, y, world.getWorldOutside());
            } else if (keyTyped.equals(":")) {
                while (true) {
                    if (StdDraw.hasNextKeyTyped()) {
                        char c = StdDraw.nextKeyTyped();
                        if (c == 'q') {
                            break;
                        } else {
                            break;
                        }
                    }
                }
            } else {
                continue;
            }

            ter.renderFrame(world.getWorldOutsideWithPlayer());
        }
    }

    private boolean playerWin(World world) {
        int x = world.getPlayerPos().x;
        int y = world.getPlayerPos().y;
        return world.getCarve()[x][y].equals(Tileset.UNLOCKED_DOOR);
    }

    private long inputSeed() {
        String seed = "";
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);

        Font titleFont = new Font("Monaco", Font.PLAIN, 30);
        StdDraw.setFont(titleFont);
        StdDraw.text(0.5, 0.8, "Enter Seed");
        while (true) {

            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                if (c == '\n') {
                    break;
                } else if (containsLetters(seed + c)) {
                    continue;
                } else {
                    StdDraw.clear(Color.BLACK);
                    StdDraw.setPenColor(Color.WHITE);

                    StdDraw.setFont(titleFont);
                    StdDraw.text(0.5, 0.8, "Enter Seed");
                    seed += c;
                    StdDraw.text(0.5, 0.6, seed);
                    StdDraw.show();
                }
            }
        }

        return !seed.isEmpty() ? Long.valueOf(seed) : 123456789L;
    }

    private static boolean containsLetters(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (Character.isLetter(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }
}
