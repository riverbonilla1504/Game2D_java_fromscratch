package main;

import entity.Entity;
import entity.Direction;
import main.GameConfig;

/**
 * Handles collision detection between entities, tiles, and objects
 * Updated to work with larger BSP maps and camera system
 */
public class CollisionChecker {
    private final GamePanel gamePanel;

    public CollisionChecker(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    /**
     * Check for collision with tiles using the improved tile manager
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

        // Check for collision with tiles based on direction using TileManager's method
        switch (entity.getDirection()) {
            case UP -> {
                int newTopY = entityTopWorldY - entity.getSpeed();
                if (gamePanel.getTileManager().isTileSolid(entityLeftWorldX, newTopY) ||
                        gamePanel.getTileManager().isTileSolid(entityRightWorldX, newTopY)) {
                    entity.setCollisionOn(true);
                }
            }
            case DOWN -> {
                int newBottomY = entityBottomWorldY + entity.getSpeed();
                if (gamePanel.getTileManager().isTileSolid(entityLeftWorldX, newBottomY) ||
                        gamePanel.getTileManager().isTileSolid(entityRightWorldX, newBottomY)) {
                    entity.setCollisionOn(true);
                }
            }
            case LEFT -> {
                int newLeftX = entityLeftWorldX - entity.getSpeed();
                if (gamePanel.getTileManager().isTileSolid(newLeftX, entityTopWorldY) ||
                        gamePanel.getTileManager().isTileSolid(newLeftX, entityBottomWorldY)) {
                    entity.setCollisionOn(true);
                }
            }
            case RIGHT -> {
                int newRightX = entityRightWorldX + entity.getSpeed();
                if (gamePanel.getTileManager().isTileSolid(newRightX, entityTopWorldY) ||
                        gamePanel.getTileManager().isTileSolid(newRightX, entityBottomWorldY)) {
                    entity.setCollisionOn(true);
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

                obj.solidArea.x = obj.getWorldX() + obj.solidArea.x;
                obj.solidArea.y = obj.getWorldY() + obj.solidArea.y;

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
}
