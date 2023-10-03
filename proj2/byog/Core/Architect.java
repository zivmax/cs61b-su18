package byog.Core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class Architect {

    public TETile[][] digCave(TETile[][] cave, Integer seed) {
        if (seed != null) {
            RANDOM.setSeed(seed);
        }
        if (cave == null || cave[0] == null) {
            throw new IllegalArgumentException("Null argument");
        }

        roomsCenPos = addRandomRooms(cave, 15, 25);
        connectRooms(cave, roomsCenPos, 1000);
        cleanMapVergeFloor(cave);
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
    private static final short MAX_ROOM_SIZE = 8;
    private static final short MIN_ROOM_SIZE = 3;

    private static final Random RANDOM = new Random();
    private List<Position> roomsCenPos = new ArrayList<>();

    private enum Direc {
        UP, DOWN, LEFT, RIGHT,
        UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT
    }

    private class Position {
        private int x;
        private int y;

        private Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private List<Position> addRandomRooms(TETile[][] grid, int numRooms, int maxTempts) {

        List<Position> roomsCenPos = new ArrayList<>();

        for (int numTempts = 0; numTempts < maxTempts; numTempts++) {

            TETile[][] room = generateRandomRoom();
            int x = RANDOM.nextInt(grid.length - room.length);
            int y = RANDOM.nextInt(grid[0].length - room[0].length);

            // roomCenPos denotes the center position of the room
            Position roomCenPos = new Position(x + room.length / 2, y + room[0].length / 2);

            // roomPos denotes the position of the room's bottom left corner
            Position roomPos = new Position(x, y);

            if (!areOverLapping(grid, room, new Position(0, 0), roomPos) && areaIsOnGrid(grid, room, roomPos)) {
                insertArea(grid, room, roomPos);
                roomsCenPos.add(roomCenPos);
            }

            if (roomsCenPos.size() == numRooms) {
                break;
            }
        }

        return roomsCenPos;
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

    private void connectRooms(TETile[][] grid, List<Position> roomsCenPos, int maxTempts) {
        int room1Index = RANDOM.nextInt(roomsCenPos.size() - 1);
        Position room1CenPos = roomsCenPos.get(room1Index);

        for (int numTempts = 0; numTempts < maxTempts; numTempts++) {
            int room2Index = findNearestRoom(roomsCenPos, room1Index, room1CenPos);
            Position room2CenPos = roomsCenPos.get(room2Index);

            connectTwoRooms(grid, room1CenPos, room2CenPos);
            roomsCenPos.remove(room1Index);

            if (roomsCenPos.size() == 1) {
                break;
            } else {
                room1Index = roomsCenPos.indexOf(room2CenPos);
                room1CenPos = room2CenPos;
            }
        }
    }

    private int findNearestRoom(List<Position> bufferList, int roomIndex, Position roomCenPos) {
        if (bufferList.size() == 1) {
            return -1;
        } else if (bufferList.size() == 2) {
            return roomIndex == 0 ? 1 : 0;
        }

        // Find the nearest room
        int nearestRoomIndex = roomIndex == 0 ? 1 : 0;
        Position nearestRoom = new Position(0, 0);
        for (int i = 0; i < bufferList.size(); i++) {
            if (i == roomIndex) {
                continue;
            }
            Position room2CenPos = bufferList.get(i);
            if (Math.abs(roomCenPos.x - room2CenPos.x) + Math.abs(roomCenPos.y - room2CenPos.y) < Math
                    .abs(roomCenPos.x - nearestRoom.x) + Math.abs(roomCenPos.y - nearestRoom.y)) {
                nearestRoomIndex = i;
                nearestRoom = room2CenPos;
            }
        }

        return nearestRoomIndex;
    }

    private boolean connectTwoRooms(TETile[][] grid, Position p1, Position p2) {
        Position midPoint = getMidPoint(grid, p1, p2);
        TETile[][] hallway1 = generateHallway(p1, midPoint);
        TETile[][] hallway2 = generateHallway(midPoint, p2);

        // Choose the right conner pos to insert hallway
        Direc midDirecToP1 = checkRelPos(midPoint, p1);
        Direc p2ToMidDirec = checkRelPos(midPoint, p2);

        switch (midDirecToP1) {
            case UP:
                insertArea(grid, hallway1, p1);
                break;
            case DOWN:
                insertArea(grid, hallway1, midPoint);
                break;
            case LEFT:
                insertArea(grid, hallway1, midPoint);
                break;
            case RIGHT:
                insertArea(grid, hallway1, p1);
                break;
            default:
                break;

        }
        switch (p2ToMidDirec) {
            case UP:
                insertArea(grid, hallway2, midPoint);
                break;
            case DOWN:
                insertArea(grid, hallway2, p2);
                break;
            case LEFT:
                insertArea(grid, hallway2, p2);
                break;
            case RIGHT:
                insertArea(grid, hallway2, midPoint);
                break;
            default:
                break;
        }

        return true;
    }

    private Direc checkRelPos(Position p1, Position p2) {
        if (p1.x == p2.x) {
            if (p1.y > p2.y) {
                return Direc.UP;
            } else {
                return Direc.DOWN;
            }
        } else if (p1.y == p2.y) {
            if (p1.x > p2.x) {
                return Direc.LEFT;
            } else {
                return Direc.RIGHT;
            }
        } else if (p1.x > p2.x) {
            if (p1.y > p2.y) {
                return Direc.UP_LEFT;
            } else {
                return Direc.DOWN_LEFT;
            }
        } else {
            if (p1.y > p2.y) {
                return Direc.UP_RIGHT;
            } else {
                return Direc.DOWN_RIGHT;
            }
        }
    }

    // Get the midpoint of two rooms for connecting them by hallway
    private Position getMidPoint(TETile[][] grid, Position p1, Position p2) {
        Position midPoint = new Position(p1.x, p2.y);
        if (!posIsOnGrid(grid, midPoint)) {
            midPoint = new Position(p2.x, p1.y);
        }
        return midPoint;
    }

    private TETile[][] generateHallway(Position startPos, Position endPos) {
        TETile[][] hallway = new TETile[Math.abs(endPos.x - startPos.x) + 1][Math.abs(endPos.y - startPos.y) + 1];

        fillGrid(hallway, Tileset.FLOOR);

        return hallway;
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

    private void cleanMapVergeFloor(TETile[][] grid) {
        for (int i = 0; i < grid.length; i++) {
            grid[i][0] = Tileset.NOTHING;
            grid[i][grid[0].length - 1] = Tileset.NOTHING;
        }
        for (int j = 0; j < grid[0].length; j++) {
            grid[0][j] = Tileset.NOTHING;
            grid[grid.length - 1][j] = Tileset.NOTHING;
        }
    }
}
