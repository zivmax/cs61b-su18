package byog.Core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class Architect {

    public TETile[][] digCave(TETile[][] cave) {
        if (cave == null || cave[0] == null) {
            throw new IllegalArgumentException("Null argument");
        }

        roomsCenPos = addRandomRooms(cave, 40, 1000);
        connectRooms(cave, roomsCenPos, 1000);
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
    private static final short MIN_ROOM_SIZE = 2;
    private static final short MAX_HALLWAY_LEN = 18;
    private static final short MIN_HALLWAY_LEN = 4;

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

        for (int numTempts = 0; numTempts < maxTempts - 1; numTempts++) {

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

    private TETile[][] generateRoom(int size) {
        TETile[][] room = new TETile[size][size];

        // Fill room with floor tiles
        fillGrid(room, Tileset.FLOOR);

        return room;
    }

    private void connectRooms(TETile[][] grid, List<Position> roomsCenPos, int maxTempts) {
        List<Position> connectedRooms = new ArrayList<>();

        for (int numTempts = 0; numTempts < maxTempts - 1; numTempts++) {
            int room1Index = RANDOM.nextInt(roomsCenPos.size() - 1);
            Position room1CenPos = roomsCenPos.get(room1Index);
            List<Position> nearRooms = findNearRooms(roomsCenPos, room1Index, room1CenPos);

            if (nearRooms.size() <= 1) {
                continue;
            }

            int room2Index = RANDOM.nextInt(nearRooms.size() - 1);

            Position room2CenPos = nearRooms.get(room2Index);

            if (connectTwoRooms(grid, room1CenPos, room2CenPos)) {
                connectedRooms.add(room1CenPos);
                connectedRooms.add(room2CenPos);
                roomsCenPos.remove(room1Index);
                roomsCenPos.remove(room2Index);
            }

            if (roomsCenPos.size() == 0) {
                break;
            }

            // Connect remain rooms with thier nearest connected rooms
            for (int i = 0; i < roomsCenPos.size(); i++) {
                Position roomCenPos = roomsCenPos.get(i);
                Position nearRoomCenPos = findNearestRoom(connectedRooms, -1, roomCenPos);
                if (nearRoomCenPos != null) {
                    connectTwoRooms(grid, roomCenPos, nearRoomCenPos);
                    connectedRooms.add(roomCenPos);
                    roomsCenPos.remove(i);
                }
            }
        }
    }

    private Position findNearestRoom(List<Position> bufferList, int roomIndex, Position roomCenPos) {
        // Find the nearest room
        Position nearestRoom = new Position(0, 0);
        for (int i = 0; i < bufferList.size(); i++) {
            if (roomIndex >= 0 && i == roomIndex) {
                continue;
            }
            Position room2CenPos = bufferList.get(i);
            if (Math.abs(roomCenPos.x - room2CenPos.x) + Math.abs(roomCenPos.y - room2CenPos.y) < Math
                    .abs(roomCenPos.x - nearestRoom.x) + Math.abs(roomCenPos.y - nearestRoom.y)) {
                nearestRoom = room2CenPos;
            }
        }

        return nearestRoom;
    }

    private List<Position> findNearRooms(List<Position> bufferList, int roomIndex, Position roomCenPos) {
        // Found a list of rooms that near room
        List<Position> nearRooms = new ArrayList<>();
        for (int i = 0; i < bufferList.size(); i++) {
            if (i != roomIndex) {
                Position room2CenPos = bufferList.get(i);
                if (Math.abs(roomCenPos.x - room2CenPos.x) <= MAX_HALLWAY_LEN
                        && Math.abs(roomCenPos.y - room2CenPos.y) <= MAX_HALLWAY_LEN) {
                    nearRooms.add(room2CenPos);
                }
            }
        }
        return nearRooms;
    }

    private boolean connectTwoRooms(TETile[][] grid, Position p1, Position p2) {
        Position midPoint = getMidPoint(grid, p1, p2);
        TETile[][] hallway1 = generateHallway(p1, midPoint);
        TETile[][] hallway2 = generateHallway(midPoint, p2);

        // Check if hallway1 and hallway2 are too long
        if (hallway1.length + hallway2.length > MAX_HALLWAY_LEN
                || hallway1[0].length + hallway2[0].length > MAX_HALLWAY_LEN) {
            return false;
        }

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
        // if (startPos.x == endPos.x) {
        // hallway = new TETile[1][Math.abs(endPos.y - startPos.y) + 1];
        // } else {
        // hallway = new TETile[Math.abs(endPos.x - startPos.x) + 1][1];
        // }

        // Fill hallway with floor tiles
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
}
