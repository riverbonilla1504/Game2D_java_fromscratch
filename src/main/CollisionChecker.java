package main;

import entity.Entity;

public class CollisionChecker {
    private GamePanel gameP;

    public CollisionChecker(GamePanel gameP) {
        this.gameP = gameP;
    }
    public void checkTile(Entity entity){

        int entityLeftWorldX = entity.x + entity.solidArea.x;
        int entityRightWorldX = entity.x + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = entity.y + entity.solidArea.y;
        int entityBottomWorldY = entity.y + entity.solidArea.y + entity.solidArea.height;

        int entityLeftCol = entityLeftWorldX / gameP.tileSize;
        int entityRightCol = entityRightWorldX / gameP.tileSize;
        int entityTopRow = entityTopWorldY / gameP.tileSize;
        int entityBottomRow = entityBottomWorldY / gameP.tileSize;

        int tileNum1, tileNum2;

        switch (entity.direction) {
            case "up":
                entityTopRow = (entityTopWorldY - entity.speed) / gameP.tileSize;
                tileNum1 = gameP.tileManager.tileIndexes[entityLeftCol][entityTopRow];
                tileNum2 = gameP.tileManager.tileIndexes[entityRightCol][entityTopRow];
                if (gameP.tileManager.tile[tileNum1].collision == true || gameP.tileManager.tile[tileNum2].collision == true) {
                    entity.CollisionOn = true;
                }
                break;
                case "down":
                entityBottomRow = (entityBottomWorldY + entity.speed) / gameP.tileSize;
                tileNum1 = gameP.tileManager.tileIndexes[entityLeftCol][entityBottomRow];
                tileNum2 = gameP.tileManager.tileIndexes[entityRightCol][entityBottomRow];
                if (gameP.tileManager.tile[tileNum1].collision == true || gameP.tileManager.tile[tileNum2].collision == true) {
                    entity.CollisionOn = true;
                }
                    break;

                case "left":  
                entityLeftCol = (entityLeftWorldX - entity.speed) / gameP.tileSize;
                tileNum1 = gameP.tileManager.tileIndexes[entityLeftCol][entityTopRow];
                tileNum2 = gameP.tileManager.tileIndexes[entityLeftCol][entityBottomRow];
                if (gameP.tileManager.tile[tileNum1].collision == true || gameP.tileManager.tile[tileNum2].collision == true) {
                    entity.CollisionOn = true;
                }
                    break;

                case "right":
                entityRightCol = (entityRightWorldX + entity.speed) / gameP.tileSize;
                tileNum1 = gameP.tileManager.tileIndexes[entityRightCol][entityTopRow];
                tileNum2 = gameP.tileManager.tileIndexes[entityRightCol][entityBottomRow];
                if (gameP.tileManager.tile[tileNum1].collision == true || gameP.tileManager.tile[tileNum2].collision == true) {
                    entity.CollisionOn = true;
                }
                    break;
            
        }
    }
}
