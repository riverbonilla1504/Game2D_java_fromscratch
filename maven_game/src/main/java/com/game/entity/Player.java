package com.game.entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.game.main.GameConfig;
import com.game.main.GamePanel;
import com.game.main.KeyHandler;
import com.game.main.ResourceManager;

/**
 * Player entity class representing the main character
 * Handles player movement, animation, and interaction
 */
public class Player extends Entity {
    private final GamePanel gamePanel;
    private final KeyHandler keyHandler;
    private int stars = 0;
    private final ResourceManager resourceManager;

    public Player(GamePanel gamePanel, KeyHandler keyHandler) {
        this.gamePanel = gamePanel;
        this.keyHandler = keyHandler;
        this.resourceManager = ResourceManager.getInstance();

        setDefaultValues();
        getEntityImages();
        setupCollisionArea();
    }

    @Override
    public void setDefaultValues() {
        x = GameConfig.SCREEN_WIDTH / 2 - 32;
        y = GameConfig.SCREEN_HEIGHT / 2 - GameConfig.TILE_SIZE;
        speed = GameConfig.PLAYER_SPEED;
        direction = Direction.DEFAULT;
    }

    @Override
    public void getEntityImages() {
        // Assign each sprite directly to the class fields
        up1 = resourceManager.loadPlayerSprite("up", 1);
        up2 = resourceManager.loadPlayerSprite("up", 2);
        up3 = resourceManager.loadPlayerSprite("up", 3);
        up4 = resourceManager.loadPlayerSprite("up", 4);

        down1 = resourceManager.loadPlayerSprite("down", 1);
        down2 = resourceManager.loadPlayerSprite("down", 2);
        down3 = resourceManager.loadPlayerSprite("down", 3);
        down4 = resourceManager.loadPlayerSprite("down", 4);

        left1 = resourceManager.loadPlayerSprite("left", 1);
        left2 = resourceManager.loadPlayerSprite("left", 2);
        left3 = resourceManager.loadPlayerSprite("left", 3);
        left4 = resourceManager.loadPlayerSprite("left", 4);

        right1 = resourceManager.loadPlayerSprite("right", 1);
        right2 = resourceManager.loadPlayerSprite("right", 2);
        right3 = resourceManager.loadPlayerSprite("right", 3);
        right4 = resourceManager.loadPlayerSprite("right", 4);

        default1 = resourceManager.loadPlayerSprite("default", 1);
        default2 = resourceManager.loadPlayerSprite("default", 2);
        default3 = resourceManager.loadPlayerSprite("default", 3);
        default4 = resourceManager.loadPlayerSprite("default", 4);
    }

    private void setupCollisionArea() {
        solidArea = new Rectangle(
                GameConfig.PLAYER_SOLID_AREA_X,
                GameConfig.PLAYER_SOLID_AREA_Y,
                GameConfig.PLAYER_SOLID_AREA_WIDTH,
                GameConfig.PLAYER_SOLID_AREA_HEIGHT);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }

    @Override
    public void update() {
        handleInput();
        handleMovement();
        handleAnimation();
    }

    private void handleInput() {
        boolean isMoving = keyHandler.upPressed || keyHandler.downPressed ||
                keyHandler.leftPressed || keyHandler.rightPressed;

        if (isMoving) {
            if (keyHandler.upPressed) {
                direction = Direction.UP;
            } else if (keyHandler.downPressed) {
                direction = Direction.DOWN;
            } else if (keyHandler.leftPressed) {
                direction = Direction.LEFT;
            } else if (keyHandler.rightPressed) {
                direction = Direction.RIGHT;
            }
        } else {
            direction = Direction.DEFAULT;
        }
    }

    private void handleMovement() {
        collisionOn = false;
        gamePanel.getCollisionChecker().checkTile(this);

        int objIndex = gamePanel.getCollisionChecker().checkObject(this, true);
        pickUpObject(objIndex);

        if (!collisionOn) {
            movePlayer();
        }
    }

    private void movePlayer() {
        switch (direction) {
            case UP -> y -= speed;
            case DOWN -> y += speed;
            case LEFT -> x -= speed;
            case RIGHT -> x += speed;
            default -> {
            } // No movement for default direction
        }
    }

    private void handleAnimation() {
        boolean isMoving = direction != Direction.DEFAULT;
        updateAnimation(isMoving);
    }

    public void pickUpObject(int index) {
        if (index != 999) {
            stars++;
            gamePanel.getGameObjects().remove(index);
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        BufferedImage currentSprite = getCurrentSprite();
        if (currentSprite != null) {
            g2.drawImage(currentSprite, x, y, GameConfig.TILE_SIZE, GameConfig.TILE_SIZE, null);
        }
    }

    // Getters
    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }
}