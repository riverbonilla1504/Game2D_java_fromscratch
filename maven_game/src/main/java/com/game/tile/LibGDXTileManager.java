package com.game.tile;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.OrthographicCamera;
import java.util.Random;
import com.game.main.GameConfig;
import com.game.main.LibGDXResourceManager;
import com.game.main.BSPDungeonGenerator;
import com.game.libgdx.LibGDXGame;
import com.game.entity.LibGDXPlayer;

/**
 * LibGDX version of TileManager that handles tiles and BSP map generation
 * Uses LibGDX rendering system for better performance
 */
public class LibGDXTileManager {
    private final LibGDXGame game;
    private final LibGDXResourceManager resourceManager;
    private final OrthographicCamera camera;
    private LibGDXTile[] tile;
    private int[][] tileIndexes;
    private final int mapWidth;
    private final int mapHeight;
    private BSPDungeonGenerator bspGenerator;
    private long currentSeed;

    public LibGDXTileManager(LibGDXGame game) {
        this.game = game;
        this.resourceManager = LibGDXResourceManager.getInstance();
        this.camera = new OrthographicCamera();
        this.mapWidth = GameConfig.MAP_WIDTH;
        this.mapHeight = GameConfig.MAP_HEIGHT;
        this.tileIndexes = new int[mapWidth][mapHeight];
        this.tile = new LibGDXTile[GameConfig.MAX_TILES];
        this.currentSeed = System.currentTimeMillis();

        // Set camera map bounds
        camera.setToOrtho(false, GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT);
        camera.position.set(GameConfig.SCREEN_WIDTH / 2f, GameConfig.SCREEN_HEIGHT / 2f, 0);

        initialize();
        generateNewMap();
    }

    private void initialize() {
        loadTileImages();
    }

    /**
     * Load all tile images using LibGDX
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
    private LibGDXTile createTile(String tileName, boolean hasCollision) {
        LibGDXTile tile = new LibGDXTile();
        tile.texture = resourceManager.loadTile(tileName);
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
    public void update(float deltaTime) {
        // Set camera target to player
        LibGDXPlayer player = game.getPlayer();
        if (player != null) {
            // Follow player with camera
            float targetX = player.getX() + GameConfig.TILE_SIZE / 2f;
            float targetY = player.getY() + GameConfig.TILE_SIZE / 2f;

            // Clamp camera to map boundaries
            float cameraX = Math.max(GameConfig.SCREEN_WIDTH / 2f,
                    Math.min(targetX, mapWidth * GameConfig.TILE_SIZE - GameConfig.SCREEN_WIDTH / 2f));
            float cameraY = Math.max(GameConfig.SCREEN_HEIGHT / 2f,
                    Math.min(targetY, mapHeight * GameConfig.TILE_SIZE - GameConfig.SCREEN_HEIGHT / 2f));

            camera.position.set(cameraX, cameraY, 0);
        }

        // Update camera
        camera.update();
    }

    /**
     * Render visible tiles using camera system
     */
    public void render(SpriteBatch spriteBatch) {
        // Get visible tile range from camera
        float camLeft = camera.position.x - camera.viewportWidth / 2f;
        float camRight = camera.position.x + camera.viewportWidth / 2f;
        float camBottom = camera.position.y - camera.viewportHeight / 2f;
        float camTop = camera.position.y + camera.viewportHeight / 2f;

        int startCol = Math.max(0, (int) (camLeft / GameConfig.TILE_SIZE));
        int endCol = Math.min(mapWidth, (int) (camRight / GameConfig.TILE_SIZE) + 1);
        int startRow = Math.max(0, (int) (camBottom / GameConfig.TILE_SIZE));
        int endRow = Math.min(mapHeight, (int) (camTop / GameConfig.TILE_SIZE) + 1);

        // Draw only visible tiles for performance
        for (int col = startCol; col < endCol; col++) {
            for (int row = startRow; row < endRow; row++) {
                if (col >= 0 && col < mapWidth && row >= 0 && row < mapHeight) {
                    int tileIndex = tileIndexes[col][row];

                    if (isValidTileIndex(tileIndex)) {
                        // Calculate world position
                        float worldX = col * GameConfig.TILE_SIZE;
                        float worldY = row * GameConfig.TILE_SIZE;

                        // Draw tile at world position (camera handles transformation)
                        if (tile[tileIndex].texture != null) {
                            spriteBatch.draw(tile[tileIndex].texture, worldX, worldY,
                                    GameConfig.TILE_SIZE, GameConfig.TILE_SIZE);
                        }
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
    public boolean isTileSolid(float worldX, float worldY) {
        int col = (int) (worldX / GameConfig.TILE_SIZE);
        int row = (int) (worldY / GameConfig.TILE_SIZE);

        if (col >= 0 && col < mapWidth && row >= 0 && row < mapHeight) {
            int tileIndex = tileIndexes[col][row];
            return isValidTileIndex(tileIndex) && tile[tileIndex].collision;
        }
        return true; // Out of bounds is considered solid
    }

    // Getters
    public OrthographicCamera getCamera() {
        return camera;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public long getCurrentSeed() {
        return currentSeed;
    }

    public int[][] getTileIndexes() {
        return tileIndexes;
    }

    public BSPDungeonGenerator getBSPGenerator() {
        return bspGenerator;
    }
}