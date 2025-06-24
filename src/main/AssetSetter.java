package main;

import object.Star;
import main.GameConfig;

/**
 * Handles placement and initialization of game objects
 */
public class AssetSetter {
    private final GamePanel gamePanel;

    public AssetSetter(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    /**
     * Initialize and place game objects in the world
     */
    public void setObject() {
        // Create a star object
        Star star = new Star();
        star.worldX = 8 * GameConfig.TILE_SIZE;
        star.worldY = 8 * GameConfig.TILE_SIZE;

        // Add to game objects list
        gamePanel.getGameObjects().add(star);
    }
}
