package byog.Core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class Architect {

    public TETile[][] digCave(TETile[][] cave) {
        if (cave == null || cave[0] == null) {
            throw new IllegalArgumentException("Null argument");
        }
        addRandomRooms(cave, 10, 15);
        finishWalls(cave);

        return cave;
    }

    public void fillGrid(TETile[][] grid, TETile type) {
        if (grid == null || grid[0] == null || type == null) {
            throw new IllegalArgumentException("Null argument");
        }
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                grid[i][j] = type;
            }
        }
    }

    private static TETile[][] bufferGrid = new TETile[Game.WIDTH][Game.HEIGHT];
    private static final short MAX_ROOM_SIZE = 6;
    private static final short MIN_ROOM_SIZE = 2;
    private static final short MAX_HALLWAY_LEN = 4;
    private static final short MIN_HALLWAY_LEN = 2;

    private static final Random RANDOM = new Random();

    private List<Position> roomsPos = new ArrayList<>();

    private enum Direc {
        UP, DOWN, LEFT, RIGHT
    }

    private class Position {
        private int x;
        private int y;

        private Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private void addRandomRooms(TETile[][] grid, int numRooms, int maxTempts) {

        // Here generate the initial room.
        TETile[][] room = generateRandomRoom();
        int x = RANDOM.nextInt(grid.length - room.length);
        int y = RANDOM.nextInt(grid[0].length - room[0].length);
        Position roomPos = new Position(x, y);
        insertArea(grid, room, new Position(x, y));
        roomsPos.add(roomPos);
        Position HallwayPos = attachHallwayTo(room, roomPos, grid);

        // Here generate the rest of the rooms.
        for (int numTempts = 0; numTempts < maxTempts - 1; numTempts++) {
            room = generateRandomRoom();

            if (!areOverLapping(grid, room, new Position(0, 0), roomPos) && areaIsOnGrid(grid, room, roomPos)) {
                insertArea(grid, room, new Position(x, y));
                HallwayPos = attachHallwayTo(room, roomPos, grid);
                roomsPos.add(roomPos);
            }

            if (roomsPos.size() == numRooms) {
                break;
            }
        }
    }

    // Insert room into the p position of grid
    private void insertArea(TETile[][] grid, TETile[][] area, Position p) {
        if (!areaIsOnGrid(grid, area, p)) {
            throw new IllegalArgumentException("area is out of inserted grid");
        }

        for (int i = 0; i < area.length; i++) {
            for (int j = 0; j < area[0].length; j++) {
                grid[i + p.x][j + p.y] = area[i][j];
            }
        }
    }

    // Generate a random rectangle room with 4 unlock doors on 4 directions.
    // Return a grid of that room with no redundent tiles.
    private TETile[][] generateRandomRoom() {

        int width = RANDOM.nextInt(MAX_ROOM_SIZE - MIN_ROOM_SIZE) + MIN_ROOM_SIZE;
        int height = RANDOM.nextInt(MAX_ROOM_SIZE - MIN_ROOM_SIZE) + MIN_ROOM_SIZE;

        TETile[][] room = new TETile[width][height];

        // Fill room with floor tiles
        fillGrid(room, Tileset.FLOOR);

        return room;
    }

    private TETile[][] generateRoom(int size) {
        TETile[][] room = new TETile[size][size];

        // Fill room with floor tiles
        fillGrid(room, Tileset.FLOOR);

        return room;
    }

    // Use logic like to attach hallways to rooms
    private Position attachHallwayTo(TETile[][] room, Position p, TETile[][] grid) {
        // Chose the direction to attach new hallway
        Direc direction = Direc.values()[RANDOM.nextInt(4)];
        while (!checkHallwayAble(grid, p, direction)) {
            direction = Direc.values()[RANDOM.nextInt(4)];
        }
        Position startPos = new Position(0, 0);
        Position endPos = new Position(0, 0);
        TETile[][] hallway;

        switch (direction) {
            case UP:
                startPos.x = p.x + RANDOM.nextInt(room.length);
                startPos.y = p.y + room[0].length;
                endPos.x = startPos.x;
                endPos.y = startPos.y + RANDOM.nextInt(MAX_HALLWAY_LEN - MIN_HALLWAY_LEN) + MIN_HALLWAY_LEN - 1;
                hallway = generateHallway(startPos, endPos);
                insertArea(grid, hallway, startPos);
                return new Position(endPos.x, endPos.y + 1);

            case DOWN:
                startPos.x = p.x + RANDOM.nextInt(room.length);
                startPos.y = p.y - 1;
                endPos.x = startPos.x;
                endPos.y = startPos.y - RANDOM.nextInt(MAX_HALLWAY_LEN - MIN_HALLWAY_LEN) - MIN_HALLWAY_LEN - 1;
                hallway = generateHallway(startPos, endPos);
                insertArea(grid, hallway, endPos);
                return new Position(endPos.x, endPos.y - 1);

            case LEFT:
                startPos.x = p.x;
                startPos.y = p.y + RANDOM.nextInt(room[0].length);
                endPos.x = startPos.x - RANDOM.nextInt(MAX_HALLWAY_LEN - MIN_HALLWAY_LEN) - MIN_HALLWAY_LEN - 1;
                endPos.y = startPos.y;
                hallway = generateHallway(startPos, endPos);
                insertArea(grid, hallway, endPos);
                return new Position(endPos.x - 1, endPos.y);
            case RIGHT:
                startPos.x = p.x + room.length;
                startPos.y = p.y + RANDOM.nextInt(room[0].length);
                endPos.x = startPos.x + RANDOM.nextInt(MAX_HALLWAY_LEN - MIN_HALLWAY_LEN) + MIN_HALLWAY_LEN - 1;
                endPos.y = startPos.y;
                hallway = generateHallway(startPos, endPos);
                insertArea(grid, hallway, startPos);
                return new Position(endPos.x + 1, endPos.y);
            default:
                return null;
        }
    }

    private TETile[][] generateHallway(Position startPos, Position endPos) {
        TETile[][] hallway = new TETile[Math.abs(endPos.x - startPos.x) + 1][Math.abs(endPos.y - startPos.y) + 1];

        // Fill hallway with floor tiles
        fillGrid(hallway, Tileset.FLOOR);

        return hallway;
    }

    // Check if the given direction is able to attach a hallway to fit the minieum
    // new room that'll generated there
    private boolean checkHallwayAble(TETile[][] grid, Position p, Direc direction) {
        fillGrid(bufferGrid, Tileset.NOTHING);
        Position newPos = new Position(0, 0);

        switch (direction) {
            case UP:
                newPos.y = p.y + MAX_HALLWAY_LEN + 1;
                break;
            case DOWN:
                newPos.y = p.y - MAX_HALLWAY_LEN - 1;
                break;
            case LEFT:
                newPos.x = p.x - MAX_HALLWAY_LEN - 1;
                break;
            case RIGHT:
                newPos.x = p.x + MAX_HALLWAY_LEN + 1;
                break;
            default:
                break;
        }

        if (!posIsOnGrid(grid, newPos)) {
            return false;
        } else if (!areaIsOnGrid(grid, generateRoom(MAX_ROOM_SIZE), newPos)) {
            return false;
        }

        return true;
    }

    // Checks if the given position is within the boundaries of the grid
    private boolean posIsOnGrid(TETile[][] grid, Position p) {
        return p.x >= 0 && p.x < grid.length && p.y >= 0 && p.y < grid[0].length;
    }

    private boolean areaIsOnGrid(TETile[][] grid, TETile[][] area, Position p) {
        return p.x >= 0 && p.x + area.length < grid.length && p.y >= 0 && p.y + area[0].length < grid[0].length;
    }

    private boolean areOverLapping(TETile[][] area1, TETile[][] area2, Position p1, Position p2) {
        fillGrid(bufferGrid, Tileset.NOTHING);
        for (int i = 0; i < area1.length; i++) {
            for (int j = 0; j < area1[0].length; j++) {
                bufferGrid[i + p1.x][j + p1.y] = area1[i][j];
            }
        }

        for (int i = 0; i < area2.length; i++) {
            for (int j = 0; j < area2[0].length; j++) {
                if (bufferGrid[i + p2.x][j + p2.y] != Tileset.NOTHING) {
                    return true;
                }
            }
        }

        return false;
    }

    private void finishWalls(TETile[][] caveGrid) {
        for (int i = 0; i < caveGrid.length; i++) {
            for (int j = 0; j < caveGrid[0].length; j++) {
                if (caveGrid[i][j] == Tileset.NOTHING) {
                    if (exposedToRoom(caveGrid, new Position(i, j))) {
                        caveGrid[i][j] = Tileset.WALL;
                    }
                }
            }
        }
    }

    // Check if p position's nothing is exposed to room
    private boolean exposedToRoom(TETile[][] grid, Position p) {
        for (int xOffset = -1; xOffset <= 1; xOffset++) {
            for (int yOffset = -1; yOffset <= 1; yOffset++) {
                int newX = p.x + xOffset;
                int newY = p.y + yOffset;

                if (posIsOnGrid(grid, new Position(newX, newY))) {
                    if (grid[newX][newY] == Tileset.FLOOR) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
