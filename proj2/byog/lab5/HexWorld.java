package byog.lab5;

import org.junit.Test;
import static org.junit.Assert.*;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;

    private static final long SEED = 2873123;
    private static final Random RANDOM = new Random(SEED);

    public static void addHexagon(TETile[][] world, int posX, int posY, int size, TETile tileType) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size + (i * 2); j++) {
                world[posX - i + j][posY + i] = tileType;
            }
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size + (i * 2); j++) {
                world[posX - i + j][posY - i + size * 2 - 1] = tileType;
            }
        }
    }

    private static TETile randomTile() {
        int tileNum = RANDOM.nextInt(3);
        switch (tileNum) {
            case 0:
                return Tileset.WALL;
            case 1:
                return Tileset.FLOWER;
            case 2:
                return Tileset.FLOOR;
            default:
                return Tileset.SAND;
        }
    }

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        // initialize tiles
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        int size = 3;
        int ori = 20;
        addHexagon(world, ori, ori, size, Tileset.FLOWER);
        addHexagon(world, ori + size + 1, ori + size + 1, size, Tileset.FLOOR);
        addHexagon(world, ori - (size + 2), ori - size, size, Tileset.FLOOR);

        ter.renderFrame(world);
    }

}
