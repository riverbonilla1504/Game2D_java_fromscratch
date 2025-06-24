package tile;

import java.awt.Graphics2D;
import java.util.Random;
import main.GamePanel;
import main.GameConfig;
import main.ResourceManager;

/**
 * Manages tile loading, generation, and rendering
 */
public class TileManager {
    public int[][] tileIndexes;
    private final GamePanel gamePanel;
    public Tile[] tile;
    private final ResourceManager resourceManager;

    public TileManager(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.resourceManager = ResourceManager.getInstance();
        tile = new Tile[GameConfig.MAX_TILES];
        tileIndexes = new int[GameConfig.MAX_SCREEN_COL][GameConfig.MAX_SCREEN_ROW];
        initialize();
        generateTiles();
    }

    private void initialize() {
        getTileImage();
    }

    public void getTileImage() {
        try {
            // Floor tiles
            tile[0] = createTile("floor1", false);
            tile[9] = createTile("floor2", false);
            tile[10] = createTile("floor3", false);

            // Wall tiles
            tile[1] = createTile("wallUp", true);
            tile[2] = createTile("wallLeft", true);
            tile[7] = createTile("wallDown", true);
            tile[8] = createTile("wallRight", true);

            // Corner tiles
            tile[3] = createTile("wallCornerUpLeft", true);
            tile[4] = createTile("wallCornerUpRight", true);
            tile[5] = createTile("wallCornerDownLeft", true);
            tile[6] = createTile("wallCornerDownRight", true);

        } catch (Exception e) {
            System.err.println("Error loading tile images: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Tile createTile(String tileName, boolean hasCollision) {
        Tile tile = new Tile();
        tile.tileImage = resourceManager.loadTile(tileName);
        tile.collision = hasCollision;
        return tile;
    }

    private void generateTiles() {
        Random random = new Random();

        // Iterate over the map
        for (int i = 0; i < tileIndexes.length; i++) {
            for (int j = 0; j < tileIndexes[i].length; j++) {
                // Check if the cell is a corner
                if (isCorner(i, j)) {
                    tileIndexes[i][j] = getCornerTile(i, j);
                } else if (isVerticalBorder(i, j)) {
                    tileIndexes[i][j] = 2; // Tile type for vertical border
                } else if (isHorizontalBorder(i, j)) {
                    tileIndexes[i][j] = 1; // Tile type for horizontal border
                } else {
                    // Floor Randomization tiles
                    if (random.nextDouble() < GameConfig.FLOOR_VARIATION_CHANCE) {
                        tileIndexes[i][j] = 9;
                    } else if (random.nextDouble() < GameConfig.FLOOR_VARIATION_CHANCE) {
                        tileIndexes[i][j] = 10;
                    } else {
                        tileIndexes[i][j] = 0;
                    }
                }
            }
        }
        replaceWallsAtBottom();
        replaceVerticalBorders();
    }

    private boolean isCorner(int x, int y) {
        return (x == 0 || x == tileIndexes.length - 1) && (y == 0 || y == tileIndexes[0].length - 1);
    }

    private boolean isVerticalBorder(int x, int y) {
        return (x == 0 || x == tileIndexes.length - 1) && !(y == 0 || y == tileIndexes[0].length - 1);
    }

    private boolean isHorizontalBorder(int x, int y) {
        return (y == 0 || y == tileIndexes[0].length - 1) && !(x == 0 || x == tileIndexes.length - 1);
    }

    private int getCornerTile(int x, int y) {
        if (x == 0 && y == 0) {
            return 3; // Upper Left Corner
        } else if (x == tileIndexes.length - 1 && y == 0) {
            return 4; // Upper Right Corner
        } else if (x == 0 && y == tileIndexes[0].length - 1) {
            return 5; // Lower Left Corner
        } else if (x == tileIndexes.length - 1 && y == tileIndexes[0].length - 1) {
            return 6; // Lower Right Corner
        }
        return 1; // Default
    }

    private void replaceWallsAtBottom() {
        for (int i = 0; i < tileIndexes.length; i++) {
            int bottomRowIndex = tileIndexes[0].length - 1; // Last row
            if (tileIndexes[i][bottomRowIndex] == 1) { // If it's a wall
                tileIndexes[i][bottomRowIndex] = 7; // Change to bottom wall tile
            }
        }
    }

    private void replaceVerticalBorders() {
        for (int j = 0; j < tileIndexes[0].length; j++) {
            // Left border
            if (tileIndexes[0][j] == 2) {
                tileIndexes[0][j] = 2; // Left vertical border
            }
            // Right border
            if (tileIndexes[tileIndexes.length - 1][j] == 2) {
                tileIndexes[tileIndexes.length - 1][j] = 8; // Right vertical border
            }
        }
    }

    public void draw(Graphics2D g2) {
        for (int i = 0; i < GameConfig.MAX_SCREEN_COL; i++) {
            for (int j = 0; j < GameConfig.MAX_SCREEN_ROW; j++) {
                int tileIndex = tileIndexes[i][j];
                if (isValidTileIndex(tileIndex)) {
                    g2.drawImage(tile[tileIndex].tileImage,
                            i * GameConfig.TILE_SIZE,
                            j * GameConfig.TILE_SIZE,
                            GameConfig.TILE_SIZE,
                            GameConfig.TILE_SIZE, null);
                } else {
                    System.err
                            .println("Error: Tile index " + tileIndex + " is invalid or the tile is not initialized.");
                }
            }
        }
    }

    private boolean isValidTileIndex(int tileIndex) {
        return tileIndex >= 0 && tileIndex < tile.length && tile[tileIndex] != null;
    }
}
