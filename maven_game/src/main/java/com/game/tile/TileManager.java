package com.game.tile;

import java.awt.Graphics2D;
import java.util.Random;
import com.game.main.GamePanel;
import com.game.main.GameConfig;
import com.game.main.ResourceManager;
import com.game.main.BSPDungeonGenerator;
import com.game.main.Camera;

/**
 * Manages tiles and BSP map generation for larger dungeons
 */
public class TileManager {
    private final GamePanel gamePanel;
    private final ResourceManager resourceManager;
    private final Camera camera;
    public Tile[] tile;
    private int[][] tileIndexes;
    private final int mapWidth;
    private final int mapHeight;
    private BSPDungeonGenerator bspGenerator;
    private long currentSeed;

    public TileManager(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.resourceManager = ResourceManager.getInstance();
        this.camera = new Camera(GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT);
        this.mapWidth = GameConfig.MAP_WIDTH;
        this.mapHeight = GameConfig.MAP_HEIGHT;
        this.tileIndexes = new int[mapWidth][mapHeight];
        this.tile = new Tile[GameConfig.MAX_TILES];
        this.currentSeed = System.currentTimeMillis();

        // Set camera map bounds
        camera.setMapBounds(mapWidth, mapHeight);

        initialize();
        generateNewMap();
    }

    private void initialize() {
        loadTileImages();
    }

    /**
     * Load all tile images
     */
    public void loadTileImages() {
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

    /**
     * Create a tile with collision property
     */
    private Tile createTile(String tileName, boolean hasCollision) {
        Tile tile = new Tile();
        tile.tileImage = resourceManager.loadTile(tileName);
        tile.collision = hasCollision;
        return tile;
    }

    /**
     * Generate a new BSP dungeon
     */
    public void generateNewMap() {
        currentSeed = System.currentTimeMillis();
        generateMap(currentSeed);
    }

    /**
     * Generate map with specific seed
     */
    public void regenerateMap(long seed) {
        this.currentSeed = seed;
        generateMap(seed);
    }

    /**
     * Generate BSP dungeon and convert to tile indexes
     */
    private void generateMap(long seed) {
        System.out.println("Generating BSP dungeon with seed: " + seed);

        // Generate BSP dungeon
        bspGenerator = new BSPDungeonGenerator(mapWidth, mapHeight, seed);
        int[][] generatedMap = bspGenerator.generateMap();

        // Convert to tile indexes with smart wall detection
        convertMapToTiles(generatedMap, seed);

        System.out.println("BSP dungeon generated and converted to tiles successfully");
    }

    /**
     * Convert raw map data to tile indexes with intelligent wall detection
     */
    private void convertMapToTiles(int[][] rawMap, long seed) {
        Random random = new Random(seed);

        for (int x = 0; x < mapWidth; x++) {
            for (int y = 0; y < mapHeight; y++) {
                if (rawMap[x][y] == 1) {
                    // Wall - determine wall type based on neighbors
                    tileIndexes[x][y] = determineWallType(x, y, rawMap);
                } else {
                    // Floor - add variation
                    double variation = random.nextDouble();
                    if (variation < GameConfig.FLOOR_VARIATION_CHANCE) {
                        tileIndexes[x][y] = 9; // floor2
                    } else if (variation < GameConfig.FLOOR_VARIATION_CHANCE * 2) {
                        tileIndexes[x][y] = 10; // floor3
                    } else {
                        tileIndexes[x][y] = 0; // floor1
                    }
                }
            }
        }
    }

    /**
     * Intelligent wall type detection based on neighboring walls
     */
    private int determineWallType(int x, int y, int[][] map) {
        boolean topWall = y > 0 && map[x][y - 1] == 1;
        boolean bottomWall = y < map[0].length - 1 && map[x][y + 1] == 1;
        boolean leftWall = x > 0 && map[x - 1][y] == 1;
        boolean rightWall = x < map.length - 1 && map[x + 1][y] == 1;

        // Corner detection
        if (topWall && leftWall && !bottomWall && !rightWall) {
            return 3; // Upper Left Corner
        } else if (topWall && rightWall && !bottomWall && !leftWall) {
            return 4; // Upper Right Corner
        } else if (bottomWall && leftWall && !topWall && !rightWall) {
            return 5; // Lower Left Corner
        } else if (bottomWall && rightWall && !topWall && !leftWall) {
            return 6; // Lower Right Corner
        }

        // Edge walls
        if (topWall && bottomWall && !leftWall && !rightWall) {
            return 2; // Vertical wall
        } else if (leftWall && rightWall && !topWall && !bottomWall) {
            return 1; // Horizontal wall
        }

        // Border walls
        if (y == 0 || y == map[0].length - 1) {
            return 1; // Top/bottom border
        } else if (x == 0 || x == map.length - 1) {
            return 2; // Left/right border
        }

        // Default wall
        return 1;
    }

    /**
     * Update camera and tile manager
     */
    public void update() {
        // Set camera target to player
        if (gamePanel.getPlayer() != null) {
            camera.setTarget(gamePanel.getPlayer());
        }

        // Update camera position
        camera.update();
    }

    /**
     * Draw visible tiles using camera system
     */
    public void draw(Graphics2D g2) {
        // Get visible tile range from camera
        Camera.TileRange range = camera.getVisibleTileRange();

        // Draw only visible tiles for performance
        for (int col = range.startCol; col < range.endCol; col++) {
            for (int row = range.startRow; row < range.endRow; row++) {
                if (col >= 0 && col < mapWidth && row >= 0 && row < mapHeight) {
                    int tileIndex = tileIndexes[col][row];

                    if (isValidTileIndex(tileIndex)) {
                        // Calculate world position
                        int worldX = col * GameConfig.TILE_SIZE;
                        int worldY = row * GameConfig.TILE_SIZE;

                        // Convert to screen position
                        int screenX = camera.worldToScreenX(worldX);
                        int screenY = camera.worldToScreenY(worldY);

                        // Draw tile
                        g2.drawImage(tile[tileIndex].tileImage,
                                screenX, screenY,
                                GameConfig.TILE_SIZE, GameConfig.TILE_SIZE, null);
                    }
                }
            }
        }
    }

    /**
     * Check if tile index is valid
     */
    private boolean isValidTileIndex(int tileIndex) {
        return tileIndex >= 0 && tileIndex < tile.length && tile[tileIndex] != null;
    }

    /**
     * Check if a tile at world coordinates is solid (for collision detection)
     */
    public boolean isTileSolid(int worldX, int worldY) {
        int col = worldX / GameConfig.TILE_SIZE;
        int row = worldY / GameConfig.TILE_SIZE;

        if (col < 0 || col >= mapWidth || row < 0 || row >= mapHeight) {
            return true; // Out of bounds is solid
        }

        int tileIndex = tileIndexes[col][row];
        if (isValidTileIndex(tileIndex)) {
            return tile[tileIndex].collision;
        }

        return true; // Unknown tiles are solid
    }

    /**
     * Get camera instance
     */
    public Camera getCamera() {
        return camera;
    }

    /**
     * Get map dimensions
     */
    public int getMapWidth() {
        return mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    /**
     * Get current seed
     */
    public long getCurrentSeed() {
        return currentSeed;
    }

    /**
     * Get tile indexes array
     */
    public int[][] getTileIndexes() {
        return tileIndexes;
    }

    /**
     * Get BSP generator for analysis
     */
    public BSPDungeonGenerator getBSPGenerator() {
        return bspGenerator;
    }
}
