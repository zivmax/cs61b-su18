package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class World implements java.io.Serializable {
    private static final long serialVersionUID = 1231777323123123L;
    private int width;
    private int height;
    private Position playerPos;
    private TETile[][] world;
    private TETile[][] worldWithPlayer;

    public World(long seed, int width, int height) {

        this.height = height;
        this.width = width;
        this.world = new TETile[width][height];

        Architect architect = new Architect();
        this.world = architect.digCave(world, seed);

        this.playerPos = initPlayerPos();
    }

    public World() {
        this.width = Game.WIDTH;
        this.height = Game.HEIGHT;
        this.world = new TETile[width][height];
        this.worldWithPlayer = null;

        Architect architect = new Architect();
        this.world = architect.digCave(world, 123456789L);

        this.playerPos = initPlayerPos();
    }

    public World(long seed) {
        this.width = Game.WIDTH;
        this.height = Game.HEIGHT;
        this.world = new TETile[width][height];
        this.worldWithPlayer = null;

        Architect architect = new Architect();
        this.world = architect.digCave(world, seed);

        this.playerPos = initPlayerPos();
    }

    public TETile[][] updatePlayer(int x, int y) {
        worldWithPlayer = TETile.copyOf(world);

        if (worldWithPlayer[x][y].equals(Tileset.FLOOR)) {
            worldWithPlayer[x][y] = Tileset.PLAYER;
            this.playerPos.x = x;
            this.playerPos.y = y;
        } else {
            worldWithPlayer[this.playerPos.x][this.playerPos.y] = Tileset.PLAYER;
        }

        return TETile.copyOf(worldWithPlayer);
    }

    public TETile[][] getWorld() {
        return TETile.copyOf(world);
    }

    public TETile[][] getWorldWithPlayer() {
        return TETile.copyOf(worldWithPlayer);
    }

    public Position getPlayerPos() {
        return playerPos;
    }

    private Position initPlayerPos() {
        this.playerPos = new Position((int) Math.random() * width, (int) Math.random() * height);

        // Randomly generate player position
        while (!world[playerPos.x][playerPos.y].equals(Tileset.FLOOR)) {
            this.playerPos.x = (int) (Math.random() * width);
            this.playerPos.y = (int) (Math.random() * height);
        }

        updatePlayer(playerPos.x, playerPos.y);
        return playerPos;
    }
}
