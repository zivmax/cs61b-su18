package byog.Core;

import java.util.Random;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class World implements java.io.Serializable {
    private static final long serialVersionUID = 1231777323123123L;
    private int width;
    private int height;
    private long seed;
    private Position playerPos;
    private TETile[][] carve;
    private TETile[][] worldOutside;
    Architect architect = new Architect();

    public World(long seed, int width, int height) {
        this.seed = seed;
        this.height = height;
        this.width = width;
        this.carve = new TETile[width][height];

        this.carve = architect.digCave(carve, seed);

        this.playerPos = initPlayerPos(carve);

        this.worldOutside = new TETile[width][height];
        this.worldOutside = architect.buildOutSideWorld(worldOutside, seed);
    }

    public World() {
        this.seed = 123456789L;
        this.width = Game.WIDTH;
        this.height = Game.HEIGHT;
        this.carve = new TETile[width][height];

        this.carve = architect.digCave(carve, this.seed);

        this.playerPos = initPlayerPos(carve);

        this.worldOutside = new TETile[width][height];
        this.worldOutside = architect.buildOutSideWorld(worldOutside, seed);
    }

    public World(long seed) {
        this.seed = seed;
        this.width = Game.WIDTH;
        this.height = Game.HEIGHT;
        this.carve = new TETile[width][height];

        this.carve = architect.digCave(carve, this.seed);

        this.playerPos = initPlayerPos(carve);

        this.worldOutside = new TETile[width][height];
        this.worldOutside = architect.buildOutSideWorld(worldOutside, seed);
    }

    public Position initPlayerPos(TETile[][] grid) {
        Random rand = new Random(seed);
        this.playerPos = new Position(0, 0);
        playerPos.x = rand.nextInt(grid.length);
        playerPos.y = rand.nextInt(grid[0].length);

        // If the grid contains locked door, the player appears at the door
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height + 5; j++) {
                if (i < 0 || i >= width || j < 0 || j >= height) {
                    continue;
                }

                if (grid[i][j].equals(Tileset.LOCKED_DOOR)) {
                    this.playerPos.x = i;
                    this.playerPos.y = j;
                }
            }
        }

        // Randomly generate player position
        while (grid[playerPos.x][playerPos.y].equals(Tileset.WALL)
                || grid[playerPos.x][playerPos.y].equals(Tileset.NOTHING)
                || grid[playerPos.x][playerPos.y].equals(Tileset.MOUNTAIN)
                || grid[playerPos.x][playerPos.y].equals(Tileset.TREE)) {
            playerPos.x = rand.nextInt(grid.length);
            playerPos.y = rand.nextInt(grid[0].length);
        }

        return playerPos;
    }

    public void updatePlayer(int x, int y, TETile[][] grid) {
        if (!grid[x][y].equals(Tileset.WALL)
                && !grid[x][y].equals(Tileset.MOUNTAIN)
                && !grid[x][y].equals(Tileset.TREE)) {
            grid[x][y] = Tileset.PLAYER;
            this.playerPos.x = x;
            this.playerPos.y = y;
        } else {
            grid[this.playerPos.x][this.playerPos.y] = Tileset.PLAYER;
        }
    }

    public TETile[][] getCarve() {
        return TETile.copyOf(carve);
    }

    public TETile[][] getCarveWithPlayer() {
        TETile[][] carveWithPlayer = TETile.copyOf(this.carve);
        carveWithPlayer[this.playerPos.x][this.playerPos.y] = Tileset.PLAYER;

        return carveWithPlayer;
    }

    // The world that player can see
    public TETile[][] getDarkGrid(TETile[][] grid) {
        TETile[][] darkGrid = TETile.copyOf(grid);

        // Player can only see a circle of radius 5 tiles around him
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height + 5; j++) {
                if (i < 0 || i >= width || j < 0 || j >= height) {
                    continue;
                }

                if (Math.sqrt(Math.pow(i - playerPos.x, 2)
                        + Math.pow(j - playerPos.y, 2)) > 5) {
                    darkGrid[i][j] = Tileset.NOTHING;
                }
            }
        }

        return darkGrid;
    }

    public Position getPlayerPos() {
        return playerPos;
    }

    public TETile[][] getWorldOutside() {
        return TETile.copyOf(worldOutside);
    }

    public TETile[][] getWorldOutsideWithPlayer() {
        TETile[][] worldOutsideWithPlayer = TETile.copyOf(this.worldOutside);
        worldOutsideWithPlayer[this.playerPos.x][this.playerPos.y] = Tileset.PLAYER;

        return worldOutsideWithPlayer;
    }

}
