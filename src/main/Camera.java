package main;

import entity.Entity;

/**
 * Camera system that follows the player
 * Allows for maps larger than the screen size
 */
public class Camera {
    private int x, y;
    private int screenWidth, screenHeight;
    private int mapWidth, mapHeight;
    private Entity target; // Usually the player

    public Camera(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.x = 0;
        this.y = 0;
    }

    /**
     * Set the camera target (usually the player)
     */
    public void setTarget(Entity target) {
        this.target = target;
    }

    /**
     * Set the map dimensions for camera bounds
     */
    public void setMapBounds(int mapWidth, int mapHeight) {
        this.mapWidth = mapWidth * GameConfig.TILE_SIZE;
        this.mapHeight = mapHeight * GameConfig.TILE_SIZE;
    }

    /**
     * Update camera position to follow the target
     */
    public void update() {
        if (target != null) {
            // Center camera on target
            int targetX = target.getX() + GameConfig.TILE_SIZE / 2;
            int targetY = target.getY() + GameConfig.TILE_SIZE / 2;

            // Calculate desired camera position (centered on target)
            int desiredX = targetX - screenWidth / 2;
            int desiredY = targetY - screenHeight / 2;

            // Clamp camera to map boundaries
            x = Math.max(0, Math.min(desiredX, mapWidth - screenWidth));
            y = Math.max(0, Math.min(desiredY, mapHeight - screenHeight));
        }
    }

    /**
     * Convert world coordinates to screen coordinates
     */
    public int worldToScreenX(int worldX) {
        return worldX - x;
    }

    /**
     * Convert world coordinates to screen coordinates
     */
    public int worldToScreenY(int worldY) {
        return worldY - y;
    }

    /**
     * Convert screen coordinates to world coordinates
     */
    public int screenToWorldX(int screenX) {
        return screenX + x;
    }

    /**
     * Convert screen coordinates to world coordinates
     */
    public int screenToWorldY(int screenY) {
        return screenY + y;
    }

    /**
     * Check if a world position is visible on screen
     */
    public boolean isVisible(int worldX, int worldY, int width, int height) {
        return worldX + width >= x && worldX < x + screenWidth &&
                worldY + height >= y && worldY < y + screenHeight;
    }

    /**
     * Get visible tile range for efficient rendering
     */
    public TileRange getVisibleTileRange() {
        int startCol = Math.max(0, x / GameConfig.TILE_SIZE);
        int endCol = Math.min((x + screenWidth) / GameConfig.TILE_SIZE + 1,
                mapWidth / GameConfig.TILE_SIZE);
        int startRow = Math.max(0, y / GameConfig.TILE_SIZE);
        int endRow = Math.min((y + screenHeight) / GameConfig.TILE_SIZE + 1,
                mapHeight / GameConfig.TILE_SIZE);

        return new TileRange(startCol, endCol, startRow, endRow);
    }

    // Getters
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    /**
     * Helper class for tile range information
     */
    public static class TileRange {
        public final int startCol, endCol, startRow, endRow;

        public TileRange(int startCol, int endCol, int startRow, int endRow) {
            this.startCol = startCol;
            this.endCol = endCol;
            this.startRow = startRow;
            this.endRow = endRow;
        }
    }
}