package main;

import entity.Entity;

public class CollisionChecker {
    private GamePanel gameP;

    // CollisionChecker constructor
    public CollisionChecker(GamePanel gameP) {
        this.gameP = gameP;
    }

    // Check for collision with tiles
    public void checkTile(Entity entity) {

        int entityLeftWorldX = entity.x + entity.solidArea.x;
        int entityRightWorldX = entity.x + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = entity.y + entity.solidArea.y;
        int entityBottomWorldY = entity.y + entity.solidArea.y + entity.solidArea.height;

        int entityLeftCol = entityLeftWorldX / gameP.tileSize;
        int entityRightCol = entityRightWorldX / gameP.tileSize;
        int entityTopRow = entityTopWorldY / gameP.tileSize;
        int entityBottomRow = entityBottomWorldY / gameP.tileSize;

        int tileNum1, tileNum2;
        // Check for collision with tiles
        switch (entity.direction) {
            // Check for collision with tiles when moving up
            case "up":
                entityTopRow = (entityTopWorldY - entity.speed) / gameP.tileSize;
                tileNum1 = gameP.tileManager.tileIndexes[entityLeftCol][entityTopRow];
                tileNum2 = gameP.tileManager.tileIndexes[entityRightCol][entityTopRow];
                if (isValidTile(tileNum1) && isValidTile(tileNum2)) {
                    if (gameP.tileManager.tile[tileNum1].collision || gameP.tileManager.tile[tileNum2].collision) {
                        entity.CollisionOn = true;
                    }
                }
                break;
                // Check for collision with tiles when moving down
            case "down":
                entityBottomRow = (entityBottomWorldY + entity.speed) / gameP.tileSize;
                tileNum1 = gameP.tileManager.tileIndexes[entityLeftCol][entityBottomRow];
                tileNum2 = gameP.tileManager.tileIndexes[entityRightCol][entityBottomRow];
                if (isValidTile(tileNum1) && isValidTile(tileNum2)) {
                    if (gameP.tileManager.tile[tileNum1].collision || gameP.tileManager.tile[tileNum2].collision) {
                        entity.CollisionOn = true;
                    }
                }
                break;
                // Check for collision with tiles when moving left
            case "left":
                entityLeftCol = (entityLeftWorldX - entity.speed) / gameP.tileSize;
                tileNum1 = gameP.tileManager.tileIndexes[entityLeftCol][entityTopRow];
                tileNum2 = gameP.tileManager.tileIndexes[entityLeftCol][entityBottomRow];
                if (isValidTile(tileNum1) && isValidTile(tileNum2)) {
                    if (gameP.tileManager.tile[tileNum1].collision || gameP.tileManager.tile[tileNum2].collision) {
                        entity.CollisionOn = true;
                    }
                }
                break;
                // Check for collision with tiles when moving right
            case "right":
                entityRightCol = (entityRightWorldX + entity.speed) / gameP.tileSize;
                tileNum1 = gameP.tileManager.tileIndexes[entityRightCol][entityTopRow];
                tileNum2 = gameP.tileManager.tileIndexes[entityRightCol][entityBottomRow];
                if (isValidTile(tileNum1) && isValidTile(tileNum2)) {
                    if (gameP.tileManager.tile[tileNum1].collision || gameP.tileManager.tile[tileNum2].collision) {
                        entity.CollisionOn = true;
                    }
                }
                break;
        }
    }

    // Check for collision with objects
    public int checkObject(Entity entity, boolean player){
        int index = 999;

        // Check for collision with objects
        for (int i = 0; i < gameP.obj.length; i++) {
            if (gameP.obj[i] !=null){
                entity.solidArea.x = entity.x + entity.solidArea.x;
                entity.solidArea.y = entity.y + entity.solidArea.y;

                gameP.obj[i].solidArea.x = gameP.obj[i].worldX + gameP.obj[i].solidArea.x;
                gameP.obj[i].solidArea.y = gameP.obj[i].worldY + gameP.obj[i].solidArea.y;

                // Check for collision with objects
                switch(entity.direction){
                    // Check for collision with objects when moving up
                    case "up":
                    entity.solidArea.y -= entity.speed;
                    if (entity.solidArea.intersects(gameP.obj[i].solidArea)){
                        if(gameP.obj[i].collision == true){
                            entity.CollisionOn = true;
                        }
                        if (player){ 
                            index = i;
                        }
                    }
                    break;
                    // Check for collision with objects when moving down
                    case "down":
                    entity.solidArea.y += entity.speed;
                    if (entity.solidArea.intersects(gameP.obj[i].solidArea)){
                        if(gameP.obj[i].collision == true){
                            entity.CollisionOn = true;
                        }
                        if (player){
                            index = i;
                        }
                    }
                    break;
                    // Check for collision with objects when moving left
                    case "left":
                    entity.solidArea.x -= entity.speed;
                    if (entity.solidArea.intersects(gameP.obj[i].solidArea)){
                        if(gameP.obj[i].collision == true){
                            entity.CollisionOn = true;
                        }
                        if (player){
                            index = i;
                        }
                    }
                    break;
                    // Check for collision with objects when moving right
                    case "right":
                    entity.solidArea.x += entity.speed;
                    if (entity.solidArea.intersects(gameP.obj[i].solidArea)){
                        if(gameP.obj[i].collision == true){
                            entity.CollisionOn = true;
                        }
                        if (player){
                            index = i;
                        }
                    }
                    break;
                }
                // Reset the solidArea of the entity and the object
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                gameP.obj[i].solidArea.x = gameP.obj[i].solidAreaDefaultX;
                gameP.obj[i].solidArea.y = gameP.obj[i].solidAreaDefaultY;
            }
        }
        return index;
    }

    // Check if the tile number is valid
    private boolean isValidTile(int tileNum) {
        return tileNum >= 0 && tileNum < gameP.tileManager.tile.length && gameP.tileManager.tile[tileNum] != null;
    }
}
