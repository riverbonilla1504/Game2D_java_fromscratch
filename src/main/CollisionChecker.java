package main;

import entity.Entity;
import entity.Direction;
import main.GameConfig;

/**
 * Handles collision detection between entities, tiles, and objects
 */
public class CollisionChecker {
    private final GamePanel gamePanel;

    public CollisionChecker(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    /**
     * Check for collision with tiles
     * 
     * @param entity The entity to check collision for
     */
    public void checkTile(Entity entity) {
        int entityLeftWorldX = entity.getX() + entity.getSolidArea().x;
        int entityRightWorldX = entity.getX() + entity.getSolidArea().x + entity.getSolidArea().width;
        int entityTopWorldY = entity.getY() + entity.getSolidArea().y;
        int entityBottomWorldY = entity.getY() + entity.getSolidArea().y + entity.getSolidArea().height;

        int entityLeftCol = entityLeftWorldX / GameConfig.TILE_SIZE;
        int entityRightCol = entityRightWorldX / GameConfig.TILE_SIZE;
        int entityTopRow = entityTopWorldY / GameConfig.TILE_SIZE;
        int entityBottomRow = entityBottomWorldY / GameConfig.TILE_SIZE;

        int tileNum1, tileNum2;

        // Check for collision with tiles based on direction
        switch (entity.getDirection()) {
            case UP -> {
                entityTopRow = (entityTopWorldY - entity.getSpeed()) / GameConfig.TILE_SIZE;
                tileNum1 = getTileIndex(entityLeftCol, entityTopRow);
                tileNum2 = getTileIndex(entityRightCol, entityTopRow);
                if (isValidTile(tileNum1) && isValidTile(tileNum2)) {
                    if (isCollisionTile(tileNum1) || isCollisionTile(tileNum2)) {
                        entity.setCollisionOn(true);
                    }
                }
            }
            case DOWN -> {
                entityBottomRow = (entityBottomWorldY + entity.getSpeed()) / GameConfig.TILE_SIZE;
                tileNum1 = getTileIndex(entityLeftCol, entityBottomRow);
                tileNum2 = getTileIndex(entityRightCol, entityBottomRow);
                if (isValidTile(tileNum1) && isValidTile(tileNum2)) {
                    if (isCollisionTile(tileNum1) || isCollisionTile(tileNum2)) {
                        entity.setCollisionOn(true);
                    }
                }
            }
            case LEFT -> {
                entityLeftCol = (entityLeftWorldX - entity.getSpeed()) / GameConfig.TILE_SIZE;
                tileNum1 = getTileIndex(entityLeftCol, entityTopRow);
                tileNum2 = getTileIndex(entityLeftCol, entityBottomRow);
                if (isValidTile(tileNum1) && isValidTile(tileNum2)) {
                    if (isCollisionTile(tileNum1) || isCollisionTile(tileNum2)) {
                        entity.setCollisionOn(true);
                    }
                }
            }
            case RIGHT -> {
                entityRightCol = (entityRightWorldX + entity.getSpeed()) / GameConfig.TILE_SIZE;
                tileNum1 = getTileIndex(entityRightCol, entityTopRow);
                tileNum2 = getTileIndex(entityRightCol, entityBottomRow);
                if (isValidTile(tileNum1) && isValidTile(tileNum2)) {
                    if (isCollisionTile(tileNum1) || isCollisionTile(tileNum2)) {
                        entity.setCollisionOn(true);
                    }
                }
            }
            default -> {
            } // No collision check for default direction
        }
    }

    /**
     * Check for collision with objects
     * 
     * @param entity   The entity to check collision for
     * @param isPlayer Whether the entity is the player
     * @return Index of the collided object, or 999 if no collision
     */
    public int checkObject(Entity entity, boolean isPlayer) {
        int index = 999;

        for (int i = 0; i < gamePanel.getGameObjects().size(); i++) {
            var obj = gamePanel.getGameObjects().get(i);
            if (obj != null) {
                // Set up collision areas
                entity.getSolidArea().x = entity.getX() + entity.getSolidArea().x;
                entity.getSolidArea().y = entity.getY() + entity.getSolidArea().y;

                obj.solidArea.x = obj.worldX + obj.solidArea.x;
                obj.solidArea.y = obj.worldY + obj.solidArea.y;

                // Check collision based on direction
                switch (entity.getDirection()) {
                    case UP -> {
                        entity.getSolidArea().y -= entity.getSpeed();
                        if (entity.getSolidArea().intersects(obj.solidArea)) {
                            if (obj.collision) {
                                entity.setCollisionOn(true);
                            }
                            if (isPlayer) {
                                index = i;
                            }
                        }
                    }
                    case DOWN -> {
                        entity.getSolidArea().y += entity.getSpeed();
                        if (entity.getSolidArea().intersects(obj.solidArea)) {
                            if (obj.collision) {
                                entity.setCollisionOn(true);
                            }
                            if (isPlayer) {
                                index = i;
                            }
                        }
                    }
                    case LEFT -> {
                        entity.getSolidArea().x -= entity.getSpeed();
                        if (entity.getSolidArea().intersects(obj.solidArea)) {
                            if (obj.collision) {
                                entity.setCollisionOn(true);
                            }
                            if (isPlayer) {
                                index = i;
                            }
                        }
                    }
                    case RIGHT -> {
                        entity.getSolidArea().x += entity.getSpeed();
                        if (entity.getSolidArea().intersects(obj.solidArea)) {
                            if (obj.collision) {
                                entity.setCollisionOn(true);
                            }
                            if (isPlayer) {
                                index = i;
                            }
                        }
                    }
                    default -> {
                    } // No collision check for default direction
                }

                // Reset collision areas
                entity.getSolidArea().x = entity.getSolidAreaDefaultX();
                entity.getSolidArea().y = entity.getSolidAreaDefaultY();
                obj.solidArea.x = obj.solidAreaDefaultX;
                obj.solidArea.y = obj.solidAreaDefaultY;
            }
        }
        return index;
    }

    /**
     * Get tile index with bounds checking
     * 
     * @param col Column index
     * @param row Row index
     * @return Tile index or -1 if out of bounds
     */
    private int getTileIndex(int col, int row) {
        if (col >= 0 && col < GameConfig.MAX_SCREEN_COL &&
                row >= 0 && row < GameConfig.MAX_SCREEN_ROW) {
            return gamePanel.getTileManager().tileIndexes[col][row];
        }
        return -1;
    }

    /**
     * Check if tile number is valid
     * 
     * @param tileNum The tile number to check
     * @return True if valid, false otherwise
     */
    private boolean isValidTile(int tileNum) {
        return tileNum >= 0 && tileNum < gamePanel.getTileManager().tile.length &&
                gamePanel.getTileManager().tile[tileNum] != null;
    }

    /**
     * Check if tile has collision
     * 
     * @param tileNum The tile number to check
     * @return True if tile has collision, false otherwise
     */
    private boolean isCollisionTile(int tileNum) {
        return gamePanel.getTileManager().tile[tileNum].collision;
    }
}
